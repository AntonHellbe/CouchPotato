package se.mah.couchpotato.activtysettings;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import java.util.Locale;
import se.mah.couchpotato.R;

/**
 * @author Jonatan Fridsten
 *
 * This activity will be in charge for settings in the application.
 */
public class ActivitySettings extends AppCompatActivity {

    //Public tag for bundle
    public static final String SETTINGS_BUNDLE_NAME = "data_settings";

    //Private tags for information stored in the activity
    private static final String SAVE_SETTEINGS = "save_settings";
    private static final String SAVE_PREV_STATE = "save_state";
    private static final String SAVE_X_VALUE = "save_x";
    private static final String SAVE_Y_VALUE = "save_y";
    private static final String SAVE_RADIUS_VALUE = "save_radius";

    private CheckBox checkBoxNsfw;
    private Spinner spinnerCountry;
    private RadioGroup radioGroup;
    private RadioGroup.OnCheckedChangeListener checkedChangeListener;
    private ArrayAdapter<CharSequence> adapterCountry;
    private int x, y, startRadius, oldLanguage;
    private ScrollView sv_settings;
    private Settings settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        animateView(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        settings = new Settings();

        if (bundle != null) {
            try {
                settings = bundle.getParcelable(SETTINGS_BUNDLE_NAME);
                if (settings.getLanguage().equals(getResources().getString(R.string.settings_language_english)))
                    oldLanguage = R.id.radio_england;
                if (settings.getLanguage().equals(getResources().getString(R.string.settings_language_swedish)))
                    oldLanguage = R.id.radio_sweden;
            } catch (NullPointerException e) {
            }
        } else {
        }
        initComp();
    }

    @Override
    protected void onResume() {
        super.onResume();
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);
        sv_settings = (ScrollView) findViewById(R.id.sv_settings);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            settings = savedInstanceState.getParcelable(SAVE_SETTEINGS);
            oldLanguage = savedInstanceState.getInt(SAVE_PREV_STATE);
            x = savedInstanceState.getInt(SAVE_X_VALUE);
            y = savedInstanceState.getInt(SAVE_Y_VALUE);
            startRadius = savedInstanceState.getInt(SAVE_RADIUS_VALUE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (settings != null) {
            outState.putParcelable(SAVE_SETTEINGS, settings);
        }
        outState.putInt(SAVE_PREV_STATE, oldLanguage);
        outState.putInt(SAVE_X_VALUE,x);
        outState.putInt(SAVE_Y_VALUE,y);
        outState.putInt(SAVE_RADIUS_VALUE,startRadius);
    }

    private void initComp() {
        checkBoxNsfw = (CheckBox) findViewById(R.id.checkBox_settings_nsfw);
        spinnerCountry = (Spinner) findViewById(R.id.spinner_settings_countries);
        radioGroup = (RadioGroup) findViewById(R.id.radio_language);


        checkBoxNsfw.setChecked(settings.isNsfw());

        adapterCountry = ArrayAdapter.createFromResource(this, R.array.settings_array_country, R.layout.spinner_settings);
        spinnerCountry.setAdapter(adapterCountry);
        spinnerCountry.getBackground().setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP);
        spinnerCountry.setSelection(settings.getPosition_count());

        checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                changeLanguage(radioGroup.getCheckedRadioButtonId());
            }
        };

        if (settings.getLanguage().equals(getResources().getString(R.string.settings_language_english))) {
            radioGroup.check(R.id.radio_england);
        } else {
            radioGroup.check(R.id.radio_sweden);
        }


    }

    private void changeLanguage(int position) {
        if (position != oldLanguage) {

            if (position == R.id.radio_sweden) {

                oldLanguage = R.id.radio_sweden;
                Locale locale = new Locale("sv");
                Configuration configuration = new Configuration();
                configuration.locale = locale;
                getApplicationContext().getResources().updateConfiguration(configuration, null);
                recreate();
            } else if (position == R.id.radio_england) {

                oldLanguage = R.id.radio_england;
                Locale locale = new Locale("en");
                Configuration configuration = new Configuration();
                configuration.locale = locale;
                getApplicationContext().getResources().updateConfiguration(configuration, null);
                recreate();
            }
        }
    }


    private void animateView(Bundle savedInstanceState) {

        if (savedInstanceState == null){

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
            if (radioGroup.getCheckedRadioButtonId() == R.id.radio_sweden) {
                settings.setLanguage(getResources().getString(R.string.settings_language_swedish));
            } else {
                settings.setLanguage(getResources().getString(R.string.settings_language_english));
            }
        } catch (NullPointerException e) {
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


    @Override
    public void finish() {
        try{
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
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void actualFinish() {
        setResult(Activity.RESULT_OK, createIntent());
        super.finish();
    }


}
