package com.jaimedediego.cubemaster.view;

import android.animation.LayoutTransition;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.jaimedediego.cubemaster.config.PrefsConfig;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.methods.PrefsMethods;
import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.utils.Session;
import com.jaimedediego.cubemaster.view.Adapters.ColorsAdapter;
import com.jaimedediego.cubemaster.view.Dialogs.ContactDialog;
import com.jaimedediego.cubemaster.view.Dialogs.RateDialog;
import com.jaimedediego.cubemaster.view.Dialogs.RestartDialog;

public class SettingsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    String freezingTimeSelector[]= {"0.0s", "0.1s", "0.2s", "0.3s", "0.4s", "0.5s", "0.6s", "0.7s", "0.8s", "0.9s", "1.0s"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.contact_us){
            final ContactDialog dialog = new ContactDialog(getActivity());
            dialog.show();
        } else if (id == R.id.rate_us){
            final RateDialog dialog = new RateDialog(getActivity(), DatabaseMethods.getInstance().countAllTimes(), true);
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.settings);

        PrefsConfig.getInstance().setContext(v.getContext());

        final Switch beep = v.findViewById(R.id.beep_switch);
        final Switch pause = v.findViewById(R.id.stopwatch_switch);
        final ImageButton stopwatchInfoButton = v.findViewById(R.id.stopwatch_info);
        final NumberPicker freezingTime = v.findViewById(R.id.frtime_setter);
        final GridView gridView = v.findViewById(R.id.color_gridview);
        final ImageButton frTimeInfoButton = v.findViewById(R.id.frtime_info);
        final LinearLayout settingsLayout = v.findViewById(R.id.settings_layout);
        final TextView frTimeInfoText = v.findViewById(R.id.frtime_info_text);
        final TextView stopwatchInfoText = v.findViewById(R.id.stopwatch_info_text);
        final ViewGroup.LayoutParams frTimeParams = frTimeInfoText.getLayoutParams();
        final ViewGroup.LayoutParams stopwatchParams = stopwatchInfoText.getLayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            LayoutTransition layoutTransition = settingsLayout.getLayoutTransition();
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        }

        freezingTime.setMaxValue(10);
        freezingTime.setMinValue(0);
        freezingTime.setDisplayedValues(freezingTimeSelector);
        freezingTime.setWrapSelectorWheel(true);
        freezingTime.setValue(PrefsMethods.getInstance().getFreezingTime()/100);

        ColorsAdapter adapter = new ColorsAdapter(getActivity());
        gridView.setAdapter(adapter);

        if(PrefsMethods.getInstance().isBeepActivated()){
            beep.setChecked(true);
        }

        if(PrefsMethods.getInstance().isPauseActivated()){
            pause.setChecked(true);
        }

        frTimeInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(frTimeInfoText.getHeight()!=0){
                    frTimeInfoButton.setColorFilter(null);
                    frTimeInfoText.setLayoutParams(frTimeParams);
                } else {
                    frTimeInfoButton.setColorFilter(Session.getInstance().lightColorTheme);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    frTimeInfoText.setLayoutParams(layoutParams);
                }
            }
        });

        beep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(beep.isChecked()){
                    PrefsMethods.getInstance().activateBeep(true);
                } else {
                    PrefsMethods.getInstance().activateBeep(false);
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pause.isChecked()){
                    PrefsMethods.getInstance().activatePause(true);
                } else {
                    PrefsMethods.getInstance().activatePause(false);
                }
            }
        });

        stopwatchInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stopwatchInfoText.getHeight()!=0){
                    stopwatchInfoButton.setColorFilter(null);
                    stopwatchInfoText.setLayoutParams(stopwatchParams);
                } else {
                    stopwatchInfoButton.setColorFilter(Session.getInstance().lightColorTheme);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    stopwatchInfoText.setLayoutParams(layoutParams);
                }
            }
        });

        freezingTime.setOnValueChangedListener( new NumberPicker.
                OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                PrefsMethods.getInstance().setFreezingTime(newVal*100);
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(position!=PrefsMethods.getInstance().getColorAccent()) {
                    final RestartDialog dialog = new RestartDialog(getActivity(), position);
                    dialog.show();
                }
            }
        });

        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
