package se.mah.couchpotato.activtysettings;

import android.animation.Animator;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.Spinner;

import java.lang.reflect.Array;

import se.mah.couchpotato.R;

public class ActivitySettings extends AppCompatActivity {

    private CheckBox checkBoxTheme;
    private CheckBox checkBoxNsfw;
    private Spinner spinnerCountry;
    private Spinner spinnerLanguage;
    private ArrayAdapter<CharSequence> adapterCountry;
    private ArrayAdapter<CharSequence> adapterLanguage;
    private Settings settings;
    private String country,language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        animateView(savedInstanceState);
        initComp();
    }


    private void initComp() {
        checkBoxTheme = (CheckBox) findViewById(R.id.checkBox_settings_theme);
        checkBoxNsfw = (CheckBox) findViewById(R.id.checkBox_settings_nsfw);
        spinnerCountry = (Spinner) findViewById(R.id.spinner_settings_countries);
        spinnerLanguage = (Spinner) findViewById(R.id.spinner_settings_language);

        adapterCountry = ArrayAdapter.createFromResource(this, R.array.settings_array_country, R.layout.spinner_settings);
        spinnerCountry.setAdapter(adapterCountry);
        spinnerCountry.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
//        spinnerCountry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                country = spinnerCountry.getSelectedItem().toString();
//                Log.v("ActivitySettings","Selected country:" + country);
//            }
//        });

        adapterLanguage = ArrayAdapter.createFromResource(this, R.array.settings_array_language, R.layout.spinner_settings);
        spinnerLanguage.setAdapter(adapterLanguage);
        spinnerLanguage.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
//        spinnerLanguage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                language = spinnerLanguage.getSelectedItem().toString();
//                Log.v("ActivitySettings","Selected language:" + language);
//            }
//        });

    }

    private void animateView(Bundle savedInstanceState) {

        final int x = getIntent().getIntExtra("revealX", 0);
        final int y = getIntent().getIntExtra("revealY", 0);
        final int startRadius = getIntent().getIntExtra("startRadius", 0);
        final ScrollView sv = (ScrollView) findViewById(R.id.sv_settings);
        sv.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                Point p = new Point();
                getWindowManager().getDefaultDisplay().getSize(p);
                Animator anim = ViewAnimationUtils.createCircularReveal(findViewById(R.id.sv_settings), x, y, startRadius, Math.max(p.x, p.y));
                anim.setDuration(300);
                anim.setInterpolator(new LinearInterpolator());
                anim.start();
            }

            @Override
            public void onViewDetachedFromWindow(View view) {

            }
        });

    }
}
