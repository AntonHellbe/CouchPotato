package se.mah.couchpotato;

import android.Manifest;
import android.animation.Animator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ActivityInterface {

    private Controller controller;
    private FloatingActionButton fabSettings, fabFilter;
    private BottomNavigationView bottomNavigationView;
    private TextView tvNetworkProblems;
    private CardView cardViewFilter;
    private RecyclerView recyclerViewFilters;
    public static final int REQUESTCODESETTINGS = 8;
    public static final int REQUESTCODETVSHOW = 7;
    private ConnectivityManager connManger;
    private NetworkInfo networkInfo;
    private Boolean networkProblem = true;

    public void hidekeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onResume() {
        connManger = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connManger.registerDefaultNetworkCallback(new NetworkHandler());
        }
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controller = new Controller(this);
        initializeComponents();
        initializeListeners();
        controller.fragmentHandling();

    }


    private void initializeComponents() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        fabSettings = (FloatingActionButton) findViewById(R.id.fab);
        fabFilter = (FloatingActionButton) findViewById(R.id.fab_filter);
        cardViewFilter = (CardView) findViewById(R.id.cv_filter);
        recyclerViewFilters = (RecyclerView) findViewById(R.id.rv_filter);
        recyclerViewFilters.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFilters.setAdapter(new RecyclerViewAdapterFilter(this, controller.getFilters()));
        tvNetworkProblems = (TextView) findViewById(R.id.tv_network_problems);
    }

    private void initializeListeners() {
        Listener listener = new Listener();
        fabSettings.setOnClickListener(listener);
        fabFilter.setOnClickListener(listener);
        bottomNavigationView.setOnNavigationItemSelectedListener(listener);
    }

    public void toggleFilter() {

        Animator anim;
        Point p = new Point();
        getWindowManager().getDefaultDisplay().getSize(p);

        if (cardViewFilter.getVisibility() == View.VISIBLE) {
            anim = ViewAnimationUtils.createCircularReveal(cardViewFilter, (int) fabFilter.getX() + 20, (int) fabFilter.getY() + 20, p.y, 0);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    cardViewFilter.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
        else {
            cardViewFilter.setVisibility(View.VISIBLE);
            anim = ViewAnimationUtils.createCircularReveal(cardViewFilter, (int) fabFilter.getX() + 20, (int) fabFilter.getY() + 20, 0, p.y);
        }
//        TODO put in controller
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.start();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESTCODESETTINGS) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                controller.saveSettings(bundle);
            }else {
                Toast.makeText(this, "Settings not applied", Toast.LENGTH_SHORT).show();    //TODO @strings
            }
        }
        if (requestCode == REQUESTCODETVSHOW) {
            if (resultCode == RESULT_OK) {
                controller.modifyFavorites(data.getStringExtra("id") , data.getBooleanExtra("favorite", false));
            }
        }
    }

    @Override
    public Controller getController() {
        return controller;
    }

    public Boolean getNetworkProblem() {
        return networkProblem;
    }

    public void setNetworkProblem(Boolean networkProblem) {
        this.networkProblem = networkProblem;
    }

    public void indicateNetworkProblem() {
        final MainActivity activity = this;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(activity, R.anim.anim_slide_in_down);
                tvNetworkProblems.startAnimation(animation);
                tvNetworkProblems.setVisibility(View.VISIBLE);
            }
        });
    }

    public void removeNetworkProblem() {
        final MainActivity activity = this;
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(activity, R.anim.anim_slide_out_up);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        tvNetworkProblems.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                if (tvNetworkProblems.getVisibility() == View.VISIBLE) {
                    tvNetworkProblems.requestLayout();
                    tvNetworkProblems.startAnimation(animation);
                }
            }
        });
    }

    public boolean checkNetworkConnection(){
        networkInfo = connManger.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }


    private class Listener implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public void onClick(View view) {
            if (view == fabSettings)
                controller.fabSettingsClicked(fabSettings.getX() + 20, fabSettings.getY() + 20);
            if (view == fabFilter)
                controller.fabFilterClicked();
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return controller.navigationClicked(item);
        }
    }

    public class NetworkHandler extends NetworkCallback{
        @Override
        public void onUnavailable() {
            indicateNetworkProblem();
            Log.v("TEST", "CHANGES IN NETWORK!! UNAVAILABLE");
            networkProblem = true;
            super.onUnavailable();
        }

        @Override
        public void onAvailable(Network network) {
            removeNetworkProblem();
            Log.v("TEST", "CHANGES IN NETWORK!! AVAILABLE");
            networkInfo = connManger.getActiveNetworkInfo();
            networkProblem = false;
            if(networkInfo != null)
                controller.networkChange(networkInfo);
            super.onAvailable(network);
        }

        @Override
        public void onLost(Network network) {
            indicateNetworkProblem();
            Log.v("TEST", "CHANGES IN NETWORK!! LOST");
            networkInfo = connManger.getActiveNetworkInfo();
            networkProblem = true;
            if(networkInfo != null)
                controller.networkChange(networkInfo);
            super.onLost(network);
        }
    }



}
