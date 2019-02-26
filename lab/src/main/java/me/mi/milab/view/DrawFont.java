package me.mi.milab.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 M = moveto
 L = lineto
 H = horizontal lineto
 V = vertical lineto
 C = curveto
 S = smooth curveto
 Q = quadratic Belzier curve
 T = smooth quadratic Belzier curveto
 A = elliptical Arc
 Z = closepath
 */
public class DrawFont extends View {
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
    private static final int LINEWIDTH = 5;
    private static final int POINTWIDTH = 10;

    private Context mContext;

    Paint mPaint;
    Path mPath;
    /**
     * 即将要穿越的点集合
     */
    private List<Point> mPoints = new ArrayList<>();
    /**
     * 中点集合
     */
    private List<Point> mMidPoints = new ArrayList<>();
    /**
     * 中点的中点集合
     */
    private List<Point> mMidMidPoints = new ArrayList<>();
    /**
     * 移动后的点集合(控制点)
     */
    private List<Point> mControlPoints = new ArrayList<>();

    ArrayList<FontData> fontDatas = new ArrayList<>();

    private int mScreenWidth;
    private int mScreenHeight;

    public DrawFont(Context context) {
        this(context, null);
    }

    public DrawFont(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawFont(Context context, AttributeSet attrs, int defStyleAttr) {
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

        mContext = context;
        getScreenParams();
        initPoints();
//        initMidPoints(this.mPoints);
//        initMidMidPoints(this.mMidPoints);
//        initControlPoints(this.mPoints, this.mMidPoints, this.mMidMidPoints);

    }
    class FontData{
        String type;
        Point start;
        Point control;
        Point end;
    }
    private void getScreenParams() {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        mScreenWidth = display.getWidth();
        mScreenHeight = display.getHeight();
    }

    private void initPoints() {
//        String x = "M 334 759 Q 328 760 323 760 Q 310 763 307 757 Q 300 750 310 734 Q 341 679 361 585 Q 365 557 383 539 Q 402 517 407 534 Q 408 541 410 551 L 407 587 Q 403 602 382 723 L 334 759 Z";
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

    private void initMidPoints(List<Point> points) {
        for (int i = 0; i < points.size(); i++) {
            Point midPoint = null;
            if (i == points.size() - 1) {
                return;
            } else {
                midPoint = new Point((points.get(i).x + points.get(i + 1).x) / 2, (points.get(i).y + points.get(i + 1).y) / 2);
            }
            Log.e("jimwind", "midPoint("+midPoint.x+","+midPoint.y+")");
            mMidPoints.add(midPoint);
        }
    }

    /**
     * 初始化中点的中点集合
     */
    private void initMidMidPoints(List<Point> midPoints) {
        for (int i = 0; i < midPoints.size(); i++) {
            Point midMidPoint = null;
            if (i == midPoints.size() - 1) {
                return;
            } else {
                midMidPoint = new Point((midPoints.get(i).x + midPoints.get(i + 1).x) / 2, (midPoints.get(i).y + midPoints.get(i + 1).y) / 2);
            }
            Log.e("jimwind", "midMidPoint("+midMidPoint.x+","+midMidPoint.y+")");
            mMidMidPoints.add(midMidPoint);
        }
    }

    private void initControlPoints(List<Point> points, List<Point> midPoints, List<Point> midMidPoints) {
        for (int i = 0; i < points.size(); i++) {
            if (i == 0 || i == points.size() - 1) {
                continue;
            } else {
                Point before = new Point();
                Point after = new Point();
                before.x = points.get(i).x - midMidPoints.get(i - 1).x + midPoints.get(i - 1).x;
                before.y = points.get(i).y - midMidPoints.get(i - 1).y + midPoints.get(i - 1).y;
                after.x = points.get(i).x - midMidPoints.get(i - 1).x + midPoints.get(i).x;
                after.y = points.get(i).y - midMidPoints.get(i - 1).y + midPoints.get(i).y;
                mControlPoints.add(before);
                mControlPoints.add(after);
            }
        }
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // ***********************************************************
        // ************* 贝塞尔进阶--曲滑穿越已知点 **********************
        // ***********************************************************

//        // 画原始点
//        drawPoints(canvas);
//        // 画穿越原始点的折线
//        drawCrossPointsBrokenLine(canvas);
//        // 画中间点
//        drawMidPoints(canvas);
//        // 画中间点的中间点
//        drawMidMidPoints(canvas);
//        // 画控制点
//        drawControlPoints(canvas);
//        // 画贝塞尔曲线
        drawBezier(canvas);

    }

    private void drawPoints(Canvas canvas) {
        mPaint.setStrokeWidth(POINTWIDTH);
        for (int i = 0; i < mPoints.size(); i++) {
            canvas.drawPoint(mPoints.get(i).x, mPoints.get(i).y, mPaint);
        }
    }

    /**
     * 画穿越原始点的折线
     */
    private void drawCrossPointsBrokenLine(Canvas canvas) {
        mPaint.setStrokeWidth(LINEWIDTH);
        mPaint.setColor(Color.RED);
        // 重置路径
        mPath.reset();
        // 画穿越原始点的折线
        mPath.moveTo(mPoints.get(0).x, mPoints.get(0).y);
        for (int i = 0; i < mPoints.size(); i++) {
            mPath.lineTo(mPoints.get(i).x, mPoints.get(i).y);
        }
        canvas.drawPath(mPath, mPaint);
    }

    /**
     * 画中间点
     */
    private void drawMidPoints(Canvas canvas) {
        mPaint.setStrokeWidth(POINTWIDTH);
        mPaint.setColor(Color.BLUE);
        for (int i = 0; i < mMidPoints.size(); i++) {
            canvas.drawPoint(mMidPoints.get(i).x, mMidPoints.get(i).y, mPaint);
        }
    }

    /**
     * 画中间点的中间点
     */
    private void drawMidMidPoints(Canvas canvas) {
        mPaint.setColor(Color.YELLOW);
        for (int i = 0; i < mMidMidPoints.size(); i++) {
            canvas.drawPoint(mMidMidPoints.get(i).x, mMidMidPoints.get(i).y, mPaint);
        }

    }

    /**
     * 画控制点
     */
    private void drawControlPoints(Canvas canvas) {
        mPaint.setColor(Color.GRAY);
        // 画控制点
        for (int i = 0; i < mControlPoints.size(); i++) {
            canvas.drawPoint(mControlPoints.get(i).x, mControlPoints.get(i).y, mPaint);
        }
    }

    private void drawBezier(Canvas canvas) {
        mPaint.setStrokeWidth(LINEWIDTH);
        mPaint.setColor(Color.BLACK);
        // 重置路径
        mPath.reset();

        for(int i=0; i<fontDatas.size(); i++){
            if("M".equals(fontDatas.get(i).type)){
                mPath.moveTo(fontDatas.get(i).start.x, fontDatas.get(i).start.y);
            } else if("Q".equals(fontDatas.get(i).type)){
                mPath.quadTo(fontDatas.get(i).control.x, fontDatas.get(i).control.y,
                        fontDatas.get(i).end.x, fontDatas.get(i).end.y);
            } else if("L".equals(fontDatas.get(i).type)){
                mPath.lineTo(fontDatas.get(i).end.x, fontDatas.get(i).end.y);
            } else if("Z".equals(fontDatas.get(i).type)){
                mPath.close();
            }

        }
        canvas.drawPath(mPath, mPaint);
    }
}
