package com.dediegomrt.cubemaster.View;

import android.animation.LayoutTransition;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dediegomrt.cubemaster.Config.PrefsConfig;
import com.dediegomrt.cubemaster.Methods.PrefsMethods;
import com.dediegomrt.cubemaster.Utils.Session;
import com.dediegomrt.cubemaster.View.Handler.ChronoThread;
import com.dediegomrt.cubemaster.Methods.DatabaseMethods;
import com.dediegomrt.cubemaster.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChronoFragment extends Fragment {

    private static MediaPlayer mp;
    private RelativeLayout animatedLine;
    private RelativeLayout infoContainer;
    private ImageButton infoButton;
    private LinearLayout infoLayout;
    private LinearLayout hoursLayout;
    private LinearLayout minsLayout;
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
        final View v = inflater.inflate(R.layout.fragment_chrono, container, false);

        PrefsConfig.getInstance().setContext(v.getContext());
        DatabaseMethods.getInstance().setDatabase(getActivity());

        infoButton = (ImageButton) v.findViewById(R.id.info_button);
        infoContainer = (RelativeLayout) v.findViewById(R.id.info_container);
        infoLayout = (LinearLayout) v.findViewById(R.id.info_layout);
        final ViewGroup.LayoutParams params = infoLayout.getLayoutParams();

        mp = MediaPlayer.create(getActivity(), R.raw.beep);
        final RelativeLayout chronoScreen = (RelativeLayout) v.findViewById(R.id.chrono_layout);
        hours= (TextView)v.findViewById(R.id.hours);
        mins= (TextView)v.findViewById(R.id.mins);
        secs = (TextView)v.findViewById(R.id.secs);
        millis = (TextView)v.findViewById(R.id.millis);
        hoursLayout = (LinearLayout)v.findViewById(R.id.hours_layout);
        minsLayout = (LinearLayout)v.findViewById(R.id.mins_layout);
        animatedLine = (RelativeLayout)v.findViewById(R.id.animated_line);
        final RadioGroup activityMenu = (RadioGroup)getActivity().findViewById(R.id.menu_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            LayoutTransition layoutTransition = infoContainer.getLayoutTransition();
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        }

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(infoLayout.getHeight()!=0){
                    infoButton.setColorFilter(null);
                    infoLayout.setLayoutParams(params);
                    chronoScreen.setEnabled(true);
                } else {
                    infoButton.setColorFilter(ResourcesCompat.getColor(getResources(), Session.getInstance().lightColorTheme, null));
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    infoLayout.setLayoutParams(layoutParams);
                    chronoScreen.setEnabled(false);
                }
            }
        });

        chronoScreen.setOnTouchListener(new View.OnTouchListener(){
            final Handler handler = new Handler();
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    animatedLine.setBackgroundResource(R.drawable.background_timer_freeze);
                    if(thread!=null&&thread.isAlive()){
                        animatedLine.setBackgroundResource(R.drawable.background_timer_off);
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
                                infoButton.setEnabled(true);
                                DatabaseMethods.getInstance().saveData(time, getDateTime());
                            }
                        }, 10);
                    } else {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                holded = true;
                                if(holded) animatedLine.setBackgroundResource(R.drawable.background_timer_on);
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
                        infoButton.setEnabled(false);
                        thread = new ChronoThread(millis, secs, mins, hours, minsLayout, hoursLayout, mp);
                        thread.start();
                    } else {
                        animatedLine.setBackgroundResource(R.drawable.background_timer_off);
                    }
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
