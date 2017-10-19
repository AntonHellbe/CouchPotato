package se.mah.couchpotato.activitytvshow;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import se.mah.couchpotato.Episode;
import se.mah.couchpotato.R;

/**
 * Created by Gustaf Bohlin on 19/10/2017.
 */

public class RecyclerViewSeasonAdapter extends RecyclerView.Adapter<RecyclerViewSeasonAdapter.ViewHolder> {

    private ArrayList<Episode> episodes;

    public void setTvShowArrayList(ArrayList<Episode> episodes){
        this.episodes = episodes;
        notifyDataSetChanged();
    }

    public RecyclerViewSeasonAdapter(ArrayList<Episode> episodes) {
        this.episodes = episodes;
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    @Override
    public RecyclerViewSeasonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        RecyclerViewSeasonAdapter.ViewHolder vh = new RecyclerViewSeasonAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerViewSeasonAdapter.ViewHolder holder, int position) {
        Episode episode = episodes.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivPoster;
        TextView tvTitle, tvPlot, tvScore;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.iv_episode_poster);
            tvTitle = itemView.findViewById(R.id.tv_episode_title);
            tvPlot = itemView.findViewById(R.id.tv_episode_plot);
            tvScore = itemView.findViewById(R.id.tv_episode_rating);
        }
    }
}
