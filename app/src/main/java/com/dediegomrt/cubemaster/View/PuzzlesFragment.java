package com.dediegomrt.cubemaster.View;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.dediegomrt.cubemaster.Methods.DatabaseMethods;
import com.dediegomrt.cubemaster.R;
import com.dediegomrt.cubemaster.View.Adapters.MyPuzzlesAdapter;
import com.dediegomrt.cubemaster.View.Dialogs.NewPuzzleDialog;
import com.dediegomrt.cubemaster.View.Dialogs.PuzzleOptionsDialog;

import java.util.ArrayList;
import java.util.List;

public class PuzzlesFragment extends Fragment {

    private ListView puzzlesList;
    private List<String> puzzles;
    private MyPuzzlesAdapter adapter;
    private MenuItem searchItem;
    private SearchView searchView;
    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_puzzles, menu);

        searchItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager)  getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setIconifiedByDefault(false);

        SearchView.SearchAutoComplete searchAutoComplete =
                (SearchView.SearchAutoComplete)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.WHITE);
        searchAutoComplete.setTextColor(Color.WHITE);

        ImageView searchIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_mag_icon);
        searchIcon.setImageResource(R.drawable.ic_search_white_24dp);

        ImageView voiceIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        voiceIcon.setImageResource(R.drawable.ic_close_white_24dp);

        View searchPlate = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        searchPlate.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String filter){
                adapter.getFilter().filter(filter);
                puzzlesList.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String filter) {
                adapter.getFilter().filter(filter);
                puzzlesList.setAdapter(adapter);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_new){
            final NewPuzzleDialog dialog = new NewPuzzleDialog(getActivity());
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (dialog.didSomething()){
                        ((MainActivity)getActivity()).refreshView();
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.my_puzzles);

        DatabaseMethods.getInstance().setDatabase(getActivity());

        puzzlesList = (ListView) v.findViewById(R.id.puzzles_list);
        fillList();

        puzzlesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                searchView.clearFocus();
                if(adapter.getItem(position).equals(getString(R.string.add_new))){
                    final NewPuzzleDialog dialog = new NewPuzzleDialog(getActivity());
                    dialog.show();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (dialog.didSomething()){
                                searchItem.collapseActionView();
                                fillList();
                            }
                        }
                    });
                } else {
                    final PuzzleOptionsDialog dialog = new PuzzleOptionsDialog(getActivity(), adapter.getItem(position));
                    dialog.show();
                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (dialog.didSomething()){
                                searchItem.collapseActionView();
                                fillList();
                            }
                        }
                    });
                }
            }
        });

        return v;
    }

    private void fillList() {
        puzzles=new ArrayList<>();
        DatabaseMethods.getInstance().fillPuzzlesList(puzzles, getContext());
        adapter = new MyPuzzlesAdapter(getActivity(), puzzles);
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
