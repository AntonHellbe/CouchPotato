package se.mah.couchpotato.activitytvshow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import se.mah.couchpotato.R;

/**
 * Created by Gustaf Bohlin on 27/10/2017.
 */

public class AirTableView extends View {

    private int background, textcolor, highlightedcolor;
    private Paint textPaint, backgroundPaint, highlightedPaint;
    private float textSize;
    private String days[] = {"M", "T", "W", "T", "F", "S", "S"};
    private boolean daysToDraw[] = {false, false, false, false, false, false, false};

    public AirTableView(Context context) {
        super(context);
        init();
    }

    public AirTableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray attrsArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AirTableView, 0, 0);
        try {
            background = attrsArray.getColor(R.styleable.AirTableView_backgroundColor, Color.BLACK);
            textcolor = attrsArray.getInteger(R.styleable.AirTableView_textcolor, Color.WHITE);
            highlightedcolor = attrsArray.getColor(R.styleable.AirTableView_highlightedcolor, context.getResources().getColor(R.color.colorPrimary, null));
            textSize = attrsArray.getDimension(R.styleable.AirTableView_textSize, 20);
        } finally {
            attrsArray.recycle();
        }
        init();
    }

    public AirTableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AirTableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public int getBackgroundColor() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
        invalidate();
        requestLayout();
    }

    public int getTextcolor() {
        return textcolor;
    }

    public void setTextcolor(int textcolor) {
        this.textcolor = textcolor;
        invalidate();
        requestLayout();
    }

    public int getHighlightedcolor() {
        return highlightedcolor;
    }

    public void setHighlightedcolor(int highlightedcolor) {
        this.highlightedcolor = highlightedcolor;
        invalidate();
        requestLayout();
    }

    private void init() {
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(textcolor);
        if (textSize == 0) {
            textSize = textPaint.getTextSize();
        }

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setStyle(Paint.Style.FILL);

        highlightedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlightedPaint.setStyle(Paint.Style.FILL);
    }

    public void daysToDraw(boolean days[]) {
        daysToDraw = days;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);
//        int minh = MeasureSpec.getSize(w) - (int)textSize + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState((int)textSize + getPaddingTop() + getPaddingBottom(), heightMeasureSpec, 0);

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        backgroundPaint.setColor(background);
        Log.v("AirTableViewOnDraw", "Width: " + getWidth() + " Height: " + getHeight());
        Log.v("AirTableViewOnDraw", "Left: " + getLeft() + " Top: " + getTop() + " Bottom: " + getBottom() + " Right: " + getRight());
        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);
        textPaint.setColor(textcolor);
        textPaint.setTextSize(textSize);
        highlightedPaint.setColor(highlightedcolor);
        int width = getWidth();
        int widthperday = width / 7;
        for(int i = 0; i < days.length; i++) {
            if (daysToDraw[i])
                canvas.drawCircle(((widthperday / 2) + (widthperday * i)), getMeasuredHeight() / 2, getHeight() / 2, highlightedPaint);
            canvas.drawText(days[i], ((widthperday / 2) + (widthperday * i)) - (textPaint.measureText(days[i]) / 2), (getMeasuredHeight() / 2) + getPaddingTop() + 5, textPaint); //TODO fix hardcoded 5
        }
//        canvas.drawCircle(mPointerX, mPointerY, mPointerSize, mTextPaint);
    }
}
