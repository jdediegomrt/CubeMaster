package com.jaimedediego.cubemaster.view.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.R;

import java.util.ArrayList;
import java.util.List;

public class PuzzleChangeDialog extends Dialog implements View.OnClickListener{

    private Context context;
    private Spinner spinner;

    public PuzzleChangeDialog(@NonNull final Context context) {
        super(context);
        this.context=context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_changedatabase);

        spinner = findViewById(R.id.puzzle_name);
        final Button ok = findViewById(R.id.accept);
        final Button cancel = findViewById(R.id.cancel);
        final Button addNew = findViewById(R.id.add_new);

        List<String> puzzles = new ArrayList<>();
        DatabaseMethods.getInstance().fillPuzzlesList(puzzles, context);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.element_puzzlechange_spinner, puzzles);
        spinner.setAdapter(adapter);
        spinner.setSelection(puzzles.indexOf(DatabaseMethods.getInstance().getCurrentPuzzleName()));

        ok.setOnClickListener(this);
        addNew.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accept:
                DatabaseMethods.getInstance().usePuzzle(spinner.getSelectedItem().toString());
                dismiss();
                break;
            case R.id.add_new:
                NewPuzzleDialog dialog = new NewPuzzleDialog(context);
                dialog.show();
                dialog.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dismiss();
                    }
                });
                break;
            case R.id.cancel:
                dismiss();
                break;
            default:
                break;
        }
    }
}
