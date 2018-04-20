package com.dediegomrt.cubemaster.View;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dediegomrt.cubemaster.Methods.DatabaseMethods;
import com.dediegomrt.cubemaster.Methods.StatsMethods;
import com.dediegomrt.cubemaster.R;
import com.dediegomrt.cubemaster.Utils.Session;
import com.dediegomrt.cubemaster.View.Dialogs.PuzzleChangeDialog;

public class StatsFragment extends Fragment {

	private OnFragmentInteractionListener mListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
		inflater.inflate(R.menu.menu_stats, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.delete_last_solve) {
			DatabaseMethods.getInstance().deleteLastSolve(Session.getInstance().currentPuzzleId);
			((MainActivity)getActivity()).refreshView();
			return true;
		} else if (id == R.id.change_database) {
			final PuzzleChangeDialog dialog = new PuzzleChangeDialog(getActivity());
			dialog.show();
			dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					((MainActivity)getActivity()).refreshView();
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
		((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.stats);

		DatabaseMethods.getInstance().setDatabase(getActivity());

        RelativeLayout puzzleNameContainer = (RelativeLayout) v.findViewById(R.id.puzzle_name_container);
        puzzleNameContainer.setBackgroundColor(Session.getInstance().lightColorTheme);
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
