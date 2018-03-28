package com.dediegomrt.cubemaster.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dediegomrt.cubemaster.Config.PrefsConfig;
import com.dediegomrt.cubemaster.Methods.PrefsMethods;
import com.dediegomrt.cubemaster.View.Handler.ChronoThread;
import com.dediegomrt.cubemaster.Methods.DatabaseMethods;
import com.dediegomrt.cubemaster.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChronoFragment extends Fragment {

    private static MediaPlayer mp;
    private RelativeLayout animatedLine;
    private LinearLayout layVisible;
    private LinearLayout layVisible2;
    private TextView hours;
    private TextView mins;
    private TextView secs;
    private TextView millis;

    ChronoThread thread = null;
    private boolean holded=false;

    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chrono, container, false);

        PrefsConfig.getInstance().setContext(v.getContext());
        DatabaseMethods.getInstance().setDatabase(getActivity());

        mp = MediaPlayer.create(getActivity(), R.raw.beep);
        RelativeLayout c = (RelativeLayout) v.findViewById(R.id.chronoLayout);
        hours= (TextView)v.findViewById(R.id.hours);
        mins= (TextView)v.findViewById(R.id.mins);
        secs = (TextView)v.findViewById(R.id.secs);
        millis = (TextView)v.findViewById(R.id.millis);
        layVisible = (LinearLayout)v.findViewById(R.id.layVisible);
        layVisible2 = (LinearLayout)v.findViewById(R.id.layVisible2);
        animatedLine = (RelativeLayout)v.findViewById(R.id.animatedLine);
        final RadioGroup activityMenu = (RadioGroup)getActivity().findViewById(R.id.menu_layout);

        if(PrefsMethods.getInstance().isColorActivated()) {
            animatedLine.setVisibility(View.VISIBLE);
        } else {
            animatedLine.setVisibility(View.GONE);
        }

        c.setOnTouchListener(new View.OnTouchListener(){
            final Handler handler = new Handler();
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    animatedLine.setBackgroundResource(R.drawable.background2);
                    if(thread!=null&&thread.isAlive()){
                        animatedLine.setBackgroundResource(R.drawable.background);
                        thread.setNo(true);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                String time;
                                if(mins.getText().equals("0")){
                                    time = secs.getText().toString()+'.'+millis.getText().toString();
                                } else {
                                    if(hours.getText().equals("0")){
                                        time = mins.getText().toString()+':'+secs.getText().toString()+'.'+millis.getText().toString();
                                    } else{
                                        time = hours.getText().toString()+':'+mins.getText().toString()+':'+secs.getText().toString()+'.'+millis.getText().toString();
                                    }
                                }
                                for (int i = 0; i < activityMenu.getChildCount(); i++) {
                                    activityMenu.getChildAt(i).setEnabled(true);
                                }
                                DatabaseMethods.getInstance().saveData(time, getDateTime());
                            }
                        }, 10);
                    } else {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                holded = true;
                                if(holded) animatedLine.setBackgroundResource(R.drawable.background3);
                            }
                        }, PrefsMethods.getInstance().getFreezingTime());
                    }
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    handler.removeCallbacksAndMessages(null);
                    if(holded) {
                        for (int i = 0; i < activityMenu.getChildCount(); i++) {
                            activityMenu.getChildAt(i).setEnabled(false);
                        }
                        thread = new ChronoThread(millis, secs, mins, hours, layVisible, layVisible2, mp);
                        thread.start();
                    } else {
                        animatedLine.setBackgroundResource(R.drawable.background);
                    }
                    Log.d("holded", String.valueOf(holded));
                    holded = false;
                    return false;
                }
                return true;
            }
        });

        return v;
    }

    private String getDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("es", "ES"));
        Date date = new Date();
        return format.format(date);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
