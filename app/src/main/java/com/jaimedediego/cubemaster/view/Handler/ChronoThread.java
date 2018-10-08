package com.jaimedediego.cubemaster.view.Handler;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaimedediego.cubemaster.methods.PrefsMethods;

public class ChronoThread extends Thread {
    private MediaPlayer mp;
    private int secs;
    private int mins;
    private int hours;
    private boolean finish;
    private boolean pause;
    private boolean pauseFlag;
    private ChronoHandler handler;

    public ChronoThread(TextView millis, TextView secs, TextView mins, TextView hours, LinearLayout minsLayout,
                        LinearLayout hoursLayout, MediaPlayer mp) {
        finish = false;
        pause = false;
        pauseFlag = false;
        this.secs = 0;
        this.mins = 0;
        this.hours = 0;
        handler = new ChronoHandler(millis, secs, mins, hours, minsLayout, hoursLayout);
        handler.setTextSecs("0");
        handler.setTextMins("0");
        handler.setTextHours("0");
        this.mp = mp;
    }

    public void run() {
        long start = System.currentTimeMillis();
        long millis = 0;
        do {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Log.e("Thread", "InterruptedException", ex);
            }
            if (pause) {
                pauseFlag = true;
            } else {
                if (pauseFlag) {
                    pauseFlag = false;
                    start = System.currentTimeMillis() - millis;
                }
                millis = System.currentTimeMillis() - start;
                if (millis < 1000) {
                    handler.setTextMillis(String.format("%03d", millis));
                } else {
                    secs++;
                    if (secs < 60) {
                        if (mins == 0) {
                            handler.setTextSecs(String.valueOf(secs));
                        } else {
                            handler.setTextSecs(String.format("%02d", secs));
                        }
                    } else {
                        mins++;
                        if (PrefsMethods.getInstance().isBeepActivated()) {
                            mp.start();
                        }
                        secs = 0;
                        handler.setTextSecs("00");
                        if (mins < 60) {
                            if (hours == 0) {
                                handler.setTextMins(String.valueOf(mins));
                            } else {
                                handler.setTextMins(String.format("%02d", mins));
                            }
                            handler.minsVisible(true);
                        } else {
                            hours++;
                            mins = 0;
                            handler.setTextMins("00");
                            handler.setTextHours(String.valueOf(hours));
                            handler.hoursVisible(true);
                        }
                    }
                    start = System.currentTimeMillis();
                }
            }
            handler.act();
        } while (!finish);
    }

    public void finalize(boolean finish) {
        this.finish = finish;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean getPause() {
        return pause;
    }
}
