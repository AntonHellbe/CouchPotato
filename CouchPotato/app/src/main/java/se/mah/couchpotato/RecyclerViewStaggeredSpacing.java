package se.mah.couchpotato;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by Gustaf Bohlin on 19/10/2017.
 */

public class RecyclerViewStaggeredSpacing extends RecyclerView.ItemDecoration {

    private int spacing;
    private int columns;

    public RecyclerViewStaggeredSpacing(int spacing, int columns) {
        this.spacing = spacing;
        this.columns = columns;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        outRect.bottom = spacing;
        outRect.top = 0;
        if (spanIndex == 0) {
            outRect.left = spacing;
            outRect.right = spacing / 2;
        } else if (spanIndex == columns - 1) {
            outRect.left = spacing / 2;
            outRect.right = spacing;
        } else {
            outRect.left = spacing / 2;
            outRect.right = spacing / 2;
        }
    }
}
