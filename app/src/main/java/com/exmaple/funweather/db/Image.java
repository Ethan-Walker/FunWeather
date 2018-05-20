package com.exmaple.funweather.db;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * Created by EthanWalker on 2017/6/17.
 */

public class Image extends DataSupport{

    private int imgId;

    private int imgUrl;

    private String imgName;

    private   ArrayList<Integer> commentId;

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(int imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public ArrayList<Integer> getCommentId() {
        return commentId;
    }

    public void setCommentId(ArrayList<Integer> commentId) {
        this.commentId = commentId;
    }

    public Image(int imgId, int imgUrl, String imgName, ArrayList<Integer> commentId) {
        this.imgId = imgId;
        this.imgUrl = imgUrl;
        this.imgName = imgName;
        this.commentId = commentId;
    }
}
