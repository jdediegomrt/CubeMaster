package com.dediegomrt.cubemaster.View.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.dediegomrt.cubemaster.Methods.PrefsMethods;
import com.dediegomrt.cubemaster.R;

public class ContactDialog extends Dialog implements View.OnClickListener{

    private Context context;

    public ContactDialog(@NonNull final Context context) {
        super(context);
        this.context=context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_contact);

        final Button accept = (Button)findViewById(R.id.accept);
        final Button cancel = (Button)findViewById(R.id.cancel);

        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept:
                Intent i = new Intent(Intent.ACTION_SENDTO);
                i.setType("message/rfc822");
                i.setData(Uri.parse("mailto:dediegomrt@gmail.com"));
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    context.startActivity(i);
//                startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cancel:
                dismiss();
                break;
            default:
                break;
        }
    }
}

