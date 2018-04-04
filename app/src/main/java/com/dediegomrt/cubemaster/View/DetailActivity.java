package com.dediegomrt.cubemaster.View;

import android.animation.Animator;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dediegomrt.cubemaster.Methods.DatabaseMethods;
import com.dediegomrt.cubemaster.Methods.StatsMethods;
import com.dediegomrt.cubemaster.R;
import com.dediegomrt.cubemaster.Utils.Detail;
import com.dediegomrt.cubemaster.Utils.Session;
import com.dediegomrt.cubemaster.View.Adapters.SortBySpinnerAdapter;

import java.util.ArrayList;
import java.util.Collections;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.getOverflowIcon().setTint(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
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

        currentPuzzle.setText(getCurrentPuzzle());
        timesCount.setText(String.valueOf(StatsMethods.getInstance().countTimes(getCurrentPuzzle())));
        bestTime.setText(StatsMethods.getInstance().getBestTime(getCurrentPuzzle()));
        worstTime.setText(StatsMethods.getInstance().getWorstTime(getCurrentPuzzle()));
        average.setText(StatsMethods.getInstance().getAverage(getCurrentPuzzle(), 0));
        average5.setText(StatsMethods.getInstance().getAverage(getCurrentPuzzle(), 5));
        average10.setText(StatsMethods.getInstance().getAverage(getCurrentPuzzle(), 10));

        SortBySpinnerAdapter adapter = new SortBySpinnerAdapter(getBaseContext());
        sortMode.setAdapter(adapter);
        sortMode.setSelection(0);

        sortMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                timesLayout.removeAllViews();
                getTimesDetail(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
    }

    private String getCurrentPuzzle(){
        return getIntent().getStringExtra("puzzleName");
    }

    private void getTimesDetail(int mode){
        final ArrayList<Detail> timesDetail = DatabaseMethods.getInstance().getTimesDetail(getCurrentPuzzle(), mode);
        switch (mode){
            case 2: Collections.sort(timesDetail, Detail.TimeComparatorAsc);
                break;
            case 3: Collections.sort(timesDetail, Detail.TimeComparatorDesc);
                break;
        }
        if(!timesDetail.isEmpty()) {
            for (int i = 0; i < timesDetail.size(); i++) {
                final Detail detail = timesDetail.get(i);

                final View v = getLayoutInflater().inflate(R.layout.element_timesdetail_list, null);

                final RelativeLayout item = (RelativeLayout)v.findViewById(R.id.detail);
                final TextView date = (TextView)v.findViewById(R.id.date);
                final TextView time = (TextView)v.findViewById(R.id.time);
                final Button button = (Button)v.findViewById(R.id.delete_puzzle);
                final View divider = v.findViewById(R.id.divider);

                date.setText(detail.getDate());
                time.setText(detail.getTime());

                button.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_cancel_black_18dp));
                button.setBackgroundTintList(ContextCompat.getColorStateList(DetailActivity.this, Session.getInstance().lightColorTheme));

                divider.setBackgroundColor(getResources().getColor(Session.getInstance().lightColorTheme));

                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        v.setClickable(false);
                        v.setEnabled(false);
                        button.setVisibility(View.VISIBLE);
                        button.animate().alpha(1.0f).setDuration(100).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        button.animate().alpha(0.0f).setDuration(300).setListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {

                                            }
                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                button.setVisibility(View.GONE);
                                                v.setClickable(true);
                                                v.setEnabled(true);
                                            }
                                            @Override
                                            public void onAnimationCancel(Animator animation) {

                                            }
                                            @Override
                                            public void onAnimationRepeat(Animator animation) {

                                            }
                                        }).start();
                                    }
                                }, 2200);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }).start();
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        (timesLayout).removeView((View)v.getParent().getParent());
                        DatabaseMethods.getInstance().deleteSolve(detail.getNumSolve());
                        timesCount.setText(String.valueOf(StatsMethods.getInstance().countTimes(getCurrentPuzzle())));
                        bestTime.setText(StatsMethods.getInstance().getBestTime(getCurrentPuzzle()));
                        worstTime.setText(StatsMethods.getInstance().getWorstTime(getCurrentPuzzle()));
                        average.setText(StatsMethods.getInstance().getAverage(getCurrentPuzzle(), 0));
                        average5.setText(StatsMethods.getInstance().getAverage(getCurrentPuzzle(), 5));
                        average10.setText(StatsMethods.getInstance().getAverage(getCurrentPuzzle(), 10));
                    }
                });

                timesLayout.addView(v);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
