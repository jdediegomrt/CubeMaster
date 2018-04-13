package com.dediegomrt.cubemaster.View;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dediegomrt.cubemaster.Methods.DatabaseMethods;
import com.dediegomrt.cubemaster.R;
import com.dediegomrt.cubemaster.Utils.Session;
import com.dediegomrt.cubemaster.View.Adapters.MyPuzzlesAdapter;
import com.dediegomrt.cubemaster.View.Dialogs.NewPuzzleDialog;
import com.dediegomrt.cubemaster.View.Dialogs.PuzzleChangeDialog;
import com.dediegomrt.cubemaster.View.Dialogs.PuzzleOptionsDialog;

import java.util.ArrayList;

public class PuzzlesFragment extends Fragment {

    private ListView puzzlesList;
    private ArrayList<String> puzzles;
    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_puzzles, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_new){
            final NewPuzzleDialog dialog = new NewPuzzleDialog(getActivity());
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    ((MainActivity)getActivity()).refreshView();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_puzzles, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.my_puzzles);

        DatabaseMethods.getInstance().setDatabase(getActivity());

        puzzlesList = (ListView) v.findViewById(R.id.puzzles_list);
        fillList();

        puzzlesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final PuzzleOptionsDialog dialog = new PuzzleOptionsDialog(getActivity(), puzzles.get(position));
                dialog.show();
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        fillList();
                    }
                });
            }
        });

        return v;
    }

    private void fillList() {
        puzzles=new ArrayList<>();
        DatabaseMethods.getInstance().fillPuzzlesArrayList(puzzles);
        MyPuzzlesAdapter adapter = new MyPuzzlesAdapter(getActivity(), puzzles);
        puzzlesList.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
