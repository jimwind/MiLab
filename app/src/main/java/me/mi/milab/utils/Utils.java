package me.mi.milab.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.io.File;
import java.util.Date;

import me.mi.milab.BuildConfig;
import me.mi.milab.config.SystemConstants;

/**
 * Created by mi.gao on 2017/8/7.
 */

public class Utils {
    /**
     * Convert Dp to Pixel
     */
    public static int dpToPx(float dp, Resources resources){
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static int getRelativeTop(View myView) {
//	    if (myView.getParent() == myView.getRootView())
        if(myView.getId() == android.R.id.content)
            return myView.getTop();
        else
            return myView.getTop() + getRelativeTop((View) myView.getParent());
    }

    public static int getRelativeLeft(View myView) {
//	    if (myView.getParent() == myView.getRootView())
        if(myView.getId() == android.R.id.content)
            return myView.getLeft();
        else
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
    }

    public static void loge(String msg){
        if(BuildConfig.DEBUG){
            Log.e("jimwind", msg);
        }
    }
    public static String takePhoto(Context context, int request_code){
        String state = Environment.getExternalStorageState();
        String takePhotoPath = "";
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            String fileName = DateUtil.df_filename.format(new Date()) + ".jpg";
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File file = new File(SystemConstants.CAMERA_DIR);
            if (!file.exists()) {
                file.mkdirs();
            }
            takePhotoPath = SystemConstants.CAMERA_DIR + fileName;

            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(takePhotoPath)));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            ((Activity)context).startActivityForResult(intent, request_code);
        }
        return takePhotoPath;
    }
}
