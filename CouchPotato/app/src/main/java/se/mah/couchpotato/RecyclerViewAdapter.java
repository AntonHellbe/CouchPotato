package se.mah.couchpotato;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import se.mah.couchpotato.activitytvshow.ActivityTvShow;

/**
 * Created by Anton on 2017-10-19.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<TvShow> tvShowArrayList;
    private AppCompatActivity activity;

    public void setTvShowArrayList(ArrayList<TvShow> tvShowList) {
        this.tvShowArrayList = tvShowList;
        notifyDataSetChanged();
        Log.v("RECYCLERVIEWADAPTER", String.valueOf(getItemCount()));
    }

    public void insertTvShow(TvShow show) {
        tvShowArrayList.add(show);
        notifyItemInserted(0);
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
        holder.show = tvShow;
        holder.tvTitle.setText(tvShow.getShow().getName());
        if (((MainActivity) activity).getController().getDataFragment().getFavorites().containsKey(tvShow.getShow().getId().toString()))
            holder.ivFavorite.setVisibility(View.VISIBLE);
        else
            holder.ivFavorite.setVisibility(View.GONE);
        if (!holder.animated) {
            setAnimation(holder.itemView);
            holder.animated = true;
        }
        if (tvShow.getShow().getImage() == null)
            return;
        if (tvShow.getShow().getImage().getMedium() == null) {
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

    private void setAnimation(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(activity, R.anim.anim_fade_in);
        animation.setStartOffset(new Random().nextInt(501));
        viewToAnimate.startAnimation(animation);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements PosterListener, View.OnClickListener, Palette.PaletteAsyncListener {
        boolean animated = false;
        ImageView ivPoster, ivFavorite;
        TextView tvTitle, tvPlot, tvScore, tvGenres;
        ProgressBar pbLoading;
        TvShow show;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.iv_poster);
            ivFavorite = itemView.findViewById(R.id.iv_favorite);
            tvTitle = itemView.findViewById(R.id.tv_title);
            pbLoading = itemView.findViewById(R.id.pb_loading_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onPosterDownloaded(String id, Bitmap bitmap) {
            if (show.getShow().getId().toString().equals(id))
                ivPoster.setImageBitmap(bitmap);
            ivPoster.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.INVISIBLE);
            ((MainActivity)activity).getController().getDataFragment().putPictureMap(id, bitmap);
            if (bitmap == null)
                Log.e("RECYCLERVIEWADAPTER", "DOWNLOADED BITMAP IS NULL");  //TODO we really should handle network problems here
        }

        @Override
        public void onClick(View view) {
            String transitionPoster = activity.getResources().getString(R.string.transition_show_poster);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, ivPoster, transitionPoster);
            Intent intent = new Intent(activity, ActivityTvShow.class);
            intent.putExtra("POSTER", ((BitmapDrawable) ivPoster.getDrawable()).getBitmap());
            intent.putExtra("id", show.getShow().getId().toString());
            intent.putExtra("title", show.getShow().getName());
            intent.putExtra("plot", show.getShow().getSummary());
            intent.putExtra("hd", show.getShow().getImage().getOriginal());
            intent.putExtra("favorite", ((MainActivity) activity).getController().getDataFragment().getFavorites().containsKey(show.getShow().getId().toString()));
            if(show.getShow().getImage().getOriginal() != null) //Apparently a show can have a medium image but not a original
                intent.putExtra("hd", show.getShow().getImage().getOriginal());
            if(show.getShow().getRating().getAverage() != null)
                intent.putExtra("rating", show.getShow().getRating().getAverage().toString());
            Bundle bundle = options.toBundle();
            ActivityCompat.startActivityForResult(activity, intent, MainActivity.REQUESTCODETVSHOW, bundle);
        }

        @Override
        public void onGenerated(Palette palette) {
            Palette.Swatch vibrant = palette.getVibrantSwatch();
            if (vibrant != null) {
                ((CardView) itemView).setCardBackgroundColor(vibrant.getRgb());
                tvTitle.setTextColor(vibrant.getTitleTextColor());
            }
        }
    }
}
