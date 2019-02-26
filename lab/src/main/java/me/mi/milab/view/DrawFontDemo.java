package me.mi.milab.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import me.mi.milab.R;

public class DrawFontDemo extends View {
    //M startPoint Q controlPoint endPoint
    String wu =
            "M 334 759 Q 328 760 323 760 Q 310 763 307 757 Q 300 750 310 734 Q 341 679 361 585 Q 365 557 383 539 Q 402 517 407 534 Q 408 541 410 551 L 407 587 Q 403 602 382 723 L 334 759 Z," +
            "M 660 626 Q 687 705 721 731 Q 739 749 724 767 Q 655 824 599 802 Q 413 742 334 759 L 382 723 Q 607 774 626 756 Q 641 740 608 632 L 660 626 Z," +
            "M 410 551 Q 416 551 423 552 Q 501 571 672 591 Q 682 592 684 602 Q 684 609 660 626 L 608 632 Q 605 633 602 632 Q 494 602 407 587 L 410 551 Z," +
            "M 508 429 Q 581 442 658 455 Q 716 468 726 475 Q 735 482 731 492 Q 724 504 694 513 Q 664 520 578 492 Q 436 458 319 449 Q 282 445 308 426 Q 344 402 420 415 Q 433 418 449 419 L 508 429 Z\"," +
            "M 519 274 Q 633 286 800 283 Q 830 282 855 283 Q 877 282 883 292 Q 889 305 871 320 Q 814 363 772 355 Q 678 337 519 314 L 458 305 Q 320 290 162 272 Q 140 271 156 251 Q 172 235 191 229 Q 215 223 232 228 Q 334 255 451 267 L 519 274 Z," +
            "M 519 314 Q 534 384 539 393 Q 546 400 540 411 Q 534 418 508 429 L 449 419 Q 467 367 458 305 L 451 267 Q 415 144 310 76 Q 265 49 185 15 Q 158 2 190 1 Q 211 -2 252 11 Q 304 23 350 47 Q 393 66 426 105 Q 469 153 504 253 L 519 314 Z," +
            "M 504 253 Q 505 252 510 246 Q 666 18 719 -1 Q 797 -19 906 -7 Q 927 -6 929 -1 Q 932 6 916 14 Q 867 36 808 59 Q 679 116 541 254 Q 528 267 519 274 L 508.90848379915906 272.96116744991343 L 504 253 Z";

    String iii = "[[[316,749],[357,701],[395,539]]," +
            "[[349,755],[354,748],[393,738],[594,782],[641,780],[675,748],[642,654],[616,644]]" +
            ",[[416,558],[426,572],[599,606],[651,608],[675,602]],[[311,439],[350,431],[391,433],[672,487],[719,486]],[[159,261],[214,251],[363,276],[782,320],[833,312],[870,299]],[[457,414],[496,388],[496,370],[479,267],[437,166],[409,127],[369,87],[296,44],[222,15],[196,11]],[[515,266],[527,242],[537,237],[591,172],[658,105],[727,48],[786,27],[923,1]]]";
    //M
    //Q control
    //L
    //Z
    Paint mPaint;
    Path mPath;
    Bitmap mBgBitmap;
    ArrayList<FontData> fontDatas = new ArrayList<>();

    private Bitmap mMaskBitmap;
    Paint userPaint;
    Path userPath;
    public DrawFontDemo(Context context) {
        this(context, null);
    }

