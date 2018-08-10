package com.jaimedediego.cubemaster.utils;

import java.util.Arrays;
import java.util.List;

public class Constants {

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

