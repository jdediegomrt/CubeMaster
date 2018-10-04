package com.jaimedediego.cubemaster.methods;

import com.jaimedediego.cubemaster.config.PrefsConfig;

public class PrefsMethods {

    private static PrefsMethods instance;

    private PrefsMethods() {
    }

    public static PrefsMethods getInstance() {
        if (instance == null) {
            instance = new PrefsMethods();
        }
        return instance;
    }

    private PrefsConfig prefsConfig = PrefsConfig.getInstance();

    public boolean isBeepActivated() {
        return prefsConfig.prefs.getBoolean("beep", false);
    }

    public void activateBeep(boolean activate) {
        prefsConfig.editor.putBoolean("beep", activate);
        prefsConfig.editor.apply();
    }

    public boolean isPauseActivated() {
        return prefsConfig.prefs.getBoolean("stopwatch", false);
    }

    public void activatePause(boolean activate) {
        prefsConfig.editor.putBoolean("stopwatch", activate);
        prefsConfig.editor.apply();
    }

    public boolean isOnboardingShown() {
        return prefsConfig.prefs.getBoolean("onboarding", false);
    }

    public void setOnboardingShown(boolean shown) {
        prefsConfig.editor.putBoolean("onboarding", shown);
        prefsConfig.editor.apply();
    }

    public int getColorAccent() {
        return prefsConfig.prefs.getInt("colorAccent", 0);
    }

    public void setColorAccent(int position) {
        prefsConfig.editor.putInt("colorAccent", position);
        prefsConfig.editor.apply();
    }

    public int getFreezingTime() {
        return prefsConfig.prefs.getInt("freezingTime", 0);
    }

    public void setFreezingTime(int freezingTime) {
        prefsConfig.editor.putInt("freezingTime", freezingTime);
        prefsConfig.editor.apply();
    }

    public boolean isRatedOrNever() {
        return prefsConfig.prefs.getBoolean("ratedOrNever", false);
    }

    public void setRatedOrNever(boolean ratedOrNever) {
        prefsConfig.editor.putBoolean("ratedOrNever", ratedOrNever);
        prefsConfig.editor.apply();
    }

    public boolean isScrambleEnabled() {
        return prefsConfig.prefs.getBoolean("scramble", false);
    }

    public void setScramble(boolean wholeCubeRotations) {
        prefsConfig.editor.putBoolean("scramble", wholeCubeRotations);
        prefsConfig.editor.apply();
    }

    public int getScrambleLength() {
        return prefsConfig.prefs.getInt("scrambleLength", 0);
    }

    public void setScrambleLength(int scrambleLength) {
        prefsConfig.editor.putInt("scrambleLength", scrambleLength);
        prefsConfig.editor.apply();
    }
}
