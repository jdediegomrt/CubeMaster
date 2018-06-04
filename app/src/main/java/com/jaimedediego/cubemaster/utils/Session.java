package com.jaimedediego.cubemaster.utils;

import com.jaimedediego.cubemaster.methods.DatabaseMethods;

public class Session {

    private static Session instance;

    public int currentUserId=1;
    public int currentPuzzleId;

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
}