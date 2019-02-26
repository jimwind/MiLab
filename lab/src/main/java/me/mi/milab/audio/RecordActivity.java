package me.mi.milab.audio;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import me.mi.milab.R;


public class RecordActivity extends Activity {
    private RecordHelper rh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_10);
        rh = new RecordHelper();
    }
    public void record(View v){
        try {
            Log.e("jimwind", "record");
            rh.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void stop(View v){
        Log.e("jimwind", "stop");
        rh.stop();

    }

}
