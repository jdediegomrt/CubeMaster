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

public class IndicatorsSpinnerAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private final List<Integer> indicators = Arrays.asList(
            R.string.dots,
            R.string.line
    );

    public IndicatorsSpinnerAdapter(Context context) {
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return indicators.size();
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
        element.setText(indicators.get(position));
        return view;
    }
}