    public DrawFontDemo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawFontDemo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context) {
        mPaint = new Paint();
        mPath = new Path();
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 防抖动
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        userPaint = new Paint();
        userPaint.setAntiAlias(true);
        userPaint.setStrokeWidth(50);
        userPaint.setColor(Color.RED);
        userPaint.setStyle(Paint.Style.STROKE);
        userPath = new Path();
        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_new_user_task);
        initPoints();
    }
    class FontData{
        String type;
        Point start;
        Point control;
        Point end;
    }

    private void initPoints() {
//      String x = "M 334 759 Q 328 760 323 760 Q 310 763 307 757 Q 300 750 310 734 Q 341 679 361 585 Q 365 557 383 539 Q 402 517 407 534 Q 408 541 410 551 L 407 587 Q 403 602 382 723 L 334 759 Z";
        String line[] = wu.split(",");
        for(int y = 0; y < line.length; y++) {
            String l = line[y];
            String p[] = l.split(" ");
            for (int i = 0; i < p.length; i++) {
                if ("M".equals(p[i].trim())) {
                    FontData data = new FontData();
                    data.type = "M";
                    data.start = new Point();
                    data.start.x = Float.valueOf(p[i + 1].trim()).intValue();
                    data.start.y = 1024 - Float.valueOf(p[i + 2].trim()).intValue();
                    fontDatas.add(data);
                    i = i + 2;
                } else if ("Q".equals(p[i].trim())) {
                    FontData data = new FontData();
                    data.type = "Q";
                    data.control = new Point();
                    data.control.x = Float.valueOf(p[i + 1].trim()).intValue();
                    data.control.y = 1024 - Float.valueOf(p[i + 2].trim()).intValue();
                    data.end = new Point();
                    data.end.x = Float.valueOf(p[i + 3].trim()).intValue();
                    data.end.y = 1024 - Float.valueOf(p[i + 4].trim()).intValue();
                    fontDatas.add(data);
                    i = i + 4;
                } else if ("L".equals(p[i].trim())) {
                    FontData data = new FontData();
                    data.type = "L";
                    data.end = new Point();
                    data.end.x = Float.valueOf(p[i + 1].trim()).intValue();
                    data.end.y = 1024 - Float.valueOf(p[i + 2].trim()).intValue();
                    fontDatas.add(data);
                    i = i + 2;
                } else if ("Z".equals(p[i].trim())) {
                    FontData data = new FontData();
                    data.type = "Z";
                }
            }
        }

    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawARGB(255, 255, 156, 151);
        canvas.drawBitmap(mBgBitmap, 0, 0, mPaint);
        drawBezier(canvas);
        //https://blog.csdn.net/u013085697/article/details/52096703
        int sc = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        if(userPath != null){
            canvas.drawPath(userPath, userPaint);
        }
        if(mMaskBitmap == null || mMaskBitmap.isRecycled()){
            mMaskBitmap = getBitmap();
        }
        mPaint.reset();
        mPaint.setXfermode(mXfermode);
        //绘制形状
        canvas.drawBitmap(mMaskBitmap, 0, 0, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);

    }
    //下层是DST 用户画字
    //上层是SRC 区域遮罩
    //IN 交集
    private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    /**
     * 返回的是一个实心吴字，与其它画布相交，即为实心吴字
     * @return
     */
    public Bitmap getBitmap(){
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        for(int i=0; i<fontDatas.size(); i++){
            if("M".equals(fontDatas.get(i).type)){
                path.moveTo(fontDatas.get(i).start.x, fontDatas.get(i).start.y);
            } else if("Q".equals(fontDatas.get(i).type)){
                path.quadTo(fontDatas.get(i).control.x, fontDatas.get(i).control.y,
                        fontDatas.get(i).end.x, fontDatas.get(i).end.y);
            } else if("L".equals(fontDatas.get(i).type)){
                path.lineTo(fontDatas.get(i).end.x, fontDatas.get(i).end.y);
            } else if("Z".equals(fontDatas.get(i).type)){
                path.close();
            }

        }
        canvas.drawPath(path, paint);
        return bitmap;
    }
    private void drawBezier(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(1);
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        for(int i=0; i<fontDatas.size(); i++){
            if("M".equals(fontDatas.get(i).type)){
                path.moveTo(fontDatas.get(i).start.x, fontDatas.get(i).start.y);
            } else if("Q".equals(fontDatas.get(i).type)){
                path.quadTo(fontDatas.get(i).control.x, fontDatas.get(i).control.y,
                        fontDatas.get(i).end.x, fontDatas.get(i).end.y);
            } else if("L".equals(fontDatas.get(i).type)){
                path.lineTo(fontDatas.get(i).end.x, fontDatas.get(i).end.y);
            } else if("Z".equals(fontDatas.get(i).type)){
                path.close();
            }

        }
        canvas.drawPath(path, paint);
    }

    private float downX,downY;
    private float tempX,tempY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                userPath.moveTo(downX,downY);
                invalidate();
                tempX = downX;
                tempY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                userPath.quadTo(tempX,tempY,moveX,moveY);
                invalidate();
                tempX = moveX;
                tempY = moveY;
                break;
        }

        return true;
    }
}
