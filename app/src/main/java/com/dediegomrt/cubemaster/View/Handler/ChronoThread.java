package com.dediegomrt.cubemaster.View.Handler;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dediegomrt.cubemaster.Methods.PrefsMethods;

public class ChronoThread extends Thread {
    private MediaPlayer mp;
    private int secs, mins, hours;
    private boolean no;
    private ChronoHandler handler;

    public ChronoThread(TextView millis, TextView secs, TextView mins, TextView hours, LinearLayout lay,
                        LinearLayout lay2, MediaPlayer mp) {
        no=false;
        this.secs=0;
        this.mins=0;
        this.hours=0;
        handler = new ChronoHandler(millis, secs, mins, hours, lay, lay2);
        handler.setTextSecs("0");
        handler.setTextMins("0");
        handler.setTextHours("0");
        this.mp=mp;
    }

    public void run(){
        long start = System.currentTimeMillis();
        while(!no){
            long millis = System.currentTimeMillis() - start;
            if(millis <1000){
                handler.setTextMillis(String.format("%03d", millis));
            } else{
                secs++;
                if(secs<60){
                    if(mins==0){
                        handler.setTextSecs(String.valueOf(secs));
                    } else {
                        handler.setTextSecs(String.format("%02d", secs));
                    }
                } else {
                    mins++;
                    if(PrefsMethods.getInstance().isBeepActivated()) {
                        mp.start();
                    }
                    secs = 0;
                    handler.setTextSecs("00");
                    if(mins<60) {
                        if(hours==0){
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
            try {
                Thread.sleep(1);
            }catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            handler.act();
        }
    }

    public void setNo(boolean no) {
        this.no = no;
    }
}
