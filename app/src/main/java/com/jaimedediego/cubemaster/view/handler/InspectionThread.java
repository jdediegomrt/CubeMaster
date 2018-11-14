package com.jaimedediego.cubemaster.view.handler;

import android.content.Context;
import android.util.Log;

import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.methods.PrefsMethods;
import com.jaimedediego.cubemaster.utils.Constants;
import com.jaimedediego.cubemaster.utils.OnThreadFinished;
import com.jaimedediego.cubemaster.utils.StringUtils;

public class InspectionThread extends Thread {
    private OnThreadFinished listener;
    private Context context;
    private int secs;
    private boolean finish;
    private boolean dnf = false;
    private boolean plus2 = false;
    private InspectionHandler handler;

    public InspectionThread(Context context, InspectionHandler handler, OnThreadFinished listener) {
        this.context = context;
        this.listener = listener;
        finish = false;
        this.handler = handler;
        String inspection = Constants.getInstance().INSPECTION_TIME_SECS.get(PrefsMethods.getInstance().getInspectionTime());
        handler.setTextSecs(inspection);
        handler.millisVisible(false);
        handler.timeVisible(true);
        handler.plus2Visible(false);
        handler.dnfVisible(false);
        this.secs = Integer.parseInt(inspection);
    }

    public synchronized void run() {
        long start = System.currentTimeMillis();
        long millis;
        do {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Log.e("Thread", "InterruptedException", ex);
            }
            millis = System.currentTimeMillis() - start;
            if (millis >= 1000) {
                secs--;
                if (secs > 0) {
                    handler.setTextSecs(String.valueOf(secs));
                } else if (secs > -2) {
                    handler.timeVisible(false);
                    handler.plus2Visible(true);
                    plus2 = true;
                } else {
                    handler.plus2Visible(false);
                    handler.dnfVisible(true);
                    plus2 = false;
                    dnf = true;
                    finish = true;
                }
                start = System.currentTimeMillis();
            }
            handler.act();
        } while (!finish);
        if(!dnf){
            handler.setTextSecs("0");
            handler.setTextMillis("000");
            handler.millisVisible(true);
            handler.timeVisible(true);
            handler.plus2Visible(false);
            handler.dnfVisible(false);
        }
        handler.act();
        listener.OnThreadFinished();
    }

    public void finalize(boolean finish) {
        this.finish = finish;
    }

    public boolean isDnf() {
        return dnf;
    }

    public boolean isPlus2() {
        return plus2;
    }
}
