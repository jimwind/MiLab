package me.mi.milab.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TryView extends View {
    private Paint mPaintBg;
    private Paint mPaintF;
    private float density;
    public TryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaintBg = new Paint();
        mPaintBg.setColor(0xffff0000);

        density = getContext().getResources().getDisplayMetrics().density;
        mPaintBg.setStrokeWidth(5f * density);

        mPaintF = new Paint();
        mPaintF.setColor(0xff63a309);
    }

    public TryView(Context context) {
        this(context, null);
    }

    private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private Bitmap mMaskBitmap;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("jimwind", "onDraw "+canvas.getWidth()+"/"+ canvas.getHeight());
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mPaintBg);

        int sc = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);

//        mPaintF.setXfermode(mXfermode);
        if(mMaskBitmap == null || mMaskBitmap.isRecycled()){
            mMaskBitmap = getBitmap();
        }
        canvas.drawBitmap(mMaskBitmap, 0f * density, 0f * density, mPaintF);

        mPaintF.setXfermode(null);
//        canvas.restoreToCount(sc);
    }
    public Bitmap getBitmap(){
        int w = getWidth();
        int h = getHeight();
        Log.e("jimwind", "getBitmap "+w+"/"+h);
        Bitmap bitmap = Bitmap.createBitmap((int)(30f*density), (int)(30f*density),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(5);
        paint.setColor(0xff55b651);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0, 0, 20f*density, 20f*density, paint);
        return bitmap;
    }
}
