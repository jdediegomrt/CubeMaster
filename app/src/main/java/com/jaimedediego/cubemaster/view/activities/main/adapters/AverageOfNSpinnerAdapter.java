package com.jaimedediego.cubemaster.view.activities.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jaimedediego.cubemaster.R;

import java.util.Arrays;
import java.util.List;

public class AverageOfNSpinnerAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private final List<Integer> avgs = Arrays.asList(20, 50, 100, 200, 500, 1000);

    public AverageOfNSpinnerAdapter(Context context) {
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return avgs.size();
    }

    @Override
    public Integer getItem(int i) {
        return avgs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.element_spinner, viewGroup, false);
        }
        TextView element = view.findViewById(R.id.element);
        element.setText(String.valueOf(avgs.get(position)));
        return view;
    }
}