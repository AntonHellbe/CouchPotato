package se.mah.couchpotato.activitytvshow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import se.mah.couchpotato.R;

public class ActivityTvShow extends AppCompatActivity {

    private SeasonViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayoutSeasons;
    private ImageView ivPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_show);
        initializeComponents();
        handlePosterAnimation();
        adapter = new SeasonViewPagerAdapter(getSupportFragmentManager(), 5);   //TODO seasons from bundle
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        tabLayoutSeasons = (TabLayout) findViewById(R.id.tl_seasons);
        tabLayoutSeasons.setupWithViewPager(viewPager);
    }

    private void initializeComponents() {
//        ScrollView sv = (ScrollView) findViewById(R.id.sv_parent);
//        sv.setFillViewport(true);
        ivPoster = (ImageView) findViewById(R.id.iv_id_poster);
    }

    private void handlePosterAnimation() {
        Intent intent = getIntent();
        Bitmap poster = intent.getParcelableExtra("POSTER");
        ivPoster.setImageBitmap(poster);
    }
}
