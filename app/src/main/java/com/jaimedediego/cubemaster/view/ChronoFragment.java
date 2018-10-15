package com.jaimedediego.cubemaster.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.PictureDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.jaimedediego.cubemaster.utils.Session;
import com.jaimedediego.cubemaster.view.CustomViews.CustomToast;
import com.jaimedediego.cubemaster.view.Dialogs.PuzzleChangeDialog;
import com.jaimedediego.cubemaster.view.Dialogs.RateDialog;
import com.jaimedediego.cubemaster.view.Handler.ChronoThread;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChronoFragment extends Fragment {

    private MediaPlayer mp;
    private ImageButton infoButton;
    private FloatingActionButton saveButton;
    private RelativeLayout infoLayout;
    private LinearLayout hoursLayout;
    private LinearLayout minsLayout;
    private TextView hours;
    private TextView mins;
    private TextView secs;
    private TextView millis;
    private RelativeLayout scrambleLayout;
    private TextView scrambleText;
    private SVGImageView scrambleImage;
    private ImageButton scrambleButton;
    private ProgressBar loadingScramble;
    private ImageView dotIndicator1;
    private ImageView dotIndicator2;
    private RadioGroup activityMenu;
    private int helpCounter = 0;
    private boolean resumeOnUp;

    ChronoThread thread = null;

    private boolean holded = false;

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
            DatabaseMethods.getInstance().deleteLastSolve(Session.getInstance().getCurrentPuzzleId());
            return true;
        } else if (id == R.id.change_database) {
            final PuzzleChangeDialog dialog = new PuzzleChangeDialog(getActivity());
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (dialog.didSomething()) {
                        /*TODO: revisar, está fallando plantearse quitar este boton funcionalidad duplicada*/
//                        scramble();
                    }
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_chrono, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.timer);

        PrefsConfig.getInstance().setContext(v.getContext());
        DatabaseMethods.getInstance().setDatabase(getActivity());

        infoButton = v.findViewById(R.id.info_button);
        RelativeLayout infoContainer = v.findViewById(R.id.info_container);
        infoLayout = v.findViewById(R.id.info_layout);
        final ViewGroup.LayoutParams params = infoLayout.getLayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            LayoutTransition layoutTransition = infoContainer.getLayoutTransition();
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        }

        mp = MediaPlayer.create(getActivity(), R.raw.beep);
        final RelativeLayout chronoScreen = v.findViewById(R.id.chrono_layout);
        hours = v.findViewById(R.id.hours);
        mins = v.findViewById(R.id.mins);
        secs = v.findViewById(R.id.secs);
        millis = v.findViewById(R.id.millis);
        hoursLayout = v.findViewById(R.id.hours_layout);
        minsLayout = v.findViewById(R.id.mins_layout);
        dotIndicator1 = v.findViewById(R.id.timer_indicator_1);
        dotIndicator2 = v.findViewById(R.id.timer_indicator_2);
        activityMenu = getActivity().findViewById(R.id.menu_layout);
        saveButton = v.findViewById(R.id.save_button);

        scrambleLayout = v.findViewById(R.id.scramble_layout);
        scrambleText = v.findViewById(R.id.scramble_text);
        scrambleImage = v.findViewById(R.id.scramble_image);
        scrambleButton = v.findViewById(R.id.scramble_button);
        loadingScramble = v.findViewById(R.id.loading_scramble);
        ScrambleConfig.getInstance().setScrambleViewItems(scrambleText, scrambleImage, scrambleButton, loadingScramble);

        scramble();

        scrambleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Session.getInstance().getNextScramble().isEmpty() || Session.getInstance().getNextScramble() == null) {
                    AndroidUtils.SwitchVisibility(scrambleText, scrambleImage, scrambleButton, loadingScramble);
                    Session.getInstance().setCurrentScramble("");
                } else {
                    Session.getInstance().setCurrentScramble(Session.getInstance().getNextScramble());
                    Session.getInstance().setCurrentScrambleSvg(Session.getInstance().getNextScrambleSvg());
                    scrambleText.setText(Session.getInstance().getCurrentScramble());
                    scrambleImage.setSVG(Session.getInstance().getCurrentScrambleSvg());
                    Session.getInstance().setNextScramble("");
                    ScrambleConfig.getInstance().doScramble();
                }
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infoLayout.getHeight() != 0) {
                    infoButton.setColorFilter(null);
                    infoLayout.setLayoutParams(params);
                    chronoScreen.setEnabled(true);
                } else {
                    infoButton.setColorFilter(Session.getInstance().getLightColorTheme());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    infoLayout.setLayoutParams(layoutParams);
                    chronoScreen.setEnabled(false);
                }

                Button gotitButton = v.findViewById(R.id.gotit_button);

                gotitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        helpCounter = 0;
                        infoButton.setColorFilter(null);
                        infoLayout.setLayoutParams(params);
                        chronoScreen.setEnabled(true);
                    }
                });
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dotIndicator1.setColorFilter(ThemeConfig.getInstance().getColorFromResource(R.color.md_grey_600));
                dotIndicator2.setColorFilter(ThemeConfig.getInstance().getColorFromResource(R.color.md_grey_600));
                thread.finalize(true);
                saveTime();
                saveButton.setVisibility(View.GONE);
            }
        });

        if (PrefsMethods.getInstance().isOnboardingNotShown()) {
            infoButton.performClick();
        }

        chronoScreen.setOnTouchListener(new View.OnTouchListener() {
            final Handler handler = new Handler();

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (loadingScramble.getVisibility() == View.VISIBLE) {
                        dotIndicator1.setColorFilter(ThemeConfig.getInstance().getColorFromResource(R.color.md_grey_600));
                        dotIndicator2.setColorFilter(ThemeConfig.getInstance().getColorFromResource(R.color.md_grey_600));
                        new CustomToast(getContext(), R.string.scrambling).showAndHide(Constants.getInstance().TOAST_SHORT_DURATION);
                    } else {
                        if (thread != null && thread.isAlive()) {
                            if (PrefsMethods.getInstance().isPauseActivated()) {
                                if (!thread.isPaused()) {
                                    thread.setPause(true);
                                    saveButton.setVisibility(View.VISIBLE);
                                    resumeOnUp = false;
                                }
                            } else {
                                dotIndicator1.setColorFilter(ThemeConfig.getInstance().getColorFromResource(R.color.md_grey_600));
                                dotIndicator2.setColorFilter(ThemeConfig.getInstance().getColorFromResource(R.color.md_grey_600));
                                thread.finalize(true);
                                final Handler handleChrono = new Handler();
                                handleChrono.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        saveTime();
                                    }
                                }, 10);
                            }
                        } else {
                            dotIndicator1.setColorFilter(ThemeConfig.getInstance().getColorFromResource(R.color.md_red_500));
                            dotIndicator2.setColorFilter(ThemeConfig.getInstance().getColorFromResource(R.color.md_red_500));
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    holded = true;
                                    dotIndicator1.setColorFilter(ThemeConfig.getInstance().getColorFromResource(R.color.md_green_500));
                                    dotIndicator2.setColorFilter(ThemeConfig.getInstance().getColorFromResource(R.color.md_green_500));
                                }
                            }, PrefsMethods.getInstance().getFreezingTime());
                        }
                    }
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (thread != null && thread.isAlive()) {
                        if (thread.isPaused() && resumeOnUp) {
                            thread.setPause(false);
                            saveButton.setVisibility(View.GONE);
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
                            helpCounter = 0;
                            thread = new ChronoThread(millis, secs, mins, hours, minsLayout, hoursLayout, mp);
                            thread.start();
                        } else {
                            dotIndicator1.setColorFilter(ThemeConfig.getInstance().getColorFromResource(R.color.md_grey_600));
                            dotIndicator2.setColorFilter(ThemeConfig.getInstance().getColorFromResource(R.color.md_grey_600));
                            helpCounter++;
                            if (helpCounter == 10) {
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

    private void saveTime() {
        String time;
        if (mins.getText().equals("0")) {
            time = secs.getText().toString() + '.' + millis.getText().toString();
        } else {
            if (hours.getText().equals("0")) {
                time = mins.getText().toString() + ':' + secs.getText().toString() + '.' + millis.getText().toString();
            } else {
                time = hours.getText().toString() + ':' + mins.getText().toString() + ':' + secs.getText().toString() + '.' + millis.getText().toString();
            }
        }
        for (int i = 0; i < activityMenu.getChildCount(); i++) {
            activityMenu.getChildAt(i).setEnabled(true);
        }
        infoButton.setEnabled(true);
        scrambleButton.setVisibility(View.VISIBLE);

        byte[] scrambleImageByteArray = null;
        if (scrambleLayout.getVisibility() == View.VISIBLE) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            PictureDrawable drawable = (PictureDrawable) scrambleImage.getDrawable();
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawPicture(drawable.getPicture());
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            scrambleImageByteArray = stream.toByteArray();
        }

        DatabaseMethods.getInstance().saveData(time, getDateTime(), scrambleText.getText().toString(), scrambleImageByteArray);
        if (!PrefsMethods.getInstance().isRatedOrNever() && DatabaseMethods.getInstance().countAllTimes() % 50 == 0) {
            final RateDialog dialog = new RateDialog(getActivity(), DatabaseMethods.getInstance().countAllTimes(), false);
            dialog.show();
        }
    }

    private void scramble() {
        if (Constants.getInstance().WCA_PUZZLES_LONG_NAMES.contains(DatabaseMethods.getInstance().getCurrentPuzzleName()) && PrefsMethods.getInstance().isScrambleEnabled()) {
            if ((Session.getInstance().getCurrentScramble().isEmpty() || Session.getInstance().getCurrentScramble() == null) && (Session.getInstance().getNextScramble().isEmpty() || Session.getInstance().getNextScramble() == null)) {
                if (!ScrambleConfig.getInstance().isScrambling()) {
                    AndroidUtils.SwitchVisibility(scrambleText, scrambleImage, scrambleButton, loadingScramble);
                    ScrambleConfig.getInstance().doScramble();
                } else {
                    AndroidUtils.SwitchVisibility(scrambleText, scrambleImage, scrambleButton, loadingScramble);
                }
            } else {
                scrambleText.setText(Session.getInstance().getCurrentScramble());
                scrambleImage.setSVG(Session.getInstance().getCurrentScrambleSvg());
            }
        } else {
            AndroidUtils.SwitchVisibility(scrambleLayout);
            AndroidUtils.SwitchVisibility(scrambleButton);
        }
    }
}