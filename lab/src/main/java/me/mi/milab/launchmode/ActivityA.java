package me.mi.milab.launchmode;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by mi.gao on 2018/3/27.
 */

public class ActivityA extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("jimwind", "create A");
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);

    }
}
