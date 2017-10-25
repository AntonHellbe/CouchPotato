package se.mah.couchpotato.activtysettings;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.Spinner;

import se.mah.couchpotato.R;

public class ActivitySettings extends AppCompatActivity {

    public static final String SETTINGS_BUNDLE_NAME = "data_settings";

    private CheckBox checkBoxNsfw;
    private Spinner spinnerCountry;
    private Spinner spinnerLanguage;
    private ArrayAdapter<CharSequence> adapterCountry;
    private ArrayAdapter<CharSequence> adapterLanguage;
    private int x, y, startRadius;
    private ScrollView sv_settings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        animateView(savedInstanceState);
        initComp();

        Bundle bundle = getIntent().getExtras();
        Settings settings;
        if (bundle != null) {
            settings = bundle.getParcelable(SETTINGS_BUNDLE_NAME);
            if (settings == null) {
                settings = new Settings();
            }
            setStartScreen(settings);
        }

    }


    private void initComp() {
        checkBoxNsfw = (CheckBox) findViewById(R.id.checkBox_settings_nsfw);
        spinnerCountry = (Spinner) findViewById(R.id.spinner_settings_countries);
        spinnerLanguage = (Spinner) findViewById(R.id.spinner_settings_language);

        adapterCountry = ArrayAdapter.createFromResource(this, R.array.settings_array_country, R.layout.spinner_settings);
        spinnerCountry.setAdapter(adapterCountry);
        spinnerCountry.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);

        adapterLanguage = ArrayAdapter.createFromResource(this, R.array.settings_array_language, R.layout.spinner_settings);
        spinnerLanguage.setAdapter(adapterLanguage);
        spinnerLanguage.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);

    }


    private void animateView(Bundle savedInstanceState) {

        x = getIntent().getIntExtra("revealX", 0);
        y = getIntent().getIntExtra("revealY", 0);
        startRadius = getIntent().getIntExtra("startRadius", 0);
        sv_settings = (ScrollView) findViewById(R.id.sv_settings);
        sv_settings.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                Point p = new Point();
                getWindowManager().getDefaultDisplay().getSize(p);
                Animator anim = ViewAnimationUtils.createCircularReveal(sv_settings, x, y, startRadius, Math.max(p.x, p.y));
                anim.setDuration(200);
                anim.setInterpolator(new LinearInterpolator());
                anim.start();
            }

            @Override
            public void onViewDetachedFromWindow(View view) {

            }
        });
    }


    /**
     * '
     * Saves settings information and returns it through a bundle inside an intent
     *
     * @return intent with settings data
     */
    private Intent createIntent() {

        //creates a new settings object
        Settings settings = new Settings();
        try {
            settings.setNsfw(checkBoxNsfw.isChecked());
            settings.setCountry(spinnerCountry.getSelectedItem().toString());
            settings.setPosition_count(spinnerCountry.getSelectedItemPosition());
            settings.setLanguage(spinnerLanguage.getSelectedItem().toString());
            settings.setPosition_lang(spinnerLanguage.getSelectedItemPosition());
        }catch (NullPointerException e){
            //TODO Tiast here or in main Activity ?
        }

        //Bundle to store the settings data
        Bundle bundle = new Bundle();
        bundle.putParcelable(SETTINGS_BUNDLE_NAME, settings);

        //Put the bundle in the intent
        Intent intent = new Intent();
        intent.putExtras(bundle);

        return intent;
    }


    private void setStartScreen(Settings settings) {
        checkBoxNsfw.setChecked(settings.isNsfw());
        spinnerCountry.setSelection(settings.getPosition_count());
        spinnerLanguage.setSelection(settings.getPosition_lang());
    }


    @Override
    public void finish() {
        Point p = new Point();
        getWindowManager().getDefaultDisplay().getSize(p);
        Animator anim = ViewAnimationUtils.createCircularReveal(sv_settings, x, y, Math.max(p.x, p.y), startRadius);
        anim.setDuration(100);
        anim.setInterpolator(new LinearInterpolator());
        anim.start();
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                sv_settings.setVisibility(View.INVISIBLE);
                actualFinish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    public void actualFinish() {
        setResult(Activity.RESULT_OK, createIntent());
        super.finish();
    }


}
