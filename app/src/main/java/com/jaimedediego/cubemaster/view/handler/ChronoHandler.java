package com.jaimedediego.cubemaster.view.handler;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChronoHandler extends Handler{
    private Boolean minsVisible;
    private Boolean hoursVisible;
    private String millis;
    private String secs;
    private String mins;
    private String hours;
    private TextView millisText;
    private TextView secsText;
    private TextView minsText;
    private TextView hoursText;
    private LinearLayout minsLayout;
    private LinearLayout hoursLayout;

    ChronoHandler(TextView m, TextView s, TextView M, TextView H, LinearLayout minsLayout, LinearLayout hoursLayout) {
        millisText = m;
        secsText = s;
        minsText = M;
        hoursText = H;
        this.minsLayout = minsLayout;
        this.hoursLayout = hoursLayout;
        minsVisible = false;
        hoursVisible = false;
    }

    public void handleMessage(Message msg)
    {
        millisText.setText(millis);
        secsText.setText(secs);
        minsText.setText(mins);
        hoursText.setText(hours);
        if(minsVisible){
            minsLayout.setVisibility(View.VISIBLE);
        } else {
            minsLayout.setVisibility(View.GONE);
        }
        if(hoursVisible){
            hoursLayout.setVisibility(View.VISIBLE);
        } else {
            hoursLayout.setVisibility(View.GONE);
        }
    }

    void setTextMillis(String millisStr) {
        millis = millisStr;
    }

    void setTextSecs(String secsStr) {
        secs = secsStr;
    }

    void setTextMins(String minsStr) {
        mins = minsStr;
    }

    void setTextHours(String hoursStr) {
        hours = hoursStr;
    }

    void minsVisible(Boolean minsVisible){
        this.minsVisible=minsVisible;
    }

    void hoursVisible(Boolean hoursVisible){
        this.hoursVisible=hoursVisible;
    }

    void act() {
        super.sendEmptyMessage(0);
    }

}
