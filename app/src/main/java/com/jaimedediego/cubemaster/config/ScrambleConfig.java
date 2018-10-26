package com.jaimedediego.cubemaster.config;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvg.SVGParseException;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.utils.AndroidUtils;
import com.jaimedediego.cubemaster.utils.Constants;
import com.jaimedediego.cubemaster.utils.OnScrambleCompleted;
import com.jaimedediego.cubemaster.utils.Session;
import com.jaimedediego.cubemaster.view.handler.ScrambleThread;

import net.gnehzr.tnoodle.scrambles.Puzzle;
import net.gnehzr.tnoodle.svglite.Svg;
import net.gnehzr.tnoodle.utils.LazyInstantiator;
import net.gnehzr.tnoodle.utils.LazyInstantiatorException;

public class ScrambleConfig {

    private static ScrambleConfig instance;
    private OnScrambleCompleted listener;

    private ScrambleConfig() {
    }

    public static ScrambleConfig getInstance() {
        if (instance == null) {
            instance = new ScrambleConfig();
        }
        return instance;
    }

    private ScrambleTask scrambleTask;
//    private ScrambleThread scrambleThread;
    private Puzzle puzzle;
//    private TextView scrambleText;
//    private SVGImageView scrambleImage;
//    private ImageButton scrambleButton;
//    private ProgressBar loadingScramble;
    private boolean scrambling = false;

    public void setListener(OnScrambleCompleted listener){
        this.listener = listener;
    }

//    public void setScrambleViewItems(TextView textView, SVGImageView imageView, ImageButton imageButton, ProgressBar loadingImage) {
//        scrambleText = textView;
//        scrambleImage = imageView;
//        scrambleButton = imageButton;
//        loadingScramble = loadingImage;
//    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void doScramble() {
        String shortName = Constants.getInstance().WCA_PUZZLES_SHORT_NAMES.get(Constants.getInstance().WCA_PUZZLES_LONG_NAMES.indexOf(DatabaseMethods.getInstance().getCurrentPuzzleName()));
        LazyInstantiator<Puzzle> lazyPuzzle = Constants.getInstance().WCA_PUZZLES.get(shortName);
        try {
            puzzle = lazyPuzzle.cachedInstance();
        } catch (LazyInstantiatorException e) {
            Log.wtf("ScrambleConfig", e);
        }

//        cancelScrambleIfScrambling();
        if(scrambleTask!=null)
            scrambleTask.cancel(true);
        scrambleTask = new ScrambleTask(puzzle);
        scrambleTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        scrambleThread = new ScrambleThread(puzzle, listener, scrambleText, scrambleImage, scrambleButton, loadingScramble);
//        scrambleThread.start();
    }

//    public boolean isScrambling() {
//        return scrambling;
//        return scrambleTask != null && scrambleTask.getStatus() == AsyncTask.Status.RUNNING;
//        return scrambleThread!=null && scrambleThread.getState() == Thread.State.RUNNABLE;
//    }

//    public void cancelScrambleIfScrambling() {
//        if (isScrambling()) {
//            scrambleTask.cancel(true);
//            scrambleThread.interrupt();
//        }
//    }

    private class ScrambleAndSvg {
        String scramble;
        Svg svg;

        ScrambleAndSvg(String scramble, Svg svg) {
            this.scramble = scramble;
            this.svg = svg;
        }
    }

    private class ScrambleTask extends AsyncTask<Void, Void, ScrambleAndSvg> {
        private Exception exception;
        private Puzzle puzzle;

        public ScrambleTask(Puzzle puzzle) {
            this.puzzle = puzzle;
        }

        protected ScrambleAndSvg doInBackground(Void... voids) {
            try {
                String scramble = puzzle.generateScramble();
                Svg svg = puzzle.drawScramble(scramble, null);
                return new ScrambleAndSvg(scramble, svg);
            } catch (Exception e) {
                this.exception = e;
                return null;
            }
        }

        private void handleException() {
            Log.w("ScrambleTask", exception);
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
//                if (Session.getInstance().getCurrentScramble().isEmpty() || Session.getInstance().getCurrentScramble() == null) {
                    Session.getInstance().setCurrentScrambleSvg(svg);
                    Session.getInstance().setCurrentScramble(scramble);

//                scrambleImage.setSVG(Session.getInstance().getCurrentScrambleSvg());
//                scrambleText.setText(Session.getInstance().getCurrentScramble());

                    listener.onScrambleCompleted();
//                    if (Session.getInstance().getNextScramble().isEmpty()) {
//                        doScramble();
//                    }
//                } else {
//                    Session.getInstance().setNextScrambleSvg(svg);
//                    Session.getInstance().setNextScramble(scramble);
//                }
//
            } catch (SVGParseException e) {
                Log.wtf("ScrambleTask", e);
            }
        }
    }
}