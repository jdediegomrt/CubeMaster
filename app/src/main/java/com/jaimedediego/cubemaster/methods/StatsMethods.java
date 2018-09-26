package com.jaimedediego.cubemaster.methods;

import com.jaimedediego.cubemaster.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class StatsMethods {

    private static StatsMethods instance;

    private StatsMethods() {
    }

    public static StatsMethods getInstance() {
        if (instance == null) {
            instance = new StatsMethods();
        }
        return instance;
    }

    public String getBestTime(String puzzleName) {
        List<String> times;
        if (puzzleName == null) {
            times = DatabaseMethods.getInstance().getTimes();
        } else {
            times = DatabaseMethods.getInstance().getTimesByName(puzzleName);
        }
        if (!times.isEmpty()) {
            return formatMillis(Float.valueOf(Collections.min(timesListToMillis(times))));
        } else {
            return "...";
        }
    }

    public String getWorstTime(String puzzleName) {
        List<String> times;
        if (puzzleName == null) {
            times = DatabaseMethods.getInstance().getTimes();
        } else {
            times = DatabaseMethods.getInstance().getTimesByName(puzzleName);
        }
        if (!times.isEmpty()) {
            return formatMillis(Float.valueOf(Collections.max(timesListToMillis(times))));
        } else {
            return "...";
        }
    }

    public int countTimes(String puzzleName) {
        if (puzzleName == null) {
            return DatabaseMethods.getInstance().countTimes();
        } else {
            return DatabaseMethods.getInstance().countTimesByName(puzzleName);
        }
    }

    public String getAverage(String puzzleName, int num) {
        List<String> times;
        if (puzzleName == null) {
            times = DatabaseMethods.getInstance().getTimes();
        } else {
            times = DatabaseMethods.getInstance().getTimesByName(puzzleName);
        }
        if (!times.isEmpty()) {
            int sum = 0;
            if (num != 0) {
                if (num <= times.size()) {
                    for (int i = 0; i < num; i++) {
                        StringTokenizer tokenizer = new StringTokenizer(times.get(i), ":.");
                        switch (tokenizer.countTokens()) {
                            case 2:
                                sum += Integer.parseInt(tokenizer.nextToken() + tokenizer.nextToken());
                                break;
                            case 3:
                                sum += Integer.parseInt(String.valueOf(Integer.parseInt(tokenizer.nextToken()) * 60 + Integer.parseInt(tokenizer.nextToken())) + tokenizer.nextToken());
                                break;
                            case 4:
                                sum += Integer.parseInt(String.valueOf(Integer.parseInt(tokenizer.nextToken()) * 3600 + Integer.parseInt(tokenizer.nextToken()) * 60 + Integer.parseInt(tokenizer.nextToken())) + tokenizer.nextToken());
                                break;
                            default:
                                break;
                        }
                    }
                }
                return formatMillis((float) sum / num);
            } else {
                for (int i = 0; i < times.size(); i++) {
                    StringTokenizer tokenizer = new StringTokenizer(times.get(i), ":.");
                    switch (tokenizer.countTokens()) {
                        case 2:
                            sum += Integer.parseInt(tokenizer.nextToken() + tokenizer.nextToken());
                            break;
                        case 3:
                            sum += Integer.parseInt(String.valueOf(Integer.parseInt(tokenizer.nextToken()) * 60 + Integer.parseInt(tokenizer.nextToken())) + tokenizer.nextToken());
                            break;
                        case 4:
                            sum += Integer.parseInt(String.valueOf(Integer.parseInt(tokenizer.nextToken()) * 3600 + Integer.parseInt(tokenizer.nextToken()) * 60 + Integer.parseInt(tokenizer.nextToken())) + tokenizer.nextToken());
                            break;
                        default:
                            break;
                    }
                }
                return formatMillis((float) sum / times.size());
            }
        } else {
            return "...";
        }
    }

    public String formatMillis(Float millis) {
        return formatMillis(millis, Constants.getInstance().MILLIS_FORMATTING);
    }

    public String formatMillis(Float millis, int mode) {
        String formattedTime;
        int milli = (int) (millis % 1000);
        int secs = ((Float) (millis / 1000)).intValue();
        secs = secs % 60;
        int mins = ((Float) (millis / 1000 / 60)).intValue();
        mins = mins % 60;
        int hours = ((Float) (millis / 60 / 1000 / 60)).intValue();
        if (hours == 0) {
            if (mins == 0) {
                if (secs == 0 && millis == 0) {
                    formattedTime = "...";
                }
                formattedTime = String.valueOf(secs) + '.' + String.format("%03d", milli);
            } else {
                formattedTime = String.valueOf(mins) + ':' + String.format("%02d", secs) + '.' + String.format("%03d", milli);
            }
        } else {
            formattedTime =  String.valueOf(hours) + ':' + String.format("%02d", mins) + ':' + String.format("%02d", secs) + '.' + String.format("%03d", milli);
        }

        switch (mode) {
            case 0:
                return formattedTime;
            case 1:
                if(mins!=0){
                    return formattedTime.substring(0, formattedTime.length()-4);
                } else {
                    return String.valueOf(mins) + ':' + formattedTime.substring(0, formattedTime.length()-4);
                }
            default:
                return null;
        }
    }

    public List<Integer> timesListToMillis(List<String> times) {
        List<Integer> timesToMillis = new ArrayList<>();
        for (int i = 0; i < times.size(); i++) {
            timesToMillis.add(timeToMillis(times.get(i)));
        }
        return timesToMillis;
    }

    public Integer timeToMillis(String time) {
        Integer millis = 0;
        StringTokenizer tokenizer = new StringTokenizer(time, ":.");
        switch (tokenizer.countTokens()) {
            case 2:
                millis = Integer.parseInt(tokenizer.nextToken() + tokenizer.nextToken());
                break;
            case 3:
                millis = Integer.parseInt(String.valueOf(Integer.parseInt(tokenizer.nextToken()) * 60 + Integer.parseInt(tokenizer.nextToken())) + tokenizer.nextToken());
                break;
            case 4:
                millis = Integer.parseInt(String.valueOf(Integer.parseInt(tokenizer.nextToken()) * 3600 + Integer.parseInt(tokenizer.nextToken()) * 60 + Integer.parseInt(tokenizer.nextToken())) + tokenizer.nextToken());
                break;
            default:
                break;
        }
        return millis;
    }
}
