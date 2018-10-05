package com.jaimedediego.cubemaster.view.CustomViews;


import android.content.Context;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.utils.Session;

public class CustomToast extends Toast {

    public CustomToast(Context context, int stringResId) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast_custom, null);
        view.setBackgroundColor(Session.getInstance().getLightColorTheme());
        ((TextView)view.findViewById(R.id.toast_text)).setText(stringResId);
        this.setView(view);
    }

    public void setTextSize(float size){
        View view = this.getView();
        view.setBackgroundColor(Session.getInstance().getLightColorTheme());
        ((TextView)view.findViewById(R.id.toast_text)).setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    @Override
    public void show() {
        super.show();
    }

    public void showAndHide(int delay){
        this.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cancel();
            }
        }, delay);

    }
}