package me.mi.milab.album;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

import java.io.File;

/**
 * Created by mi.gao on 2017/12/11.
 *
 * 拍完照后, 需要通知系统图片更新
 */

public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {

    private MediaScannerConnection msc;
    private File mFile;
    public SingleMediaScanner(Context context, File file){
        mFile = file;
        msc = new MediaScannerConnection(context, this);
        msc.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        msc.scanFile(mFile.getAbsolutePath(), null);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        msc.disconnect();
    }
}
