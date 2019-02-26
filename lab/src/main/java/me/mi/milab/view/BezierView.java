package me.mi.milab.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * https://www.2cto.com/kf/201604/497130.html
 */
public class BezierView extends View {
    private final String TAG = "bezier";
    private final int POINTWIDTH = 3;
    private Path mPath;
    private Point startPoint;
    private Point endPoint;
    private Paint mPaint;
    // 辅助点
    private Point assistPoint;
    public BezierView(Context context) {
        this(context, null);
    }

    public BezierView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BezierView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPath = new Path();
        startPoint = new Point(300, 600);
        endPoint = new Point(900, 600);
        assistPoint = new Point(600, 900);
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 防抖动
        mPaint.setDither(true);
    }
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                assistPoint.x = (int) event.getX();
                assistPoint.y = (int) event.getY();
                Log.i(TAG, "assistPoint.x = " + assistPoint.x);
                Log.i(TAG, "assistPoint.Y = " + assistPoint.y);
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLACK);
        // 笔宽
        mPaint.setStrokeWidth(POINTWIDTH);
        // 空心
        mPaint.setStyle(Paint.Style.STROKE);
        // 重置路径
        mPath.reset();
        // 起点 (不调用moveTo, 默认为(0, 0))
        mPath.moveTo(startPoint.x, startPoint.y);
        // 重要的就是这句
        mPath.quadTo(assistPoint.x, assistPoint.y, endPoint.x, endPoint.y);
        // 画路径
        canvas.drawPath(mPath, mPaint);
        // 画辅助点
//        canvas.drawPoint(assistPoint.x, assistPoint.y, mPaint);
    }
}
