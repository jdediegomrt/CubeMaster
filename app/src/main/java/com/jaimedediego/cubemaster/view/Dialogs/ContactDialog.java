package com.jaimedediego.cubemaster.view.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jaimedediego.cubemaster.R;

public class ContactDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private RadioGroup contactOptions;
    private EditText editText;
    private InputMethodManager imm;

    public ContactDialog(@NonNull final Context context) {
        super(context);
        this.context = context;

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_contact);

        contactOptions = findViewById(R.id.contact_options);
        editText = findViewById(R.id.contact_text);
        final Button send = findViewById(R.id.send);
        final Button cancel = findViewById(R.id.cancel);

        contactOptions.check(R.id.contact_error);
        send.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        switch (v.getId()) {
            case R.id.send:
                if (editText.getText().toString().isEmpty()) {
                    editText.setHint(R.string.cannot_empty);
                } else {
                    dismiss();
                    Intent i = new Intent(Intent.ACTION_SENDTO);
                    i.setType("message/rfc822");
                    i.setData(Uri.parse("mailto:dediegomrt.dev@gmail.com"));
                    i.putExtra(Intent.EXTRA_SUBJECT, ((RadioButton) findViewById(contactOptions.getCheckedRadioButtonId())).getText());
                    i.putExtra(Intent.EXTRA_TEXT, editText.getText());
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        context.startActivity(i);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }
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

