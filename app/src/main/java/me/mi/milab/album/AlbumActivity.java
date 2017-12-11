package me.mi.milab.album;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import me.mi.milab.R;
import me.mi.milab.bean.AlbumBean;

/**
 * Created by mi.gao on 2017/12/8.
 *
 */

public class AlbumActivity extends AppCompatActivity {
    private GridView gvAlbums;
    private AlbumHelper mAlbumHelper;
    private ArrayList<AlbumBean> mAlbumList;//相册集合
    private AlbumAdapter mAlbumAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        gvAlbums = (GridView) findViewById(R.id.albums);
        mAlbumHelper = AlbumHelper.getInstance();
        mAlbumHelper.init(getApplicationContext());
        new GetAlbumTask().execute();
    }
    class GetAlbumTask extends AsyncTask<Void, Integer, ArrayList<AlbumBean>> {

        @Override
        protected ArrayList<AlbumBean> doInBackground(Void... params) {
            mAlbumList = mAlbumHelper.getAlbumList();
            return mAlbumList;
        }

        @Override
        protected void onPostExecute(ArrayList<AlbumBean> result) {
            super.onPostExecute(result);
            mAlbumAdapter = new AlbumAdapter(AlbumActivity.this, result);
            gvAlbums.setAdapter(mAlbumAdapter);
        }
    }
    class AlbumAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<AlbumBean> mAlbumList = new ArrayList<>();//相册集合
        public AlbumAdapter(Context context, ArrayList<AlbumBean> albumList) {
            this.context = context;
            if(albumList != null){
                mAlbumList.addAll(albumList);
            }

        }
        @Override
        public int getCount() {
            return mAlbumList.size();
        }

        @Override
        public Object getItem(int position) {
            if(position < mAlbumList.size()) {
                return mAlbumList.get(position);
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(context, R.layout.activity_album_item, null);
                holder.cover = (ImageView) convertView.findViewById(R.id.album_cover);
                holder.name = (TextView) convertView.findViewById(R.id.album_name);
                holder.count = (TextView) convertView.findViewById(R.id.picture_count);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            AlbumBean item = mAlbumList.get(position);
            holder.count.setText(" (" + item.getPictureCount()+")");
            holder.name.setText(item.getName());
            if (item.getPictureList() != null && item.getPictureList().size() > 0) {
                String sourcePath = item.getPictureList().get(0).getPath();
                holder.cover.setTag(sourcePath);
//                bitmapUtils.display(holder.imageView, sourcePath);
//                holder.cover.setImageURI(Uri.fromFile(new File(sourcePath)));
            } else {
                holder.cover.setImageBitmap(null);
            }
            return convertView;
        }
        class ViewHolder {
            private ImageView cover;//相册图片缩略图
            private TextView name;//相册名称
            private TextView count;//照片数量
        }

    }
}
