package com.jaimedediego.cubemaster.utils;

import net.gnehzr.tnoodle.scrambles.Puzzle;
import net.gnehzr.tnoodle.scrambles.PuzzlePlugins;
import net.gnehzr.tnoodle.utils.BadLazyClassDescriptionException;
import net.gnehzr.tnoodle.utils.LazyInstantiator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

public class Constants {
    private static Constants instance;
    private static final String TAG = Constants.class.getName();

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
    public SortedMap<String, LazyInstantiator<Puzzle>> puzzles;

    public ArrayList<String> shortNames;

    {
        try {
            puzzles = new TreeMap<>(PuzzlePlugins.getScramblers());
            shortNames = new ArrayList<>(puzzles.keySet());

        } catch (BadLazyClassDescriptionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Time selector constant
    public String FREEZING_TIME_SELECTOR[] = {"0.0s", "0.1s", "0.2s", "0.3s", "0.4s", "0.5s", "0.6s", "0.7s", "0.8s", "0.9s", "1.0s"};

    //Toasts timing constants
    public int TOAST_MEDIUM_DURATION = 1000;

    public int TOAST_SHORT_DURATION = 500;

}

