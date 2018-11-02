package com.jaimedediego.cubemaster.view.activities.main.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.jaimedediego.cubemaster.view.activities.main.adapters.PuzzlesListAdapter;
import com.jaimedediego.cubemaster.view.dialogs.NewPuzzleDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PuzzlesFragment extends Fragment {

    private PuzzlesListAdapter adapter;
    private SearchView searchView;
    private boolean isFiltering = false;
    private boolean filterMatchAddNew = false;
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
                if (!filter.isEmpty()) {
                    isFiltering = true;
                } else {
                    isFiltering = false;
                }
                adapter.getFilter().filter(filter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String filter) {
                if (!filter.isEmpty()) {
                    isFiltering = true;
                    if (getString(R.string.add_new).toLowerCase().contains(filter.toLowerCase())) {
                        filterMatchAddNew = true;
                    }
                } else {
                    isFiltering = false;
                }
                adapter.getFilter().filter(filter);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                isFiltering = false;
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_new) {
            searchView.clearFocus();
            final String previousPuzzle = DatabaseMethods.getInstance().getCurrentPuzzleName();
            final NewPuzzleDialog dialog = new NewPuzzleDialog(getActivity());
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (dialog.didSomething()) {
                        if (isFiltering) {
                            if (filterMatchAddNew) {
                                adapter.getViewHolder().addNewPuzzle(dialog.newPuzzleName(), adapter.getItemCount() - 1, previousPuzzle);
                            } else {
                                adapter.getViewHolder().addNewPuzzle(dialog.newPuzzleName(), adapter.getItemCount(), previousPuzzle);
                            }
                        } else {
                            adapter.getViewHolder().addNewPuzzle(dialog.newPuzzleName(), adapter.getItemCount() - 1, previousPuzzle);
                        }
                    }
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_puzzles, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.my_puzzles);

        DatabaseMethods.getInstance().setDatabase(getActivity());

        RecyclerView puzzlesList = v.findViewById(R.id.puzzles_list);
        List<String> puzzles = new ArrayList<>();
        DatabaseMethods.getInstance().fillPuzzlesList(puzzles, getContext());
        adapter = new PuzzlesListAdapter(getActivity(), puzzles);
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
