package se.mah.couchpotato.activitytvshow;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Gustaf Bohlin on 22/10/2017.
 */

public class CustomScrollView extends ScrollView {

    private GestureDetector gestureDetector;

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gestureDetector = new GestureDetector(context, new YScrollDetector());
        setFadingEdgeLength(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) && gestureDetector.onTouchEvent(ev);
    }

    // Return false if we're scrolling in the x direction
    private class YScrollDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return (Math.abs(distanceY) > Math.abs(distanceX));
        }
    }
}
