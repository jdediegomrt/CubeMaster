package com.dediegomrt.cubemaster.View.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dediegomrt.cubemaster.R;

import java.util.Arrays;
import java.util.List;

public class SortBySpinnerAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private final List<Integer> modes = Arrays.asList(
            R.string.recent,
            R.string.latest,
            R.string.best_time,
            R.string.worst_time
    );

    public SortBySpinnerAdapter(Context context) {
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return modes.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.element_sortby_spinner, viewGroup, false);
        }
        TextView element = view.findViewById(R.id.element);
        element.setText(modes.get(position));
        return view;
    }
}