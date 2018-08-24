package com.jaimedediego.cubemaster.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.methods.PrefsMethods;
import com.jaimedediego.cubemaster.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScrambleConfig {

    private static ScrambleConfig instance;
    public List<String> puzzlesWithScramble = Arrays.asList("3x3x3", "2x2x2", "4x4x4", "5x5x5");

    private ScrambleConfig() {
    }

    public static ScrambleConfig getInstance() {
        if (instance == null) {
            instance = new ScrambleConfig();
        }
        return instance;
    }

}
