package id.arei.home.wallpaperapplication.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import id.arei.home.wallpaperapplication.R;
import id.arei.home.wallpaperapplication.progressBarAnimation.ProgressAnimation;

public class SplashScreen extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView textLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.progressBar);
        textLoading = findViewById(R.id.textLoading);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
                textLoading.setVisibility(View.VISIBLE);

                progressBar.setMax(100);
                progressBar.setScaleY(3f);

                progressAnimation();
            }
        }, 1500);
    }

    public void progressAnimation() {
        ProgressAnimation progressAnimation = new ProgressAnimation(getApplicationContext(), progressBar, textLoading, 0f, 100f);
        progressAnimation.setDuration(8000);
        progressBar.setAnimation(progressAnimation);
    }
}
