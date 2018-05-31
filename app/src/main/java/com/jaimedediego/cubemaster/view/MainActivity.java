package com.jaimedediego.cubemaster.view;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.config.PrefsConfig;
import com.jaimedediego.cubemaster.config.ThemeConfig;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.methods.PrefsMethods;

public class MainActivity extends AppCompatActivity
        implements ChronoFragment.OnFragmentInteractionListener, StatsFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener, PuzzlesFragment.OnFragmentInteractionListener {

    private FragmentManager fm;

    private String chronoStr = "Chrono";
    private String statsStr = "Stats";
    private String settingsStr="Settings";
    private String puzzlesStr="Puzzles";
    private Boolean exit = false;

    private RelativeLayout bannerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseMethods.getInstance().setDatabase(getBaseContext());
        PrefsConfig.getInstance().setContext(this);
        PrefsConfig.getInstance().initConfig();
        ThemeConfig.getInstance().setActivity(this);
        ThemeConfig.getInstance().initConfig();

        setContentView(R.layout.activity_main);
        if (!PrefsMethods.getInstance().isOnboardingShown()) {
            startActivity(new Intent(this, OnboardingActivity.class));
        }

//        MobileAds.initialize(this, "ca-app-pub-8962656574856623~3014810195");
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final AdView banner = findViewById(R.id.banner);
        ImageButton closeBanner = findViewById(R.id.close_banner);
        bannerLayout = findViewById(R.id.banner_layout);
        final ViewGroup.LayoutParams params = bannerLayout.getLayoutParams();

        RadioButton timer = findViewById(R.id.timer);
        RadioButton stats = findViewById(R.id.stats);
        RadioButton settings = findViewById(R.id.settings);
        RadioButton myPuzzles = findViewById(R.id.mypuzzles);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            LayoutTransition layoutTransition = ((RelativeLayout)findViewById(R.id.banner_container)).getLayoutTransition();
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        }

//        banner.loadAd(new AdRequest.Builder().build());
        banner.loadAd(new AdRequest.Builder().addTestDevice("9291F3AB05D2610244D1D11FF443BCC0").build());

        banner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showBanner();
                    }
                }, 5000);
            }
        });

        closeBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bannerLayout.getHeight()!=0){
                    bannerLayout.setLayoutParams(params);
                    banner.loadAd(new AdRequest.Builder().addTestDevice("9291F3AB05D2610244D1D11FF443BCC0").build());
                }
            }
        });

        timer.setBackground(ThemeConfig.getInstance().getMenuAnimation());
        stats.setBackground(ThemeConfig.getInstance().getMenuAnimation());
        myPuzzles.setBackground(ThemeConfig.getInstance().getMenuAnimation());
        settings.setBackground(ThemeConfig.getInstance().getMenuAnimation());

        fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, new ChronoFragment(), chronoStr).commit();

        timer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    fm.beginTransaction().replace(R.id.container, new ChronoFragment(), chronoStr).commit();
            }
        });
        stats.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    fm.beginTransaction().replace(R.id.container, new StatsFragment(), statsStr).commit();
            }
        });
        myPuzzles.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    fm.beginTransaction().replace(R.id.container, new PuzzlesFragment(), puzzlesStr).commit();
            }
        });
        settings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    fm.beginTransaction().replace(R.id.container, new SettingsFragment(), settingsStr).commit();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {/*Do nothing*/}

    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            Toast.makeText(this, R.string.press_back_again_to_exit, Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3000);
        }
    }

    void refreshView() {
        fm = getSupportFragmentManager();
        if(fm.findFragmentByTag(statsStr)!=null&&fm.findFragmentByTag(statsStr).isVisible()){
            replaceFragment(new StatsFragment(), statsStr);
        } else {
            if(fm.findFragmentByTag(puzzlesStr)!=null&&fm.findFragmentByTag(puzzlesStr).isVisible()){
                replaceFragment(new PuzzlesFragment(), puzzlesStr);
            }
        }
    }

    private void showBanner(){
        if(bannerLayout.getHeight()==0) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            bannerLayout.setLayoutParams(layoutParams);
        }
    }

    private void replaceFragment(Fragment fr, String tag){
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.container, fr, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
