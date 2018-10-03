package com.jaimedediego.cubemaster.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvg.SVGParseException;
import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.config.PrefsConfig;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.methods.PrefsMethods;
import com.jaimedediego.cubemaster.utils.Constants;
import com.jaimedediego.cubemaster.utils.Session;
import com.jaimedediego.cubemaster.view.Dialogs.PuzzleChangeDialog;
import com.jaimedediego.cubemaster.view.Dialogs.RateDialog;
import com.jaimedediego.cubemaster.view.Handler.ChronoThread;

import net.gnehzr.tnoodle.scrambles.Puzzle;
import net.gnehzr.tnoodle.svglite.Svg;
import net.gnehzr.tnoodle.utils.LazyInstantiator;
import net.gnehzr.tnoodle.utils.LazyInstantiatorException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChronoFragment extends Fragment {

    private MediaPlayer mp;
    private RelativeLayout animatedLine;
    private ImageButton infoButton;
    private ImageButton pauseButton;
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
    private int helpCounter = 0;

    private ScrambleTask scrambleTask;
    private Puzzle puzzle;

    private static final String TAG = ChronoFragment.class.getName();

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
            DatabaseMethods.getInstance().deleteLastSolve(Session.getInstance().currentPuzzleId);
            return true;
        } else if (id == R.id.change_database) {
            final PuzzleChangeDialog dialog = new PuzzleChangeDialog(getActivity());
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (dialog.didSomething()) {
                        scrambleText.setText(Session.getInstance().currentPuzzleScramble);
                    }
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_chrono, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.timer);

        PrefsConfig.getInstance().setContext(v.getContext());
        DatabaseMethods.getInstance().setDatabase(getActivity());

        infoButton = v.findViewById(R.id.info_button);
        RelativeLayout infoContainer = v.findViewById(R.id.info_container);
        infoLayout = v.findViewById(R.id.info_layout);
        pauseButton = v.findViewById(R.id.pause_button);
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
        animatedLine = v.findViewById(R.id.animated_line);
        final RadioGroup activityMenu = getActivity().findViewById(R.id.menu_layout);

        scrambleLayout = v.findViewById(R.id.scramble_layout);
        scrambleText = v.findViewById(R.id.scramble_text);
        scrambleImage = v.findViewById(R.id.scramble_image);
        scrambleButton = v.findViewById(R.id.scramble_button);

        if (Constants.getInstance().shortNames.contains(DatabaseMethods.getInstance().getCurrentPuzzleName()) && PrefsMethods.getInstance().isScrambleEnabled()) {
            String shortName = DatabaseMethods.getInstance().getCurrentPuzzleName();
            LazyInstantiator<Puzzle> lazyPuzzle = Constants.getInstance().puzzles.get(shortName);
            try {
                puzzle = lazyPuzzle.cachedInstance();
            } catch (LazyInstantiatorException e) {
                Log.wtf(TAG, e);
            }
            if ((Session.getInstance().CURRENT_SCRAMBLE.isEmpty() || Session.getInstance().CURRENT_SCRAMBLE == null) && (Session.getInstance().NEXT_SCRAMBLE.isEmpty() || Session.getInstance().NEXT_SCRAMBLE == null)) {
                doScramble();
            } else {
                scrambleText.setText(Session.getInstance().CURRENT_SCRAMBLE);
                scrambleImage.setSVG(Session.getInstance().CURRENT_SCRAMBLE_SVG);
            }
        } else {
            scrambleLayout.setVisibility(View.GONE);
        }

        scrambleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Session.getInstance().NEXT_SCRAMBLE.isEmpty() || Session.getInstance().NEXT_SCRAMBLE == null) {
                    scrambleText.setText(R.string.scrambling);
                    scrambleImage.setVisibility(View.INVISIBLE);
                    Session.getInstance().CURRENT_SCRAMBLE = "";
                } else {
                    Session.getInstance().CURRENT_SCRAMBLE = Session.getInstance().NEXT_SCRAMBLE;
                    Session.getInstance().CURRENT_SCRAMBLE_SVG = Session.getInstance().NEXT_SCRAMBLE_SVG;
                    scrambleText.setText(Session.getInstance().CURRENT_SCRAMBLE);
                    scrambleImage.setVisibility(View.VISIBLE);
                    scrambleImage.setSVG(Session.getInstance().CURRENT_SCRAMBLE_SVG);
                    Session.getInstance().NEXT_SCRAMBLE = "";
                }
                doScramble();
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
                    infoButton.setColorFilter(Session.getInstance().lightColorTheme);
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

        if (!PrefsMethods.getInstance().isOnboardingShown()) {
            infoButton.performClick();
        }

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (helpCounter > 0) {
                    helpCounter = 0;
                }
                if (thread != null && thread.isAlive()) {
                    if (!thread.getPause()) {
                        thread.setPause(true);
                        chronoScreen.setEnabled(false);
                        pauseButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play_circle_filled_white_48dp, null));
                    } else {
                        thread.setPause(false);
                        chronoScreen.setEnabled(true);
                        pauseButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause_circle_filled_white_48dp, null));
                    }
                }
            }
        });

        chronoScreen.setOnTouchListener(new View.OnTouchListener() {
            final Handler handler = new Handler();

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    animatedLine.setBackgroundResource(R.drawable.background_timer_freeze);
                    if (thread != null && thread.isAlive()) {
                        animatedLine.setBackgroundResource(R.drawable.background_timer_off);
                        thread.finalize(true);
                        final Handler handleChrono = new Handler();
                        handleChrono.postDelayed(new Runnable() {
                            @Override
                            public void run() {
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
                                if (PrefsMethods.getInstance().isPauseActivated()) {
                                    pauseButton.setVisibility(View.GONE);
                                }
                                infoButton.setEnabled(true);
                                DatabaseMethods.getInstance().saveData(time, getDateTime(), scrambleText.getText().toString());
                                if (!PrefsMethods.getInstance().isRatedOrNever() && DatabaseMethods.getInstance().countAllTimes() % 50 == 0) {
                                    final RateDialog dialog = new RateDialog(getActivity(), DatabaseMethods.getInstance().countAllTimes(), false);
                                    dialog.show();
                                }
                            }
                        }, 10);
                    } else {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                holded = true;
                                animatedLine.setBackgroundResource(R.drawable.background_timer_on);
                            }
                        }, PrefsMethods.getInstance().getFreezingTime());
                    }
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    handler.removeCallbacksAndMessages(null);
                    if (holded) {
                        for (int i = 0; i < activityMenu.getChildCount(); i++) {
                            activityMenu.getChildAt(i).setEnabled(false);
                        }
                        infoButton.setEnabled(false);
                        helpCounter = 0;
                        thread = new ChronoThread(millis, secs, mins, hours, minsLayout, hoursLayout, mp);
                        thread.start();
                        if (PrefsMethods.getInstance().isPauseActivated()) {
                            pauseButton.setVisibility(View.VISIBLE);
                        }
                    } else {
                        animatedLine.setBackgroundResource(R.drawable.background_timer_off);
                        helpCounter++;
                        if (helpCounter == 10) {
                            infoButton.performClick();
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

    private void doScramble() {
        cancelScrambleIfScrambling();
        scrambleTask = new ScrambleTask();
        scrambleTask.execute(puzzle);
    }

    private void cancelScrambleIfScrambling() {
        if (scrambleTask != null && scrambleTask.getStatus() == AsyncTask.Status.RUNNING) {
            scrambleTask.cancel(true);
        }
    }

    private class ScrambleAndSvg {
        String scramble;
        Svg svg;

        ScrambleAndSvg(String scramble, Svg svg) {
            this.scramble = scramble;
            this.svg = svg;
        }
    }

    private class ScrambleTask extends AsyncTask<Puzzle, Void, ScrambleAndSvg> {

        private Exception exception;

        public ScrambleTask() {
        }

        protected ScrambleAndSvg doInBackground(Puzzle... puzzles) {
            try {
                assert puzzles.length == 1;
                Puzzle puzzle = puzzles[0];
                String scramble = puzzle.generateScramble();
                Svg svg = puzzle.drawScramble(scramble, null);
                return new ScrambleAndSvg(scramble, svg);
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        private void handleException() {
            Log.w(TAG, exception);
        }

        protected void onCancelled(ScrambleAndSvg scrambleAndSvg) {
            if (exception != null) {
                handleException();
                return;
            }
        }

        protected void onPostExecute(ScrambleAndSvg scrambleAndSvg) {
            if (exception != null) {
                handleException();
                return;
            }
            String scramble = scrambleAndSvg.scramble;
            Svg svgLite = scrambleAndSvg.svg;

            try {
                SVG svg = SVG.getFromString(svgLite.toString());

                if (Session.getInstance().CURRENT_SCRAMBLE.isEmpty() || Session.getInstance().CURRENT_SCRAMBLE == null) {
//                    scrambleImage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//                    scrambleImage.setImageDrawable(drawable);
                    scrambleImage.setSVG(svg);
                    if(scrambleImage.getVisibility()==View.INVISIBLE){
                        scrambleImage.setVisibility(View.VISIBLE);
                    }
                    Session.getInstance().CURRENT_SCRAMBLE_SVG = svg;
                    scrambleText.setText(scramble);
                    Session.getInstance().CURRENT_SCRAMBLE = scramble;
                    if (Session.getInstance().NEXT_SCRAMBLE.isEmpty()) {
                        new ScrambleTask().execute(puzzle);
                    }
                } else {
                    Session.getInstance().NEXT_SCRAMBLE_SVG = svg;
                    Session.getInstance().NEXT_SCRAMBLE = scramble;
                }

            } catch (SVGParseException e) {
                Log.wtf(TAG, e);
            }
        }
    }
}
