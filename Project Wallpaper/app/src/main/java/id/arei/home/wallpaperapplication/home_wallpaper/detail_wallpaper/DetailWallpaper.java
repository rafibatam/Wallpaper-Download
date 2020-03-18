package id.arei.home.wallpaperapplication.home_wallpaper.detail_wallpaper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import id.arei.home.wallpaperapplication.R;

public class DetailWallpaper extends AppCompatActivity {
    private ImageView imageViewWallpaper, setLockWallpaperImage, setHomeWallpaperImage, setLockHomeWallpaperImage;
    private Toolbar toolbar;
    private TextView setLockWallpaperText, setHomeWallpaperText, setLockHomeWallpaperText;
    private FloatingActionButton floatingActionButton;
    private static final int REQUEST_PERMISSION_CODE = 123;
    private String[] permissionList;
    private static final String TAG = DetailWallpaper.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wallpaper);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        floatingActionButton = findViewById(R.id.setWallpaperButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibilityButton();
            }
        });

//        setToolbar();
        setImageViewWallpaper();

        permissionList = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (checkPermission()) {
            createWallpaperFolder();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        String getTitle = getIntent().getStringExtra("titleImage");
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getTitle);
        spannableStringBuilder.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, getTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        toolbar.setTitle(spannableStringBuilder);
        setSupportActionBar(toolbar);
    }

    private void setImageViewWallpaper() {
        imageViewWallpaper = findViewById(R.id.detailImage);
        String getImage = getIntent().getStringExtra("imageURL");
        Glide.with(this)
                .load(getImage)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .into(imageViewWallpaper);
    }

    private void setVisibilityButton() {
        setLockWallpaperImage = findViewById(R.id.setLockWallpaperImage);
        setLockWallpaperText = findViewById(R.id.setLockWallpaperText);
        setHomeWallpaperImage = findViewById(R.id.setHomeWallpaperImage);
        setHomeWallpaperText = findViewById(R.id.setHomeWallpaperText);
        setLockHomeWallpaperImage = findViewById(R.id.setLockHomeWallpaperImage);
        setLockHomeWallpaperText = findViewById(R.id.setLockHomeWallpaperText);

        if (setLockWallpaperImage.getVisibility() == View.GONE && setLockWallpaperText.getVisibility() == View.GONE && setHomeWallpaperImage.getVisibility() == View.GONE && setHomeWallpaperText.getVisibility() == View.GONE && setLockHomeWallpaperImage.getVisibility() == View.GONE && setLockHomeWallpaperText.getVisibility() == View.GONE) {
            setLockWallpaperImage.setVisibility(View.VISIBLE);
            setLockWallpaperText.setVisibility(View.VISIBLE);
            setHomeWallpaperImage.setVisibility(View.VISIBLE);
            setHomeWallpaperText.setVisibility(View.VISIBLE);
            setLockHomeWallpaperImage.setVisibility(View.VISIBLE);
            setLockHomeWallpaperText.setVisibility(View.VISIBLE);

            setHomeWallpaper();
        } else {
            setLockWallpaperImage.setVisibility(View.GONE);
            setLockWallpaperText.setVisibility(View.GONE);
            setHomeWallpaperImage.setVisibility(View.GONE);
            setHomeWallpaperText.setVisibility(View.GONE);
            setLockHomeWallpaperImage.setVisibility(View.GONE);
            setLockHomeWallpaperText.setVisibility(View.GONE);
        }
    }

    private void setHomeWallpaper() {
        setLockHomeWallpaperImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLockHomeWallpapers();
            }
        });

        setLockHomeWallpaperText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLockHomeWallpapers();
            }
        });

        setLockWallpaperImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                setLockWallpapers();
            }
        });

        setLockWallpaperText.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                setLockWallpapers();
            }
        });

        setHomeWallpaperImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                setHomeWallpapers();
            }
        });

        setHomeWallpaperText.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                setHomeWallpapers();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setHomeWallpapers() {
        String getImage = getIntent().getStringExtra("imageURL");
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            InputStream inputStream = new URL(getImage).openStream();
            wallpaperManager.setStream(inputStream, null, true, WallpaperManager.FLAG_SYSTEM);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setLockWallpapers() {
        String getImage = getIntent().getStringExtra("imageURL");
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            InputStream inputStream = new URL(getImage).openStream();
            wallpaperManager.setStream(inputStream, null, true, WallpaperManager.FLAG_LOCK);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setLockHomeWallpapers() {
        String getImage = getIntent().getStringExtra("imageURL");
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            InputStream inputStream = new URL(getImage).openStream();
            wallpaperManager.setStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createWallpaperFolder() {
        File wallpaperFolder = new File(Environment.getExternalStorageDirectory() + "/WallpaperApplication/");
        if (!wallpaperFolder.exists()) {
            wallpaperFolder.mkdirs();
        }
    }

    public boolean checkPermission() {
        int result;
        List<String> permissions = new ArrayList<>();

        for (String needPermission : permissionList) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(), needPermission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissions.add(needPermission);
            }
        }

        if (!permissions.isEmpty()) {
            ActivityCompat.requestPermissions(DetailWallpaper.this, permissions.toArray(new String[permissions.size()]), REQUEST_PERMISSION_CODE);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createWallpaperFolder();
                } else {
                    String permission = "";
                    for (String needPermission : permissionList) {
                        permission += "\n" + needPermission;
                    }
                }
            }
        }
    }
}
