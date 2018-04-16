package com.dediegomrt.cubemaster.View.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dediegomrt.cubemaster.Methods.DatabaseMethods;
import com.dediegomrt.cubemaster.R;
import com.dediegomrt.cubemaster.Utils.Session;

import java.util.List;

public class MyPuzzlesAdapter extends ArrayAdapter<String> {

    private List<String> puzzles;
    private Context context;
    private Drawable originalBackground;

    public MyPuzzlesAdapter(Context context, List<String> puzzles) {
        super(context, R.layout.element_puzzles_list, puzzles);
        this.puzzles = puzzles;
        this.context=context;
    }

    @NonNull
    public View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
        View item = convertView;
        final ViewHolder holder;

        DatabaseMethods.getInstance().setDatabase(context);

        if(item==null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.element_puzzles_list, null);
            holder = new ViewHolder();
            holder.name = (TextView)item.findViewById(R.id.element);
            originalBackground = holder.name.getBackground();
            item.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.name.setText(puzzles.get(position));
        if(holder.name.getText().toString().equals(DatabaseMethods.getInstance().getCurrentPuzzleName())) {
            holder.name.setBackgroundResource(Session.getInstance().lightColorTheme);
        } else {
            holder.name.setBackground(originalBackground);
        }
        if(holder.name.getText().toString().equals(context.getResources().getString(R.string.add_new))){
            holder.name.setTextColor(context.getColor(Session.getInstance().lightColorTheme));
        } else {
            holder.name.setTextColor(context.getColor(R.color.md_black_1000));
        }
        return(item);
    }

    private class ViewHolder{
        TextView name;
    }
}
