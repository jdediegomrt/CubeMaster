package com.jaimedediego.cubemaster.view.activities.main.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caverock.androidsvg.SVGImageView;
import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.config.PrefsConfig;
import com.jaimedediego.cubemaster.config.ScrambleConfig;
import com.jaimedediego.cubemaster.config.ThemeConfig;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.methods.PrefsMethods;
import com.jaimedediego.cubemaster.utils.AndroidUtils;
import com.jaimedediego.cubemaster.utils.Constants;
import com.jaimedediego.cubemaster.utils.OnScrambleCompleted;
import com.jaimedediego.cubemaster.utils.OnThreadFinished;
import com.jaimedediego.cubemaster.utils.Session;
import com.jaimedediego.cubemaster.utils.StringUtils;
import com.jaimedediego.cubemaster.view.activities.detail.DetailActivity;
import com.jaimedediego.cubemaster.view.customViews.CustomToast;
import com.jaimedediego.cubemaster.view.dialogs.NewFeatureDialog;
import com.jaimedediego.cubemaster.view.dialogs.RateDialog;
import com.jaimedediego.cubemaster.view.handler.ChronoHandler;
import com.jaimedediego.cubemaster.view.handler.ChronoThread;
import com.jaimedediego.cubemaster.view.handler.InspectionHandler;
import com.jaimedediego.cubemaster.view.handler.InspectionThread;

import java.io.ByteArrayOutputStream;

public class ChronoFragment extends Fragment {

    byte[] scrambleImageByteArray = null;
    private ImageButton infoButton;
    private ImageButton saveButton;
    private LinearLayout timeLayout;
    private LinearLayout hoursLayout;
    private LinearLayout minsLayout;
    private LinearLayout millisLayout;
    private TextView plus2;
    private TextView dnf;
    private TextView hours;
    private TextView mins;
    private TextView secs;
    private TextView millis;
    private TextView lastSolve;
    private TextView tutorial;
    private RelativeLayout scrambleLayout;
    private TextView scrambleText;
    private SVGImageView scrambleImage;
    private ImageButton scrambleButton;
    private ImageButton scrambleSwitch;
    private RelativeLayout scrambleContainer;
    private ProgressBar loadingScramble;
    private ImageView lineIndicator;
    private ImageView dotIndicator1;
    private ImageView dotIndicator2;
    private RadioGroup activityMenu;

    private int helpCounter = 0;
    private boolean resumeOnUp;
    ChronoThread chronoThread = null;
    InspectionThread inspectionThread = null;
    private boolean holded = false;

