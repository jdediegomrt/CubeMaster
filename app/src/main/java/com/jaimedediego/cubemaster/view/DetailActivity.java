package com.jaimedediego.cubemaster.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jaimedediego.cubemaster.config.ThemeConfig;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.methods.StatsMethods;
import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.utils.Detail;
import com.jaimedediego.cubemaster.utils.Session;
import com.jaimedediego.cubemaster.view.Adapters.SortBySpinnerAdapter;
import com.jaimedediego.cubemaster.view.Dialogs.DeletePuzzleDialog;

import java.util.Collections;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private LinearLayout timesLayout;
    TextView bestTime;
    TextView worstTime;
    TextView average;
    TextView average5;
    TextView average10;
    TextView timesCount;
    TextView currentPuzzle;
    Spinner sortMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThemeConfig.getInstance().setActivity(this);
        ThemeConfig.getInstance().initConfig();

        setContentView(R.layout.activity_detail);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.detail);

        DatabaseMethods.getInstance().setDatabase(getBaseContext());

        RelativeLayout puzzleNameContainer = findViewById(R.id.puzzle_name_container);
        puzzleNameContainer.setBackgroundColor(Session.getInstance().lightColorTheme);
        RelativeLayout detailContainer = findViewById(R.id.times_detail);
        detailContainer.setBackgroundColor(Session.getInstance().lightColorTheme);
        timesLayout = findViewById(R.id.times);
        bestTime = findViewById(R.id.best_time);
        worstTime = findViewById(R.id.worst_time);
        average = findViewById(R.id.average);
        average5 = findViewById(R.id.averageof5);
        average10 = findViewById(R.id.averageof10);
        timesCount = findViewById(R.id.times_count);
        currentPuzzle = findViewById(R.id.puzzle_name);
        sortMode = findViewById(R.id.sort_mode);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            LayoutTransition layoutTransition = timesLayout.getLayoutTransition();
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        }

        currentPuzzle.setText(getCurrentPuzzle());
        refreshView();

        SortBySpinnerAdapter adapter = new SortBySpinnerAdapter(getBaseContext());
        sortMode.setAdapter(adapter);
        sortMode.setSelection(0);

        sortMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                timesLayout.removeAllViewsInLayout();
                getTimesDetail(position, DetailActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {/*Do nothing*/}
        });
    }

    private String getCurrentPuzzle(){
        return getIntent().getStringExtra("puzzleName");
    }

    private void getTimesDetail(int mode, final Context context){
        final List<Detail> timesDetail = DatabaseMethods.getInstance().getTimesDetail(getCurrentPuzzle(), mode);
        switch (mode){
            case 2: Collections.sort(timesDetail, Detail.TimeComparatorAsc);
                break;
            case 3: Collections.sort(timesDetail, Detail.TimeComparatorDesc);
                break;
            default: break;
        }
        if(!timesDetail.isEmpty()) {
            for (int i = 0; i < timesDetail.size(); i++) {
                final Detail detail = timesDetail.get(i);

                final View v = getLayoutInflater().inflate(R.layout.element_timesdetail_list, null);

                final TextView date = v.findViewById(R.id.date);
                final TextView time = v.findViewById(R.id.time);
                final TextView scramble = v.findViewById(R.id.scramble);
                final ImageButton button = v.findViewById(R.id.delete_puzzle);

                date.setText(detail.getDate());
                time.setText(detail.getTime());
                if(detail.getScramble()!=null && !detail.getScramble().equals("")){
                    scramble.setVisibility(View.VISIBLE);
                    scramble.setText(detail.getScramble());
                }

                button.setColorFilter(Session.getInstance().lighterColorTheme);
                time.setBackgroundColor(Session.getInstance().darkColorTheme);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final DeletePuzzleDialog dialog = new DeletePuzzleDialog(context, timesLayout, v, detail.getNumSolve());
                        dialog.show();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                if(dialog.didSomething()){
                                    refreshView();
                                }
                            }
                        });
                    }
                });

                timesLayout.addView(v);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            finish();
        }
        return true;
    }

    private void refreshView(){
        timesCount.setText(String.valueOf(StatsMethods.getInstance().countTimes(getCurrentPuzzle())));
        bestTime.setText(StatsMethods.getInstance().getBestTime(getCurrentPuzzle()));
        worstTime.setText(StatsMethods.getInstance().getWorstTime(getCurrentPuzzle()));
        average.setText(StatsMethods.getInstance().getAverage(getCurrentPuzzle(), 0));
        average5.setText(StatsMethods.getInstance().getAverage(getCurrentPuzzle(), 5));
        average10.setText(StatsMethods.getInstance().getAverage(getCurrentPuzzle(), 10));
    }
}
