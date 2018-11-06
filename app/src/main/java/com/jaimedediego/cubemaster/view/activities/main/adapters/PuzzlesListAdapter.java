package com.jaimedediego.cubemaster.view.activities.main.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.config.ScrambleConfig;
import com.jaimedediego.cubemaster.config.ThemeConfig;
import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.methods.PrefsMethods;
import com.jaimedediego.cubemaster.utils.Constants;
import com.jaimedediego.cubemaster.utils.Session;
import com.jaimedediego.cubemaster.view.activities.detail.DetailActivity;
import com.jaimedediego.cubemaster.view.customViews.CustomToast;
import com.jaimedediego.cubemaster.view.dialogs.AreYouSureDialog;
import com.jaimedediego.cubemaster.view.dialogs.NewPuzzleDialog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class PuzzlesListAdapter extends RecyclerView.Adapter<PuzzlesListAdapter.ViewHolder> implements Filterable {

    private List<String> puzzles;
    private List<String> filteredPuzzles;
    private Context context;
    private Drawable originalBackground;
    private SearchFilter filter;
    private CharSequence filterSequence;
    private ViewHolder viewHolder;

    public PuzzlesListAdapter(Context context, List<String> puzzles) {
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
        if (getItem(position).equals(DatabaseMethods.getInstance().getCurrentPuzzleName())) {
            holder.elementCard.setBackgroundColor(Session.getInstance().getLighterColorTheme());
            holder.element.setVisibility(View.VISIBLE);
            holder.use.setImageResource(R.drawable.baseline_check_circle_outline_white_24);

            holder.more.getBackground().setTint(Color.WHITE);
            holder.more.setColorFilter(Session.getInstance().getLightColorTheme());
        } else {
            holder.elementCard.setBackground(originalBackground);
            holder.element.setVisibility(View.VISIBLE);
            holder.use.setImageResource(R.drawable.baseline_radio_button_unchecked_white_24);

            holder.more.getBackground().setTint(Session.getInstance().getLighterColorTheme());
            holder.more.setColorFilter(Color.WHITE);
        }

        if (getItem(position).equals(context.getResources().getString(R.string.add_new))) {
            holder.name.setTextColor(Session.getInstance().getLightColorTheme());
            holder.element.setVisibility(View.VISIBLE);
            holder.optionsLayout.setVisibility(View.GONE);
        } else {
            if (holder.name.getText().toString().isEmpty()) {
                holder.element.setVisibility(View.GONE);
            } else {
                holder.name.setTextColor(ThemeConfig.getInstance().getColorFromResource(R.color.md_black_1000));
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
        ImageButton more;
        ImageView use;
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
            more = view.findViewById(R.id.more_icon);
            use = view.findViewById(R.id.use_icon);

            use.setColorFilter(Session.getInstance().getLightColorTheme());
            originalBackground = name.getBackground();

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(context, more);
                    popup.inflate(R.menu.menu_puzzle_options);

                    popup.getMenu().findItem(R.id.detail).getIcon().setTint(Session.getInstance().getLightColorTheme());
                    popup.getMenu().findItem(R.id.reset).getIcon().setTint(Session.getInstance().getLightColorTheme());
                    popup.getMenu().findItem(R.id.delete).getIcon().setTint(Session.getInstance().getLightColorTheme());

                    try {
                        Field[] fields = popup.getClass().getDeclaredFields();
                        for (Field field : fields) {
                            if ("mPopup".equals(field.getName())) {
                                field.setAccessible(true);
                                Object menuPopupHelper = field.get(popup);
                                Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                                Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                                setForceIcons.invoke(menuPopupHelper, true);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.detail:
                                    goToDetail(position);
                                    return true;
                                case R.id.reset:
                                    AreYouSureDialog areYouSureReset = new AreYouSureDialog(context, getItem(position), R.id.reset);
                                    areYouSureReset.show();
                                    return true;
                                case R.id.delete:
                                    if (DatabaseMethods.getInstance().countPuzzles() == 1) {
                                        new CustomToast(context, R.string.must_be_one_puzzle).showAndHide(Constants.getInstance().TOAST_MEDIUM_DURATION);
                                    } else {
                                        final String currentPuzzle = DatabaseMethods.getInstance().getCurrentPuzzleName();
                                        final AreYouSureDialog areYouSureDelete = new AreYouSureDialog(context, getItem(position), R.id.delete);
                                        areYouSureDelete.show();
                                        areYouSureDelete.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialogInterface) {
                                                if (areYouSureDelete.didSomething()) {
                                                    String removed = filteredPuzzles.remove(position);
                                                    puzzles.remove(removed);
                                                    notifyItemRemoved(position);
                                                    for (int i = position; i < filteredPuzzles.size(); i++) {
                                                        notifyItemChanged(i);
                                                    }
                                                    if (removed.toLowerCase().equals(currentPuzzle.toLowerCase())) {
                                                        notifyItemChanged(0);
                                                    }
                                                }
                                            }
                                        });
                                    }
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.show();
                }
            });

            element.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String previousPuzzle = DatabaseMethods.getInstance().getCurrentPuzzleName();
                    if (getItem(position).equals(context.getString(R.string.add_new))) {
                        final NewPuzzleDialog dialog = new NewPuzzleDialog(context);
                        dialog.show();
                        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                if (dialog.didSomething()) {
                                    addNewPuzzle(dialog.newPuzzleName(), position, previousPuzzle);
                                }
                            }
                        });
                    } else {
                        elementCard.setBackgroundColor(Session.getInstance().getLighterColorTheme());
                        DatabaseMethods.getInstance().usePuzzle(getItem(position));
                        if (!DatabaseMethods.getInstance().getCurrentPuzzleName().equals(previousPuzzle)) {
                            Session.getInstance().setCurrentScramble("");
                            Session.getInstance().setCurrentScrambleSvg(null);
                            /*TODO: this is not efficient at all, little leak when change puzzles a lot of times*/
                            if (Constants.getInstance().CUBEMASTER_DEFAULT_PUZZLES_NAMES.contains(DatabaseMethods.getInstance().getCurrentPuzzleName()) && PrefsMethods.getInstance().isScrambleEnabled()) {
                                ScrambleConfig.getInstance().doScramble();
                            }
                            for (int i = 0; i < filteredPuzzles.size(); i++) {
                                if (getItem(i).equals(getItem(position)) || getItem(i).equals(previousPuzzle)) {
                                    notifyItemChanged(i);
                                }
                            }
                        }
                    }
                }
            });
        }

        public void addNewPuzzle(String newPuzzleName, int position, String previousPuzzle) {
            filteredPuzzles.add(position, newPuzzleName);
            if (filteredPuzzles.size() != puzzles.size()) {
                puzzles.add(puzzles.size() - 1, newPuzzleName);
            }
            if (filterSequence != null && !filterSequence.toString().isEmpty()) {
                if (newPuzzleName.toLowerCase().contains(filterSequence.toString().toLowerCase())) {
                    notifyItemInserted(position);
                    for (int i = 0; i < filteredPuzzles.size(); i++) {
                        if (getItem(i).equals(previousPuzzle)) {
                            notifyItemChanged(i);
                        }
                    }
                } else {
                    filter.filter(filterSequence);
                }
            } else {
                notifyItemInserted(position);
                for (int i = 0; i < filteredPuzzles.size(); i++) {
                    if (getItem(i).equals(previousPuzzle) || getItem(i).equals(context.getString(R.string.add_new))) {
                        notifyItemChanged(i);
                    }
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