    private final int REQUEST_CODE_FOR_REFRESH_CHRONO = 1;

    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_stats, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_last_solve) {
            DatabaseMethods.getInstance().deleteCurrentPuzzleLastSolve();
            lastSolve.setText(String.format(getResources().getString(R.string.last_solve), DatabaseMethods.getInstance().getCurrentPuzzleLastSolve()));
            return true;
        } else if (id == R.id.go_to_details) {
            goToDetail();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_chrono, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(DatabaseMethods.getInstance().getCurrentPuzzleName());

        PrefsConfig.getInstance().setContext(v.getContext());
        DatabaseMethods.getInstance().setDatabase(getActivity());

        final RelativeLayout chronoScreen = v.findViewById(R.id.screen);

        infoButton = v.findViewById(R.id.info_button);
        tutorial = v.findViewById(R.id.tutorial);

        scrambleLayout = v.findViewById(R.id.scramble_layout);
        scrambleText = v.findViewById(R.id.scramble_text);
        scrambleImage = v.findViewById(R.id.scramble_image);
        scrambleButton = v.findViewById(R.id.scramble_button);
        scrambleSwitch = v.findViewById(R.id.scramble_switch);
        scrambleContainer = v.findViewById(R.id.scramble_container);
        loadingScramble = v.findViewById(R.id.loading_scramble);

        hours = v.findViewById(R.id.hours);
        mins = v.findViewById(R.id.mins);
        secs = v.findViewById(R.id.secs);
        millis = v.findViewById(R.id.millis);
        plus2 = v.findViewById(R.id.plus2);
        dnf = v.findViewById(R.id.dnf);
        timeLayout = v.findViewById(R.id.time_layout);
        hoursLayout = v.findViewById(R.id.hours_layout);
        minsLayout = v.findViewById(R.id.mins_layout);
        millisLayout = v.findViewById(R.id.millis_layout);
        lineIndicator = v.findViewById(R.id.line_indicator);
        dotIndicator1 = v.findViewById(R.id.dot_indicator_1);
        dotIndicator2 = v.findViewById(R.id.dot_indicator_2);
        activityMenu = getActivity().findViewById(R.id.menu_layout);
        saveButton = v.findViewById(R.id.save_button);
        lastSolve = v.findViewById(R.id.last_solve);

        if (PrefsMethods.getInstance().isNotShowedNewFeature()) {
            NewFeatureDialog newFeatureDialog = new NewFeatureDialog(getContext());
            newFeatureDialog.show();
            newFeatureDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (newFeatureDialog.didSomething()) {
                        AndroidUtils.switchVisibility(scrambleLayout, scrambleText, scrambleImage, loadingScramble);
                        ScrambleConfig.getInstance().doScramble();
                    }
                }
            });
        }

        tutorial.setBackgroundColor(Session.getInstance().getLightColorTheme());
        AndroidUtils.initLayoutTransitions(chronoScreen, v.findViewById(R.id.buttons));
        initIndicators();

        if (DatabaseMethods.getInstance().getCurrentPuzzleLastSolve().isEmpty() || DatabaseMethods.getInstance().getCurrentPuzzleLastSolve().equals("")) {
            lastSolve.setText(String.format(getResources().getString(R.string.last_solve), getResources().getString(R.string.threedots)));
        } else {
            lastSolve.setText(String.format(getResources().getString(R.string.last_solve), DatabaseMethods.getInstance().getCurrentPuzzleLastSolve()));
        }

        scrambleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidUtils.switchVisibility(scrambleContainer, scrambleButton, scrambleSwitch, loadingScramble);
                Session.getInstance().setCurrentScramble("");
                Session.getInstance().setCurrentScrambleSvg(null);
                ScrambleConfig.getInstance().doScramble();
            }
        });

        scrambleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scrambleSwitch.getDrawable().getConstantState().equals(ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_text_fields_white_18, null).getConstantState())) {
                    scrambleSwitch.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_grid_on_white_18, null));
                } else {
                    scrambleSwitch.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.baseline_text_fields_white_18, null));
                }
                AndroidUtils.switchVisibility(scrambleText, scrambleImage);
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AndroidUtils.switchVisibility(tutorial);
                if (tutorial.getVisibility() == View.VISIBLE) {
                    tutorial.setText(R.string.press_the_screen);
                    infoButton.setColorFilter(Session.getInstance().getLightColorTheme());
                } else {
                    helpCounter = 0;
                    infoButton.setColorFilter(null);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronoThread.finalize(true);
                saveButton.setVisibility(View.INVISIBLE);
            }
        });

        if (PrefsMethods.getInstance().isOnboardingNotShown()) {
            infoButton.performClick();
        }

        ChronoHandler chronoHandler = new ChronoHandler(millis, secs, mins, hours, minsLayout, hoursLayout);
        InspectionHandler inspectionHandler = new InspectionHandler(millis, secs, mins, hours, millisLayout, timeLayout, plus2, dnf);
        chronoScreen.setOnTouchListener(new View.OnTouchListener() {
            final Handler handler = new Handler();

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (loadingScramble.getVisibility() == View.VISIBLE) {
                        colorIndicators(R.color.md_grey_600);
                        new CustomToast(getContext(), R.string.scrambling).showAndHide(Constants.getInstance().TOAST_SHORT_DURATION);
                    } else if (chronoThread != null && chronoThread.isAlive()) {
                        if (PrefsMethods.getInstance().isPauseActivated()) {
                            if (!chronoThread.isPaused()) {
                                chronoThread.setPause(true);
                                tutorial.setText(R.string.press_the_screen_again_to_resume);
                                saveButton.setVisibility(View.VISIBLE);
                                resumeOnUp = false;
                            }
                        } else {
                            chronoThread.finalize(true);
                        }
                    } else if (inspectionThread != null && inspectionThread.isAlive()) {
                        tutorial.setText(R.string.stop_holding);
                    } else {
                        tutorial.setText(R.string.wait_until_indicator_turns_green);
                        colorIndicators(R.color.md_red_500);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                holded = true;
                                colorIndicators(R.color.md_green_500);
                                tutorial.setText(R.string.stop_holding);
                            }
                        }, PrefsMethods.getInstance().getFreezingTime());
                    }
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (inspectionThread != null && inspectionThread.isAlive()) {
                        inspectionThread.finalize(true);
                    } else if (chronoThread != null && chronoThread.isAlive()) {
                        if (chronoThread.isPaused() && resumeOnUp) {
                            chronoThread.setPause(false);
                            tutorial.setText(R.string.press_to_stop_the_timer);
                            saveButton.setVisibility(View.INVISIBLE);
                        } else {
                            resumeOnUp = true;
                        }
                    } else {
                        handler.removeCallbacksAndMessages(null);
                        if (holded) {
                            for (int i = 0; i < activityMenu.getChildCount(); i++) {
                                activityMenu.getChildAt(i).setEnabled(false);
                            }
                            infoButton.setEnabled(false);
                            scrambleButton.setVisibility(View.GONE);
                            scrambleSwitch.setVisibility(View.GONE);
                            helpCounter = 0;
                            if (Integer.parseInt(Constants.getInstance().INSPECTION_TIME_SECS.get(PrefsMethods.getInstance().getInspectionTime())) != 0) {
                                if ((inspectionThread == null) || (inspectionThread != null && !inspectionThread.isAlive())) {
                                    inspectionThread = new InspectionThread(getContext(), inspectionHandler, new OnThreadFinished() {
                                        @Override
                                        public void OnThreadFinished() {
                                            if (inspectionThread.isDnf()) {
                                                saveTime(true);
                                            } else if (inspectionThread.isPlus2()) {
                                                chronoThread = new ChronoThread(getContext(), chronoHandler, true, new OnThreadFinished() {
                                                    @Override
                                                    public void OnThreadFinished() {
                                                        saveTime();
                                                    }
                                                });
                                                chronoThread.start();
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        tutorial.setText(R.string.press_to_stop_the_timer);
                                                    }
                                                });
                                            } else {
                                                chronoThread = new ChronoThread(getContext(), chronoHandler, false, new OnThreadFinished() {
                                                    @Override
                                                    public void OnThreadFinished() {
                                                        saveTime();
                                                    }
                                                });
                                                chronoThread.start();
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        tutorial.setText(R.string.press_to_stop_the_timer);
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                                inspectionThread.start();
                                tutorial.setText(R.string.press_to_finish_the_inspection);
                            } else {
                                chronoThread = new ChronoThread(getContext(), chronoHandler, false, new OnThreadFinished() {
                                    @Override
                                    public void OnThreadFinished() {
                                        saveTime();
                                    }
                                });
                                chronoThread.start();
                                tutorial.setText(R.string.press_to_stop_the_timer);
                            }
                        } else {
                            colorIndicators(R.color.md_grey_600);
                            tutorial.setText(R.string.press_the_screen);
                            helpCounter++;
                            if (helpCounter == 10 && tutorial.getVisibility() == View.GONE) {
                                infoButton.performClick();
                            }
                        }
                    }
                    holded = false;
                    return false;
                }
                return true;
            }
        });

        ScrambleConfig.getInstance().setListener(new OnScrambleCompleted() {
            @Override
            public void onScrambleCompleted() {
                if (PrefsMethods.getInstance().isScrambleEnabled()) {
                    scrambleImage.setSVG(Session.getInstance().getCurrentScrambleSvg());
                    scrambleText.setText(Session.getInstance().getCurrentScramble());
                    AndroidUtils.switchVisibility(scrambleContainer, scrambleButton, scrambleSwitch, loadingScramble);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    PictureDrawable drawable = (PictureDrawable) scrambleImage.getDrawable();
                    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap);
                    canvas.drawPicture(drawable.getPicture());
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    scrambleImageByteArray = stream.toByteArray();
                }
            }
        });

        scramble();

        return v;
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

    private void goToDetail() {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("puzzleName", DatabaseMethods.getInstance().getCurrentPuzzleName());
        startActivityForResult(intent, REQUEST_CODE_FOR_REFRESH_CHRONO);
    }

    private void saveTime() {
        saveTime(false);
    }

    private void saveTime(boolean dnf) {
        holded = false;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String time;
                if (dnf) {
                    time = getResources().getString(R.string.dnf);
                } else {
                    if (mins.getText().equals("0")) {
                        time = secs.getText().toString() + '.' + millis.getText().toString();
                    } else {
                        if (hours.getText().equals("0")) {
                            time = mins.getText().toString() + ':' + secs.getText().toString() + '.' + millis.getText().toString();
                        } else {
                            time = hours.getText().toString() + ':' + mins.getText().toString() + ':' + secs.getText().toString() + '.' + millis.getText().toString();
                        }
                    }
                }

                DatabaseMethods.getInstance().saveData(time, StringUtils.getDateTime(), scrambleText.getText().toString(), scrambleImageByteArray);
                lastSolve.setText(String.format(getResources().getString(R.string.last_solve), DatabaseMethods.getInstance().getCurrentPuzzleLastSolve()));

                if (tutorial.getVisibility() == View.VISIBLE) {
                    infoButton.setColorFilter(null);
                    AndroidUtils.switchVisibility(tutorial);
                    new CustomToast(getContext(), R.string.well_done_you_finished_the_tutorial).showAndHide(Constants.getInstance().TOAST_LONG_DURATION);
                }

                colorIndicators(R.color.md_grey_600);
                for (int i = 0; i < activityMenu.getChildCount(); i++) {
                    activityMenu.getChildAt(i).setEnabled(true);
                }
                infoButton.setEnabled(true);

                if (scrambleLayout.getVisibility() == View.VISIBLE) {
                    scrambleButton.setVisibility(View.VISIBLE);
                    scrambleSwitch.setVisibility(View.VISIBLE);
                }

                if (!PrefsMethods.getInstance().isRatedOrNever() && DatabaseMethods.getInstance().countAllTimes() % 50 == 0) {
                    final RateDialog dialog = new RateDialog(getActivity(), DatabaseMethods.getInstance().countAllTimes(), false);
                    dialog.show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_REFRESH_CHRONO && resultCode == Activity.RESULT_OK) {
            lastSolve.setText(String.format(getResources().getString(R.string.last_solve), DatabaseMethods.getInstance().getCurrentPuzzleLastSolve()));
        }
    }

    private void scramble() {
        if (PrefsMethods.getInstance().isScrambleEnabled() && Constants.getInstance().WCA_PUZZLES_LONG_NAMES.contains(DatabaseMethods.getInstance().getCurrentPuzzleName())) {
            if ((Session.getInstance().getCurrentScramble().isEmpty() || Session.getInstance().getCurrentScramble() == null)) {
                if (ScrambleConfig.getInstance().getPuzzle() == null || !ScrambleConfig.getInstance().getPuzzle().getLongName().equals(DatabaseMethods.getInstance().getCurrentPuzzleName())) {
                    AndroidUtils.switchVisibility(scrambleContainer, scrambleButton, scrambleSwitch, loadingScramble);
                    ScrambleConfig.getInstance().doScramble();
                } else {
                    AndroidUtils.switchVisibility(scrambleContainer, scrambleButton, scrambleSwitch, loadingScramble);
                }
            } else {
                scrambleText.setText(Session.getInstance().getCurrentScramble());
                scrambleImage.setSVG(Session.getInstance().getCurrentScrambleSvg());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                PictureDrawable drawable = (PictureDrawable) scrambleImage.getDrawable();
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawPicture(drawable.getPicture());
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                scrambleImageByteArray = stream.toByteArray();
            }
        } else {
            AndroidUtils.switchVisibility(scrambleLayout, scrambleButton, scrambleSwitch);
            scrambleImageByteArray = null;
        }
    }

    private void initIndicators() {
        switch (PrefsMethods.getInstance().getIndicator()) {
            case 0:
                lineIndicator.setVisibility(View.GONE);
                break;
            case 1:
                dotIndicator1.setVisibility(View.GONE);
                dotIndicator2.setVisibility(View.GONE);
                break;
            default:
                dotIndicator1.setVisibility(View.GONE);
                dotIndicator2.setVisibility(View.GONE);
                break;
        }
    }

    private void colorIndicators(int colorResource) {
        switch (PrefsMethods.getInstance().getIndicator()) {
            case 0:
                dotIndicator1.setColorFilter(ThemeConfig.getInstance().getColorFromResource(colorResource));
                dotIndicator2.setColorFilter(ThemeConfig.getInstance().getColorFromResource(colorResource));
                break;
            case 1:
                switch (colorResource) {
                    case R.color.md_grey_600:
                        lineIndicator.setImageResource(R.drawable.line_indicator_grey);
                        break;
                    case R.color.md_green_500:
                        lineIndicator.setImageResource(R.drawable.line_indicator_green);
                        break;
                    case R.color.md_red_500:
                        lineIndicator.setImageResource(R.drawable.line_indicator_red);
                        break;
                    default:
                        lineIndicator.setImageResource(R.drawable.line_indicator_grey);
                        break;
                }
                break;
        }
    }
}