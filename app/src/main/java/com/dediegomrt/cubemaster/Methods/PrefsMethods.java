package com.dediegomrt.cubemaster.Methods;

import com.dediegomrt.cubemaster.Config.PrefsConfig;

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

    public boolean isColorActivated(){
        if(prefsConfig.prefs.getBoolean("color", false)){
            return true;
        } else {
            return false;
        }
    }

    public void activateColor(boolean activate){
        prefsConfig.editor.putBoolean("color", activate);
        prefsConfig.editor.apply();
    }

    public int getColorAccent(){
        return prefsConfig.prefs.getInt("colorAccent", 0);
    }

    public void setColorAccent(int position){
        prefsConfig.editor.putInt("colorAccent", position);
        prefsConfig.editor.apply();
    }
}
