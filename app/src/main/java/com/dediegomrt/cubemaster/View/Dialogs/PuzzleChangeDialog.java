package com.dediegomrt.cubemaster.View.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.dediegomrt.cubemaster.Methods.DatabaseMethods;
import com.dediegomrt.cubemaster.R;

import java.util.ArrayList;

public class PuzzleChangeDialog extends Dialog implements View.OnClickListener{

    private Context context;
    private Spinner spinner;

    public PuzzleChangeDialog(@NonNull final Context context) {
        super(context);
        this.context=context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_changedatabase);

        spinner = (Spinner) findViewById(R.id.databaseName);
        final Button ok = (Button) findViewById(R.id.okDialog);
        final Button addNew = (Button) findViewById(R.id.newDialog);

        ArrayList<String> puzzles = new ArrayList<>();
        DatabaseMethods.getInstance().fillPuzzlesArrayList(puzzles);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_element, puzzles);
        spinner.setAdapter(adapter);
        spinner.setSelection(puzzles.indexOf(DatabaseMethods.getInstance().getCurrentPuzzleName()));

        ok.setOnClickListener(this);
        addNew.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.okDialog:
                DatabaseMethods.getInstance().usePuzzle(spinner.getSelectedItem().toString());
                dismiss();
                break;
            case R.id.newDialog:
                NewPuzzleDialog dialog = new NewPuzzleDialog(context);
                dialog.show();
                dialog.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dismiss();
                    }
                });
                break;
            default:
                break;
        }
    }
}
