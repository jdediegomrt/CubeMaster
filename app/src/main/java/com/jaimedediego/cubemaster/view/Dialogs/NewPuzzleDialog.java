package com.jaimedediego.cubemaster.view.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;

public class NewPuzzleDialog extends Dialog implements View.OnClickListener {

    private EditText name;
    private Context context;

    private boolean didSomething = false;

    public NewPuzzleDialog(@NonNull final Context context) {
        super(context);
        this.context = context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_changedatabase2);

        final Button ok = findViewById(R.id.accept);
        final Button cancel = findViewById(R.id.cancel);
        name = findViewById(R.id.new_puzzle);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept:
                if (!name.getText().toString().equals("")) {
                    if (!DatabaseMethods.getInstance().existPuzzle(name.getText().toString())
                            && !name.getText().toString().equals(context.getString(R.string.add_new))) {
                        DatabaseMethods.getInstance().addNewPuzzle(name.getText().toString());
                        DatabaseMethods.getInstance().usePuzzle(name.getText().toString());
                        didSomething = true;
                        dismiss();
                    } else {
                        name.setText("");
                        name.setHint(context.getResources().getString(R.string.already_exists));
                    }
                } else {
                    name.setHint(context.getResources().getString(R.string.cannot_empty));
                }
                break;
            case R.id.cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    public String newPuzzleName(){
        return name.getText().toString();
    };

    public boolean didSomething() {
        return didSomething;
    }
}

