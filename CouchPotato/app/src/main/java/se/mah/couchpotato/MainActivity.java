package se.mah.couchpotato;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class MainActivity extends AppCompatActivity implements ActivityInterface {

    private Controller controller;
    private FloatingActionButton fabSettings, fabFilter;
    private BottomNavigationView bottomNavigationView;

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
        controller = new Controller(this);
        initializeComponents();
        initializeListeners();
        controller.fragmentHandling();
    }

    private void initializeComponents() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        fabSettings = (FloatingActionButton) findViewById(R.id.fab);
        fabFilter = (FloatingActionButton) findViewById(R.id.fab_filter);
    }

    private void initializeListeners() {
        Listener listener = new Listener();
        fabSettings.setOnClickListener(listener);
        fabFilter.setOnClickListener(listener);
        bottomNavigationView.setOnNavigationItemSelectedListener(listener);
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

    private class Listener implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public void onClick(View view) {
            if (view == fabSettings)
                controller.fabSettingsClicked(fabSettings.getX(), fabSettings.getY());
            if (view == fabFilter)
                controller.fabFilterClicked();
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return controller.navigationClicked(item);
        }
    }
}
