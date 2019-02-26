package me.mi.milab.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import me.mi.milab.R;

/**
 * Created by mi.gao on 2017/7/21.
 * 目标: 直观了解Shader类
 * http://blog.csdn.net/u012422440/article/details/52328883
 */

public class MyView extends View {
    public MyView(Context context) {
        super(context);
    }
    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.photo);
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(bitmapShader);
        canvas.drawColor(Color.GRAY);
//        canvas.drawRect(0,0,800,800,paint);
        canvas.drawCircle(bitmap.getWidth(), bitmap.getHeight(), bitmap.getWidth() / 2, paint);
    }
}
