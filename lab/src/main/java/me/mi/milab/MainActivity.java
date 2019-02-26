package me.mi.milab;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import me.mi.milab.album.AlbumActivity;
import me.mi.milab.album.AlbumHelper;
import me.mi.milab.album.SingleMediaScanner;
import me.mi.milab.audio.RecordActivity;
import me.mi.milab.config.SystemConstants;
import me.mi.milab.ui.Bezier;
import me.mi.milab.utils.DateUtil;
import me.mi.milab.utils.OKHttpUtils;
import me.mi.milab.utils.Utils;
import me.mi.milab.video.Player;
import me.mi.milab.video.VideoEdit;
import me.mi.milab.view.AutoNewlineView;
import me.mi.milab.view.RefreshableView;
import me.mi.milab.view.StepArcView;
import me.mi.milab.view.piechart.PieBean;
import me.mi.milab.view.piechart.PieView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private final int toggle = 10;
    private Context mContext;
    private TransformMatrixView view;
    private final int REQUEST_CODE_TAKE_PHOTO = 1;
    private String mTakePhotoFile = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        switch (toggle){
            case 0:{
                setContentView(R.layout.activity_main);
                case0();
                break;
            }
            case 1:{
                OKHttpUtils okHttpUtils = new OKHttpUtils();
                okHttpUtils.simplePost();
                break;
            }
            //http://www.cnblogs.com/qiengo/archive/2012/06/30/2570874.html#code
            case 2:{
                view = new TransformMatrixView(this);
                view.setScaleType(ImageView.ScaleType.MATRIX);
                view.setOnTouchListener(this);
                setContentView(view);
                break;
            }
            case 3:{
                setContentView(R.layout.activity_start);
                case3();
                break;
            }
            case 4:{
                setContentView(R.layout.activity_case_4);
                case4();
                break;
            }
            case 5:{
                setContentView(R.layout.activity_menu);
                break;
            }
            case 6:{
                Intent intent = new Intent(mContext, Player.class);
                startActivity(intent);
                break;
            }
            case 7:{
                setContentView(R.layout.activity_case_7_refreshableview);
                case7();
                break;
            }
            case 8:{
                setContentView(R.layout.activity_case_8);
                case8();
                break;
            }
            case 9:{
                Intent intent = new Intent(mContext, VideoEdit.class);
                startActivity(intent);
                break;
            }
            case 10:{
                Intent intent = new Intent(mContext, RecordActivity.class);
                startActivity(intent);
            }

        }

        WindowManager manager = getWindowManager();
        Display display = manager.getDefaultDisplay();
        int widthScreen = display.getWidth();
        int heightScreen = display.getHeight();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        float density = metric.density;
        Log.e("jimwind", "widthScreen:"+widthScreen+"|heightScreen:"+heightScreen+"|density:"+density);
    }
    private void case0(){
        findViewById(R.id.goto_market).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri = Uri.parse("market://details?id=com.up360.teacher.android.activity");
                    Intent gotoMarket = new Intent(Intent.ACTION_VIEW, uri);
                    gotoMarket.setPackage("com.tencent.android.qqdownloader");
                    gotoMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(gotoMarket);
                } catch (ActivityNotFoundException e) {
                    // 如果没有安装应用宝,就直接打开应用宝页面
                    Intent browser = new Intent(Intent.ACTION_VIEW);
        			browser.setData(Uri.parse("http://app.qq.com/#id=detail&appid=1101958205"));
//                    browser.setData(Uri.parse(appStoreDownloadUrl));
                    mContext.startActivity(browser);
                    e.printStackTrace();
                }
            }
        });
    }
    private void case3(){
        findViewById(R.id.album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AlbumActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.take_picture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTakePhotoFile = Utils.takePhoto(mContext, REQUEST_CODE_TAKE_PHOTO);
            }
        });
        final TextView show = (TextView) findViewById(R.id.show);
        show.setText(getSystemProperty());
