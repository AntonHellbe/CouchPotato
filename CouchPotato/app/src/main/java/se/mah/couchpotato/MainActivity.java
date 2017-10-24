package se.mah.couchpotato;

import android.animation.Animator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import se.mah.couchpotato.activtysettings.ActivitySettings;

public class MainActivity extends AppCompatActivity implements ActivityInterface {

    private Controller controller;
    private ContainerFragment containerFragment;
    private FloatingActionButton fabSettings;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Log.v("MAINACTIVITY", "NAVIGATION IS ALLOWED: " + String.valueOf(getController().getDataFragment().isAllowNavigation()));
            if (controller.getDataFragment().isAllowNavigation()) {
                hideKeyBoard();
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

    public void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        controller = new Controller(this);
        initializeComponents();
        //TODO flytta detta till controller
        fragmentHandling(savedInstanceState);
    }

    private void initializeComponents() {
        fabSettings = (FloatingActionButton) findViewById(R.id.fab);
        final MainActivity activity = this;
        fabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, ActivitySettings.class);
                i.putExtra("revealX", (int) fabSettings.getX());
                i.putExtra("revealY", (int) fabSettings.getY());
                i.putExtra("startRadius", 24);
                activity.startActivity(i);
            }
        });
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
