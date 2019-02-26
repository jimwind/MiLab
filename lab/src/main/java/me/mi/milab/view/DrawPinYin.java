package me.mi.milab.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import me.mi.milab.R;


public class DrawPinYin extends View {
    /*
    指令 参数           描述
    M   x y         起始点坐标x y （Move to）
    L   x y         从当前点的坐标画直线到指定点的 x y坐标 （Line to）
    H   x           从当前点的坐标画水平直线到指定的x轴坐标 （Horizontal line to）
    V   y           从当前点的座标画垂直直线到指定的y轴坐标 （Vertical line to）
    C   x1 y1 x2 y2 x y 从当前点的坐标画条贝塞风线到指定点的x, y坐标，其中 x1 y1及x2, y2为控制点 （Curve）
    S   x2 y2 x y       从当前点的坐标画条反射的贝塞曲线到指定点的x, y坐标，其中x2, y2为反射的控制点（Smooth curve）
    Q   x1 y1 x y       从当前点的坐标画条反射二次贝塞曲线到指定点的x, y坐标，其中x1 y1为控制点（Quadratic Bézier curve）
    T   x y             从当前点的坐标画条反射二次贝塞曲线到指定点的x, y坐标，以前一个坐标为反射控制点（Smooth Quadratic Bézier curve）
    A   rx ry x-axis-rotation large-arc-flag sweep-flag x y
         从当前点的坐标画个椭圆形到指定点的x, y坐标，其中rx, ry为椭圆形的x轴及y轴的半径，
         x-axis-rotation是弧线与x轴的旋转角度，
         large-arc-flag则设定1最大角度的弧线或是0最小角度的弧线，
         sweep-flag设定方向为1顺时针方向或0逆时针方向（Arc）
    Z   关闭路径，将当前点坐标与第一个点的坐标连接起来（Closepath）著作权归作者所有。

    以上所有命令均允许小写字母。大写表示绝对定位，小写表示相对定位。
*/
    //为了相对值时取前一个点
    //M start.x start.y
    //L end.x end.y
    //H -> L end.x end.y
    //V -> L end.x end.y
    //C
    //S
    //Q end.x end.y
    //T -> Q end.x end.y
    //A
    //Z

    //点前面不是数字加0
    //负号前面是数字加逗号
    String pinyin_a =
            "M 592 590.33" +
                    " q -26.21 32.85 -56.09 52.22" +
                    " T 451.4 661.92" +
                    " q -69 -0.74 -116.62 -42.81" +
                    " q -26.2 -22.87 -44.1 -62.55" +
                    " t -17.9 -95.4" +
                    " a 317.08 317.08 0 0 1 7.2 -67" +
                    " q 7.2 -33.39 34.13 -67.72" +
                    " q 26.2 -34.68 63.66 -50.74" +
                    " t 73.63 -16.05" +
                    " q 81.55 0 137.28 66.79" +
                    " m -132.48 -10" +
                    " q -53.52 0 -86.73 42.81" +
                    " T 336.26 461.16" +
                    " q 0 59.43 31.73 101.31" +
                    " t 88.21 41.88" +
                    " q 55.71 0.75 86.54 -39.85" +
                    " t 30.81 -103.34" +
                    " q 0 -63.11 -31.74 -103.88" +
                    " T 456.2 316.5" +
                    " Z" +
                    " M 582.51 326.46" +
                    " l -0.36 -52.4" +
                    " h 51.29" +
                    " q 0 67.53 0.19 135.81" +
                    " t 0.18 135.44" +
                    " q 0 36.16 16.24 50.37" +
                    " a 73.33 73.33 0 0 0 38 17.53" +
                    " a 243.22 243.22 0 0 1 4.61 24.17" +
                    " q 1.67 12 4.25 24.54" +
                    " q -39.49 1.11 -69.75 -13.65" +
                    " t -41.33 -57.94";

    String aaa = "560.5 336.5 517.67 298.69 454.5 286.5 385.5 296.5 336.5 332.5 314.5 377.5 301.5 436.5 307.5 511.5 321.5 558.5 363.5 608.5 432.5 631.5 480.5 632.5 533.5 612.5 553.5 593.5 573.5 572.5&608.5 296.5 608.5 333.5 608.5 396.5 608.5 482.5 613.5 558.5 623.5 613.5 666.5 639.";


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

