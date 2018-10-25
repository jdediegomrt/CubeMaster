package com.jaimedediego.cubemaster.view.handler;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.jaimedediego.cubemaster.utils.AndroidUtils;

public class ScrambleHandler extends Handler{
    private String scramble;
    private SVG svg;
    private TextView scrambleText;
    private SVGImageView scrambleImage;
    private ImageButton scrambleButton;
    private ProgressBar loadingScramble;

    ScrambleHandler(TextView text, SVGImageView image, ImageButton button, ProgressBar progress) {
        scrambleText = text;
        scrambleImage = image;
        scrambleButton = button;
        loadingScramble = progress;
    }

    public void handleMessage(Message msg) {
        scrambleText.setText(scramble);
        scrambleImage.setSVG(svg);
        AndroidUtils.SwitchVisibility(scrambleText, scrambleImage, scrambleButton, loadingScramble);
    }

    void setScramble(String scramble) {
        this.scramble = scramble;
    }

    void setSvg(SVG svg) {
        this.svg = svg;
    }

    void act() {
        super.sendEmptyMessage(0);
    }

}
