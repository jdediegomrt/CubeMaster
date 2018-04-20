package com.dediegomrt.cubemaster.View.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dediegomrt.cubemaster.Methods.DatabaseMethods;
import com.dediegomrt.cubemaster.R;
import com.dediegomrt.cubemaster.View.DetailActivity;

public class PuzzleOptionsDialog extends Dialog implements View.OnClickListener{

    private String puzzle;
    private Context context;

    private boolean didSomething=false;

    public PuzzleOptionsDialog(@NonNull Context context, String puzzle) {
        super(context);
        this.puzzle=puzzle;
        this.context=context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_whattodopuzzle);

        TextView currentPuzzle = (TextView) findViewById(R.id.puzzle_name);
        final Button delete = (Button) findViewById(R.id.delete_puzzle);
        final Button showStats = (Button) findViewById(R.id.show_stats);
        final Button use = (Button) findViewById(R.id.use_puzzle);
        final Button reset = (Button) findViewById(R.id.reset_puzzle);

        currentPuzzle.setText(puzzle);
        if(DatabaseMethods.getInstance().countPuzzles()==1) {
            delete.setTextColor(context.getColor(R.color.md_blue_grey_600));
        }
        delete.setOnClickListener(this);
        showStats.setOnClickListener(this);
        use.setOnClickListener(this);
        reset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_puzzle:
                if(DatabaseMethods.getInstance().countPuzzles()==1){
                    Toast.makeText(context, R.string.must_be_one_puzzle, Toast.LENGTH_SHORT).show();
                } else {
                    areYouSure(R.id.delete_puzzle);
                }
                break;
            case R.id.reset_puzzle:
                areYouSure(R.id.reset_puzzle);
                break;
            case R.id.use_puzzle:
                DatabaseMethods.getInstance().usePuzzle(puzzle);
                didSomething=true;
                dismiss();
                break;
            case R.id.show_stats:
                dismiss();
                goToDetail();
                break;
            default:
                break;
        }
    }

    private void areYouSure(final int id){
        setContentView(R.layout.layout_dialog_areyousure);

        TextView currentPuzzle = (TextView) findViewById(R.id.puzzle_name);
        final TextView text = (TextView)findViewById(R.id.info_areyousure);
        final Button ok = (Button)findViewById(R.id.accept);
        final Button cancel = (Button)findViewById(R.id.cancel);

        currentPuzzle.setText(puzzle);
        if(id==R.id.delete_puzzle) {
            text.setText(R.string.areyousuredelete);
        } else {
            text.setText(R.string.areyousurereset);
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id==R.id.delete_puzzle) {
                    DatabaseMethods.getInstance().deletePuzzle(puzzle);
                } else {
                    DatabaseMethods.getInstance().resetPuzzle(puzzle);
                }
                didSomething=true;
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

    public boolean didSomething (){
        return didSomething;
    }
}
