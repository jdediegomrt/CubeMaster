package com.jaimedediego.cubemaster.view.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;

public class AreYouSureDialog extends Dialog {

    private Context context;

    private boolean didSomething = false;

    public AreYouSureDialog(@NonNull Context context, final String puzzle, final int id) {
        super(context);
        this.context = context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_areyousure);

        TextView currentPuzzle = findViewById(R.id.puzzle_name);
        final TextView text = findViewById(R.id.info_areyousure);
        final Button ok = findViewById(R.id.accept);
        final Button cancel = findViewById(R.id.cancel);

        currentPuzzle.setText(puzzle);
        
        if (id == R.id.reset) {
            text.setText(R.string.areyousurereset);
        } else {
            text.setText(R.string.areyousuredelete);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (id == R.id.reset) {
                    DatabaseMethods.getInstance().resetPuzzle(puzzle);
                } else {
                    DatabaseMethods.getInstance().deletePuzzle(puzzle);
                }
                didSomething = true;
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public boolean didSomething() {
        return didSomething;
    }
}
