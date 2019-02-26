package me.mi.milab.video;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import me.mi.milab.R;

/**
 *   https://blog.csdn.net/wozuihaole/article/details/60867076
 */
public class Player extends AppCompatActivity implements View.OnTouchListener {

    private SurfaceView mSurfaceView;//能够播放图像的控件
    private SeekBar sb;//进度条
    private String path ;//本地文件路径
    private SurfaceHolder holder;
    private MediaPlayer player;//媒体播放器
    private Button Play;//播放按钮
    private Timer timer;//定时器
    private TimerTask task;//定时器任务
    private int position = 0;
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initView();
    }

    //初始化控件，并且为进度条和图像控件添加监听
    private void initView() {
        mSurfaceView = (SurfaceView) findViewById(R.id.sfv);
        sb = (SeekBar) findViewById(R.id.sb);
        Play = (Button) findViewById(R.id.play);
        et = (EditText) findViewById(R.id.et);
        Play.setEnabled(false);

        holder = mSurfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //当进度条停止拖动的时候，把媒体播放器的进度跳转到进度条对应的进度
                if (player != null) {
                    player.seekTo(seekBar.getProgress());
                }
            }
        });

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                //为了避免图像控件还没有创建成功，用户就开始播放视频，造成程序异常，所以在创建成功后才使播放按钮可点击
                Log.d("zhangdi","surfaceCreated");
                Play.setEnabled(true);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.d("zhangdi","surfaceChanged");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //当程序没有退出，但不在前台运行时，因为surfaceview很耗费空间，所以会自动销毁，
                // 这样就会出现当你再次点击进程序的时候点击播放按钮，声音继续播放，却没有图像
                //为了避免这种不友好的问题，简单的解决方式就是只要surfaceview销毁，我就把媒体播放器等
                //都销毁掉，这样每次进来都会重新播放，当然更好的做法是在这里再记录一下当前的播放位置，
                //每次点击进来的时候把位置赋给媒体播放器，很简单加个全局变量就行了。
                Log.d("zhangdi","surfaceDestroyed");
                if (player != null) {
                    position = player.getCurrentPosition();
                    stop();
                }
            }
        });
    }

    private void play() {

        Play.setEnabled(false);//在播放时不允许再点击播放按钮

        if (isPause) {//如果是暂停状态下播放，直接start
            isPause = false;
            player.start();
            return;
        }

        path = Environment.getExternalStorageDirectory().getPath()+"/";
        path = path + et.getText().toString();//sdcard的路径加上文件名称是文件全路径
        File file = new File(path);
        if (!file.exists()) {//判断需要播放的文件路径是否存在，不存在退出播放流程
            Toast.makeText(this,"文件路径不存在",Toast.LENGTH_LONG).show();
            return;
        }

        try {
            player = new MediaPlayer();
//            player.setDataSource(path);
            player.setDataSource("https://aoshu.up360.com/3X0JH-01-KK01-FAN.mp4");
            player.setDisplay(holder);//将影像播放控件与媒体播放控件关联起来

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {//视频播放完成后，释放资源
                    Play.setEnabled(true);
                    stop();
                }
            });

            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //媒体播放器就绪后，设置进度条总长度，开启计时器不断更新进度条，播放视频
                    Log.d("zhangdi","onPrepared");
                    sb.setMax(player.getDuration());
                    timer = new Timer();
                    task = new TimerTask() {
                        @Override
                        public void run() {
                            if (player != null) {
                                int time = player.getCurrentPosition();
                                sb.setProgress(time);
                            }
                        }
                    };
                    timer.schedule(task,0,500);
                    sb.setProgress(position);
                    player.seekTo(position);
                    player.start();
                }
            });

            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play(View v) {
        play();
        Log.d("zhangdi",path);
    }

    public void fullscreen(View v){
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mSurfaceView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        et.setVisibility(View.GONE);

    }
    private boolean isPause;
    private void pause() {
        if (player != null && player.isPlaying()) {
            player.pause();
            isPause = true;
            Play.setEnabled(true);
        }
    }

    public void pause(View v) {
        pause();
    }

    private void replay() {
        isPause = false;
        if (player != null) {
            stop();
            play();
        }
    }

    public void replay(View v) {
        replay();
    }

    private void stop(){
        isPause = false;
        if (player != null) {
            sb.setProgress(0);
            player.stop();
            player.release();
            player = null;
            if (timer != null) {
                timer.cancel();
            }
            Play.setEnabled(true);
        }
    }

    public void stop(View v) {
        stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
