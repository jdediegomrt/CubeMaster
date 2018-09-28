package com.jaimedediego.cubemaster.view.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.config.ThemeConfig;
import com.jaimedediego.cubemaster.methods.PrefsMethods;
import com.jaimedediego.cubemaster.view.Dialogs.RestartDialog;

public class ColorsAdapter {

    private LayoutInflater inflater;
    private Context context;

    public ColorsAdapter(Context applicationContext) {
        context = applicationContext;
        inflater = (LayoutInflater.from(context));
    }

    public View getView(final int position, ViewGroup viewGroup) {
        View view = inflater.inflate(R.layout.element_color_grid, viewGroup, false);
        View color = view.findViewById(R.id.element);
        color.setBackgroundResource(ThemeConfig.getInstance().colors().get(position));
        if (position == PrefsMethods.getInstance().getColorAccent()) {
            color.setAlpha(0.3f);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != PrefsMethods.getInstance().getColorAccent()) {
                    final RestartDialog dialog = new RestartDialog(context, position);
                    dialog.show();
                }
            }
        });
        return view;
    }
}