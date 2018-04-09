package com.dediegomrt.cubemaster.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.dediegomrt.cubemaster.Config.PrefsConfig;
import com.dediegomrt.cubemaster.Config.ThemeConfig;
import com.dediegomrt.cubemaster.Methods.DatabaseMethods;
import com.dediegomrt.cubemaster.Methods.PrefsMethods;
import com.dediegomrt.cubemaster.R;
import com.dediegomrt.cubemaster.Utils.Session;
import com.dediegomrt.cubemaster.View.Dialogs.NewPuzzleDialog;
import com.dediegomrt.cubemaster.View.Dialogs.PuzzleChangeDialog;

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
        setContentView(R.layout.activity_main);

        DatabaseMethods.getInstance().setDatabase(getBaseContext());
        PrefsConfig.getInstance().setContext(this);
        PrefsConfig.getInstance().initConfig();
        ThemeConfig.getInstance().setActivity(this);
        ThemeConfig.getInstance().initConfig();

    /*TODO Eliminar primer setOnboardingShown(false) al subir, es para pruebas*/
        PrefsMethods.getInstance().setOnboardingShown(false);
        if (!PrefsMethods.getInstance().isOnboardingShown()) {
            startActivity(new Intent(this, OnboardingActivity.class));
        }

        getSupportActionBar().setTitle(R.string.timer);

        fm = getSupportFragmentManager();
        RadioButton timer = (RadioButton) findViewById(R.id.timer);
        RadioButton stats = (RadioButton) findViewById(R.id.stats);
        RadioButton settings = (RadioButton) findViewById(R.id.settings);
        RadioButton myPuzzles = (RadioButton) findViewById(R.id.mypuzzles);

        getSupportActionBar().setBackgroundDrawable(getDrawable(Session.getInstance().darkColorTheme));
        timer.setBackground(ThemeConfig.getInstance().getMenuAnimation());
        stats.setBackground(ThemeConfig.getInstance().getMenuAnimation());
        myPuzzles.setBackground(ThemeConfig.getInstance().getMenuAnimation());
        settings.setBackground(ThemeConfig.getInstance().getMenuAnimation());

        fm.beginTransaction().replace(R.id.container, new ChronoFragment(), chronoStr).commit();
        ((TransitionDrawable)timer.getBackground()).startTransition(0);

        timer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    getSupportActionBar().setTitle(R.string.timer);
                    fm.beginTransaction().replace(R.id.container, new ChronoFragment(), chronoStr).commit();
                    animate(buttonView);
                } else {
                    reverseAnimate(buttonView);
                }
            }
        });
        stats.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    getSupportActionBar().setTitle(R.string.stats);
                    fm.beginTransaction().replace(R.id.container, new StatsFragment(), statsStr).commit();
                    animate(buttonView);
                } else {
                    reverseAnimate(buttonView);
                }
            }
        });
        myPuzzles.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    getSupportActionBar().setTitle(R.string.my_puzzles);
                    fm.beginTransaction().replace(R.id.container, new PuzzlesFragment(), puzzlesStr).commit();
                    animate(buttonView);
                } else {
                    reverseAnimate(buttonView);
                }
            }
        });
        settings.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    getSupportActionBar().setTitle(R.string.settings);
                    fm.beginTransaction().replace(R.id.container, new SettingsFragment(), settingsStr).commit();
                    animate(buttonView);
                } else {
                    reverseAnimate(buttonView);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem addNew = menu.findItem(R.id.add_new);
        addNew.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_last_solve) {
            DatabaseMethods.getInstance().deleteLastSolve(Session.getInstance().currentPuzzleId);
            refreshView();
            return true;
        } else if (id == R.id.change_database) {
            final PuzzleChangeDialog dialog = new PuzzleChangeDialog(MainActivity.this);
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    refreshView();
                }
            });
            return true;
        } else if (id == R.id.add_new){
            final NewPuzzleDialog dialog = new NewPuzzleDialog(MainActivity.this);
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    refreshView();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}

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

    private void refreshView() {
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

    private void reverseAnimate(CompoundButton v) {
        ((TransitionDrawable)v.getBackground()).startTransition(0);
        ((TransitionDrawable)v.getBackground()).reverseTransition(300);
    }

    private void animate(CompoundButton v) {
        ((TransitionDrawable)v.getBackground()).startTransition(300);
    }
}
