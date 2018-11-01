package com.jaimedediego.cubemaster.utils;

import net.gnehzr.tnoodle.scrambles.Puzzle;
import net.gnehzr.tnoodle.scrambles.PuzzlePlugins;
import net.gnehzr.tnoodle.utils.BadLazyClassDescriptionException;
import net.gnehzr.tnoodle.utils.LazyInstantiator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    public final int MILLIS_FORMATTING = 0;

    public final int SECS_FORMATTING = 1;

    public final int MINS_FORMATTING = 2;

    public final int HOURS_FORMATTING = 3;

    //Scramble constants

    public final List<String> CUBEMASTER_DEFAULT_PUZZLES_NAMES = Arrays.asList("3x3x3", "2x2x2", "4x4x4", "5x5x5", "6x6x6", "7x7x7", "Megamynx", "Piramynx", "Skewb", "Square-1", "Clock");

    public SortedMap<String, LazyInstantiator<Puzzle>> WCA_PUZZLES;

    public ArrayList<String> WCA_PUZZLES_SHORT_NAMES;

    public ArrayList<String> WCA_PUZZLES_LONG_NAMES;

    {
        try {
            WCA_PUZZLES = new TreeMap<>(PuzzlePlugins.getScramblers());
            WCA_PUZZLES_SHORT_NAMES = new ArrayList<>(WCA_PUZZLES.keySet());

            WCA_PUZZLES_LONG_NAMES = new ArrayList<>();
            for (int i = 0; i < WCA_PUZZLES_SHORT_NAMES.size(); i++) {
                if(CUBEMASTER_DEFAULT_PUZZLES_NAMES.contains(PuzzlePlugins.getScramblerLongName(WCA_PUZZLES_SHORT_NAMES.get(i)))) {
                    WCA_PUZZLES_LONG_NAMES.add(PuzzlePlugins.getScramblerLongName(WCA_PUZZLES_SHORT_NAMES.get(i)));
                }
            }

        } catch (BadLazyClassDescriptionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Time selector constant
    public final String FREEZING_TIME_SELECTOR[] = {"0.0s", "0.1s", "0.2s", "0.3s", "0.4s", "0.5s", "0.6s", "0.7s", "0.8s", "0.9s", "1.0s"};

    //Toasts timing constants
    public final int TOAST_LONG_DURATION = 1500;
    public final int TOAST_MEDIUM_DURATION = 1000;
    public final int TOAST_SHORT_DURATION = 500;

}

