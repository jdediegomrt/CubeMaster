package com.dediegomrt.cubemaster.View.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dediegomrt.cubemaster.Methods.DatabaseMethods;
import com.dediegomrt.cubemaster.R;
import com.dediegomrt.cubemaster.View.DetailActivity;

public class DeletePuzzleDialog extends Dialog implements View.OnClickListener{

    private LinearLayout parent;
    private View child;
    private int numSolve;

    public DeletePuzzleDialog(@NonNull Context context, LinearLayout parent, View child, int numSolve) {
        super(context);
        this.parent=parent;
        this.child=child;
        this.numSolve=numSolve;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_areyousure);

        final TextView text = (TextView)findViewById(R.id.info_areyousure);
        final Button ok = (Button)findViewById(R.id.accept);
        final Button cancel = (Button)findViewById(R.id.cancel);

        text.setText(R.string.areyousuredeletepuzzle);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept:
                parent.removeView((View)child.getParent().getParent());
                DatabaseMethods.getInstance().deleteSolve(numSolve);
                dismiss();
                break;
            case R.id.cancel:
                dismiss();
                break;
            default: break;
        }
    }
}