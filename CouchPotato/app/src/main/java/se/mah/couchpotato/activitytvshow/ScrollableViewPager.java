package se.mah.couchpotato.activitytvshow;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Gustaf Bohlin on 22/10/2017.
 */

public class ScrollableViewPager extends ViewPager {

    private int hardCodedHeight = 600;

    public ScrollableViewPager(Context context) {
        super(context);
    }

    public ScrollableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.v("ScrollableViewPager", "HEIGHT = " + hardCodedHeight);
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(hardCodedHeight, MeasureSpec.EXACTLY));
    }

    public void setHardCodedHeight(int height) {
        this.hardCodedHeight = height;
    }

    public void measureCurrentView(View currentView) {
        requestLayout();
    }

}
