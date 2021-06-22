package com.mao.bookmanage.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ListView;

import com.mao.bookmanage.entity.Book;
import com.mao.bookmanage.entity.Comment;
import com.mao.bookmanage.entity.Share;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2021/6/11.
 */

public class DataBaseHandler extends SQLiteOpenHelper {
    private static DataBaseHandler dataBaseHandler;


    private static final int DATABASE_VERSION = 1;
    // book 相关
    private static final String DATABASE_NAME = "bookManager";
    private static final String TABLE_BOOK = "book";
    private static final String KEY_ID = "_id";
    private static final String BOOK_NAME = "bookName";
    private static final String AUTHOR = "author";
    private static final String CATEGORY = "category";
    private static final String INFO = "info";
    // 评论相关
    private static final String TABLE_COMMENT = "comment";
    private static final String USER_NAME = "username";
    private static final String COMMENT = "comment";
    private static final String COMMENT_TIME = "commentTime";
    /*******************分享相关********************/
    private static final String TABLE_SHARE = "isShare";
    // 创建 book 表
    private static final String CREATE_BOOK_SQL = "CREATE TABLE " + TABLE_BOOK + " (" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + BOOK_NAME + " VARCHAR(255)," + AUTHOR + " VARCHAR(255)," +
            CATEGORY + " VARCHAR(255)," + INFO + " TEXT)";
    // 创建评论 comment 表
    private static final String CREATE_COMMENT_SQL = "CREATE TABLE " + TABLE_COMMENT + " (" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_NAME + " VARCHAR(255)," + BOOK_NAME + " VARCHAR(255)," +
            COMMENT_TIME + " VARCHAR(32)," +
            COMMENT + " TEXT)";
    // 创建分享表
    private static final String CREATE_SHARE_SQL = "CREATE TABLE " + TABLE_SHARE + " (" +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + USER_NAME + " VARCHAR(255)," + BOOK_NAME + " VARCHAR(255))";
    private DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static DataBaseHandler getInstance(Context context){
        if(dataBaseHandler == null){
            synchronized (DataBaseHandler.class){
                if(dataBaseHandler == null){
                    dataBaseHandler = new DataBaseHandler(context);
                }
            }
        }
        return dataBaseHandler;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
//        Log.wtf("sql",CREATE_BOOK_SQL);
//        Log.wtf("SQL",CREATE_COMMENT_SQL);
        db.execSQL(CREATE_BOOK_SQL);
        db.execSQL(CREATE_COMMENT_SQL);
        db.execSQL(CREATE_SHARE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHARE);
        // Create tables again
        onCreate(db);
    }
    /******************************** BOOK相关 *************************************/
    /**
     * 查询全部书籍
     * @return
     */
    public List<Book> getAllBooks(){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryBooks = "select * from " + TABLE_BOOK;
        List<Book> bookList = new ArrayList<>();
        Cursor cursor = db.rawQuery(queryBooks, null);
        if(cursor.moveToFirst()){
            do {
                Book book = new Book();
                book.set_id(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                book.set_bookName(cursor.getString(cursor.getColumnIndex(BOOK_NAME)));
                book.set_author(cursor.getString(cursor.getColumnIndex(AUTHOR)));
                book.set_category(cursor.getString(cursor.getColumnIndex(CATEGORY)));
                book.set_info(cursor.getString(cursor.getColumnIndex(INFO)));
                bookList.add(book);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookList;
    }

    /**
     *
     * @param book
     * @return
     */
    public int addBook(Book book){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // 往 values 里面put值
        values.put(BOOK_NAME,book.get_bookName());
        values.put(AUTHOR,book.get_author());
        values.put(CATEGORY,book.get_category());
        values.put(INFO,book.get_info());
        int result = (int)db.insert(TABLE_BOOK, null, values);
        db.close();
        // 插入成功，返回true
        return result;
    }

    /**
     * 根据id 查询书籍
     * @param id
     * @return
     */
    public Book queryBookById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_BOOK, new String[]{},KEY_ID + " = ?",new String[]{String.valueOf(id)}, null, null, null, null);
        Book book = null;
        if(cursor.moveToFirst()){
            // 将结果返回
            book = new Book(
                    cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(BOOK_NAME)),
                    cursor.getString(cursor.getColumnIndex(AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(CATEGORY)),
                    cursor.getString(cursor.getColumnIndex(INFO)));
        }else {
            Log.i("cursor","cursor没有值");
        }
        cursor.close();
        db.close();
        return book;
    }
    /**
     * 根据作者查询书籍
     * @param author
     * @return
     */
    public List<Book> queryBookByAuthor(String author){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_BOOK, new String[]{},AUTHOR + " = ?",new String[]{author}, null, null, null, null);
        List<Book> bookList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
            // 将结果返回
            Book book = new Book(
                    cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(BOOK_NAME)),
                    cursor.getString(cursor.getColumnIndex(AUTHOR)),
                    cursor.getString(cursor.getColumnIndex(CATEGORY)),
                    cursor.getString(cursor.getColumnIndex(INFO)));
                bookList.add(book);
            }while (cursor.moveToNext());
        }else {
            Log.i("cursor","cursor没有值");
        }
        cursor.close();
        db.close();
        return bookList;
    }

    /**
     * 根据名称查询书籍
     * @param bookName
     * @return
     */
    public List<Book> queryBookByBookName(String bookName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_BOOK, new String[]{},BOOK_NAME + " = ?",new String[]{bookName},null, null, null, null);
        List<Book> bookList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                // 将结果返回
                Book book = new Book(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(BOOK_NAME)),
                        cursor.getString(cursor.getColumnIndex(AUTHOR)),
                        cursor.getString(cursor.getColumnIndex(CATEGORY)),
                        cursor.getString(cursor.getColumnIndex(INFO)));
                bookList.add(book);
            }while (cursor.moveToNext());
        }else {
            Log.i("cursor","cursor没有值");
        }
        cursor.close();
        db.close();
        return bookList;
    }

    /**
     * 根据书籍类别查询
     * @param category
     * @return
     */
    public List<Book> queryBookByBookCategory(String category){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_BOOK, new String[]{},CATEGORY + " = ?",new String[]{category},null, null, null, null);
        List<Book> bookList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                // 将结果返回
                Book book = new Book(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(BOOK_NAME)),
                        cursor.getString(cursor.getColumnIndex(AUTHOR)),
                        cursor.getString(cursor.getColumnIndex(CATEGORY)),
                        cursor.getString(cursor.getColumnIndex(INFO)));
                bookList.add(book);
            }while (cursor.moveToNext());
        }else {
            Log.i("cursor","cursor没有值");
        }
        cursor.close();
        db.close();
        return bookList;
    }

    /**
     * 根据id删除书籍
     * @param id
     * @return
     */
    public boolean deleteBook(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_BOOK, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return result != -1;
    }

    /**
     * 更新书记
     * @param book
     * @return
     */
    public boolean updateBook(Book book){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BOOK_NAME,book.get_bookName());
        values.put(AUTHOR,book.get_author());
        values.put(CATEGORY,book.get_category());
        values.put(INFO,book.get_info());
        int updateResult = db.update(TABLE_BOOK, values, KEY_ID + " = ?",
                new String[]{String.valueOf(book.get_id())});
        db.close();
        return updateResult != 0;
    }
    /************************************* 评论相关 ***********************************/
    /**
     * 根据书记查询全部评论
     * @return
     */
    public List<Comment> getAllComment(String bookName){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Comment> commentList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_COMMENT, null,BOOK_NAME + " = ?",
                new String[]{bookName}, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                Comment comment = new Comment(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(USER_NAME)),
                        cursor.getString(cursor.getColumnIndex(BOOK_NAME)),
                        cursor.getString(cursor.getColumnIndex(COMMENT_TIME)),
                        cursor.getString(cursor.getColumnIndex(COMMENT))
                );
                commentList.add(comment);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return commentList;
    }
    /**
     * 添加评论
     * @param comment
     * @return
     */
    public boolean addComment(Comment comment){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // 往 values 里面put值
        values.put(USER_NAME,comment.getUsername());
        values.put(BOOK_NAME,comment.getBookName());
        values.put(COMMENT_TIME,comment.getCommentTime());
        values.put(COMMENT,comment.getComment());
        int result = (int)db.insert(TABLE_COMMENT, null, values);
        db.close();
        // 插入成功，返回true
        return result != -1;
    }
    /**
     * 根据书籍名称查询评论
     * @param bookName
     * @return
     */
    public List<Comment> queryCommentByBookName(String bookName){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Comment> commentList = new ArrayList<>();
        Cursor cursor = db.query(TABLE_COMMENT, null, BOOK_NAME + " = ?",
                new String[]{bookName}, null, null, null, null);
        if(cursor.moveToFirst()){
            do{
                Comment comment = new Comment(
                        cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                        cursor.getString(cursor.getColumnIndex(USER_NAME)),
                        cursor.getString(cursor.getColumnIndex(BOOK_NAME)),
                        cursor.getString(cursor.getColumnIndex(COMMENT_TIME)),
                        cursor.getString(cursor.getColumnIndex(COMMENT))
                );
                commentList.add(comment);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return commentList;
    }

    /**********************分享相关************************/
    /**
     * 添加分享
     * @param share
     * @return
     */
    public boolean addShare(Share share){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // 往 values 里面put值
        values.put(USER_NAME,share.getUsername());
        values.put(BOOK_NAME,share.getBookName());
        int result = (int)db.insert(TABLE_SHARE, null, values);
        db.close();
        // 插入成功，返回true
        return result != -1;
    }

    /**
     * 查询是否分享
     * @param username
     * @param bookName
     * @return
     */
    public boolean isShare(String username,String bookName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SHARE, null, USER_NAME + " = ? and " + BOOK_NAME + " = ?",
                new String[]{username, bookName},null,null,null,null);
        int count = cursor.getCount();
        return count == 1;
    }

    /**
     * 删除分享
     * @param username
     * @param bookName
     * @return
     */
    public boolean deleteShare(String username,String bookName){
        SQLiteDatabase db = this.getWritableDatabase();
        int deleteResult = db.delete(TABLE_SHARE, USER_NAME + " = ? and " + BOOK_NAME + " = ?",
                new String[]{username, bookName});
        db.close();
        return deleteResult != -1;
    }
    /**
     * 查询对应书籍的分享数
     * 返回分享的数量
     * @return
     */
    public int getShareCount(String bookName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SHARE, null, BOOK_NAME + " = ?",
                new String[]{bookName}, null, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

}
