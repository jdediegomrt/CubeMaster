package com.dediegomrt.cubemaster.View.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dediegomrt.cubemaster.Config.ThemeConfig;
import com.dediegomrt.cubemaster.Methods.PrefsMethods;
import com.dediegomrt.cubemaster.R;

public class ColorsAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    public ColorsAdapter(Context applicationContext) {
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return ThemeConfig.getInstance().colors().size();
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
            view = inflater.inflate(R.layout.color_element, viewGroup, false);
        }
        TextView color = (TextView) view.findViewById(R.id.color);
        color.setBackgroundResource(ThemeConfig.getInstance().colors().get(position));
        if(position == PrefsMethods.getInstance().getColorAccent()) {
            color.setAlpha(0.3f);
        }
        return view;
    }
}