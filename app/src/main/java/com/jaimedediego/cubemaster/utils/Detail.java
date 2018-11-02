package com.jaimedediego.cubemaster.utils;

import android.graphics.Picture;

import com.jaimedediego.cubemaster.methods.StatsMethods;

import java.util.Arrays;
import java.util.Comparator;

public class Detail {
    private String time;
    private String date;
    private String scramble;
    private Picture image;
    private int numSolve;

    public Detail(String time, String date, String scramble, Picture image, int numSolve) {
        this.time = time;
        this.date = date;
        this.scramble = scramble;
        this.image = image;
        this.numSolve = numSolve;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getScramble() {
        return scramble;
    }

    public void setScramble(String scramble) {
        this.scramble = scramble;
    }

    public int getNumSolve() {
        return numSolve;
    }

    public void setNumSolve(int numSolve) {
        this.numSolve = numSolve;
    }

    public Picture getImage() {
        return image;
    }

    public void setImage(Picture image) {
        this.image = image;
    }

    public static Comparator<Detail> TimeComparatorAsc = new Comparator<Detail>() {

        public int compare(Detail detail1, Detail detail2) {
            int time1 = StatsMethods.getInstance().timesListToMillis(Arrays.asList(detail1.getTime())).get(0);
            int time2 = StatsMethods.getInstance().timesListToMillis(Arrays.asList(detail2.getTime())).get(0);
            return time1 - time2;
        }
    };

    public static Comparator<Detail> TimeComparatorDesc = new Comparator<Detail>() {

        public int compare(Detail detail1, Detail detail2) {
            int time1 = StatsMethods.getInstance().timesListToMillis(Arrays.asList(detail1.getTime())).get(0);
            int time2 = StatsMethods.getInstance().timesListToMillis(Arrays.asList(detail2.getTime())).get(0);
            return time2 - time1;
        }
    };
}
