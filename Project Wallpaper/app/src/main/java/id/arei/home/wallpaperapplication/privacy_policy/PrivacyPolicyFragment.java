package id.arei.home.wallpaperapplication.privacy_policy;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import id.arei.home.wallpaperapplication.R;

public class PrivacyPolicyFragment extends Fragment {
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_privacy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("Privacy Policy");
        spannableStringBuilder.setSpan(new android.text.style.StyleSpan(Typeface.ITALIC), 0, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        toolbar.setTitle(spannableStringBuilder);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }
}
