package id.arei.home.wallpaperapplication.home_wallpaper;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import id.arei.home.wallpaperapplication.ModelWallpaper.ModelGetWallpaper;
import id.arei.home.wallpaperapplication.R;
import id.arei.home.wallpaperapplication.base_api.BaseAPIService;
import id.arei.home.wallpaperapplication.base_api.RetrofitClient;
import id.arei.home.wallpaperapplication.base_api.UtilsAPI;
import id.arei.home.wallpaperapplication.home_wallpaper.adapter_wallpaper.AdapterWallpaper;
import id.arei.home.wallpaperapplication.pagination_load.PaginationLoadMore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeWallpaperFragment extends Fragment {
    private static final String NEXT_PAGE_TOKEN = "CgkIChjjkIHohi4QybiIt8XsmPBH";
    private static final String FIX_TOKEN = "AIzaSyCfxpk5yzqsJHxUTr7tU9o-bss-J7A-b1I";

    private Toolbar toolbar;
    private BaseAPIService baseAPIService;
    private ProgressBar progressBar;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private Context mContext;
    private static final String TAG = HomeWallpaperFragment.class.getSimpleName();
    private RecyclerView recyclerViewWallpaper;
    private AdapterWallpaper adapterWallpaper;
    private List<ModelGetWallpaper> modelGetWallpaperList;
    private int TOTAL_PAGES = 1;
    private int currentPage = PAGE_START;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_wallpaper, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        baseAPIService = RetrofitClient.getClient(UtilsAPI.BASE_URL_API).create(BaseAPIService.class);

        adapterWallpaper = new AdapterWallpaper(mContext);

        progressBar = view.findViewById(R.id.mainProgressBar);

        recyclerViewWallpaper = view.findViewById(R.id.recyclerWallpaper);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(mContext, 3, LinearLayoutManager.VERTICAL, false);

        recyclerViewWallpaper.setLayoutManager(linearLayoutManager);
        recyclerViewWallpaper.setAdapter(adapterWallpaper);
        recyclerViewWallpaper.setItemAnimator(new DefaultItemAnimator());
        recyclerViewWallpaper.addOnScrollListener(new PaginationLoadMore(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        loadFirstPage();
        setToolbar(view);
    }

    private void setToolbar(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("Wallpaper HD");
        spannableStringBuilder.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        toolbar.setTitle(spannableStringBuilder);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void loadFirstPage() {
        modelGetWallpaperCall().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray itemsArray = jsonObject.getJSONArray("items");
                        modelGetWallpaperList = new ArrayList<>();
                        progressBar.setVisibility(View.GONE);

                        for (int a = 0; a < itemsArray.length(); a++) {
                            JSONObject objectData = itemsArray.getJSONObject(a);

                            String getContent = objectData.getString("content");
                            Document convertHtml = Jsoup.parse(getContent);
                            String getLink = convertHtml.select(".separator a").attr("href");

                            ModelGetWallpaper modelGetWallpaper = new ModelGetWallpaper();
                            modelGetWallpaper.setTitle(objectData.getString("title"));
                            modelGetWallpaper.setContentImage(getLink);

                            modelGetWallpaperList.add(modelGetWallpaper);
                            progressBar.setVisibility(View.GONE);
                        }

                        adapterWallpaper.addAll(modelGetWallpaperList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (currentPage <= TOTAL_PAGES && currentPage < adapterWallpaper.getItemCount()) adapterWallpaper.addLoadingFooter();
                    else isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void loadNextPage() {
        modelGetWallpaperCall().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                adapterWallpaper.removeLoadingFooter();
                isLoading = false;

                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        JSONArray itemsArray = jsonObject.getJSONArray("items");
                        modelGetWallpaperList = new ArrayList<>();
                        progressBar.setVisibility(View.GONE);

                        for (int a = 0; a < itemsArray.length(); a++) {
                            JSONObject objectData = itemsArray.getJSONObject(a);

                            String getContent = objectData.getString("content");
                            Document convertHtml = Jsoup.parse(getContent);
                            String getLink = convertHtml.select(".separator a").attr("href");

                            ModelGetWallpaper modelGetWallpaper = new ModelGetWallpaper();
                            modelGetWallpaper.setTitle(objectData.getString("title"));
                            modelGetWallpaper.setContentImage(getLink);

                            modelGetWallpaperList.add(modelGetWallpaper);
                            progressBar.setVisibility(View.GONE);
                        }

                        adapterWallpaper.addAll(modelGetWallpaperList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (currentPage != TOTAL_PAGES && currentPage < adapterWallpaper.getItemCount()) adapterWallpaper.addLoadingFooter();
                    else isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private Call<JsonObject> modelGetWallpaperCall() {
        return baseAPIService.getImage(FIX_TOKEN, NEXT_PAGE_TOKEN);
    }
}