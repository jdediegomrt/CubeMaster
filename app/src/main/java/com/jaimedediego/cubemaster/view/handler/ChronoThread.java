package com.jaimedediego.cubemaster.view.handler;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.methods.PrefsMethods;
import com.jaimedediego.cubemaster.utils.OnThreadFinished;
import com.jaimedediego.cubemaster.utils.StringUtils;

public class ChronoThread extends Thread {
    private OnThreadFinished listener;

    private MediaPlayer mp;
    private int secs;
    private int mins;
    private int hours;
    private boolean finish;
    private boolean pause;
    private boolean pauseFlag;
    private ChronoHandler handler;

    public ChronoThread(Context context, ChronoHandler handler, boolean plus2, OnThreadFinished listener) {
        this.listener = listener;
        finish = false;
        pause = false;
        pauseFlag = false;
        this.handler = handler;
        if (plus2) {
            this.secs = 2;
            handler.setTextSecs("2");
        } else {
            this.secs = 0;
            handler.setTextSecs("0");
        }
        this.mins = 0;
        this.hours = 0;
        handler.setTextMins("0");
        handler.setTextHours("0");
        this.mp = MediaPlayer.create(context, R.raw.beep);
    }

    public synchronized void run() {
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
        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Log.e("Thread", "InterruptedException", ex);
        }
        listener.OnThreadFinished();
    }

    public void finalize(boolean finish) {
        this.finish = finish;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean isPaused() {
        return pause;
    }
}
