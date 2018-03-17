package com.dediegomrt.cubemaster.View.Handler;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChronoHandler extends Handler{
    private Boolean visible;
    private Boolean visible2;
    private String millis;
    private String secs;
    private String mins;
    private String hours;
    private TextView millisText;
    private TextView secsText;
    private TextView minsText;
    private TextView hoursText;
    private LinearLayout layVisible;
    private LinearLayout layVisible2;

    public ChronoHandler(TextView m, TextView s, TextView M, TextView H, LinearLayout lay, LinearLayout lay2) {
        millisText = m;
        secsText = s;
        minsText = M;
        hoursText = H;
        layVisible = lay;
        layVisible2 = lay2;
        visible = false;
        visible2 = false;
    }

    public void handleMessage(Message msg)
    {
        millisText.setText(millis);
        secsText.setText(secs);
        minsText.setText(mins);
        hoursText.setText(hours);
        if(visible){
            layVisible.setVisibility(View.VISIBLE);
        } else {
            layVisible.setVisibility(View.GONE);
        }
        if(visible2){
            layVisible2.setVisibility(View.VISIBLE);
        } else {
            layVisible2.setVisibility(View.GONE);
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

    public void minsVisible(Boolean visible){
        this.visible=visible;
    }

    public void hoursVisible(Boolean visible){
        this.visible2=visible;
    }

    public void act() {
        super.sendEmptyMessage(0);
    }

}
