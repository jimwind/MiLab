package me.mi.milab.launchmode;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by mi.gao on 2018/3/27.
 */

public class ActivityD extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("jimwind", "create D");
    }
}