//        show.setText("我是第一行\n我是第二行\n\n我是第三行\n\n\n我是最后一行");
//        findViewById(R.id.program).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String showText = "";
//                int a = 0x0001;
//                showText += String.format("%08d\n", Integer.parseInt(Integer.toBinaryString(a)));
//                a = a << 1;
//                showText += String.format("%08d\n", Integer.parseInt(Integer.toBinaryString(a)));
//                a = a << 1;
//                showText += String.format("%08d\n", Integer.parseInt(Integer.toBinaryString(a)));
//
//                show.setText(showText);
//            }
//        });

        findViewById(R.id.animator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        String[] words = {"ɑn","en", "in","un","ün"};
        AutoNewlineView anv = (AutoNewlineView) findViewById(R.id.pinyin);
        for(int i=0; i<words.length; i++){
            TextView tv = new TextView(mContext);
            tv.setTextSize(100);
            tv.setIncludeFontPadding(false);
            tv.setBackgroundResource(R.drawable.round_corner_green_solid_radius49);
            tv.setText(words[i]);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            anv.addView(tv, params);
        }

    }

    private void case4(){
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.photo, null);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds=true decode时只返回宽和高，给outWidth&outHeight
//                -> outWidth&outHeight
//        options.inSampleSize 采样率 节省内存

        //bitmap.extractAlpha(); ?
    }
    RefreshableView refreshableView;
    ListView listView;
    ArrayAdapter<String> adapter;
    String[] items = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L" };
    private void case7(){
        refreshableView = (RefreshableView) findViewById(R.id.refreshable_view);
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                refreshableView.finishRefreshing();
            }
        }, 0);
    }

    private void case8(){
        StepArcView srv = (StepArcView) findViewById(R.id.srv);
        srv.setCurrentCount(100, 70);

        pieView();
    }
    private void pieView(){
        PieView pvLevelInfo = (PieView) findViewById(R.id.pie_view);

        //设置文字颜色 默认白色
        pvLevelInfo.setTextColor(Color.WHITE);
        //设置中间圆大小 0不显示中间圆 1到10中间圆逐渐减小
        pvLevelInfo.setCenterCir(1);

        //设置中间文字颜色 默认黑色
        pvLevelInfo.setCenterTextColor(0x666666);
        //是否显示百分比文字 默认true
        pvLevelInfo.setPercentageTextShow(true);
        //是否显示动画 默认true
        pvLevelInfo.setShowAnimation(true);
        //是否绘制分割线 默认true
        pvLevelInfo.setDrawLine(false);
        //设置分割线颜色默认白色
        pvLevelInfo.setLineColor(Color.WHITE);
        //是否绘制中心阴影 默认true
        pvLevelInfo.isShadow(false);
        //点击是否自动旋转到底部 默认true
        pvLevelInfo.setTouchStart(true);
        //点击是否切割扇形 默认true
        pvLevelInfo.setTouchCarve(true);

        ArrayList<PieBean> pieDatas = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        pvLevelInfo.setCenterText("3个");
        pvLevelInfo.setCenterText2("知识点");
        String level[] = {"A+", "A", "B+", "B", "C", "D"};
        for (int i = 0; i < level.length; i++) {
            colors.add(getColorByName(level[i]));
            PieBean pieBean = new PieBean();
            pieBean.setName(level[i]);
            pieBean.setValue(50);
            pieDatas.add(pieBean);
        }
        if (pieDatas.size() == 1) {
            //设置分割线颜色默认白色
            pvLevelInfo.setDrawLine(false);
        }

        //设置所有颜色
        pvLevelInfo.setmColors(colors);
        //设置数据
        pvLevelInfo.setData(pieDatas);
    }
    private Integer getColorByName(String name) {
        String[] names = {"A+", "A", "B+", "B", "C", "D"};
        int[] colors = {R.color.study_report_level_a_plus, R.color.study_report_level_a, R.color.study_report_level_b_plus,
                R.color.study_report_level_b, R.color.study_report_level_c, R.color.study_report_level_d};
        int color;
        if (names[0].equals(name)) {
            color = colors[0];
        } else if (names[1].equals(name)) {
            color = colors[1];
        } else if (names[2].equals(name)) {
            color = colors[2];
        } else if (names[3].equals(name)) {
            color = colors[3];
        } else if (names[4].equals(name)) {
            color = colors[4];
        } else {
            color = colors[5];
        }
        return color;
    }
    class TransformMatrixView extends ImageView {
        private Bitmap bitmap;
        private Matrix matrix;
        public TransformMatrixView(Context context)
        {
            super(context);
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.sophie);
            matrix = new Matrix();
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            // 画出原图像
            canvas.drawBitmap(bitmap, 0, 0, null);
            // 画出变换后的图像
            canvas.drawBitmap(bitmap, matrix, null);
            super.onDraw(canvas);
        }

        @Override
        public void setImageMatrix(Matrix matrix)
        {
            this.matrix.set(matrix);
            super.setImageMatrix(matrix);
        }

        public Bitmap getImageBitmap()
        {
            return bitmap;
        }
    }

    public boolean onTouch(View v, MotionEvent e) {
        if(e.getAction() == MotionEvent.ACTION_UP) {
            Matrix matrix = new Matrix();
            {
                // 输出图像的宽度和高度(162 x 251)
                Log.e("jimwind", "image size: width x height = " + view.getImageBitmap().getWidth() + " x " + view.getImageBitmap().getHeight());
                // 1. 平移
                matrix.postTranslate(view.getImageBitmap().getWidth(), view.getImageBitmap().getHeight());
                // 在x方向平移view.getImageBitmap().getWidth()，在y轴方向view.getImageBitmap().getHeight()
                view.setImageMatrix(matrix);

                // 下面的代码是为了查看matrix中的元素
                float[] matrixValues = new float[9];
                matrix.getValues(matrixValues);
                for (int i = 0; i < 3; ++i) {
                    String temp = new String();
                    for (int j = 0; j < 3; ++j) {
                        temp += matrixValues[3 * i + j] + "\t";
                    }
                    Log.e("jimwind", temp);
                }
            }
//            {
//                // 2. 旋转(围绕图像的中心点)
//                matrix.setRotate(45f, view.getImageBitmap().getWidth() / 2f, view.getImageBitmap().getHeight() / 2f);
//
//                // 做下面的平移变换，纯粹是为了让变换后的图像和原图像不重叠
//                matrix.postTranslate(view.getImageBitmap().getWidth() * 1.5f, 0f);
//                view.setImageMatrix(matrix);
//
//                // 下面的代码是为了查看matrix中的元素
//                float[] matrixValues = new float[9];
//                matrix.getValues(matrixValues);
//                for (int i = 0; i < 3; ++i) {
//                    String temp = new String();
//                    for (int j = 0; j < 3; ++j) {
//                        temp += matrixValues[3 * i + j] + "\t";
//                    }
//                    Log.e("jimwind", temp);
//                }
//
//            }
//            {
//                // 3. 旋转(围绕坐标原点) + 平移(效果同2)
//                matrix.setRotate(45f);
//                matrix.preTranslate(-1f * view.getImageBitmap().getWidth() / 2f, -1f * view.getImageBitmap().getHeight() / 2f);
//                matrix.postTranslate((float) view.getImageBitmap().getWidth() / 2f, (float) view.getImageBitmap().getHeight() / 2f);
//
//                // 做下面的平移变换，纯粹是为了让变换后的图像和原图像不重叠
//                matrix.postTranslate((float) view.getImageBitmap().getWidth() * 1.5f, 0f);
//                view.setImageMatrix(matrix);
//
//                // 下面的代码是为了查看matrix中的元素
//                float[] matrixValues = new float[9];
//                matrix.getValues(matrixValues);
//                for (int i = 0; i < 3; ++i) {
//                    String temp = new String();
//                    for (int j = 0; j < 3; ++j) {
//                        temp += matrixValues[3 * i + j] + "\t";
//                    }
//                    Log.e("jimwind", temp);
//                }
//            }
//            {
//                // 4. 缩放
//                matrix.setScale(2f, 2f);
//                // 下面的代码是为了查看matrix中的元素
//                float[] matrixValues = new float[9];
//                matrix.getValues(matrixValues);
//                for (int i = 0; i < 3; ++i) {
//                    String temp = new String();
//                    for (int j = 0; j < 3; ++j) {
//                        temp += matrixValues[3 * i + j] + "\t";
//                    }
//                    Log.e("jimwind", temp);
//                }
//
//                // 做下面的平移变换，纯粹是为了让变换后的图像和原图像不重叠
//                matrix.postTranslate(view.getImageBitmap().getWidth(), view.getImageBitmap().getHeight());
//                view.setImageMatrix(matrix);
//
//                // 下面的代码是为了查看matrix中的元素
//                matrixValues = new float[9];
//                matrix.getValues(matrixValues);
//                for (int i = 0; i < 3; ++i) {
//                    String temp = new String();
//                    for (int j = 0; j < 3; ++j) {
//                        temp += matrixValues[3 * i + j] + "\t";
//                    }
//                    Log.e("jimwind", temp);
//                }
//            }
//            {
//                // 5. 错切 - 水平
//                matrix.setSkew(0.5f, 0f);
//                // 下面的代码是为了查看matrix中的元素
//                float[] matrixValues = new float[9];
//                matrix.getValues(matrixValues);
//                for (int i = 0; i < 3; ++i) {
//                    String temp = new String();
//                    for (int j = 0; j < 3; ++j) {
//                        temp += matrixValues[3 * i + j] + "\t";
//                    }
//                    Log.e("jimwind", temp);
//                }
//
//                // 做下面的平移变换，纯粹是为了让变换后的图像和原图像不重叠
//                matrix.postTranslate(view.getImageBitmap().getWidth(), 0f);
//                view.setImageMatrix(matrix);
//
//                // 下面的代码是为了查看matrix中的元素
//                matrixValues = new float[9];
//                matrix.getValues(matrixValues);
//                for (int i = 0; i < 3; ++i) {
//                    String temp = new String();
//                    for (int j = 0; j < 3; ++j) {
//                        temp += matrixValues[3 * i + j] + "\t";
//                    }
//                    Log.e("jimwind", temp);
//                }
//            }
//            {
//                // 6. 错切 - 垂直
//                matrix.setSkew(0f, 0.5f);
//                // 下面的代码是为了查看matrix中的元素
//                float[] matrixValues = new float[9];
//                matrix.getValues(matrixValues);
//                for (int i = 0; i < 3; ++i) {
//                    String temp = new String();
//                    for (int j = 0; j < 3; ++j) {
//                        temp += matrixValues[3 * i + j] + "\t";
//                    }
//                    Log.e("jimwind", temp);
//                }
//
//                // 做下面的平移变换，纯粹是为了让变换后的图像和原图像不重叠
//                matrix.postTranslate(0f, view.getImageBitmap().getHeight());
//                view.setImageMatrix(matrix);
//
//                // 下面的代码是为了查看matrix中的元素
//                matrixValues = new float[9];
//                matrix.getValues(matrixValues);
//                for (int i = 0; i < 3; ++i) {
//                    String temp = new String();
//                    for (int j = 0; j < 3; ++j) {
//                        temp += matrixValues[3 * i + j] + "\t";
//                    }
//                    Log.e("jimwind", temp);
//                }
//            }
//            {
//                //7. 错切 - 水平 + 垂直
//                matrix.setSkew(0.5f, 0.5f);
//                // 下面的代码是为了查看matrix中的元素
//                float[] matrixValues = new float[9];
//                matrix.getValues(matrixValues);
//                for (int i = 0; i < 3; ++i) {
//                    String temp = new String();
//                    for (int j = 0; j < 3; ++j) {
//                        temp += matrixValues[3 * i + j] + "\t";
//                    }
//                    Log.e("jimwind", temp);
//                }
//
//                // 做下面的平移变换，纯粹是为了让变换后的图像和原图像不重叠
//                matrix.postTranslate(0f, view.getImageBitmap().getHeight());
//                view.setImageMatrix(matrix);
//
//                // 下面的代码是为了查看matrix中的元素
//                matrixValues = new float[9];
//                matrix.getValues(matrixValues);
//                for (int i = 0; i < 3; ++i) {
//                    String temp = new String();
//                    for (int j = 0; j < 3; ++j) {
//                        temp += matrixValues[3 * i + j] + "\t";
//                    }
//                    Log.e("jimwind", temp);
//                }
//            }
//            {
//                // 8. 对称 (水平对称)
//                float matrix_values[] = {1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f};
//                matrix.setValues(matrix_values);
//                // 下面的代码是为了查看matrix中的元素
//                float[] matrixValues = new float[9];
//                matrix.getValues(matrixValues);
//                for (int i = 0; i < 3; ++i) {
//                    String temp = new String();
//                    for (int j = 0; j < 3; ++j) {
//                        temp += matrixValues[3 * i + j] + "\t";
//                    }
//                    Log.e("jimwind", temp);
//                }
//
//                // 做下面的平移变换，纯粹是为了让变换后的图像和原图像不重叠
//                matrix.postTranslate(0f, view.getImageBitmap().getHeight() * 2f);
//                view.setImageMatrix(matrix);
//
//                // 下面的代码是为了查看matrix中的元素
//                matrixValues = new float[9];
//                matrix.getValues(matrixValues);
//                for (int i = 0; i < 3; ++i) {
//                    String temp = new String();
//                    for (int j = 0; j < 3; ++j) {
//                        temp += matrixValues[3 * i + j] + "\t";
//                    }
//                    Log.e("jimwind", temp);
//                }
//            }
//            {
//                // 9. 对称 - 垂直
//                float matrix_values[] = {-1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f};
//                matrix.setValues(matrix_values);
//                // 下面的代码是为了查看matrix中的元素
//                float[] matrixValues = new float[9];
//                matrix.getValues(matrixValues);
//                for (int i = 0; i < 3; ++i) {
//                    String temp = new String();
//                    for (int j = 0; j < 3; ++j) {
//                        temp += matrixValues[3 * i + j] + "\t";
//                    }
//                    Log.e("jimwind", temp);
//                }
//
//                // 做下面的平移变换，纯粹是为了让变换后的图像和原图像不重叠
//                matrix.postTranslate(view.getImageBitmap().getWidth() * 2f, 0f);
//                view.setImageMatrix(matrix);
//
//                // 下面的代码是为了查看matrix中的元素
//                matrixValues = new float[9];
//                matrix.getValues(matrixValues);
//                for (int i = 0; i < 3; ++i) {
//                    String temp = new String();
//                    for (int j = 0; j < 3; ++j) {
//                        temp += matrixValues[3 * i + j] + "\t";
//                    }
//                    Log.e("jimwind", temp);
//                }
//            }
//            {
//                // 10. 对称(对称轴为直线y = x)
//                float matrix_values[] = {0f, -1f, 0f, -1f, 0f, 0f, 0f, 0f, 1f};
//                matrix.setValues(matrix_values);
//                // 下面的代码是为了查看matrix中的元素
//                float[] matrixValues = new float[9];
//                matrix.getValues(matrixValues);
//                for (int i = 0; i < 3; ++i) {
//                    String temp = new String();
//                    for (int j = 0; j < 3; ++j) {
//                        temp += matrixValues[3 * i + j] + "\t";
//                    }
//                    Log.e("jimwind", temp);
//                }
//
//                // 做下面的平移变换，纯粹是为了让变换后的图像和原图像不重叠
//                matrix.postTranslate(view.getImageBitmap().getHeight() + view.getImageBitmap().getWidth(),
//                        view.getImageBitmap().getHeight() + view.getImageBitmap().getWidth());
//                view.setImageMatrix(matrix);
//
//                // 下面的代码是为了查看matrix中的元素
//                matrixValues = new float[9];
//                matrix.getValues(matrixValues);
//                for (int i = 0; i < 3; ++i) {
//                    String temp = new String();
//                    for (int j = 0; j < 3; ++j) {
//                        temp += matrixValues[3 * i + j] + "\t";
//                    }
//                    Log.e("jimwind", temp);
//                }
//            }

            view.invalidate();
        }
        return true;
    }

    @Override
    protected void onActivityResult(int req, int resultCode, Intent data) {
        super.onActivityResult(req, resultCode, data);
        if(req == REQUEST_CODE_TAKE_PHOTO){
            if(resultCode == Activity.RESULT_OK){
                if(!TextUtils.isEmpty(mTakePhotoFile)){
                    File file = new File(mTakePhotoFile);
                    if(file.exists()){
                        new SingleMediaScanner(this, file);
                        AlbumHelper.getInstance().refresh();
                    }
                }
            }
        }
    }

    public void bezier(View view){
        Intent intent = new Intent(this, Bezier.class);
        startActivity(intent);
    }

    public static String getSystemProperty() {
        String line;
        BufferedReader input = null;
        String prop = "ro.build.version.incremental";
        try {
//            Process p = Runtime.getRuntime().exec("getprop " + "ro.build.display.id");
            Process p = Runtime.getRuntime().exec("getprop " + prop);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            Log.e("jimwind", "Unable to read sysprop " + prop, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e("jimwind", "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }


    private void a() throws IllegalAccessException, InstantiationException {
//        Class<?>[] classType = TextView.class.getDeclaredClasses();


        Class<?> classType = TextView.class;
        Object invokeTest = classType.newInstance();
        try {
            Method addMethod = classType.getMethod("add", new Class[]{int.class, int.class});
            Object result = addMethod.invoke(invokeTest, new Object[]{1, 2});

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
