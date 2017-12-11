package me.mi.milab;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;

import me.mi.milab.album.AlbumActivity;
import me.mi.milab.album.AlbumHelper;
import me.mi.milab.album.SingleMediaScanner;
import me.mi.milab.config.SystemConstants;
import me.mi.milab.utils.DateUtil;
import me.mi.milab.utils.OKHttpUtils;
import me.mi.milab.utils.Utils;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    //TODO
    private int toggle = 3;
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

        }
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
    }
    class TransformMatrixView extends ImageView
    {
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

    public boolean onTouch(View v, MotionEvent e)
    {
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
}
