package com.dediegomrt.cubemaster.View.Dialogs;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.dediegomrt.cubemaster.R;

public class RateDialog extends Dialog implements View.OnClickListener{

    private Context context;

    public RateDialog(@NonNull final Context context, int numTimes) {
        super(context);
        this.context=context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_rate);
        setCanceledOnTouchOutside(false);

        final Button accept = (Button)findViewById(R.id.accept);
        final Button cancel = (Button)findViewById(R.id.cancel);
        final Button later = (Button)findViewById(R.id.later);
        final TextView text = (TextView)findViewById(R.id.rate_text);

        text.setText(String.format(context.getResources().getString(R.string.rate_us), numTimes));

        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);
        later.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept:
                dismiss();
                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                }
                try {
                     context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                }
                break;
            case R.id.cancel:
                dismiss();
                break;
            case R.id.later:
                dismiss();
                break;
            default:
                break;
        }
    }
}

