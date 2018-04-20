package com.dediegomrt.cubemaster.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.dediegomrt.cubemaster.Config.PrefsConfig;
import com.dediegomrt.cubemaster.Config.ThemeConfig;
import com.dediegomrt.cubemaster.Methods.DatabaseMethods;
import com.dediegomrt.cubemaster.Methods.PrefsMethods;
import com.dediegomrt.cubemaster.R;
import com.dediegomrt.cubemaster.Utils.Session;

public class MainActivity extends AppCompatActivity
        implements ChronoFragment.OnFragmentInteractionListener, StatsFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener, PuzzlesFragment.OnFragmentInteractionListener {

    private FragmentManager fm;
    
    private String chronoStr = "Chrono";
    private String statsStr = "Stats";
    private String settingsStr="Settings";
    private String puzzlesStr="Puzzles";

    private Boolean exit = false;

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

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fm = getSupportFragmentManager();
        RadioButton timer = (RadioButton) findViewById(R.id.timer);
        RadioButton stats = (RadioButton) findViewById(R.id.stats);
        RadioButton settings = (RadioButton) findViewById(R.id.settings);
        RadioButton myPuzzles = (RadioButton) findViewById(R.id.mypuzzles);

        timer.setBackground(ThemeConfig.getInstance().getMenuAnimation());
        stats.setBackground(ThemeConfig.getInstance().getMenuAnimation());
        myPuzzles.setBackground(ThemeConfig.getInstance().getMenuAnimation());
        settings.setBackground(ThemeConfig.getInstance().getMenuAnimation());

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

    private void replaceFragment(Fragment fr, String tag){
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.container, fr, tag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
