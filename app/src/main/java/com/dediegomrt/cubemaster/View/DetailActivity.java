package com.dediegomrt.cubemaster.View;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dediegomrt.cubemaster.Config.ThemeConfig;
import com.dediegomrt.cubemaster.Methods.DatabaseMethods;
import com.dediegomrt.cubemaster.Methods.StatsMethods;
import com.dediegomrt.cubemaster.R;
import com.dediegomrt.cubemaster.Utils.Detail;
import com.dediegomrt.cubemaster.Utils.Session;
import com.dediegomrt.cubemaster.View.Adapters.SortBySpinnerAdapter;
import com.dediegomrt.cubemaster.View.Dialogs.DeletePuzzleDialog;

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
        setContentView(R.layout.activity_detail);

        ThemeConfig.getInstance().setActivity(this);
        ThemeConfig.getInstance().initConfig();

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.detail);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(Session.getInstance().darkColorTheme)));

        DatabaseMethods.getInstance().setDatabase(getBaseContext());

        RelativeLayout puzzleNameContainer = (RelativeLayout) findViewById(R.id.puzzle_name_container);
        puzzleNameContainer.setBackgroundResource(Session.getInstance().lightColorTheme);
        RelativeLayout detailContainer = (RelativeLayout) findViewById(R.id.times_detail);
        detailContainer.setBackgroundResource(Session.getInstance().lightColorTheme);
        timesLayout = (LinearLayout)findViewById(R.id.times);
        bestTime = (TextView)findViewById(R.id.best_time);
        worstTime = (TextView)findViewById(R.id.worst_time);
        average = (TextView) findViewById(R.id.average);
        average5 = (TextView) findViewById(R.id.averageof5);
        average10 = (TextView) findViewById(R.id.averageof10);
        timesCount = (TextView) findViewById(R.id.times_count);
        currentPuzzle = (TextView) findViewById(R.id.puzzle_name);
        sortMode = (Spinner) findViewById(R.id.sort_mode);

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

                final TextView date = (TextView)v.findViewById(R.id.date);
                final TextView time = (TextView)v.findViewById(R.id.time);
                final ImageButton button = (ImageButton)v.findViewById(R.id.delete_puzzle);
                final View divider = v.findViewById(R.id.divider);

                date.setText(detail.getDate());
                time.setText(detail.getTime());

                button.setColorFilter(ResourcesCompat.getColor(getResources(), Session.getInstance().lightColorTheme, null));
                divider.setBackgroundColor(getResources().getColor(Session.getInstance().lightColorTheme));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
