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
    private int defaultHeight = 600;

    public ScrollableViewPager(Context context) {
        super(context);
    }

    public ScrollableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Math.max(hardCodedHeight, defaultHeight), MeasureSpec.EXACTLY));
    }

    public void setHardCodedHeight(int height) {
        this.hardCodedHeight = height;
    }

    public void measureCurrentView(View currentView) {
        requestLayout();
    }

}
