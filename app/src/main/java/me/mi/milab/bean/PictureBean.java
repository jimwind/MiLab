package me.mi.milab.bean;

import java.io.Serializable;

/**
 * Created by mi.gao on 2017/12/8.
 */

public class PictureBean implements Serializable {
    private long id;
    private String thumbnailPath;
    private String path;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
