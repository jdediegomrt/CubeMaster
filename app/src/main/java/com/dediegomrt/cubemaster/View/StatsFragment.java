package com.dediegomrt.cubemaster.View;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dediegomrt.cubemaster.Methods.DatabaseMethods;
import com.dediegomrt.cubemaster.Methods.StatsMethods;
import com.dediegomrt.cubemaster.R;
import com.dediegomrt.cubemaster.Utils.Session;

public class StatsFragment extends Fragment {

	private OnFragmentInteractionListener mListener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_stats, container, false);

		DatabaseMethods.getInstance().setDatabase(getActivity());

        RelativeLayout puzzleNameContainer = (RelativeLayout) v.findViewById(R.id.puzzle_name_container);
        puzzleNameContainer.setBackgroundResource(Session.getInstance().lightColorTheme);
		TextView bestTime = (TextView)v.findViewById(R.id.best_time);
		TextView worstTime = (TextView)v.findViewById(R.id.worst_time);
		TextView average = (TextView) v.findViewById(R.id.average);
		TextView average5 = (TextView) v.findViewById(R.id.averageof5);
		TextView average10 = (TextView) v.findViewById(R.id.averageof10);
		TextView timesCount = (TextView) v.findViewById(R.id.times_count);
		TextView currentPuzzle = (TextView) v.findViewById(R.id.puzzle_name);

		timesCount.setText(String.valueOf(StatsMethods.getInstance().countTimes(null)));
		currentPuzzle.setText(DatabaseMethods.getInstance().getCurrentPuzzleName());
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

	interface OnFragmentInteractionListener {
		void onFragmentInteraction(Uri uri);
	}
}
