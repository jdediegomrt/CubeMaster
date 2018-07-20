package com.jaimedediego.cubemaster.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ScrambleConfig {

    private static ScrambleConfig instance;

    private ScrambleConfig() {
    }

    public static ScrambleConfig getInstance() {
        if (instance == null) {
            instance = new ScrambleConfig();
        }
        return instance;
    }

    public void scrambleNxNxN(int size){
        List<String> allSides = new ArrayList<>();
        List<String> extSides = Constants.getInstance().NxNxN;
        for(int i = 0; i < (size/2); i++){
            for(int j = 0; j < extSides.size(); j++){
                allSides.add(extSides.get(j) + (i+1));
            }
        }
        allSides.addAll(extSides);
    }
}
