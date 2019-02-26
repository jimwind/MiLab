package me.mi.milab.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by mi.gao on 2018/3/12.
 *
 * 左右两边半圆的View
 */

public class TwoSemiCircleView extends TextView {
    private Paint paint;
    private Paint paint2;
    private Paint paint3;

    public TwoSemiCircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TwoSemiCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TwoSemiCircleView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        paint2 = new Paint();
        paint2.setXfermode(null);

        paint3 = new Paint();

    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas2 = new Canvas(bitmap);
        LinearGradient lg = new LinearGradient(0,0,getWidth(),getHeight(),0xffffc300, 0xffff9500, Shader.TileMode.CLAMP);
//        LinearGradient lg = new LinearGradient(0,0,getWidth(),getHeight(),0xffffffff, 0xff000000, Shader.TileMode.CLAMP);
        paint3.setShader(lg);
        canvas2.drawRect(0, 0, getWidth(), getHeight(), paint3);
        super.draw(canvas2);


        drawLeftUp(canvas2);
        drawRightUp(canvas2);
        drawLeftDown(canvas2);
        drawRightDown(canvas2);
        canvas.drawBitmap(bitmap, 0, 0, paint2);
        bitmap.recycle();
    }

    /**
     *
     * 0, 0
     * 0, getHeight()/2
     * @param canvas
     */
    private void drawLeftUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, getHeight() / 2);
        path.lineTo(0, 0);
        path.lineTo(getHeight()/2, 0);
        path.arcTo(new RectF(
                        0,
                        0,
                        getHeight(),
                        getHeight()),
                -90,
                -90);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     *
     * 0, getHeight()/2
     * 0, getHeight()       getHeight()/2, getHeight()
     *
     * @param canvas
     */
    private void drawLeftDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, getHeight()/2);
        path.lineTo(0, getHeight());
        path.lineTo(getHeight()/2, getHeight());
        path.arcTo(new RectF(
                        0,
                        0,
                        getHeight(),
                        getHeight()),
                90,
                90);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     *                                                             getWidth(), getHeight()/2
     *              getWidth() - getHeight()/2, getHeight()        getWidth(), getHeight()
     * @param canvas
     */
    private void drawRightDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth()-getHeight()/2, getHeight());
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth(), getHeight()/2);
        path.arcTo(new RectF(
                getWidth()-getHeight(),
                0,
                getWidth(),
                getHeight()), 0, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    /**
     *          getWidth()-getHeight()/2, 0     getWidth(), 0
     *                                          getWidth(), getHeight()/2
     * @param canvas
     */
    private void drawRightUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth(), getHeight()/2);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth()-getHeight()/2, 0);
        path.arcTo(new RectF(
                        getWidth()-getHeight(),
                        0,
                        getWidth(),
                        getHeight()),
                -90,
                90);
        path.close();
        canvas.drawPath(path, paint);
    }

}
