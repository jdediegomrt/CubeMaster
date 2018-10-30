package com.jaimedediego.cubemaster.view.activities.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.config.PrefsConfig;
import com.jaimedediego.cubemaster.config.ThemeConfig;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.methods.PrefsMethods;
import com.jaimedediego.cubemaster.utils.AndroidUtils;
import com.jaimedediego.cubemaster.utils.Constants;
import com.jaimedediego.cubemaster.view.activities.main.fragments.ChronoFragment;
import com.jaimedediego.cubemaster.view.activities.main.fragments.PuzzlesFragment;
import com.jaimedediego.cubemaster.view.activities.main.fragments.SettingsFragment;
import com.jaimedediego.cubemaster.view.activities.main.fragments.StatsFragment;
import com.jaimedediego.cubemaster.view.activities.onboarding.OnboardingActivity;
import com.jaimedediego.cubemaster.view.customViews.CustomToast;

public class MainActivity extends AppCompatActivity
        implements ChronoFragment.OnFragmentInteractionListener, StatsFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener, PuzzlesFragment.OnFragmentInteractionListener {

    private FragmentManager fm;
    private BroadcastReceiver broadcastReceiver;
    private IntentFilter broadcastFilter;
    private boolean isReceiverRegistered = false;

    private String chronoStr = "Chrono";
    private String statsStr = "Stats";
    private String settingsStr = "Settings";
    private String puzzlesStr = "Puzzles";
    private Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        DatabaseMethods.getInstance().setDatabase(getBaseContext());
        PrefsConfig.getInstance().setContext(this);
        PrefsConfig.getInstance().initConfig();
        ThemeConfig.getInstance().setActivity(this);
        ThemeConfig.getInstance().initConfig();

        setContentView(R.layout.activity_main);

//        PrefsMethods.getInstance().setOnboardingShown(false);
        if (PrefsMethods.getInstance().isOnboardingNotShown()) {
            startActivity(new Intent(this, OnboardingActivity.class));
        }

//        MobileAds.initialize(this, "ca-app-pub-8962656574856623~3014810195");
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final AdView banner = findViewById(R.id.banner);

        RadioButton timer = findViewById(R.id.timer);
        RadioButton stats = findViewById(R.id.stats);
        RadioButton settings = findViewById(R.id.settings);
        RadioButton myPuzzles = findViewById(R.id.mypuzzles);

        AndroidUtils.initLayoutTransitions(findViewById(R.id.content_main));

//        banner.loadAd(new AdRequest.Builder().build());
        banner.loadAd(new AdRequest.Builder().addTestDevice("9291F3AB05D2610244D1D11FF443BCC0").build());

        broadcastFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                final ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isAvailable() || manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable()) {
//                    banner.loadAd(new AdRequest.Builder().build());
                    banner.loadAd(new AdRequest.Builder().addTestDevice("9291F3AB05D2610244D1D11FF443BCC0").build());
                }
            }
        };

        timer.setBackground(ThemeConfig.getInstance().getMenuAnimation());
        stats.setBackground(ThemeConfig.getInstance().getMenuAnimation());
        myPuzzles.setBackground(ThemeConfig.getInstance().getMenuAnimation());
        settings.setBackground(ThemeConfig.getInstance().getMenuAnimation());

        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, new ChronoFragment(), chronoStr).commit();

        timer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    fm.beginTransaction().replace(R.id.container, new ChronoFragment(), chronoStr).commit();
            }
        });
        stats.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    fm.beginTransaction().replace(R.id.container, new StatsFragment(), statsStr).commit();
            }
        });
        myPuzzles.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    fm.beginTransaction().replace(R.id.container, new PuzzlesFragment(), puzzlesStr).commit();
            }
        });
        settings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    fm.beginTransaction().replace(R.id.container, new SettingsFragment(), settingsStr).commit();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isReceiverRegistered) {
            registerReceiver(broadcastReceiver, broadcastFilter);
            isReceiverRegistered = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isReceiverRegistered) {
            unregisterReceiver(broadcastReceiver);
            isReceiverRegistered = false;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {/*Do nothing*/}

    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            new CustomToast(this, R.string.press_back_again_to_exit).showAndHide(Constants.getInstance().TOAST_MEDIUM_DURATION);
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2000);
        }
    }

    public void refreshView() {
        fm = getSupportFragmentManager();
        if (fm.findFragmentByTag(statsStr) != null && fm.findFragmentByTag(statsStr).isVisible()) {
            replaceFragment(new StatsFragment(), statsStr);
        } else {
            if (fm.findFragmentByTag(puzzlesStr) != null && fm.findFragmentByTag(puzzlesStr).isVisible()) {
                replaceFragment(new PuzzlesFragment(), puzzlesStr);
            }
        }
    }

    private void replaceFragment(Fragment fr, String tag) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.container, fr, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
