package com.jaimedediego.cubemaster.methods;

import com.jaimedediego.cubemaster.config.PrefsConfig;

public class PrefsMethods {

    private static PrefsMethods instance;

    private PrefsMethods() {}

    public static PrefsMethods getInstance() {
        if (instance == null) {
            instance = new PrefsMethods();
        }
        return instance;
    }


    private PrefsConfig prefsConfig = PrefsConfig.getInstance();

    public boolean isBeepActivated(){
        if(prefsConfig.prefs.getBoolean("beep", false)){
            return true;
        } else {
            return false;
        }
    }

    public void activateBeep(boolean activate){
        prefsConfig.editor.putBoolean("beep", activate);
        prefsConfig.editor.apply();
    }

    public boolean isPauseActivated(){
        if(prefsConfig.prefs.getBoolean("stopwatch", false)){
            return true;
        } else {
            return false;
        }
    }

    public void activatePause(boolean activate){
        prefsConfig.editor.putBoolean("stopwatch", activate);
        prefsConfig.editor.apply();
    }

    public boolean isOnboardingShown(){
        if(prefsConfig.prefs.getBoolean("onboarding", false)){
            return true;
        } else {
            return false;
        }
    }

    public void setOnboardingShown(boolean shown){
        prefsConfig.editor.putBoolean("onboarding", shown);
        prefsConfig.editor.apply();
    }

    public int getColorAccent(){
        return prefsConfig.prefs.getInt("colorAccent", 0);
    }

    public void setColorAccent(int position){
        prefsConfig.editor.putInt("colorAccent", position);
        prefsConfig.editor.apply();
    }

    public int getFreezingTime(){
        return prefsConfig.prefs.getInt("freezingTime", 0);
    }

    public void setFreezingTime(int freezingTime){
        prefsConfig.editor.putInt("freezingTime", freezingTime);
        prefsConfig.editor.apply();
    }

    public boolean isRatedOrNever(){
        if(prefsConfig.prefs.getBoolean("ratedOrNever", false)){
            return true;
        } else {
            return false;
        }
    }

    public void setRatedOrNever(boolean ratedOrNever){
        prefsConfig.editor.putBoolean("ratedOrNever", ratedOrNever);
        prefsConfig.editor.apply();
    }


    public boolean isScrambleEnabled(){
        if(prefsConfig.prefs.getBoolean("scramble", false)){
            return true;
        } else {
            return false;
        }
    }

    public void setScramble(boolean wholeCubeRotations){
        prefsConfig.editor.putBoolean("scramble", wholeCubeRotations);
        prefsConfig.editor.apply();
    }
}
