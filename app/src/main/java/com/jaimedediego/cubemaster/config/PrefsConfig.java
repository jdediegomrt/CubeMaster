package com.jaimedediego.cubemaster.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.jaimedediego.cubemaster.methods.DatabaseMethods;

public class PrefsConfig {

    private static PrefsConfig instance;

    private PrefsConfig() {
    }

    public static PrefsConfig getInstance() {
        if (instance == null) {
            instance = new PrefsConfig();
        }
        return instance;
    }

    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;

    public void setContext(Context context) {
        prefs = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void initConfig() {
        if (!prefs.contains("onboarding")) {
            editor.putBoolean("onboarding", false);
            editor.apply();
        }

        if (!prefs.contains("chart")) {
            editor.putBoolean("chart", false);
            editor.apply();
        }

        if (!prefs.contains("beep")) {
            editor.putBoolean("beep", false);
            editor.apply();
        }

        if (!prefs.contains("stopwatch")) {
            editor.putBoolean("stopwatch", false);
            editor.apply();
        }

        if (!prefs.contains("freezingTime")) {
            editor.putInt("freezingTime", 500);
            editor.apply();
        }

        if (!prefs.contains("user")) {
            DatabaseMethods.getInstance().addUserAndDefaultPuzzles();
            editor.putBoolean("user", true);
            editor.apply();
        }

        if (!prefs.contains("colorAccent")) {
            editor.putInt("colorAccent", 0);
            editor.apply();
        }

        if (!prefs.contains("ratedOrNever")) {
            editor.putBoolean("ratedOrNever", false);
            editor.apply();
        }

        if (!prefs.contains("scramble")) {
            editor.putBoolean("scramble", true);
            editor.apply();
        }

        if (!prefs.contains("indicators")) {
            editor.putInt("indicators", 0);
            editor.apply();
        }

        if (!prefs.contains("averageOfN")) {
            editor.putInt("averageOfN", 0);
            editor.apply();
        }

        if (!prefs.contains("showedNewFeature")) {
            editor.putBoolean("showedNewFeature", true);
            editor.apply();
        }
    }
}
