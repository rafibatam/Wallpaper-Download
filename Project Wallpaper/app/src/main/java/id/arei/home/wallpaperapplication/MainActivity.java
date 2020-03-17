package id.arei.home.wallpaperapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import id.arei.home.wallpaperapplication.home_wallpaper.HomeWallpaperFragment;
import id.arei.home.wallpaperapplication.more_apps.MoreAppsFragment;
import id.arei.home.wallpaperapplication.privacy_policy.PrivacyPolicyFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        loadFragment(new HomeWallpaperFragment());
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.menuHomeWallpaper:
                selectedFragment = new HomeWallpaperFragment();
                break;
            case R.id.menuMoreApplication:
                selectedFragment = new MoreAppsFragment();
                break;
            case R.id.menuPrivacy:
                selectedFragment = new PrivacyPolicyFragment();
                break;
        }

        return loadFragment(selectedFragment);
    }
}
