package se.mah.couchpotato.activtysettings;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by Gustaf Bohlin on 25/10/2017.
 */

public class LanguageSpinner extends Spinner {
    public LanguageSpinner(Context context) {
        super(context);
    }

    public LanguageSpinner(Context context, int mode) {
        super(context, mode);
    }

    public LanguageSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LanguageSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LanguageSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        super(context, attrs, defStyleAttr, mode);
    }

    public LanguageSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode) {
        super(context, attrs, defStyleAttr, defStyleRes, mode);
    }

    public LanguageSpinner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, defStyleRes, mode, popupTheme);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        ((ActivitySettings) getContext()).setSpinnerListener();
    }
}
