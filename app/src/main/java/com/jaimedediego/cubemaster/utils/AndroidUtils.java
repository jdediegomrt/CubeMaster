package com.jaimedediego.cubemaster.utils;

import android.animation.LayoutTransition;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class AndroidUtils {

    public static void SwitchVisibility(View... views) {
        for (View view : views) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    public static void initLayoutTransitions(View... views) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            for (View view : views) {
                if (view instanceof RelativeLayout) {
                    LayoutTransition layoutTransition = ((RelativeLayout) view).getLayoutTransition();
                    layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
                } else if (view instanceof LinearLayout) {
                    LayoutTransition layoutTransition = ((LinearLayout) view).getLayoutTransition();
                    layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
                } else {
                    Log.e("initLayoutTransitions", "Cannot init layout transition of view " + view.getClass().getName());
                }
            }
        }
    }
}
