package com.jaimedediego.cubemaster.view.activities.main.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.methods.PrefsMethods;
import com.jaimedediego.cubemaster.methods.StatsMethods;
import com.jaimedediego.cubemaster.utils.Detail;
import com.jaimedediego.cubemaster.utils.Session;
import com.jaimedediego.cubemaster.view.activities.detail.DetailActivity;
import com.jaimedediego.cubemaster.view.customViews.CustomLineChart;
import com.jaimedediego.cubemaster.view.customViews.CustomLineDataSet;
import com.jaimedediego.cubemaster.view.activities.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class StatsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_stats, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_last_solve) {
            DatabaseMethods.getInstance().deleteCurrentPuzzleLastSolve();
            ((MainActivity) getActivity()).refreshView();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stats, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(DatabaseMethods.getInstance().getCurrentPuzzleName());

        DatabaseMethods.getInstance().setDatabase(getActivity());

        TextView bestTime = v.findViewById(R.id.best_time);
        TextView worstTime = v.findViewById(R.id.worst_time);
        TextView average = v.findViewById(R.id.average);
        TextView average5 = v.findViewById(R.id.averageof5);
        TextView average12 = v.findViewById(R.id.averageof12);
        EditText averageNumber = v.findViewById(R.id.averageofn_edittext);
        TextView averageN = v.findViewById(R.id.averageofn);
        TextView timesCount = v.findViewById(R.id.times_count);

        CardView chartCard = v.findViewById(R.id.chart_card);
        CustomLineChart chart = v.findViewById(R.id.chart);
        TextView chartName = v.findViewById(R.id.chart_name);
        chartName.setBackgroundColor(Session.getInstance().getDarkColorTheme());
        chartName.setText(R.string.times_chart_name);
        final List<Detail> timesDetail = DatabaseMethods.getInstance().getTimesDetail(DatabaseMethods.getInstance().getCurrentPuzzleName(), 1);
        if (timesDetail.size() != 0) {
            List<Entry> entries = new ArrayList<>();
            float i = 0;
            for (Detail data : timesDetail) {
                i++;
                entries.add(new Entry(i, StatsMethods.getInstance().timeToMillis(data.getTime())));
            }
            CustomLineDataSet dataSet = new CustomLineDataSet(entries, "Times chart"); // add entries to dataset
            LineData lineData = new LineData(dataSet);
            chart.setData(lineData);
            if (entries.size() > 25) {
                dataSet.setDrawValues(false);
            }
        } else {
            chartCard.setVisibility(View.GONE);
        }
        chart.invalidate();

        timesCount.setText(String.valueOf(StatsMethods.getInstance().countTimes(null)));
        bestTime.setText(StatsMethods.getInstance().getBestTime(null));
        worstTime.setText(StatsMethods.getInstance().getWorstTime(null));
        average.setText(StatsMethods.getInstance().getAverage(null, 0));
        average5.setText(StatsMethods.getInstance().getAverage(null, 5));
        average12.setText(StatsMethods.getInstance().getAverage(null, 12));

        if (PrefsMethods.getInstance().getAverageOfN() != 0) {
            averageN.setText(StatsMethods.getInstance().getAverage(null, PrefsMethods.getInstance().getAverageOfN()));
            averageNumber.setText(String.valueOf(PrefsMethods.getInstance().getAverageOfN()));
        } else {
            averageN.setText(R.string.threedots);
            averageNumber.setHint(R.string.avg_n_example);
        }

        averageNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent event) {
                if (!textView.getText().toString().equals("")) {
                    if (i == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)){
                        PrefsMethods.getInstance().setAverageOfN(Integer.parseInt(textView.getText().toString()));
                        averageN.setText(StatsMethods.getInstance().getAverage(null, Integer.parseInt(textView.getText().toString())));
                    }
                } else {
                    averageN.setText(R.string.threedots);
                }
                return false;
            }
        });

        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else throw new RuntimeException(context.toString()
                + " must implement OnFragmentInteractionListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
