package com.jaimedediego.cubemaster.view.handler;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaimedediego.cubemaster.methods.PrefsMethods;

public class InspectionHandler extends Handler {
    private Boolean timeVisible;
    private Boolean plus2Visible;
    private Boolean dnfVisible;
    private Boolean millisVisible;
    private String secs;
    private TextView secsText;
    private TextView plus2;
    private TextView dnf;
    private LinearLayout millisLayout;
    private LinearLayout timeLayout;

    public InspectionHandler(TextView s, LinearLayout millisLayout, LinearLayout timeLayout, TextView plus2, TextView dnf) {
        secsText = s;
        this.millisLayout = millisLayout;
        this.timeLayout = timeLayout;
        this.plus2 = plus2;
        this.dnf = dnf;
        millisVisible = PrefsMethods.getInstance().getInspectionTime() == 0;
        timeVisible = true;
        plus2Visible = false;
        dnfVisible = false;
    }

    public void handleMessage(Message msg) {
        secsText.setText(secs);
        if (timeVisible) {
            timeLayout.setVisibility(View.VISIBLE);
        } else {
            timeLayout.setVisibility(View.GONE);
        }
        if (millisVisible) {
            millisLayout.setVisibility(View.VISIBLE);
        } else {
            millisLayout.setVisibility(View.GONE);
        }
        if (plus2Visible) {
            plus2.setVisibility(View.VISIBLE);
        } else {
            plus2.setVisibility(View.GONE);
        }
        if (dnfVisible) {
            dnf.setVisibility(View.VISIBLE);
        } else {
            dnf.setVisibility(View.GONE);
        }
    }

    void setTextSecs(String secsStr) {
        secs = secsStr;
    }

    void millisVisible(Boolean millisVisible) {
        this.millisVisible = millisVisible;
    }

    void timeVisible(Boolean timeVisible) {
        this.timeVisible = timeVisible;
    }

    void plus2Visible(Boolean plus2Visible) {
        this.plus2Visible = plus2Visible;
    }

    void dnfVisible(Boolean dnfVisible) {
        this.dnfVisible = dnfVisible;
    }

    void act() {
        super.sendEmptyMessage(0);
    }

}
