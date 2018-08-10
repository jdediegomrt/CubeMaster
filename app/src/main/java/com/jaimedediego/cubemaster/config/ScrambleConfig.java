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

    public List<String> scrambleNxNxN(int size){
        List<String> allSides = new ArrayList<>();
        if(size%2!=0){
            allSides.addAll(Constants.getInstance().oddNumberNxNxN);
        }
        List<String> extSides = Constants.getInstance().NxNxN;
        for(int i = 0; i < (size/2); i++){
            for(int j = 0; j < extSides.size(); j++){
                if(i>0) {
                    allSides.add(extSides.get(j) + (i + 1));
                    allSides.add((i + 1) + extSides.get(j));
                }
            }
        }
        allSides.addAll(extSides);
        return allSides;
    }
}
