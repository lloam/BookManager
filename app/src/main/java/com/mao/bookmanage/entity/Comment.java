package com.mao.bookmanage.entity;

/**
 * Created by Administrator on 2021/6/12.
 */

public class Comment {
    private int _id;
    private String username;
    private String bookName;
    private String commentTime;
    private String comment;

    public Comment(String username, String bookName, String commentTime, String comment) {
        this.username = username;
        this.bookName = bookName;
        this.commentTime = commentTime;
        this.comment = comment;
    }

    public Comment(int _id, String username, String bookName, String commentTime, String comment) {
        this._id = _id;
        this.username = username;
        this.bookName = bookName;
        this.commentTime = commentTime;
        this.comment = comment;
    }

    public int get_id() {
        return _id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }

}
