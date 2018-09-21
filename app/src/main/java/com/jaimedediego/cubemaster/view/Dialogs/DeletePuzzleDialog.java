package com.jaimedediego.cubemaster.view.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;

public class DeletePuzzleDialog extends Dialog implements View.OnClickListener {

    private LinearLayout parent;
    private View child;
    private int numSolve;

    private boolean didSomething = false;

    public DeletePuzzleDialog(@NonNull Context context, LinearLayout parent, View child, int numSolve) {
        super(context);
        this.parent = parent;
        this.child = child;
        this.numSolve = numSolve;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_areyousure);

        RelativeLayout nameContainer = findViewById(R.id.puzzle_name_container);
        nameContainer.setVisibility(View.GONE);

        final TextView text = findViewById(R.id.info_areyousure);
        final Button ok = findViewById(R.id.accept);
        final Button cancel = findViewById(R.id.cancel);

        text.setText(R.string.areyousuredeletepuzzle);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept:
                parent.removeView(child);
                DatabaseMethods.getInstance().deleteSolve(numSolve);
                didSomething = true;
                dismiss();
                break;
            case R.id.cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    public boolean didSomething() {
        return didSomething;
    }
}
