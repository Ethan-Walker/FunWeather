package com.exmaple.funweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by EthanWalker on 2017/6/17.
 */

public class Comment extends DataSupport {

    private int commentId;

    private String content;

    private String  username;

    private int imgId;

    public int getImgId() {
        return imgId;
    }

    public Comment(int commentId, String content, String username, int imgId) {
        this.commentId = commentId;
        this.content = content;
        this.username = username;
        this.imgId = imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
