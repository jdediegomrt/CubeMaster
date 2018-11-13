package com.jaimedediego.cubemaster.view.handler;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaimedediego.cubemaster.methods.PrefsMethods;
import com.jaimedediego.cubemaster.utils.Constants;

public class InspectionThread extends Thread {
    private int secs;
    private boolean finish;
    private boolean dnf = false;
    private boolean plus2 = false;
    private InspectionHandler handler;

    public InspectionThread(TextView secs,
                            LinearLayout millisLayout,
                            LinearLayout timeLayout, TextView plus2, TextView dnf) {
        finish = false;
        handler = new InspectionHandler(secs, millisLayout, timeLayout, plus2, dnf);
        String inspection = Constants.getInstance().INSPECTION_TIME_SECS.get(PrefsMethods.getInstance().getInspectionTime());
        handler.setTextSecs(inspection);
        handler.millisVisible(false);
        this.secs = Integer.parseInt(inspection);
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
            millis = System.currentTimeMillis() - start;
            if (millis >= 1000) {
                secs--;
                if (secs > 0) {
                    handler.setTextSecs(String.valueOf(secs));
                } else if (secs > -2){
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
        if(!dnf && !plus2){
            handler.millisVisible(true);
            handler.timeVisible(true);
            handler.plus2Visible(false);
            handler.dnfVisible(false);
        }
        handler.act();
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
