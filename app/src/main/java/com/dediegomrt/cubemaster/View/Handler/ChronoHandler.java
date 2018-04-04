package com.dediegomrt.cubemaster.View.Handler;

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

    public ChronoHandler(TextView m, TextView s, TextView M, TextView H, LinearLayout minsLayout, LinearLayout hoursLayout) {
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

    public void setTextMillis(String millisStr) {
        millis = millisStr;
    }

    public void setTextSecs(String secsStr) {
        secs = secsStr;
    }

    public void setTextMins(String minsStr) {
        mins = minsStr;
    }

    public void setTextHours(String hoursStr) {
        hours = hoursStr;
    }

    public void minsVisible(Boolean minsVisible){
        this.minsVisible=minsVisible;
    }

    public void hoursVisible(Boolean hoursVisible){
        this.hoursVisible=hoursVisible;
    }

    public void act() {
        super.sendEmptyMessage(0);
    }

}
