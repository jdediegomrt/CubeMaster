package com.jaimedediego.cubemaster.utils;

import com.jaimedediego.cubemaster.methods.DatabaseMethods;

import java.util.List;

public class Session {

    private static Session instance;

    public int currentUserId=1;
    public int currentPuzzleId;
    public List<String> currentPuzzleNotation = null;
    public String currentPuzzleScramble = null;

    public int darkColorTheme=0;
    public int lightColorTheme=0;
    public int lighterColorTheme=0;

    private Session() {}

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
            instance.currentPuzzleId = DatabaseMethods.getInstance().setDefaultCurrentPuzzle();
        }
        return instance;
    }

    public String scramble2x2x2="";
    public String scramble3x3x3="";
    public String scramble4x4x4="";
    public String scramble5x5x5="";
}