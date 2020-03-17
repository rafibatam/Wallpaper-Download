package id.arei.home.wallpaperapplication.home_wallpaper.adapter_wallpaper;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import id.arei.home.wallpaperapplication.ModelWallpaper.ModelGetWallpaper;
import id.arei.home.wallpaperapplication.R;
import id.arei.home.wallpaperapplication.home_wallpaper.detail_wallpaper.DetailWallpaper;

public class AdapterWallpaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ModelGetWallpaper> modelGetWallpaperList;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    public AdapterWallpaper(Context context) {
        this.context = context;
        modelGetWallpaperList = new ArrayList<>();
    }

    public List<ModelGetWallpaper> getMovies() {
        return modelGetWallpaperList;
    }

    public void setMovies(List<ModelGetWallpaper> movieResults) {
        this.modelGetWallpaperList = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.load_more, parent, false);
                viewHolder = new LoadingViewHolder(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        View v1 = inflater.inflate(R.layout.row_home_wallpaper, parent, false);

        return new ViewHolder(v1);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ModelGetWallpaper modelGetWallpaper = modelGetWallpaperList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                final ViewHolder viewHolder = (ViewHolder) holder;

                viewHolder.textWallpaper.setText(modelGetWallpaper.getTitle());
                Glide.with(context)
                        .load(modelGetWallpaper.getContentImage())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                viewHolder.progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                viewHolder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .skipMemoryCache(true)
                        .into(viewHolder.imageWallpaper);
                viewHolder.cardViewWallpaper.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent moveDetail = new Intent(context, DetailWallpaper.class);
                        moveDetail.putExtra("imageURL", modelGetWallpaper.getContentImage());
                        moveDetail.putExtra("titleImage", modelGetWallpaper.getTitle());
                        context.startActivity(moveDetail);
                    }
                });

                break;

            case LOADING:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return modelGetWallpaperList == null ? 0 : modelGetWallpaperList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == modelGetWallpaperList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(ModelGetWallpaper r) {
        modelGetWallpaperList.add(r);
        notifyItemInserted(modelGetWallpaperList.size() - 1);
    }

    public void addAll(List<ModelGetWallpaper> modelGetWallpaperList) {
        for (ModelGetWallpaper result : modelGetWallpaperList) {
            add(result);
        }
    }

    public void remove(ModelGetWallpaper r) {
        int position = modelGetWallpaperList.indexOf(r);
        if (position > -1) {
            modelGetWallpaperList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new ModelGetWallpaper());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = modelGetWallpaperList.size() - 1;
        ModelGetWallpaper result = getItem(position);

        if (result != null) {
            modelGetWallpaperList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ModelGetWallpaper getItem(int position) {
        return modelGetWallpaperList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageWallpaper;
        private TextView textWallpaper;
        private CardView cardViewWallpaper;
        private ProgressBar progressBar;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageWallpaper = itemView.findViewById(R.id.sourceWallpaper);
            textWallpaper = itemView.findViewById(R.id.nameWallpaper);
            cardViewWallpaper = itemView.findViewById(R.id.cardViewWallpaper);
            progressBar = itemView.findViewById(R.id.imageProgress);
        }
    }

    protected class LoadingViewHolder extends RecyclerView.ViewHolder {
        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }
}