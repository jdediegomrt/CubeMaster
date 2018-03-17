package com.dediegomrt.cubemaster.View.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.dediegomrt.cubemaster.Methods.DatabaseMethods;
import com.dediegomrt.cubemaster.R;

public class NewPuzzleDialog extends Dialog implements View.OnClickListener{

    private EditText name;
    private Context context;

    public NewPuzzleDialog(@NonNull final Context context) {
        super(context);
        this.context=context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_changedatabase2);

        final Button ok = (Button) findViewById(R.id.okDialog);
        name = (EditText) findViewById(R.id.newPuzzle);

        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okDialog:
                if(!name.getText().toString().equals("")) {
                    if(!DatabaseMethods.getInstance().existPuzzle(name.getText().toString())) {
                        DatabaseMethods.getInstance().addNewPuzzle(name.getText().toString());
                        DatabaseMethods.getInstance().usePuzzle(name.getText().toString());
                        dismiss();
                    } else {
                        name.setText("");
                        name.setHint(context.getResources().getString(R.string.already_exists));
                    }
                } else {
                    name.setHint(context.getResources().getString(R.string.cannot_empty));
                }
                break;
            default:
                break;
        }
    }
}

