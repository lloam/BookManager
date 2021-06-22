package com.mao.bookmanage.entity;

/**
 * Created by Administrator on 2021/6/12.
 */

public class Share {
    private int _id;
    private String username;
    private String bookName;

    public Share(String username, String bookName) {
        this.username = username;
        this.bookName = bookName;
    }

    public int get_id() {
        return _id;
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
}
