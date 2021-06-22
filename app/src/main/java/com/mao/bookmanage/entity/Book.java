package com.mao.bookmanage.entity;

/**
 * Created by Administrator on 2021/6/11.
 */

public class Book {
    private int _id;
    private String _bookName;
    private String _author;
    private String _category;
    private String _info;
    public Book(int _id, String _bookName, String _author, String _category, String _info) {
        this._id = _id;
        this._bookName = _bookName;
        this._author = _author;
        this._category = _category;
        this._info = _info;
    }

    public Book() {

    }

    public Book(String _bookName, String _author, String _category, String _info) {
        this._bookName = _bookName;
        this._author = _author;
        this._category = _category;
        this._info = _info;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_bookName() {
        return _bookName;
    }

    public void set_bookName(String _bookName) {
        this._bookName = _bookName;
    }

    public String get_author() {
        return _author;
    }

    public void set_author(String _author) {
        this._author = _author;
    }

    public String get_category() {
        return _category;
    }

    public void set_category(String _category) {
        this._category = _category;
    }

    public String get_info() {
        return _info;
    }

    public void set_info(String _info) {
        this._info = _info;
    }
}
