package se.mah.couchpotato;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import se.mah.couchpotato.activitytvshow.ActivityTvShow;

/**
 * Created by Anton on 2017-10-19.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<TvShow> tvShowArrayList;
    private AppCompatActivity activity;
    private boolean imagesLoaded = false;

    public void setTvShowArrayList(ArrayList<TvShow> tvShowList) {
        this.tvShowArrayList = tvShowList;
        imagesLoaded = false;
        notifyDataSetChanged();
    }

    public RecyclerViewAdapter(AppCompatActivity activity) {
        this.activity = activity;
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
        holder.id = tvShow.getShow().getId().toString();
        holder.tvTitle.setText(tvShow.getShow().getName());
        if (!holder.animated) {
            setAnimation(holder.itemView, position);
            holder.animated = true;
        }
        if (tvShow.getShow().getImage() == null) {
            holder.pbLoading.setVisibility(View.INVISIBLE);
            holder.ivPoster.setVisibility(View.VISIBLE);
            holder.ivPoster.setImageResource(android.R.drawable.sym_def_app_icon);
        } else {
            Bitmap poster = ((MainActivity) activity).getController().getDataFragment().getPicture(tvShow.getShow().getId().toString());
            if (poster != null) {
                holder.pbLoading.setVisibility(View.INVISIBLE);
                holder.ivPoster.setVisibility(View.VISIBLE);
                holder.ivPoster.setImageBitmap(poster);
            } else {
                holder.pbLoading.setVisibility(View.VISIBLE);
                holder.ivPoster.setVisibility(View.INVISIBLE);
                ((MainActivity) activity).getController().downloadPoster(tvShow.getShow().getImage().getMedium(), tvShow.getShow().getId().toString(), holder);
            }
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.anim_slide_up);
        animation.setStartOffset(position * 10);
        viewToAnimate.startAnimation(animation);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements PosterListener, View.OnClickListener {
        boolean animated = false;
        ImageView ivPoster;
        TextView tvTitle, tvPlot, tvScore, tvGenres;
        ProgressBar pbLoading;
        String id;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.iv_poster);
            tvTitle = itemView.findViewById(R.id.tv_title);
            pbLoading = itemView.findViewById(R.id.pb_loading_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onPosterDownloaded(Bitmap bitmap) {
            ivPoster.setImageBitmap(bitmap);
            ivPoster.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onClick(View view) {
            String transitionPoster = activity.getResources().getString(R.string.transition_show_poster);
            String transitionTitle = activity.getResources().getString(R.string.transition_show_title);
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                            ivPoster,   // The view which starts the transition
                            transitionPoster    // The transitionName of the view we’re transitioning to
                    );
            Intent intent = new Intent(activity, ActivityEpisode.class);
            Bundle bundle = new Bundle();
            ActivityCompat.startActivity(activity, intent, options.toBundle());
            //intent.putExtra("id", id);
            //activity.startActivity(intent);
        }
    }
}
