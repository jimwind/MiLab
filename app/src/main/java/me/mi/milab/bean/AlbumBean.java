package me.mi.milab.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mi.gao on 2017/12/8.
 */

public class AlbumBean implements Serializable {
    private long id;
    private int pictureCount;
    private String name;
    private ArrayList<PictureBean> pictureList;
    private long coverPictureId;
    private String coverPicturePath;
    private String coverPictureName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPictureCount() {
        return pictureCount;
    }

    public void setPictureCount(int pictureCount) {
        this.pictureCount = pictureCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<PictureBean> getPictureList() {
        return pictureList;
    }

    public void setPictureList(ArrayList<PictureBean> pictureList) {
        this.pictureList = pictureList;
    }

    public long getCoverPictureId() {
        return coverPictureId;
    }

    public void setCoverPictureId(long coverPictureId) {
        this.coverPictureId = coverPictureId;
    }

    public String getCoverPicturePath() {
        return coverPicturePath;
    }

    public void setCoverPicturePath(String coverPicturePath) {
        this.coverPicturePath = coverPicturePath;
    }

    public String getCoverPictureName() {
        return coverPictureName;
    }

    public void setCoverPictureName(String coverPictureName) {
        this.coverPictureName = coverPictureName;
    }
}
