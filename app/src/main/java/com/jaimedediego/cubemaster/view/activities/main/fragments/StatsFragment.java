package com.jaimedediego.cubemaster.view.activities.main.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.methods.StatsMethods;
import com.jaimedediego.cubemaster.utils.Detail;
import com.jaimedediego.cubemaster.utils.Session;
import com.jaimedediego.cubemaster.view.customViews.CustomLineChart;
import com.jaimedediego.cubemaster.view.customViews.CustomLineDataSet;
import com.jaimedediego.cubemaster.view.dialogs.PuzzleChangeDialog;
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
            DatabaseMethods.getInstance().deleteLastSolve(Session.getInstance().getCurrentPuzzleId());
            ((MainActivity) getActivity()).refreshView();
            return true;
        } else if (id == R.id.change_database) {
            final PuzzleChangeDialog dialog = new PuzzleChangeDialog(getActivity());
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    ((MainActivity) getActivity()).refreshView();
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stats, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.stats);

        DatabaseMethods.getInstance().setDatabase(getActivity());

        RelativeLayout puzzleNameContainer = v.findViewById(R.id.puzzle_name_container);
        puzzleNameContainer.setBackgroundColor(Session.getInstance().getLightColorTheme());
        TextView bestTime = v.findViewById(R.id.best_time);
        TextView worstTime = v.findViewById(R.id.worst_time);
        TextView average = v.findViewById(R.id.average);
        TextView average5 = v.findViewById(R.id.averageof5);
        TextView average10 = v.findViewById(R.id.averageof10);
        TextView timesCount = v.findViewById(R.id.times_count);
        TextView currentPuzzle = v.findViewById(R.id.puzzle_name);

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

        currentPuzzle.setText(DatabaseMethods.getInstance().getCurrentPuzzleName());
        timesCount.setText(String.valueOf(StatsMethods.getInstance().countTimes(null)));
        bestTime.setText(StatsMethods.getInstance().getBestTime(null));
        worstTime.setText(StatsMethods.getInstance().getWorstTime(null));
        average.setText(StatsMethods.getInstance().getAverage(null, 0));
        average5.setText(StatsMethods.getInstance().getAverage(null, 5));
        average10.setText(StatsMethods.getInstance().getAverage(null, 10));

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
