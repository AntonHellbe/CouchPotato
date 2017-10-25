package se.mah.couchpotato;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

/**
 * Created by Gustaf Bohlin on 24/10/2017.
 */

public class RecyclerViewAdapterFilter extends RecyclerView.Adapter<RecyclerViewAdapterFilter.ViewHolder> {

    private String[] filters;
    private MainActivity activity;

    public RecyclerViewAdapterFilter(MainActivity activity, String[] filters) {
        this.activity = activity;
        this.filters = filters;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_filter_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.checkBox.setText(filters[position]);
        holder.checkBox.setChecked(activity.getController().getDataFragment().getFilterIncludeMap().get(filters[position]));
    }

    @Override
    public int getItemCount() {
        return filters.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView;
            checkBox.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            String category = compoundButton.getText().toString();
            activity.getController().modifyFilter(category, checked);
        }
    }
}
