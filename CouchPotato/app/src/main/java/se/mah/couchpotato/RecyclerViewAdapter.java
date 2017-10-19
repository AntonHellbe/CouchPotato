package se.mah.couchpotato;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Anton on 2017-10-19.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<TvShow> tvShowArrayList;

    public void setTvShowArrayList(ArrayList<TvShow> tvShowList){
        this.tvShowArrayList = tvShowList;
        notifyDataSetChanged();
    }

    public RecyclerViewAdapter(){
        tvShowArrayList = new ArrayList<>(); // To avoid crash (List not null)
    }

    @Override
    public int getItemCount() {
        return tvShowArrayList.size();
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        TvShow tvShow = tvShowArrayList.get(position);
        holder.tvTitle.setText(tvShow.getName());
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivPoster;
        TextView tvTitle, tvPlot, tvScore, tvGenres;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.iv_poster);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }
}
