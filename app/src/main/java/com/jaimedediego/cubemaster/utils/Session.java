package com.jaimedediego.cubemaster.utils;

import com.caverock.androidsvg.SVG;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;

import java.util.List;

public class Session {

    private static Session instance;

    private int currentUserId = 1;
    private int currentPuzzleId;
    private List<String> currentPuzzleNotation = null;
    private String currentPuzzleScramble = null;

    private int darkColorTheme = 0;
    private int lightColorTheme = 0;
    private int lighterColorTheme = 0;


    private String currentScramble = "";
    private SVG currentScrambleSvg;
//    private String nextScramble = "";
//    private SVG nextScrambleSvg;

    private Session() {
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
            instance.currentPuzzleId = DatabaseMethods.getInstance().setDefaultCurrentPuzzle();
        }
        return instance;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }


    public int getCurrentPuzzleId() {
        return currentPuzzleId;
    }

    public void setCurrentPuzzleId(int currentPuzzleId) {
        this.currentPuzzleId = currentPuzzleId;
    }

    public int getDarkColorTheme() {
        return darkColorTheme;
    }

    public void setDarkColorTheme(int darkColorTheme) {
        this.darkColorTheme = darkColorTheme;
    }

    public int getLightColorTheme() {
        return lightColorTheme;
    }

    public void setLightColorTheme(int lightColorTheme) {
        this.lightColorTheme = lightColorTheme;
    }

    public int getLighterColorTheme() {
        return lighterColorTheme;
    }

    public void setLighterColorTheme(int lighterColorTheme) {
        this.lighterColorTheme = lighterColorTheme;
    }

    public String getCurrentScramble() {
        return currentScramble;
    }

    public void setCurrentScramble(String currentScramble) {
        this.currentScramble = currentScramble;
    }

    public SVG getCurrentScrambleSvg() {
        return currentScrambleSvg;
    }

    public void setCurrentScrambleSvg(SVG currentScrambleSvg) {
        this.currentScrambleSvg = currentScrambleSvg;
    }

//    public String getNextScramble() {
//        return nextScramble;
//    }
//
//    public void setNextScramble(String nextScramble) {
//        this.nextScramble = nextScramble;
//    }
//
//    public SVG getNextScrambleSvg() {
//        return nextScrambleSvg;
//    }
//
//    public void setNextScrambleSvg(SVG nextScrambleSvg) {
//        this.nextScrambleSvg = nextScrambleSvg;
//    }
}