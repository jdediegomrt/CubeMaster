package com.dediegomrt.cubemaster.Utils;

import com.dediegomrt.cubemaster.Methods.StatsMethods;

import java.util.Arrays;
import java.util.Comparator;

public class Detail {
    private String time;
    private String date;
    private int numSolve;

    public Detail(String time, String date, int numSolve) {
        this.time = time;
        this.date = date;
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

    public int getNumSolve() {
        return numSolve;
    }

    public void setNumSolve(int numSolve) {
        this.numSolve = numSolve;
    }

    public static Comparator<Detail> TimeComparatorAsc = new Comparator<Detail>() {

        public int compare(Detail detail1, Detail detail2) {
            int time1 = StatsMethods.getInstance().timesListToMillis(Arrays.asList(detail1.getTime())).get(0);
            int time2 = StatsMethods.getInstance().timesListToMillis(Arrays.asList(detail2.getTime())).get(0);
            return time1-time2;
        }
    };

    public static Comparator<Detail> TimeComparatorDesc = new Comparator<Detail>() {

        public int compare(Detail detail1, Detail detail2) {
            int time1 = StatsMethods.getInstance().timesListToMillis(Arrays.asList(detail1.getTime())).get(0);
            int time2 = StatsMethods.getInstance().timesListToMillis(Arrays.asList(detail2.getTime())).get(0);
            return time2-time1;
        }
    };
}
