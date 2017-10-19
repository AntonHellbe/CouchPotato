package se.mah.couchpotato;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ActivityInterface {

    private TextView mTextMessage;
    private String currentTag;  //TODO flytta till datafragment

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_feed:
                    showFragment("FEED");
                    return true;
                case R.id.navigation_favorites:
                    showFragment("FAVORITES");
                    return true;
                case R.id.navigation_search:
                    showFragment("SEARCH");
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //TODO flytta detta till controller

        if(savedInstanceState==null) {
            FragmentFeed feed = new FragmentFeed();
            FragmentFavorites favorites = new FragmentFavorites();
            FragmentSearch search = new FragmentSearch();
            FragmentManager manager = getFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(R.id.content, feed, "FEED");
            ft.add(R.id.content, favorites, "FAVORITES");
            ft.add(R.id.content, search, "SEARCH");

            ft.hide(feed);
            ft.hide(favorites);
            ft.hide(search);
            ft.show(feed);
            ft.commit();
        }

        Button testknapp = (Button) findViewById(R.id.testknapp);
        testknapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO testa service här
            }
        });
    }

    public void showFragment(String TAG) {
        FragmentManager manager = getFragmentManager();
        Fragment fragment = manager.findFragmentByTag(TAG);
        Fragment currentFragment = manager.findFragmentByTag(currentTag);
        if(fragment!=null) {
            FragmentTransaction ft = manager.beginTransaction();
            if(currentFragment!=null) {
                ft.hide(currentFragment);
            }
            ft.show(fragment);
            ft.commit();
            currentTag = TAG;
        }
    }
}
