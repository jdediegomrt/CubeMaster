package com.jaimedediego.cubemaster.utils;

import android.view.View;

public class AndroidUtils {

    public static void SwitchVisibility(View... views){
        for (View view : views) {
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }
    }
}
