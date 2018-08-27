package com.jaimedediego.cubemaster.utils;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public String freezingTimeSelector[]= {"0.0s", "0.1s", "0.2s", "0.3s", "0.4s", "0.5s", "0.6s", "0.7s", "0.8s", "0.9s", "1.0s"};

    public int maxScrambleLength = 60;

    public List<String> NxNxN = Arrays.asList("U", "F", "R", "B", "L", "D");

    public List<String> NxNxNRotations = Arrays.asList("X", "Y", "Z");

    public List<String> oddNumberNxNxN = Arrays.asList("M", "E", "S");

    private static Constants instance;

    private Constants() {
    }

    public static Constants getInstance() {
        if (instance == null) {
            instance = new Constants();
        }
        return instance;
    }

}

