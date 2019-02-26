package me.mi.milab.audio;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import me.mi.milab.utils.FileUtils;
import me.mi.milab.utils.Logger;

public class RecordHelper {
    private static final String TAG = RecordHelper.class.getSimpleName();
    //=======================AudioRecord Default Settings=======================
    private final int DEFAULT_AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    /**
     * 以下三项为默认配置参数。Google Android文档明确表明只有以下3个参数是可以在所有设备上保证支持的。
     */
    private final int DEFAULT_SAMPLING_RATE = 16000; //speech的采样率必须是16000

    private final int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;

    //	下面是对此的封装
    private final int DEFAULT_AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
//	private static final PCMFormat DEFAULT_AUDIO_FORMAT = PCMFormat.PCM_16BIT;

    //======================Lame Default Settings=====================
    private final int DEFAULT_LAME_MP3_QUALITY = 7;
    /**
     * 与DEFAULT_CHANNEL_CONFIG相关，因为是mono单声，所以是1
     */
    private final int DEFAULT_LAME_IN_CHANNEL = 1;
    /**
     *  Encoded bit rate. MP3 file will be encoded with bit rate 32kbps
     */
    private final int DEFAULT_LAME_MP3_BIT_RATE = 32;

    //==================================================================

    /**
     * 自定义 每160帧作为一个周期，通知一下需要进行编码
     */
    private final int FRAME_COUNT = 160;
    private AudioRecord mAudioRecord = null;
    private int mBufferSize;
    private byte[] mPCMBuffer;
    private boolean mIsRecording = false;
    private boolean mIsPause = false;
    private Context mContext;

    private File tmpFile = null;

    /**
     * Start recording. Create an encoding thread. Start record from this
     * thread.
     *
     * @throws IOException  initAudioRecorder throws
     */
    public synchronized void start() throws IOException {
        if (mIsRecording) return;
        if (!mIsPause) {
            if(!initAudioRecorder()){
                return;
            }
        }
        String tempFilePath = getTempFilePath();
        tmpFile = new File(tempFilePath);

        try {
            mAudioRecord.startRecording();
        } catch (IllegalStateException e) {
            mIsRecording = false;
            throw new IOException();
        } catch (Throwable e) {
            mIsRecording = false;
            throw new IOException();
        }
        new Thread() {

            @Override
            public void run() {
                FileOutputStream fos = null;
                //设置线程权限
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                mIsRecording = true;
                mIsPause = false;
                try{
                    fos = new FileOutputStream(tmpFile);
                    while (mIsRecording) {
                        int readSize = mAudioRecord.read(mPCMBuffer, 0, mBufferSize);
                        fos.write(mPCMBuffer, 0, readSize);
                        fos.flush();;
                    }
                } catch (Exception e){

                } finally {
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // release and finalize audioRecord
                if(mAudioRecord == null){
                    return;
                }
                try{
                    mAudioRecord.stop();
                } catch(IllegalStateException e){
                }
                if(!mIsPause){
                    if(mAudioRecord != null) {
                        mAudioRecord.release();
                        mAudioRecord = null;
                    }

                }
            }
        }.start();
    }

    public synchronized void stop(){
        mIsPause = false;
        if(!mIsRecording){//如果是暂停状态是选择停止!
            if(mAudioRecord == null){
                return ;
            }
            try{
                mAudioRecord.stop();
            } catch(IllegalStateException e){
            }
            if(mAudioRecord != null) {
                mAudioRecord.release();
                mAudioRecord = null;
            }
        } else { //如果是录音状态，停止的后续工作在线程中完成
            mIsRecording = false;
        }
    }

    private boolean initAudioRecorder() {
        mBufferSize = AudioRecord.getMinBufferSize(DEFAULT_SAMPLING_RATE,
                DEFAULT_CHANNEL_CONFIG, DEFAULT_AUDIO_FORMAT/*.getAudioFormat()*/);

        int bytesPerFrame = DEFAULT_AUDIO_FORMAT/*.getBytesPerFrame()*/;
        /* Get number of samples. Calculate the buffer size
         * (round up to the factor of given frame size)
         * 使能被整除，方便下面的周期性通知
         * */
        int frameSize = mBufferSize / bytesPerFrame;
        if (frameSize % FRAME_COUNT != 0) {
            frameSize += (FRAME_COUNT - frameSize % FRAME_COUNT);
            mBufferSize = frameSize * bytesPerFrame;
        }

        try {
            /* Setup audio recorder */
            mAudioRecord = new AudioRecord(DEFAULT_AUDIO_SOURCE,
                    DEFAULT_SAMPLING_RATE, DEFAULT_CHANNEL_CONFIG,
                    DEFAULT_AUDIO_FORMAT/*.getAudioFormat()*/,
                    mBufferSize);

        } catch(IllegalArgumentException e){
            return false;
        }
        mPCMBuffer = new byte[mBufferSize];

        mAudioRecord.setPositionNotificationPeriod(FRAME_COUNT);
//        mAudioRecord.setRecordPositionUpdateListener(mEncodeThread, mEncodeThread.getHandler());
        return true;
    }

    /**
     * 根据当前的时间生成相应的文件名
     * 实例 record_20160101_13_15_12
     */
    private String getTempFilePath() {
        String fileDir = String.format(Locale.getDefault(), "%s/Record/", Environment.getExternalStorageDirectory().getAbsolutePath());
        if (!FileUtils.createOrExistsDir(fileDir)) {
            Logger.e(TAG, "文件夹创建失败：%s", fileDir);
        }
        String fileName = String.format(Locale.getDefault(), "record_tmp_%s", FileUtils.getNowString(new SimpleDateFormat("yyyyMMdd_HH_mm_ss", Locale.SIMPLIFIED_CHINESE)));
        return String.format(Locale.getDefault(), "%s%s.pcm", fileDir, fileName);
    }
}
