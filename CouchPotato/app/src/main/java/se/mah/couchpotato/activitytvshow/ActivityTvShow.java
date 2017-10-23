package se.mah.couchpotato.activitytvshow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import se.mah.couchpotato.AllEpisodesListener;
import se.mah.couchpotato.EpisodeListener;
import se.mah.couchpotato.R;
import se.mah.couchpotato.TvShow;

public class ActivityTvShow extends AppCompatActivity {

    private SeasonViewPagerAdapter adapter;
    private ScrollableViewPager viewPager;
    private TabLayout tabLayoutSeasons;
    private ProgressBar pbLoading;
    private ImageView ivPoster;
    private TextView tvTitle, tvRating, tvPlot, tvAir;
    private TvShowController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show);
        initializeComponents();
        handlePosterAnimation();
        controller = new TvShowController(this, getIntent().getStringExtra("id"));
    }

    private void initializeComponents() {
        ivPoster = (ImageView) findViewById(R.id.iv_id_poster);
        pbLoading = (ProgressBar) findViewById(R.id.pb_loading_episodes);
        tvTitle = (TextView) findViewById(R.id.tv_id_title);
        tvPlot = (TextView) findViewById(R.id.tv_id_plot);
        String title = getIntent().getStringExtra("title");
        String plot = getIntent().getStringExtra("plot");
        plot = plot.replace("<p>", "");
        plot = plot.replace("</p>", "");
        plot = plot.replace("<b>", "");
        plot = plot.replace("</b>", "");
        plot = plot.replace("<i>", "");
        plot = plot.replace("</i>", "");
        tvTitle.setText(title);
        tvPlot.setText(plot);
    }

    private void handlePosterAnimation() {
        Intent intent = getIntent();
        Bitmap poster = intent.getParcelableExtra("POSTER");
        ivPoster.setImageBitmap(poster);
    }

    public TvShowController getController() {
        return controller;
    }

    public void updateData(int seasons) {
        adapter = new SeasonViewPagerAdapter(getSupportFragmentManager(), seasons);
        viewPager = (ScrollableViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        Point p = new Point();
        getWindowManager().getDefaultDisplay().getSize(p);
        viewPager.setHardCodedHeight(p.y / 2);
        tabLayoutSeasons = (TabLayout) findViewById(R.id.tl_seasons);
        tabLayoutSeasons.setupWithViewPager(viewPager);
        pbLoading.setVisibility(View.INVISIBLE);
        tabLayoutSeasons.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        controller.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        controller.onDestroy();
    }
}
