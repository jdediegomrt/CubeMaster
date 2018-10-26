package com.jaimedediego.cubemaster.config;

import android.os.AsyncTask;
import android.util.Log;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.utils.Constants;
import com.jaimedediego.cubemaster.utils.OnScrambleCompleted;
import com.jaimedediego.cubemaster.utils.Session;

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
    private Puzzle puzzle;

    public void setListener(OnScrambleCompleted listener) {
        this.listener = listener;
    }

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

        if (scrambleTask != null) {
            scrambleTask.cancel(true);
        }
        scrambleTask = new ScrambleTask(puzzle);
        scrambleTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

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
                Session.getInstance().setCurrentScrambleSvg(svg);
                Session.getInstance().setCurrentScramble(scramble);
                listener.onScrambleCompleted();
            } catch (SVGParseException e) {
                Log.wtf("ScrambleTask", e);
            }
        }
    }
}