    public DrawPinYin(Context context) {
        this(context, null);
    }

    public DrawPinYin(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawPinYin(Context context, AttributeSet attrs, int defStyleAttr) {
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
        initPinYinPoints();
//        initPoints();
    }

    class FontData {
        String trueType;
        String type;
        CPoint start;
        CPoint control;
        CPoint control2;
        CPoint end;
    }
    class CPoint {
        double x;
        double y;
    }

    private CPoint lastPoint() {
        FontData lastData = fontDatas.get(fontDatas.size() - 1);
        CPoint point = new CPoint();
        if ("M".equals(lastData.type)) {
            point.x = lastData.start.x;
            point.y = lastData.start.y;
        } else if ("L".equals(lastData.type)
                || "Q".equals(lastData.type)
                || "A".equals(lastData.type)) {
            point.x = lastData.end.x;
            point.y = lastData.end.y;
        }
        return point;
    }
    /*
        指令 参数           描述
        M   x y         起始点坐标x y （Move to）
        L   x y         从当前点的坐标画直线到指定点的 x y坐标 （Line to）
        H   x           从当前点的坐标画水平直线到指定的x轴坐标 （Horizontal line to）
        V   y           从当前点的座标画垂直直线到指定的y轴坐标 （Vertical line to）
        C   x1 y1 x2 y2 x y 从当前点的坐标画条贝塞风线到指定点的x, y坐标，其中 x1 y1及x2, y2为控制点 （Curve）
        S   x2 y2 x y       从当前点的坐标画条反射的贝塞曲线到指定点的x, y坐标，其中x2, y2为反射的控制点（Smooth curve）
        Q   x1 y1 x y       从当前点的坐标画条反射二次贝塞曲线到指定点的x, y坐标，其中x1 y1为控制点（Quadratic Bézier curve）
        T   x y             从当前点的坐标画条反射二次贝塞曲线到指定点的x, y坐标，以前一个坐标为反射控制点（Smooth Quadratic Bézier curve）
        A   rx ry x-axis-rotation large-arc-flag sweep-flag x y     从当前点的坐标画个椭圆形到指定点的x, y坐标，其中rx, ry为椭圆形的x轴及y轴的半径，x-axis-rotation是弧线与x轴的旋转角度，large-arc-flag则设定1最大角度的弧线或是0最小角度的弧线，sweep-flag设定方向为1顺时针方向或0逆时针方向（Arc）
        Z       关闭路径，将当前点坐标与第一个点的坐标连接起来（Closepath）著作权归作者所有。

        以上所有命令均允许小写字母。大写表示绝对定位，小写表示相对定位。
    */
    //为了相对值时取前一个点
    //M start.x start.y
    //L end.x end.y
    //H -> L end.x end.y
    //V -> L end.x end.y
    //C
    //S
    //Q end.x end.y
    //T -> Q end.x end.y
    //A
    //Z

    private void initPinYinPoints() {
        String line[] = pinyin_a.split(",");
        for (int y = 0; y < line.length; y++) {
            String l = line[y];
            String p[] = l.split(" ");
            for (int i = 0; i < p.length; i++) {
                if ("M".equals(p[i].trim())) {
                    FontData data = new FontData();
                    data.type = "M";
                    data.start = new CPoint();
                    data.start.x = Double.valueOf(p[i + 1].trim());
                    data.start.y = Double.valueOf(p[i + 2].trim());
                    fontDatas.add(data);
                    i = i + 2;
                } else if ("m".equals(p[i].trim())) {
                    CPoint point = lastPoint();
                    double lx = point.x;
                    double ly = point.y;
                    FontData data = new FontData();
                    data.trueType = "m";
                    data.type = "M";
                    data.start = new CPoint();
                    data.start.x = lx + Double.valueOf(p[i + 1].trim());
                    data.start.y = ly + Double.valueOf(p[i + 2].trim());
                    fontDatas.add(data);
                    i = i + 2;
                } else if ("Q".equals(p[i].trim())) {
                    FontData data = new FontData();
                    data.type = "Q";
                    data.control = new CPoint();
                    data.control.x = Double.valueOf(p[i + 1].trim());
                    data.control.y = Double.valueOf(p[i + 2].trim());
                    data.end = new CPoint();
                    data.end.x = Double.valueOf(p[i + 3].trim());
                    data.end.y = Double.valueOf(p[i + 4].trim());
                    fontDatas.add(data);
                    i = i + 4;
                } else if ("q".equals(p[i].trim())) {
                    CPoint point = lastPoint();
                    double lx = point.x;
                    double ly = point.y;
                    FontData data = new FontData();
                    data.trueType = "q";
                    data.type = "Q";
                    data.control = new CPoint();
                    data.control.x = lx + Double.valueOf(p[i + 1].trim());
                    data.control.y = ly + Double.valueOf(p[i + 2].trim());
                    data.end = new CPoint();
                    data.end.x = lx + Double.valueOf(p[i + 3].trim());
                    data.end.y = ly + Double.valueOf(p[i + 4].trim());
                    fontDatas.add(data);
                    i = i + 4;
                } else if ("T".equals(p[i].trim())) {
                    CPoint point = lastPoint();
                    double lx = point.x;
                    double ly = point.y;

                    FontData data = new FontData();
                    data.trueType = "T";
                    data.type = "Q";
                    data.control = new CPoint();
                    data.control.x = lx;
                    data.control.y = ly;
                    data.end = new CPoint();
                    data.end.x = Double.valueOf(p[i + 1].trim());
                    data.end.y = Double.valueOf(p[i + 2].trim());
                    fontDatas.add(data);
                    i = i + 2;
                } else if ("t".equals(p[i].trim())) {
                    CPoint point = lastPoint();
                    double lx = point.x;
                    double ly = point.y;

                    FontData data = new FontData();
                    data.trueType = "t";
                    data.type = "Q";
                    data.control = new CPoint();
                    data.control.x = lx;
                    data.control.y = ly;
                    data.end = new CPoint();
                    data.end.x = lx + Double.valueOf(p[i + 1].trim());
                    data.end.y = ly + Double.valueOf(p[i + 2].trim());
                    fontDatas.add(data);
                    i = i + 2;
                } else if ("L".equals(p[i].trim())) {
                    FontData data = new FontData();
                    data.type = "L";
                    data.end = new CPoint();
                    data.end.x = Double.valueOf(p[i + 1].trim());
                    data.end.y = Double.valueOf(p[i + 2].trim());
                    fontDatas.add(data);
                    i = i + 2;
                } else if ("l".equals(p[i].trim())) {
                    CPoint point = lastPoint();
                    double lx = point.x;
                    double ly = point.y;

                    FontData data = new FontData();
                    data.trueType = "l";
                    data.type = "L";
                    data.end = new CPoint();
                    data.end.x = lx + Double.valueOf(p[i + 1].trim());
                    data.end.y = ly + Double.valueOf(p[i + 2].trim());
                    fontDatas.add(data);
                    i = i + 2;
                } else if ("H".equals(p[i].trim())) {
                    CPoint point = lastPoint();
                    double ly = point.y;

                    FontData data = new FontData();
                    data.trueType = "H";
                    data.type = "L";
                    data.end = new CPoint();
                    data.end.x = Double.valueOf(p[i + 1].trim());
                    data.end.y = ly;
                    fontDatas.add(data);
                    i = i + 1;
                } else if ("h".equals(p[i].trim())) {
                    CPoint point = lastPoint();
                    double lx = point.x;
                    double ly = point.y;

                    FontData data = new FontData();
                    data.trueType = "h";
                    data.type = "L";
                    data.end = new CPoint();
                    data.end.x = lx + Double.valueOf(p[i + 1].trim());
                    data.end.y = ly;
                    fontDatas.add(data);
                    i = i + 1;
                } else if ("V".equals(p[i].trim())) {
                    CPoint point = lastPoint();
                    double lx = point.x;

                    FontData data = new FontData();
                    data.trueType = "V";
                    data.type = "L";
                    data.end = new CPoint();
                    data.end.x = lx;
                    data.end.y = Double.valueOf(p[i + 1].trim());
                    fontDatas.add(data);
                    i = i + 1;
                } else if ("v".equals(p[i].trim())) {
                    CPoint point = lastPoint();
                    double lx = point.x;
                    double ly = point.y;

                    FontData data = new FontData();
                    data.trueType = "v";
                    data.type = "L";
                    data.end = new CPoint();
                    data.end.x = lx;
                    data.end.y = ly + Double.valueOf(p[i + 1].trim());
                    fontDatas.add(data);
                    i = i + 1;
                } else if ("A".equals(p[i].trim())) {
                    CPoint point = lastPoint();
                    double lx = point.x;
                    double ly = point.y;

                    FontData data = new FontData();
                    data.type = "A";
                    data.end = new CPoint();
                    data.end.x = Double.valueOf(p[i + 6].trim());
                    data.end.y = Double.valueOf(p[i + 7].trim());

                    double r = Double.valueOf(p[i + 1].trim());

                    calcCircleCenter(point, data.end, r, p[i + 4].trim(), p[i + 5].trim());

                    fontDatas.add(data);
                    i = i + 7;
                } else if ("a".equals(p[i].trim())) {
                    CPoint point = lastPoint();
                    double lx = point.x;
                    double ly = point.y;

                    FontData data = new FontData();
                    data.trueType = "a";
                    data.type = "A";
                    data.end = new CPoint();
                    data.end.x = lx + Double.valueOf(p[i + 6].trim());
                    data.end.y = ly + Double.valueOf(p[i + 7].trim());
                    fontDatas.add(data);

                    double r = Double.valueOf(p[i + 1].trim());

//                    calcCircleCenter(point, data.end, r);
                    i = i + 7;
                } else if ("Z".equals(p[i].trim())) {
                    FontData data = new FontData();
                    data.type = "Z";
                    fontDatas.add(data);
                }
            }
        }
    }
    /*
        指令 参数           描述
        M   x y         起始点坐标x y （Move to）
        L   x y         从当前点的坐标画直线到指定点的 x y坐标 （Line to）
        H   x           从当前点的坐标画水平直线到指定的x轴坐标 （Horizontal line to）
        V   y           从当前点的座标画垂直直线到指定的y轴坐标 （Vertical line to）
        C   x1 y1 x2 y2 x y 从当前点的坐标画条贝塞风线到指定点的x, y坐标，其中 x1 y1及x2, y2为控制点 （Curve）
        S   x2 y2 x y       从当前点的坐标画条反射的贝塞曲线到指定点的x, y坐标，其中x2, y2为反射的控制点（Smooth curve）
        Q   x1 y1 x y       从当前点的坐标画条反射二次贝塞曲线到指定点的x, y坐标，其中x1 y1为控制点（Quadratic Bézier curve）
        T   x y             从当前点的坐标画条反射二次贝塞曲线到指定点的x, y坐标，以前一个坐标为反射控制点（Smooth Quadratic Bézier curve）
        A   rx ry x-axis-rotation large-arc-flag sweep-flag x y     从当前点的坐标画个椭圆形到指定点的x, y坐标，其中rx, ry为椭圆形的x轴及y轴的半径，x-axis-rotation是弧线与x轴的旋转角度，large-arc-flag则设定1最大角度的弧线或是0最小角度的弧线，sweep-flag设定方向为1顺时针方向或0逆时针方向（Arc）
        Z       关闭路径，将当前点坐标与第一个点的坐标连接起来（Closepath）著作权归作者所有。

        以上所有命令均允许小写字母。大写表示绝对定位，小写表示相对定位。
    */

    private void initPoints() {
//      String x = "M 334 759 Q 328 760 323 760 Q 310 763 307 757 Q 300 750 310 734 Q 341 679 361 585 Q 365 557 383 539 Q 402 517 407 534 Q 408 541 410 551 L 407 587 Q 403 602 382 723 L 334 759 Z";
        String line[] = wu.split(",");
        for (int y = 0; y < line.length; y++) {
            String l = line[y];
            String p[] = l.split(" ");
            for (int i = 0; i < p.length; i++) {
                if ("M".equals(p[i].trim())) {
                    FontData data = new FontData();
                    data.type = "M";
                    data.start = new CPoint();
                    data.start.x = Float.valueOf(p[i + 1].trim());
                    data.start.y = 1024 - Float.valueOf(p[i + 2].trim());
                    fontDatas.add(data);
                    i = i + 2;
                } else if ("Q".equals(p[i].trim())) {
                    FontData data = new FontData();
                    data.type = "Q";
                    data.control = new CPoint();
                    data.control.x = Float.valueOf(p[i + 1].trim());
                    data.control.y = 1024 - Float.valueOf(p[i + 2].trim());
                    data.end = new CPoint();
                    data.end.x = Float.valueOf(p[i + 3].trim());
                    data.end.y = 1024 - Float.valueOf(p[i + 4].trim());
                    fontDatas.add(data);
                    i = i + 4;
                } else if ("L".equals(p[i].trim())) {
                    FontData data = new FontData();
                    data.type = "L";
                    data.end = new CPoint();
                    data.end.x = Float.valueOf(p[i + 1].trim());
                    data.end.y = 1024 - Float.valueOf(p[i + 2].trim());
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
//        canvas.drawBitmap(mBgBitmap, 0, 0, mPaint);
        drawBezier(canvas);
        //https://blog.csdn.net/u013085697/article/details/52096703
        int sc = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        if (userPath != null) {
            canvas.drawPath(userPath, userPaint);
        }
        if (mMaskBitmap == null || mMaskBitmap.isRecycled()) {
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

    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        for (int i = 0; i < fontDatas.size(); i++) {
            if ("M".equals(fontDatas.get(i).type)) {
                path.moveTo((float)fontDatas.get(i).start.x, (float)fontDatas.get(i).start.y);
            } else if ("Q".equals(fontDatas.get(i).type)) {
                path.quadTo((float)fontDatas.get(i).control.x, (float)fontDatas.get(i).control.y,
                        (float)fontDatas.get(i).end.x, (float)fontDatas.get(i).end.y);
            } else if ("L".equals(fontDatas.get(i).type)) {
                path.lineTo((float)fontDatas.get(i).end.x, (float)fontDatas.get(i).end.y);
            } else if ("Z".equals(fontDatas.get(i).type)) {
                path.close();
            }

        }
        canvas.drawPath(path, paint);
        return bitmap;
    }

    private void drawBezier(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(4);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        Path path = new Path();
        for (int i = 0; i < fontDatas.size(); i++) {
            if ("M".equals(fontDatas.get(i).type)) {
                path.moveTo((float)fontDatas.get(i).start.x, (float)fontDatas.get(i).start.y);
            } else if ("Q".equals(fontDatas.get(i).type)) {
                path.quadTo((float)fontDatas.get(i).control.x, (float)fontDatas.get(i).control.y,
                        (float)fontDatas.get(i).end.x, (float)fontDatas.get(i).end.y);
            } else if ("L".equals(fontDatas.get(i).type)) {
                path.lineTo((float)fontDatas.get(i).end.x, (float)fontDatas.get(i).end.y);
            } else if ("A".equals(fontDatas.get(i).type)) {

            } else if ("Z".equals(fontDatas.get(i).type)) {
                path.close();
            }

        }
        canvas.drawPath(path, paint);
    }

    private float downX, downY;
    private float tempX, tempY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                userPath.moveTo(downX, downY);
                invalidate();
                tempX = downX;
                tempY = downY;
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                userPath.quadTo(tempX, tempY, moveX, moveY);
                invalidate();
                tempX = moveX;
                tempY = moveY;
                break;
        }

        return true;
    }

//    x-axis-rotation是弧线与x轴的旋转角度
//    large-arc-flag则设定1最大角度的弧线或是0最小角度的弧线
//    sweep-flag设定方向为1顺时针方向或0逆时针方向（Arc）
    /**
     *
     *
     * @param p1
     * @param p2
     * @param dRadius
     * @param large_arc_flag
     * @param sweep_flag
     */
    private void calcCircleCenter(CPoint p1, CPoint p2, double dRadius,
                                  String large_arc_flag, String sweep_flag) {
        calcCircleCenter1(p1, p2, dRadius, large_arc_flag, sweep_flag);
//        calcCircleCenter2(p1, p2, dRadius);
    }

    /**
     * 完全参考代码https://blog.csdn.net/liumoude6/article/details/78114255?locationNum=2&fps=1
     * @param p1
     * @param p2
     * @param dRadius
     */
    private void calcCircleCenter1(CPoint p1, CPoint p2, double dRadius,
                                   String large_arc_flag, String sweep_flag) {
        double k = 0.0, k_verticle = 0.0;
        double mid_x = 0.0, mid_y = 0.0;
        double a = 1.0;
        double b = 1.0;
        double c = 1.0;

        CPoint center1 = new CPoint(), center2 = new CPoint();
        k = (p2.y - p1.y) / (p2.x - p1.x);
        if (k == 0) {
            center1.x = (p1.x + p2.x) / 2.0;
            center2.x = (p1.x + p2.x) / 2.0;
            center1.y = (p1.y + Math.sqrt(dRadius * dRadius - (p1.x - p2.x) * (p1.x - p2.x) / 4.0));
            center2.y = (p2.y - Math.sqrt(dRadius * dRadius - (p1.x - p2.x) * (p1.x - p2.x) / 4.0));
        } else {
            k_verticle = -1.0 / k;
            mid_x = ((p1.x + p2.x) / 2.0);
            mid_y = ((p1.y + p2.y) / 2.0);

            a = 1.0 + k_verticle * k_verticle;
            b = -2 * mid_x - k_verticle * k_verticle * (p1.x + p2.x);
            c = (mid_x * mid_x + k_verticle * k_verticle * (p1.x + p2.x) * (p1.x + p2.x) / 4.0 -
                (dRadius * dRadius - ((mid_x - p1.x) * (mid_x - p1.x) + (mid_y - p1.y) * (mid_y - p1.y))));

            center1.x = ((-1.0 * b + Math.sqrt(b * b - 4 * a * c)) / (2 * a));
            center2.x = ((-1.0 * b - Math.sqrt(b * b - 4 * a * c)) / (2 * a));

            center1.y = Y_Coordinates(mid_x, mid_y, k_verticle, center1.x);
            center2.y = Y_Coordinates(mid_x, mid_y, k_verticle, center2.x);

            Log.e("jimwind", "a "+a);
            Log.e("jimwind", "b "+b);
            Log.e("jimwind", "c "+c);
        }
        CPoint targetCenter = center1;
        //1最大角度的弧线
        if("1".equals(large_arc_flag)){
            double degree = angle(p1, p2, targetCenter);
            //为1顺时针方向
            if("1".equals(sweep_flag)){

            }
            //0逆时针方向（Arc）
            else if("0".equals(sweep_flag)){

            }
        }
        //0最小角度的弧线
        else if("0".equals(large_arc_flag)){
            //为1顺时针方向
            if("1".equals(sweep_flag)){

            }
            //0逆时针方向（Arc）
            else if("0".equals(sweep_flag)){

            }
        }

        Log.e("jimwind", "point1("+p1.x+","+p1.y+")");
        Log.e("jimwind", "point2("+p2.x+","+p2.y+")");
        Log.e("jimwind","r = "+dRadius);
        Log.e("jimwind", "center1(" + center1.x + "," + center1.y + ")");
        Log.e("jimwind", "center2(" + center2.x + "," + center2.y + ")");
    }

    private double Y_Coordinates(double mid_x, double mid_y, double k, double centerX) {
        return k * centerX - k * mid_x + mid_y;
    }



    //https://blog.csdn.net/liumoude6/article/details/78114255?locationNum=2&fps=1
    //C1 = (x2 + x1)/2 + ((y2 - y1)/(x2 - x1)) * ((y2 + y2) / 2)
    //C1 = mid_x + k * mid_y

    //C2 = (y2 - y1) / (x2 - x1)
    //C2 = k;

    //A = C2*C2 + 1 = k*k + 1
    //B = 2C2*x1 - 2C1C2 - 2y1 = 2*k*x1 - 2*(mid_x + k*mid_y)*k - 2*y1
    //C = x1*x1 - 2C1x1 + C1*C1 + y1*y1 - R*R
    // = x1*x1 - 2*(mid_x + k*mid_y)*x1 + (mid_x + k*mid_y)*(mid_x + k*mid_y) + y1*y1 - R*R
    // = x1*x1 - 2*mid_x*x1 - 2*k*mid_y*x1 + mid_x*mid_x + 2*mid_x*mid_y + mid_y*mid_y +y1*y1 - R*R

    //center.y = (-1.0*B + sqrt(B*B - 4*A*C))/(2*A)
    //center.x = C1 - center.y * C2
    //         = mid_x + k * mid_y - center.y * k

    /**
     * 参考推导过程得出 https://blog.csdn.net/liumoude6/article/details/78114255?locationNum=2&fps=1
     */
//    private void calcCircleCenter2(CPoint p1, CPoint p2, double dRadius,
//                                   int large_arc_flag, int sweep_flag) {
//        double k = 0.0, k_verticle = 0.0;
//        double mid_x = 0.0, mid_y = 0.0;
//        double a = 1.0;
//        double b = 1.0;
//        double c = 1.0;
//
//        CPoint center1 = new CPoint(), center2 = new CPoint();
//        k = (p2.y - p1.y) / (p2.x - p1.x);
//        if (k == 0) {
//            center1.x = (p1.x + p2.x) / 2.0;
//            center2.x = (p1.x + p2.x) / 2.0;
//            center1.y = p1.y + Math.sqrt(dRadius * dRadius - (p1.x - p2.x) * (p1.x - p2.x) / 4.0);
//            center2.y = p2.y - Math.sqrt(dRadius * dRadius - (p1.x - p2.x) * (p1.x - p2.x) / 4.0);
//        } else {
//            mid_x = (p1.x + p2.x) / 2.0;
//            mid_y = (p1.y + p2.y) / 2.0;
//
//            a = 1.0 + k * k;
//            b = 2 * k * p1.x - 2 * (mid_x + k * mid_y) * k - 2 * p1.y;
//            c = p1.x * p1.x - 2 * mid_x * p1.x - 2 * k * mid_y * p1.x + mid_x * mid_x + 2 * mid_x * mid_y + mid_y * mid_y + p1.y * p1.y - dRadius * dRadius;
//
//            center1.y = ((-1.0 * b + Math.sqrt(b * b - 4 * a * c)) / (2 * a));
//            center2.y = ((-1.0 * b - Math.sqrt(b * b - 4 * a * c)) / (2 * a));
//
//            center1.x = X_Coordinates(mid_x, mid_y, k, center1.y);
//            center2.x = X_Coordinates(mid_x, mid_y, k, center2.y);
//
//            Log.e("jimwind", "a "+a);
//            Log.e("jimwind", "b "+b);
//            Log.e("jimwind", "c "+c);
//        }
//        Log.e("jimwind", "point1("+p1.x+","+p1.y+")");
//        Log.e("jimwind", "point2("+p2.x+","+p2.y+")");
//        Log.e("jimwind","r = "+dRadius);
//        Log.e("jimwind", "center1(" + center1.x + "," + center1.y + ")");
//        Log.e("jimwind", "center2(" + center2.x + "," + center2.y + ")");
//
//        double degree1 = angle(p1, p2, center1);
//        double degree2 = angle(p1, p2, center2);
//
//    }
//
//    //center.x = mid_x + k*mid_y - center.y * k
//    private double X_Coordinates(double mid_x, double mid_y, double k, double centerY) {
//        return mid_x + k * mid_y - centerY * k;
//    }

    //https://blog.csdn.net/Three_Zhang/article/details/68059744
    private double angle(CPoint p1, CPoint p2, CPoint center) {
        //p1-----center  &  p2-----center
        double a = p1.x - center.x;
        double b = p1.y - center.y;
        double c = p2.x - center.x;
        double d = p2.y - center.y;
        double rads = Math.acos(((a * c) + (b * d)) / ((Math.sqrt(a * a + b * b)) * (Math.sqrt(c * c + d * d))));


//        if (center.y > p2.y) {
//            rads = -rads;
//        }
        double degree = Math.toDegrees(rads);
        Log.e("jimwind", "degree " + degree);

        return degree;

    }

}
