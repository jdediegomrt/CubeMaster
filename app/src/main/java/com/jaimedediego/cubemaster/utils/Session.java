package com.jaimedediego.cubemaster.utils;

import android.graphics.drawable.Drawable;

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
    public Drawable CURRENT_SCRAMBLE_DRAWABLE;
    public String NEXT_SCRAMBLE = "";
    public Drawable NEXT_SCRAMBLE_DRAWABLE;

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