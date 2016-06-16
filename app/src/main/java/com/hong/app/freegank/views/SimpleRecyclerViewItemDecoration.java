package com.hong.app.freegank.views;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hong.app.freegank.utils.UnitUtil;

/**
 * Created by Freewheel on 2016/5/8.
 */
public class SimpleRecyclerViewItemDecoration extends RecyclerView.ItemDecoration {

    private static final float DEFAULT_STROKE_WIDTH = 2.0f;
    private static final int DEFAULT_PADDING_LEFT = 10;
    private static final int DEFAULT_PADDING_RIGHT = 10;
    Paint paint;
    float paintStrokeWidth = DEFAULT_STROKE_WIDTH;
    private int paddingLeft = DEFAULT_PADDING_LEFT;
    private int paddingRight = DEFAULT_PADDING_RIGHT;

    public SimpleRecyclerViewItemDecoration() {
        super();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(paintStrokeWidth);
        paint.setColor(Color.parseColor("#D2D2D2"));
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft()+ UnitUtil.dp2px(paddingLeft);
        int right = parent.getWidth() - parent.getPaddingRight()-UnitUtil.dp2px(paddingRight);

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            c.drawLine(left,top,right,top,paint);
        }
    }
}
