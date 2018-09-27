package com.jaimedediego.cubemaster.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.config.ThemeConfig;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.view.Adapters.MyPuzzlesAdapter;
import com.jaimedediego.cubemaster.view.Dialogs.NewPuzzleDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PuzzlesFragment extends Fragment {

    private RecyclerView puzzlesList;
    private MyPuzzlesAdapter adapter;
    private SearchView searchView;
    private boolean isFiltering = false;
    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_puzzles, menu);

        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        ThemeConfig.getInstance().themeSearchView(searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String filter) {
                adapter.getFilter().filter(filter);
                isFiltering = true;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String filter) {
                adapter.getFilter().filter(filter);
                isFiltering = true;
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_new) {
            searchView.clearFocus();
            final NewPuzzleDialog dialog = new NewPuzzleDialog(getActivity());
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (dialog.didSomething()) {
                        if(isFiltering){
                            adapter.getViewHolder().addNewPuzzle(dialog.newPuzzleName(), adapter.getItemCount());
                        } else {
                            adapter.getViewHolder().addNewPuzzle(dialog.newPuzzleName(), adapter.getItemCount()-2);
                        }
                    }
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_puzzles, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.my_puzzles);

        DatabaseMethods.getInstance().setDatabase(getActivity());

        puzzlesList = v.findViewById(R.id.puzzles_list);
        List<String> puzzles = new ArrayList<>();
        DatabaseMethods.getInstance().fillPuzzlesList(puzzles, getContext());
        adapter = new MyPuzzlesAdapter(getActivity(), puzzles);
        puzzlesList.setAdapter(adapter);

        return v;
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
