package me.mi.milab.album;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.mi.milab.bean.AlbumBean;
import me.mi.milab.bean.PictureBean;
import me.mi.milab.utils.Utils;

/**
 * Created by mi.gao on 2017/12/8.
 *
 * 注意: 单例模式
 *
 */

public class AlbumHelper {
    private Context context;

    private ContentResolver contentResolver;
    private HashMap<String, AlbumBean> mAlbumList = new HashMap<>();
    /**
     * 是否创建了图片集
     */
    boolean hasBuildAlbumList = false;
    //本应用自己拍照后, 需要刷新
    private boolean needRefresh = false;
    private static AlbumHelper instance;

    public static AlbumHelper getInstance() {
        if (instance == null) {
            instance = new AlbumHelper();
        }
        return instance;
    }


    public void init(Context context) {
        if (this.context == null) {
            this.context = context;
            contentResolver = context.getContentResolver();
        }
    }
    public void refresh(){
        needRefresh = true;
    }
    public ArrayList<AlbumBean> getAlbumList() {
        Utils.loge("needRefresh/hasBuildAlbumList " +needRefresh + "/" + hasBuildAlbumList);
        if (needRefresh || !hasBuildAlbumList) {
            buildAlbumList();
            needRefresh = false;
        }
        ArrayList<AlbumBean> albumList = new ArrayList<>();
        Iterator<Map.Entry<String, AlbumBean>> itr = mAlbumList.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<String, AlbumBean> entry = itr.next();
            albumList.add(entry.getValue());
        }
        return albumList;
    }
    private void buildAlbumList() {
        long startTime = System.currentTimeMillis();
        mAlbumList.clear();
        // 构造缩略图索引
        getThumbnail();

        // 构造相册索引
        String columns[] = new String[] {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.PICASA_ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        // 得到一个游标
        Cursor cur = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns,
                null,
                null,
                MediaStore.Images.Media.DATE_TAKEN + " desc");

        if (cur.moveToFirst()) {
            // 获取指定列的索引
            int idIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int pathIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int nameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
            int titleIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
            int sizeIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
            int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
            int picasaIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.PICASA_ID);
            // 获取图片总数
            int totalNum = cur.getCount();

            do {
                try {
                    String _id = cur.getString(idIndex);
                    String name = cur.getString(nameIndex);
                    //这里过滤出。gif格式的图片
                    int pos = name.lastIndexOf(".");

                    if(pos == -1){
                        pos = 0;//这是需要做一个保护。
                    }
                    String extention = name.substring(pos);
                    if(extention.equalsIgnoreCase(".gif")){
                        continue;
                    }

                    String path = cur.getString(pathIndex);
                    String title = cur.getString(titleIndex);
                    String size = cur.getString(sizeIndex);
                    String bucketName = cur.getString(bucketDisplayNameIndex);
                    String bucketId = cur.getString(bucketIdIndex);
                    String picasaId = cur.getString(picasaIdIndex);

                    AlbumBean album = mAlbumList.get(bucketId);
                    if (album == null) {
                        album = new AlbumBean();
                        album.setPictureList(new ArrayList<PictureBean>());
                        album.setName(bucketName);
                        album.setPictureCount(0);
                        mAlbumList.put(bucketId, album);
                    }
                    album.setPictureCount(album.getPictureCount() + 1);
//                    photoAlbumBean.count++;
                    PictureBean picture = new PictureBean();
                    picture.setId(Long.parseLong(_id));
                    picture.setPath(path);
                    picture.setThumbnailPath(thumbnailList.get(_id));
                    album.getPictureList().add(picture);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cur.moveToNext());
        }

//        Iterator<Map.Entry<String, AlbumBean>> itr = mAlbumList.entrySet().iterator();
//        while (itr.hasNext()) {
//            Map.Entry<String, AlbumBean> entry = itr.next();
//            AlbumBean album = entry.getValue();
//            for (int i = 0; i < album.getPictureList().size(); ++i) {
//                PictureBean image = album.getPictureList().get(i);
//            }
//        }

        hasBuildAlbumList = true;
        long endTime = System.currentTimeMillis();
        cur.close();
        Log.e("jimwind", "use time: " + (endTime - startTime) + " ms");
    }
    // 缩略图列表
    private HashMap<String, String> thumbnailList = new HashMap<>();
    /**
     * 得到缩略图
     */
    private void getThumbnail() {
        String[] projection = { MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID,
                MediaStore.Images.Thumbnails.DATA };
        Cursor cursor = contentResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                projection, null, null, null);
        getThumbnailColumnData(cursor);
        cursor.close();
    }

    /**
     * 从数据库中得到缩略图
     *
     * @param cur
     */
    private void getThumbnailColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int _id;
            int image_id;
            String image_path;
            int _idColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails._ID);
            int image_idColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.DATA);

            do {
                // Get the field values
                _id = cur.getInt(_idColumn);
                image_id = cur.getInt(image_idColumn);
                image_path = cur.getString(dataColumn);

                // Do something with the values.
                // Log.i(TAG, _id + " image_id:" + image_id + " path:"
                // + image_path + "---");
                // HashMap<String, String> hash = new HashMap<String, String>();
                // hash.put("image_id", image_id + "");
                // hash.put("path", image_path);
                // thumbnailList.add(hash);
                thumbnailList.put(String.valueOf(image_id), image_path);
            } while (cur.moveToNext());
        }
    }
}
