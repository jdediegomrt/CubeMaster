package com.jaimedediego.cubemaster.view.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.config.ScrambleConfig;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.methods.ScrambleMethods;
import com.jaimedediego.cubemaster.utils.Constants;
import com.jaimedediego.cubemaster.utils.Session;
import com.jaimedediego.cubemaster.view.CustomViews.CustomToast;
import com.jaimedediego.cubemaster.view.DetailActivity;
import com.jaimedediego.cubemaster.view.Dialogs.AreYouSureDialog;
import com.jaimedediego.cubemaster.view.Dialogs.NewPuzzleDialog;

import java.util.ArrayList;
import java.util.List;

public class MyPuzzlesAdapter extends RecyclerView.Adapter<MyPuzzlesAdapter.ViewHolder> implements Filterable {

    private List<String> puzzles;
    private List<String> filteredPuzzles;
    private Context context;
    private Drawable originalBackground;
    private SearchFilter filter;
    private CharSequence filterSequence;
    private ViewHolder viewHolder;

    public MyPuzzlesAdapter(Context context, List<String> puzzles) {
        this.puzzles = puzzles;
        filteredPuzzles = puzzles;
        this.context = context;
        if (filter == null) {
            filter = new SearchFilter();
        }
    }

    String getItem(int position) {
        return filteredPuzzles.get(position);
    }

    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_puzzles_list, parent, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DatabaseMethods.getInstance().setDatabase(context);
        holder.setPosition(position);

        holder.name.setText(getItem(position));
        if (holder.name.getText().toString().equals(DatabaseMethods.getInstance().getCurrentPuzzleName())) {
            holder.elementCard.setBackgroundColor(Session.getInstance().lighterColorTheme);
            holder.element.setVisibility(View.VISIBLE);
            holder.use.setVisibility(View.GONE);
        } else {
            holder.elementCard.setBackground(originalBackground);
            holder.element.setVisibility(View.VISIBLE);
            holder.use.setVisibility(View.VISIBLE);
        }

        if (holder.name.getText().toString().equals(context.getResources().getString(R.string.add_new))) {
            holder.name.setTextColor(Session.getInstance().lightColorTheme);
            holder.element.setVisibility(View.VISIBLE);
            holder.optionsLayout.setVisibility(View.GONE);
        } else {
            if (holder.name.getText().equals("")) {
                holder.element.setVisibility(View.INVISIBLE);
            } else {
                holder.name.setTextColor(context.getColor(R.color.md_black_1000));
                holder.optionsLayout.setVisibility(View.VISIBLE);
                holder.element.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return filteredPuzzles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout element;
        RelativeLayout elementCard;
        TextView name;
        LinearLayout optionsLayout;
        ImageButton detail;
        ImageButton reset;
        ImageButton delete;
        Button use;
        int position;

        void setPosition(int position) {
            this.position = position;
        }

        ViewHolder(View view) {
            super(view);
            element = view.findViewById(R.id.element);
            elementCard = view.findViewById(R.id.element_card);
            name = view.findViewById(R.id.name);
            optionsLayout = view.findViewById(R.id.options_layout);
            detail = view.findViewById(R.id.detail_icon);
            reset = view.findViewById(R.id.reset_icon);
            delete = view.findViewById(R.id.delete_icon);
            use = view.findViewById(R.id.use_icon);

            detail.setColorFilter(Session.getInstance().lightColorTheme);
            reset.setColorFilter(Session.getInstance().lightColorTheme);
            delete.setColorFilter(Session.getInstance().lightColorTheme);
            use.setTextColor(Session.getInstance().lightColorTheme);

            originalBackground = name.getBackground();

            element.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getItem(position).equals(context.getString(R.string.add_new))) {
                        final NewPuzzleDialog dialog = new NewPuzzleDialog(context);
                        dialog.show();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                if (dialog.didSomething()) {
                                    addNewPuzzle(dialog.newPuzzleName(), position);
                                }
                            }
                        });
                    }
                }
            });

            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToDetail(position);
                }
            });

            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AreYouSureDialog areYouSureDialog = new AreYouSureDialog(context, getItem(position), R.id.reset_icon);
                    areYouSureDialog.show();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (DatabaseMethods.getInstance().countPuzzles() == 1) {
                        new CustomToast(context, R.string.must_be_one_puzzle).showAndHide(Constants.getInstance().TOAST_MEDIUM_DURATION);
                    } else {
                        final AreYouSureDialog areYouSureDialog = new AreYouSureDialog(context, getItem(position), R.id.delete_icon);
                        areYouSureDialog.show();
                        areYouSureDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                if (areYouSureDialog.didSomething()) {
                                    String removed = filteredPuzzles.remove(position);
                                    puzzles.remove(removed);
                                    notifyItemRemoved(position);
                                    for (int i = 0; i < filteredPuzzles.size(); i++) {
                                        notifyItemChanged(i);
                                    }
                                }
                            }
                        });
                    }
                }
            });

            use.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    elementCard.setBackgroundColor(Session.getInstance().lighterColorTheme);
                    DatabaseMethods.getInstance().usePuzzle(getItem(position));
                    if (ScrambleConfig.getInstance().puzzlesWithScramble.contains(DatabaseMethods.getInstance().getCurrentPuzzleName())) {
                        ScrambleMethods.getInstance().getCurrentNxNxNPuzzleNotation();
                        Session.getInstance().currentPuzzleScramble = ScrambleMethods.getInstance().scramble();
                        Log.e("Notation", "Scramble --- " + Session.getInstance().currentPuzzleScramble);
                    }
                    for (int i = 0; i < filteredPuzzles.size(); i++) {
                        notifyItemChanged(i);
                    }
                }
            });
        }

        public void addNewPuzzle(String newPuzzleName, int position) {
            filteredPuzzles.add(position, newPuzzleName);
            if (filteredPuzzles.size() != puzzles.size()) {
                puzzles.add(puzzles.size() - 2, newPuzzleName);
            }
            if (filterSequence != null && !filterSequence.equals("")) {
                if (newPuzzleName.toLowerCase().contains(filterSequence.toString().toLowerCase())) {
                    notifyItemInserted(position);
                    for (int i = 0; i < filteredPuzzles.size(); i++) {
                        notifyItemChanged(i);
                    }
                } else {
                    filter.filter(filterSequence);
                }
            } else {
                notifyItemInserted(position);
                for (int i = 0; i < filteredPuzzles.size(); i++) {
                    notifyItemChanged(i);
                }
            }
        }
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
            filterSequence = constraint;
            filteredPuzzles = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }
    }

    private void goToDetail(int position) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("puzzleName", getItem(position));
        context.startActivity(intent);
    }
}
