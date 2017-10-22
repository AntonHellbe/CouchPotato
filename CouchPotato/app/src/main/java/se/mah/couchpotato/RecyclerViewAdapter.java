package se.mah.couchpotato;

import android.graphics.Bitmap;
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
        holder.tvTitle.setText(tvShow.getShow().getName());
        if (!holder.animated) {
            setAnimation(holder.itemView, position);
            holder.animated = true;
        }
        Bitmap poster = ((MainActivity) activity).getController().getDataFragment().getPicture(tvShow.getShow().getImage().getMedium());
        if (poster != null) {
            holder.pbLoading.setVisibility(View.INVISIBLE);
            holder.ivPoster.setVisibility(View.VISIBLE);
            holder.ivPoster.setImageBitmap(poster);
        }
        else {
            holder.pbLoading.setVisibility(View.VISIBLE);
            holder.ivPoster.setVisibility(View.INVISIBLE);

        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.anim_slide_up);
        animation.setStartOffset(position * 10);
        viewToAnimate.startAnimation(animation);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements PosterListener {
        boolean animated = false;
        ImageView ivPoster;
        TextView tvTitle, tvPlot, tvScore, tvGenres;
        ProgressBar pbLoading;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.iv_poster);
            tvTitle = itemView.findViewById(R.id.tv_title);
            pbLoading = itemView.findViewById(R.id.pb_loading_poster);
        }

        @Override
        public void onPosterDownloaded(Bitmap bitmap) {
            ivPoster.setImageBitmap(bitmap);
            ivPoster.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.INVISIBLE);
        }
    }
}
