package com.jaimedediego.cubemaster.view.activities.main.fragments;

import android.animation.LayoutTransition;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.config.PrefsConfig;
import com.jaimedediego.cubemaster.config.ThemeConfig;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.methods.PrefsMethods;
import com.jaimedediego.cubemaster.utils.Constants;
import com.jaimedediego.cubemaster.utils.Session;
import com.jaimedediego.cubemaster.view.activities.detail.DetailActivity;
import com.jaimedediego.cubemaster.view.activities.main.adapters.ColorsAdapter;
import com.jaimedediego.cubemaster.view.activities.main.adapters.IndicatorsSpinnerAdapter;
import com.jaimedediego.cubemaster.view.activities.main.adapters.SortBySpinnerAdapter;
import com.jaimedediego.cubemaster.view.dialogs.ContactDialog;
import com.jaimedediego.cubemaster.view.dialogs.RateDialog;

public class SettingsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.contact_us) {
            final ContactDialog dialog = new ContactDialog(getActivity());
            dialog.show();
        } else if (id == R.id.rate_us) {
            final RateDialog dialog = new RateDialog(getActivity(), DatabaseMethods.getInstance().countAllTimes(), true);
            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.settings);

        PrefsConfig.getInstance().setContext(v.getContext());

        RelativeLayout timerSettings = v.findViewById(R.id.timer_settings);
        timerSettings.setBackgroundColor(Session.getInstance().getLightColorTheme());

        RelativeLayout customizationSettings = v.findViewById(R.id.customization_settings);
        customizationSettings.setBackgroundColor(Session.getInstance().getLightColorTheme());

        final Switch beep = v.findViewById(R.id.beep_switch);
        final Switch pause = v.findViewById(R.id.stopwatch_switch);
        final Switch scramble = v.findViewById(R.id.scramble_switch);
        final ImageButton stopwatchInfoButton = v.findViewById(R.id.stopwatch_info);
        final NumberPicker freezingTime = v.findViewById(R.id.frtime_setter);
        final GridLayout colorGrid = v.findViewById(R.id.color_grid);
        final ImageButton frTimeInfoButton = v.findViewById(R.id.frtime_info);
        final LinearLayout settingsLayout = v.findViewById(R.id.settings_layout);
        final TextView frTimeInfoText = v.findViewById(R.id.frtime_info_text);
        final TextView stopwatchInfoText = v.findViewById(R.id.stopwatch_info_text);
        final ViewGroup.LayoutParams frTimeParams = frTimeInfoText.getLayoutParams();
        final ViewGroup.LayoutParams stopwatchParams = stopwatchInfoText.getLayoutParams();
        final Spinner indicators = v.findViewById(R.id.indicators);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            LayoutTransition layoutTransition = settingsLayout.getLayoutTransition();
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        }

        freezingTime.setMaxValue(10);
        freezingTime.setMinValue(0);
        freezingTime.setDisplayedValues(Constants.getInstance().FREEZING_TIME_SELECTOR);
        freezingTime.setWrapSelectorWheel(true);
        freezingTime.setValue(PrefsMethods.getInstance().getFreezingTime() / 100);

        for (int i = 0; i < ThemeConfig.getInstance().colors().size(); i++) {
            colorGrid.addView(new ColorsAdapter(getActivity()).getView(i, colorGrid));
        }

        if (PrefsMethods.getInstance().isBeepActivated()) {
            beep.setChecked(true);
        }

        if (PrefsMethods.getInstance().isPauseActivated()) {
            pause.setChecked(true);
        }

        if (!Constants.getInstance().WCA_PUZZLES_LONG_NAMES.contains(DatabaseMethods.getInstance().getCurrentPuzzleName())) {
            scramble.setEnabled(false);
        } else {
            if (PrefsMethods.getInstance().isScrambleEnabled()) {
                scramble.setEnabled(true);
                scramble.setChecked(true);
            }
        }

        IndicatorsSpinnerAdapter adapter = new IndicatorsSpinnerAdapter(getContext());
        indicators.setAdapter(adapter);
        indicators.setSelection(PrefsMethods.getInstance().getIndicator());

        indicators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                PrefsMethods.getInstance().setIndicator(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {/*Do nothing*/}
        });

        frTimeInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (frTimeInfoText.getHeight() != 0) {
                    frTimeInfoButton.setColorFilter(null);
                    frTimeInfoText.setLayoutParams(frTimeParams);
                } else {
                    frTimeInfoButton.setColorFilter(Session.getInstance().getLightColorTheme());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    frTimeInfoText.setLayoutParams(layoutParams);
                }
            }
        });

        beep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (beep.isChecked()) {
                    PrefsMethods.getInstance().activateBeep(true);
                } else {
                    PrefsMethods.getInstance().activateBeep(false);
                }
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pause.isChecked()) {
                    PrefsMethods.getInstance().activatePause(true);
                } else {
                    PrefsMethods.getInstance().activatePause(false);
                }
            }
        });

        stopwatchInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stopwatchInfoText.getHeight() != 0) {
                    stopwatchInfoButton.setColorFilter(null);
                    stopwatchInfoText.setLayoutParams(stopwatchParams);
                } else {
                    stopwatchInfoButton.setColorFilter(Session.getInstance().getLightColorTheme());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    stopwatchInfoText.setLayoutParams(layoutParams);
                }
            }
        });

        scramble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scramble.isChecked()) {
                    PrefsMethods.getInstance().setScramble(true);
                } else {
                    PrefsMethods.getInstance().setScramble(false);
                }
            }
        });

        freezingTime.setOnValueChangedListener(new NumberPicker.
                OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                PrefsMethods.getInstance().setFreezingTime(newVal * 100);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
