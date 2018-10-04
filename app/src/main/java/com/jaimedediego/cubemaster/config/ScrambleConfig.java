package com.jaimedediego.cubemaster.config;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvg.SVGParseException;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.utils.Constants;
import com.jaimedediego.cubemaster.utils.Session;

import net.gnehzr.tnoodle.scrambles.Puzzle;
import net.gnehzr.tnoodle.svglite.Svg;
import net.gnehzr.tnoodle.utils.LazyInstantiator;
import net.gnehzr.tnoodle.utils.LazyInstantiatorException;

public class ScrambleConfig {

    private static ScrambleConfig instance;

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

    public void doScramble(TextView textView, SVGImageView imageView, ImageButton imageButton) {
        String shortName = Constants.getInstance().WCA_PUZZLES_SHORT_NAMES.get(Constants.getInstance().WCA_PUZZLES_LONG_NAMES.indexOf(DatabaseMethods.getInstance().getCurrentPuzzleName()));
        LazyInstantiator<Puzzle> lazyPuzzle = Constants.getInstance().WCA_PUZZLES.get(shortName);
        try {
            puzzle = lazyPuzzle.cachedInstance();
        } catch (LazyInstantiatorException e) {
            Log.wtf("ScrambleConfig", e);
        }

        cancelScrambleIfScrambling();
        scrambleTask = new ScrambleTask(textView, imageView, imageButton);
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

        private TextView scrambleText;
        private SVGImageView scrambleImage;
        private ImageButton scrambleButton;

        public ScrambleTask() {
        }


        public ScrambleTask(TextView textView, SVGImageView imageView, ImageButton imageButton) {
            scrambleText = textView;
            scrambleImage = imageView;
            scrambleButton = imageButton;
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

                if (Session.getInstance().CURRENT_SCRAMBLE.isEmpty() || Session.getInstance().CURRENT_SCRAMBLE == null) {
                    scrambleImage.setSVG(svg);
                    if(scrambleImage.getVisibility()== View.INVISIBLE){
                        scrambleImage.setVisibility(View.VISIBLE);
                    }
                    if(scrambleButton.getVisibility()==View.GONE){
                        scrambleButton.setVisibility(View.VISIBLE);
                    }
                    Session.getInstance().CURRENT_SCRAMBLE_SVG = svg;
                    scrambleText.setText(scramble);
                    Session.getInstance().CURRENT_SCRAMBLE = scramble;
                    if (Session.getInstance().NEXT_SCRAMBLE.isEmpty()) {
                        doScramble(scrambleText, scrambleImage, scrambleButton);
                    }
                } else {
                    Session.getInstance().NEXT_SCRAMBLE_SVG = svg;
                    Session.getInstance().NEXT_SCRAMBLE = scramble;
                }

            } catch (SVGParseException e) {
                Log.wtf("ScrambleTask", e);
            }
        }
    }
}
