package se.mah.couchpotato.activitytvshow;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import se.mah.couchpotato.PosterListener;
import se.mah.couchpotato.R;
import se.mah.couchpotato.TvShow;

/**
 * Created by Gustaf Bohlin on 19/10/2017.
 */

public class RecyclerViewSeasonAdapter extends RecyclerView.Adapter<RecyclerViewSeasonAdapter.ViewHolder> {

    private ArrayList<TvShow> episodes;
    private ActivityTvShow activity;

    public void setTvShowArrayList(ArrayList<TvShow> episodes){
        this.episodes = episodes;
        notifyDataSetChanged();
    }

    public RecyclerViewSeasonAdapter(ArrayList<TvShow> episodes, ActivityTvShow activity) {
        this.episodes = episodes;
        this.activity = activity;
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    @Override
    public RecyclerViewSeasonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_season_item, parent, false);
        RecyclerViewSeasonAdapter.ViewHolder vh = new RecyclerViewSeasonAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerViewSeasonAdapter.ViewHolder holder, int position) {
        TvShow episode = episodes.get(position);
        holder.tvTitle.setText(episode.getName());
        String summary = (String) episode.getSummary();
        if (summary != null) {
            summary = summary.replace("<p>", "");
            summary = summary.replace("</p>", "");
        } else
            summary = "";
        holder.tvPlot.setText(summary);
        String number = "S" + (episode.getSeason() < 10 ? "0" + String.valueOf(episode.getSeason()) : String.valueOf(episode.getSeason())) + "E" + (episode.getNumber() < 10 ? "0" + String.valueOf(episode.getNumber()) : String.valueOf(episode.getNumber()));
        holder.tvNumber.setText(number);
//        TvShow.Image im = (TvShow.Image) episode.getImage();
//        if (im.getMedium() == null) {
//            holder.pbLoading.setVisibility(View.INVISIBLE);
//            holder.ivPoster.setVisibility(View.VISIBLE);
//            holder.ivPoster.setImageResource(android.R.drawable.sym_def_app_icon);
//        } else {
//            Bitmap poster = activity.getController().getDataFragment().getPicture(episode.getId().toString());
//            if (poster != null) {
//                holder.pbLoading.setVisibility(View.INVISIBLE);
//                holder.ivPoster.setVisibility(View.VISIBLE);
//                holder.ivPoster.setImageBitmap(poster);
//            } else {
//                holder.pbLoading.setVisibility(View.VISIBLE);
//                holder.ivPoster.setVisibility(View.INVISIBLE);
//                activity.getController().downloadPoster(im.getMedium(), episode.getId().toString(), holder);
//            }
//        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PosterListener {
        ImageView ivPoster;
        TextView tvTitle, tvPlot, tvScore, tvNumber;
        ProgressBar pbLoading;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.iv_episode_poster);
            tvTitle = itemView.findViewById(R.id.tv_episode_title);
            tvPlot = itemView.findViewById(R.id.tv_episode_plot);
            tvScore = itemView.findViewById(R.id.tv_episode_rating);
            tvNumber = itemView.findViewById(R.id.tv_episode_number);
            pbLoading = itemView.findViewById(R.id.pb_loading_episode_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onPosterDownloaded(String id, Bitmap bitmap) {
            ivPoster.setImageBitmap(bitmap);
            ivPoster.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.INVISIBLE);
            activity.getController().getDataFragment().putPicture(id, bitmap);
        }

        @Override
        public void onClick(View view) {
            EpisodeDialog dialog = new EpisodeDialog();
            dialog.show(activity.getFragmentManager(), "EPISODE");
        }
    }
}
