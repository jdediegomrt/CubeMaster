package com.jaimedediego.cubemaster.view.activities.detail;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.config.PrefsConfig;
import com.jaimedediego.cubemaster.config.ThemeConfig;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.utils.AndroidUtils;
import com.jaimedediego.cubemaster.utils.Detail;
import com.jaimedediego.cubemaster.utils.Session;
import com.jaimedediego.cubemaster.view.activities.main.adapters.SortBySpinnerAdapter;
import com.jaimedediego.cubemaster.view.dialogs.DeletePuzzleDialog;

import java.util.Collections;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private LinearLayout timesLayout;
    Spinner sortMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseMethods.getInstance().setDatabase(getBaseContext());
        PrefsConfig.getInstance().setContext(this);
        PrefsConfig.getInstance().initConfig();
        ThemeConfig.getInstance().setActivity(this);
        ThemeConfig.getInstance().initConfig();

        setContentView(R.layout.activity_detail);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Drawable upArrow = getResources().getDrawable(R.drawable.baseline_arrow_back_white_24);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getCurrentPuzzle());

        DatabaseMethods.getInstance().setDatabase(getBaseContext());

        RelativeLayout detailContainer = findViewById(R.id.sort_container);
        detailContainer.setBackgroundColor(Session.getInstance().getLightColorTheme());
        timesLayout = findViewById(R.id.details_container);
        sortMode = findViewById(R.id.details_sort_mode);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            LayoutTransition layoutTransition = timesLayout.getLayoutTransition();
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        }

        SortBySpinnerAdapter adapter = new SortBySpinnerAdapter(getBaseContext());
        sortMode.setAdapter(adapter);

        if(DatabaseMethods.getInstance().countTimesByName(getCurrentPuzzle()) != 0) {
            AndroidUtils.switchVisibility(findViewById(R.id.times_detail), findViewById(R.id.solve_puzzle));
            sortMode.setSelection(0);
        }

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

    private String getCurrentPuzzle() {
        return getIntent().getStringExtra("puzzleName");
    }

    private void getTimesDetail(int mode, final Context context) {
        final List<Detail> timesDetail = DatabaseMethods.getInstance().getTimesDetail(getCurrentPuzzle(), mode);
        switch (mode) {
            case 2:
                Collections.sort(timesDetail, Detail.TimeComparatorAsc);
                break;
            case 3:
                Collections.sort(timesDetail, Detail.TimeComparatorDesc);
                break;
            default:
                break;
        }
        if (!timesDetail.isEmpty()) {
            for (int i = 0; i < timesDetail.size(); i++) {
                final Detail detail = timesDetail.get(i);

                final View v = getLayoutInflater().inflate(R.layout.element_timesdetail_list, null);

                final TextView date = v.findViewById(R.id.date);
                final TextView time = v.findViewById(R.id.time);
                final TextView scramble = v.findViewById(R.id.scramble);
                final ImageView image = v.findViewById(R.id.image);
                final ImageButton button = v.findViewById(R.id.delete_puzzle);

                date.setText(detail.getDate());
                time.setText(detail.getTime());
                if (detail.getScramble() != null && !detail.getScramble().isEmpty()) {
                    scramble.setVisibility(View.VISIBLE);
                    scramble.setText(detail.getScramble());
                    image.setVisibility(View.VISIBLE);
                    image.setImageDrawable(new PictureDrawable(detail.getImage()));
                }

                button.setColorFilter(Session.getInstance().getLighterColorTheme());
                time.setBackgroundColor(Session.getInstance().getDarkColorTheme());

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final DeletePuzzleDialog dialog = new DeletePuzzleDialog(context, detail.getNumSolve(), timesLayout, v);
                        dialog.show();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                if(dialog.didSomething()){
                                    if(DatabaseMethods.getInstance().countTimesByName(getCurrentPuzzle()) == 0) {
                                        AndroidUtils.switchVisibility(findViewById(R.id.times_detail), findViewById(R.id.solve_puzzle));
                                    }
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
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }
}
