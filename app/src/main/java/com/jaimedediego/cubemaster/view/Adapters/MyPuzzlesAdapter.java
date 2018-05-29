package com.jaimedediego.cubemaster.view.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.utils.Session;

import java.util.ArrayList;
import java.util.List;

public class MyPuzzlesAdapter extends ArrayAdapter<String> implements Filterable {

    private List<String> puzzles;
    private List<String> filteredPuzzles;
    private Context context;
    private Drawable originalBackground;
    private SearchFilter filter;

    public MyPuzzlesAdapter(Context context, List<String> puzzles) {
        super(context, R.layout.element_puzzles_list, puzzles);
        this.puzzles = puzzles;
        filteredPuzzles = puzzles;
        this.context = context;
        if (filter == null){
            filter = new SearchFilter();
        }
    }

    public int getCount() {
        return filteredPuzzles.size();
    }

    public String getItem(int position) {
        return filteredPuzzles.get(position);
    }

    @NonNull
    public View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
        View item = convertView;
        final ViewHolder holder;

        DatabaseMethods.getInstance().setDatabase(context);

        if (item == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            item = inflater.inflate(R.layout.element_puzzles_list, null);
            holder = new ViewHolder();
            holder.name = item.findViewById(R.id.element);
            holder.more = item.findViewById(R.id.more_icon);
            originalBackground = holder.name.getBackground();
            item.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(filteredPuzzles.get(position));
        if (holder.name.getText().toString().equals(DatabaseMethods.getInstance().getCurrentPuzzleName())) {
            holder.name.setBackgroundColor(Session.getInstance().lighterColorTheme);
        } else {
            holder.name.setBackground(originalBackground);
        }
        if (holder.name.getText().toString().equals(context.getResources().getString(R.string.add_new))) {
            holder.name.setTextColor(Session.getInstance().lightColorTheme);
            holder.more.setVisibility(View.GONE);
        } else {
            holder.name.setTextColor(context.getColor(R.color.md_black_1000));
            holder.more.setVisibility(View.VISIBLE);
        }
        return (item);
    }

    private class ViewHolder {
        TextView name;
        ImageView more;
    }

    @NonNull
    public Filter getFilter() {
        return filter;
    }

    private class SearchFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();
            FilterResults filteredResults = new FilterResults();
            int count = puzzles.size();
            List<String> filterList = new ArrayList<>(count);
            String filterableString;
            for (int i = 0; i < count; i++) {
                filterableString = puzzles.get(i);
                if (filterableString.toLowerCase().contains(filterString)) {
                    filterList.add(filterableString);
                }
            }
            filteredResults.values = filterList;
            filteredResults.count = filterList.size();
            return filteredResults;

        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredPuzzles = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }
}
