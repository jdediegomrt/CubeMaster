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
    private Context context;

    public void setContext(Context context){
        this.context = context;
        prefs = this.context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void initConfig(){
        if(!prefs.contains("onboarding")){
            editor.putBoolean("onboarding", false);
            editor.apply();
        }
        if(!prefs.contains("beep")){
            editor.putBoolean("beep", false);
            editor.apply();
        }
        if(!prefs.contains("stopwatch")){
            editor.putBoolean("stopwatch", false);
            editor.apply();
        }
        if(!prefs.contains("freezingTime")){
            editor.putInt("freezingTime", 500);
            editor.apply();
        }
        if(!prefs.contains("user")){
            DatabaseMethods.getInstance().addUser();
            editor.putBoolean("user", true);
            editor.apply();
        }
        if(!prefs.contains("colorAccent")){
            editor.putInt("colorAccent", 0);
            editor.apply();
        }
        if(!prefs.contains("ratedOrNever")){
            editor.putBoolean("ratedOrNever", false);
            editor.apply();
        }
    }
}