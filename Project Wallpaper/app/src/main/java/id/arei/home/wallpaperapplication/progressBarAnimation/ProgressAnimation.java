package id.arei.home.wallpaperapplication.progressBarAnimation;

import android.content.Context;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

import id.arei.home.wallpaperapplication.MainActivity;

public class ProgressAnimation extends Animation {
    private Context context;
    private ProgressBar progressBar;
    private TextView textLoading;
    private float start, end;

    public ProgressAnimation(Context context, ProgressBar progressBar, TextView textLoading, float start, float end) {
        this.context = context;
        this.progressBar = progressBar;
        this.textLoading = textLoading;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        float value = start + (end - start) * interpolatedTime;
        progressBar.setProgress((int) value);
        textLoading.setText((int) value + " %");

        if (value == end) {
            Intent moveMain = new Intent(context, MainActivity.class);
            moveMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(moveMain);
        }
    }
}
