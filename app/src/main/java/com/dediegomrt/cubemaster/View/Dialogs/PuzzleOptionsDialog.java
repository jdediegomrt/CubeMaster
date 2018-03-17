package com.dediegomrt.cubemaster.View.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.dediegomrt.cubemaster.Methods.DatabaseMethods;
import com.dediegomrt.cubemaster.R;
import com.dediegomrt.cubemaster.View.DetailActivity;

public class PuzzleOptionsDialog extends Dialog implements View.OnClickListener{

    private String puzzle;
    private Context context;

    public PuzzleOptionsDialog(@NonNull Context context, String puzzle) {
        super(context);
        this.puzzle=puzzle;
        this.context=context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_whattodopuzzle);

        final Button delete = (Button) findViewById(R.id.deletePuzzle);
        final Button showStats = (Button) findViewById(R.id.showStats);
        final Button use = (Button) findViewById(R.id.usePuzzle);
        final Button reset = (Button) findViewById(R.id.resetPuzzle);

        if(!puzzle.equals(context.getString(R.string.default_puzzle))) {
            delete.setOnClickListener(this);
        } else {
            delete.setVisibility(View.GONE);
        }
        showStats.setOnClickListener(this);
        use.setOnClickListener(this);
        reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deletePuzzle:
                areYouSure(R.id.deletePuzzle);
                break;
            case R.id.resetPuzzle:
                areYouSure(R.id.resetPuzzle);
                break;
            case R.id.usePuzzle:
                DatabaseMethods.getInstance().usePuzzle(puzzle);
                dismiss();
                break;
            case R.id.showStats:
                dismiss();
                goToDetail();
                break;
            default:
                break;
        }
    }

    private void areYouSure(final int id){
        setContentView(R.layout.layout_dialog_areyousure);

        final TextView text = (TextView)findViewById(R.id.text);
        final Button ok = (Button)findViewById(R.id.ok);
        final Button cancel = (Button)findViewById(R.id.cancel);

        if(id==R.id.deletePuzzle) {
            text.setText(R.string.areyousuredelete);
        } else {
            text.setText(R.string.areyousurereset);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id==R.id.deletePuzzle) {
                    DatabaseMethods.getInstance().deletePuzzle(puzzle);
                } else {
                    DatabaseMethods.getInstance().resetPuzzle(puzzle);
                }
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

    private void goToDetail() {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("puzzleName", puzzle);
        getContext().startActivity(intent);
    }
}
