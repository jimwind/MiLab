package me.mi.milab.video;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.IOException;

import me.mi.milab.R;
import me.mi.milab.config.SystemConstants;

public class VideoEdit extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_edit);


    }

    public void extractor(View v) {
        File dir = new File(SystemConstants.VIDEO_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File outFile = new File(SystemConstants.VIDEO_DIR + System.currentTimeMillis() + ".mp4");
        File video_for_audio = new File(SystemConstants.VIDEO_DIR + "qpy2.mp4");
//        File video_for_audio = new File(SystemConstants.AUDIO_DIR +"1234567.mp3");
        File video_for_picture = new File(SystemConstants.VIDEO_DIR + "kxwk1.mp4");
        if (video_for_audio.exists() && video_for_picture.exists()) {
            Util.combineTwoVideos(video_for_audio.getAbsolutePath(), 0, video_for_picture.getAbsolutePath(), outFile);
        }

    }

    public void muxer(View v) throws IOException {
        File dir = new File(SystemConstants.VIDEO_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

//        String outFile = SystemConstants.VIDEO_DIR + System.currentTimeMillis() + ".mp4";
//        File video_for_audio = new File(SystemConstants.VIDEO_DIR + "12_1535098443.mp4");
//        File video_for_picture = new File(SystemConstants.VIDEO_DIR + "58_1535448057.mp4");
//        if (video_for_audio.exists() && video_for_picture.exists()) {
//            MediaUtil.getInstance().combineMedia(video_for_picture.getAbsolutePath(), video_for_audio.getAbsolutePath(), outFile);
//        }

        String inFile = SystemConstants.VIDEO_DIR + "1502011806820.mp4";
        String outFile = SystemConstants.VIDEO_DIR + System.currentTimeMillis() + ".mp4";
        Util.process(inFile, outFile);
    }
}
