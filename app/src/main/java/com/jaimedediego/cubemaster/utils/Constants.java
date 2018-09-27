package com.jaimedediego.cubemaster.utils;

import java.util.Arrays;
import java.util.List;

public class Constants {
    private static Constants instance;

    private Constants() {
    }

    public static Constants getInstance() {
        if (instance == null) {
            instance = new Constants();
        }
        return instance;
    }

    //Formatting constants
    public int MILLIS_FORMATTING = 0;

    public int SECS_FORMATTING = 1;

    public int MINS_FORMATTING = 2;

    public int HOURS_FORMATTING = 3;

    //Scramble constants
    public int maxScrambleLength = 60;

    public List<String> NxNxN = Arrays.asList("U", "F", "R", "B", "L", "D");

    public List<String> NxNxN_ROTATIONS = Arrays.asList("X", "Y", "Z");

    public List<String> ODD_NUMBER_NxNxN = Arrays.asList("M", "E", "S");

    //Time selector constant
    public String FREEZING_TIME_SELECTOR[] = {"0.0s", "0.1s", "0.2s", "0.3s", "0.4s", "0.5s", "0.6s", "0.7s", "0.8s", "0.9s", "1.0s"};

    //Toasts timing constants
    public int TOAST_MEDIUM_DURATION = 700;
    public int TOAST_SHORT_DURATION = 500;

}

