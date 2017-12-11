package me.mi.milab.config;

import android.os.Environment;

/**
 * Created by mi.gao on 2017/12/11.
 */

public class SystemConstants {
    public static final String APP_SDCARD_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/milab/";
    public static final String CAMERA_DIR = APP_SDCARD_PATH + "milabcamera/";
}
