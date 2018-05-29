package com.dediegomrt.cubemaster.View.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.dediegomrt.cubemaster.Methods.PrefsMethods;
import com.dediegomrt.cubemaster.R;

public class RestartDialog extends Dialog implements View.OnClickListener{

    private Context context;
    private int position;

    public RestartDialog(@NonNull final Context context, final int position) {
        super(context);
        this.position=position;
        this.context=context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_restart);

        final Button accept = findViewById(R.id.accept);
        final Button cancel = findViewById(R.id.cancel);

        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept:
                PrefsMethods.getInstance().setColorAccent(position);
                Intent i = context.getPackageManager().
                        getLaunchIntentForPackage(context.getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
                break;
            case R.id.cancel:
                dismiss();
                break;
            default:
                break;
        }
    }
}

