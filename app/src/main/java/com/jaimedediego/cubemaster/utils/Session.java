package com.jaimedediego.cubemaster.utils;

import com.caverock.androidsvg.SVG;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;

import java.util.List;

public class Session {

    private static Session instance;

    public int currentUserId = 1;
    public int currentPuzzleId;
    public List<String> currentPuzzleNotation = null;
    public String currentPuzzleScramble = null;

    public int darkColorTheme = 0;
    public int lightColorTheme = 0;
    public int lighterColorTheme = 0;


    public String CURRENT_SCRAMBLE = "";
    public SVG CURRENT_SCRAMBLE_SVG;
    public String NEXT_SCRAMBLE = "";
    public SVG NEXT_SCRAMBLE_SVG;

    private Session() {
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
            instance.currentPuzzleId = DatabaseMethods.getInstance().setDefaultCurrentPuzzle();
        }
        return instance;
    }
}