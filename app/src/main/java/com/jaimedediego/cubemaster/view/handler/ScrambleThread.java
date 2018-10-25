package com.jaimedediego.cubemaster.view.handler;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvg.SVGParseException;
import com.jaimedediego.cubemaster.methods.PrefsMethods;
import com.jaimedediego.cubemaster.utils.OnScrambleCompleted;
import com.jaimedediego.cubemaster.utils.Session;

import net.gnehzr.tnoodle.scrambles.InvalidScrambleException;
import net.gnehzr.tnoodle.scrambles.Puzzle;
import net.gnehzr.tnoodle.svglite.Svg;

public class ScrambleThread extends Thread {
    private Puzzle puzzle;
    private ScrambleHandler handler;
    private OnScrambleCompleted listener;

    public ScrambleThread(Puzzle puzzle, OnScrambleCompleted listener, TextView scrambleText, SVGImageView scrambleImage, ImageButton scrambleButton, ProgressBar loadingScramble) {
        this.puzzle = puzzle;
        this.listener = listener;
        handler = new ScrambleHandler(scrambleText, scrambleImage, scrambleButton, loadingScramble);
    }

    public void run() {
        try {
            String scramble = puzzle.generateScramble();
            Svg svgLite = puzzle.drawScramble(scramble, null);
            try {
                SVG svg = SVG.getFromString(svgLite.toString());
                Session.getInstance().setCurrentScrambleSvg(svg);
                Session.getInstance().setCurrentScramble(scramble);
                handler.setSvg(svg);
                handler.setScramble(scramble);
                handler.act();
            } catch (SVGParseException e) {
                Log.wtf("ScrambleTask", e);
            }
        } catch (Exception e) {
            Log.w("ScrambleTask", e);
        }
    }
}
