package se.mah.couchpotato.activitytvshow;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import se.mah.couchpotato.R;

public class ActivityTvShow extends AppCompatActivity {

    private SeasonViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayoutSeasons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Toast.makeText(this, intent.getStringExtra("id"), Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_tv_show);
        adapter = new SeasonViewPagerAdapter(getSupportFragmentManager(), 5);   //TODO seasons from bundle
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        tabLayoutSeasons = (TabLayout) findViewById(R.id.tl_seasons);
        tabLayoutSeasons.setupWithViewPager(viewPager);
    }
}
