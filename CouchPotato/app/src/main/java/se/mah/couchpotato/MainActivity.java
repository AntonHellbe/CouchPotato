package se.mah.couchpotato;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ActivityInterface {

    private Controller controller;
    private ContainerFragment containerFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Log.v("MAINACTIVITY", "NAVIGATION IS ALLOWED: " + String.valueOf(getController().getDataFragment().isAllowNavigation()));
            if (controller.getDataFragment().isAllowNavigation()) {
                switch (item.getItemId()) {
                    case R.id.navigation_feed:
                        containerFragment.show(ContainerFragment.TAG_FEED);
                        return true;
                    case R.id.navigation_favorites:
                        containerFragment.show(ContainerFragment.TAG_FAVORITES);
                        return true;
                    case R.id.navigation_search:
                        containerFragment.show(ContainerFragment.TAG_SEARCH);
                        return true;
                }
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

        controller = new Controller(this);
        //TODO flytta detta till controller
        fragmentHandling(savedInstanceState);
    }

    //@TODO borde vara i kontrollern
    public void fragmentHandling(Bundle bundle){

        //This schould be in Main activity!
        FragmentManager fml = getFragmentManager();
        containerFragment = (ContainerFragment) fml.findFragmentById(R.id.container_fragment);

        //This schould be in The Main Controller!
        FragmentManager fm = containerFragment.getChildFragmentManager();

        //@TODO Should not be in bundle, move to dataFragment
        if (bundle == null) {
            containerFragment.add(new FragmentFavorites(), ContainerFragment.TAG_FAVORITES);
            containerFragment.add(new FragmentFeed(), ContainerFragment.TAG_FEED);
            containerFragment.add(new FragmentSearch(), ContainerFragment.TAG_SEARCH);
            containerFragment.setCurrentTag(ContainerFragment.TAG_FEED);
        } else {
            containerFragment.add(fm.findFragmentByTag(ContainerFragment.TAG_FEED),ContainerFragment.TAG_FEED);
            containerFragment.add(fm.findFragmentByTag(ContainerFragment.TAG_FAVORITES),ContainerFragment.TAG_FAVORITES);
            containerFragment.add(fm.findFragmentByTag(ContainerFragment.TAG_SEARCH),ContainerFragment.TAG_SEARCH);
        }
    }

    public FragmentInterface getFragmentByTag(String tag) {
        FragmentManager fm = containerFragment.getChildFragmentManager();
        return (FragmentInterface) fm.findFragmentByTag(tag);
    }

    @Override
    protected void onPause() {
        controller.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        controller.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        controller.onResume();
        super.onResume();
    }

    @Override
    public Controller getController() {
        return controller;
    }
}
