package com.mao.bookmanage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mao.bookmanage.adapter.CommentAdapter;
import com.mao.bookmanage.entity.Book;
import com.mao.bookmanage.entity.Comment;
import com.mao.bookmanage.entity.Share;
import com.mao.bookmanage.tools.DataBaseHandler;
import com.mao.bookmanage.tools.FileHelper;
import com.mao.bookmanage.tools.SharedPreferenceHelper;
import com.mao.bookmanage.tools.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class BookContentActivity extends Activity {
    private Book book;// 首页传递过来的书籍
    private Button book_content_update_button;
    private Button book_content_delete_button;
    private ImageView book_content_image;
    private TextView book_content_bookName;
    private TextView book_content_author;
    private TextView book_content_category;
    private TextView book_content_info;
    private TextView book_content_share_count;
    private TextView book_content_comment_count;
    private TextView book_content_comment_self;
    private TextView book_content_share_button;
    private TextView book_content_comment_button;
    private ListView book_content_comment_listView;
    private int bookId;
    private DataBaseHandler db;
    private FileHelper fileHelper;
    private SharedPreferenceHelper sharedPreferenceHelper;
    private String username;// 用户名

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_content);
        Intent intent = getIntent();
        bookId = intent.getIntExtra("id", -1);
        initView();
        // 显示书籍内容
        showBookContent();
        // 显示评论
        showComment();
        book_content_comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book_content_comment_button_onclick();
            }
        });
        book_content_delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book_content_delete_button_onclick();
            }
        });
        book_content_update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book_content_update_button_onclick();
            }
        });
        book_content_share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book_content_share_button_onclick();
            }
        });
    }

    /**
     * 初始化组件
     */
    void initView(){
        book_content_update_button = findViewById(R.id.book_content_update_button);
        book_content_delete_button = findViewById(R.id.book_content_delete_button);
        book_content_image = findViewById(R.id.book_content_image);
        book_content_bookName = findViewById(R.id.book_content_bookName);
        book_content_author = findViewById(R.id.book_content_author);
        book_content_category = findViewById(R.id.book_content_category);
        book_content_info = findViewById(R.id.book_content_info);
        book_content_share_count = findViewById(R.id.book_content_share_count);
        book_content_comment_count = findViewById(R.id.book_content_comment_count);
        book_content_comment_self = findViewById(R.id.book_content_comment_self);
        book_content_share_button = findViewById(R.id.book_content_share_button);
        book_content_comment_button = findViewById(R.id.book_content_comment_button);
        book_content_comment_listView = findViewById(R.id.book_content_comment_listView);
        db = DataBaseHandler.getInstance(this);
        fileHelper = FileHelper.getInstance();
        sharedPreferenceHelper = SharedPreferenceHelper.getInstance(this);
        book = db.queryBookById(bookId);
        username = sharedPreferenceHelper.getUsername();
        // 判断当前用户是否分享该书，以便设置分享按钮文本
        boolean share = db.isShare(username, book.get_bookName());
        if(share){
            book_content_share_button.setText("取消分享");
        }
    }

    /**
     * 显示书籍内容
     */
    public void showBookContent(){
        /**************书籍相关*****************/
        book_content_image.setImageBitmap(
                fileHelper.loadImageBitmap(BookContentActivity.this,book.get_id()+"")
        );
        book_content_bookName.setText(book.get_bookName());
        book_content_author.setText(book.get_author());
        book_content_category.setText(book.get_category());
        book_content_info.setText(book.get_info());
        // /**************分享相关***************/
        showShareCount();// 显示分享数
        /**************评论相关***************/
        showCommentCount();// 显示评论数
    }

    /**
     * 书记更新操作点击事件，去更新书籍的页面
     */
    public void book_content_update_button_onclick(){
        Intent intent = new Intent(BookContentActivity.this, AddBookActivity.class);
        intent.putExtra("id",bookId);
        startActivity(intent);
    }
    /**
     * 书籍删除操作点击事件
     */
    public void book_content_delete_button_onclick(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(BookContentActivity.this);
        builder.setTitle("删除书籍");
        builder.setIcon(R.drawable.delete);
        builder.setMessage("确定要删除这本书籍吗？？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean success = db.deleteBook(bookId);
                if(success){
                    ToastUtils.showToast(BookContentActivity.this,"删除成功");
                    Intent intent = new Intent(BookContentActivity.this, IndexActivity.class);
                    startActivity(intent);
                }else {
                    ToastUtils.showToast(BookContentActivity.this,"删除失败");
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    /**
     * 添加评论操作事件
     */
    public void book_content_comment_button_onclick(){
        String book_comment = book_content_comment_self.getText().toString();// 评论内容
        if("".equals(book_comment)){
            ToastUtils.showToast(BookContentActivity.this,"不能评论空哦！");
        }else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:sss");
            Date date = new Date();
            String commentTime = simpleDateFormat.format(date);// 评论时间
            String bookName = book.get_bookName();
            Comment comment = new Comment(username, bookName, commentTime, book_comment);
            boolean success = db.addComment(comment);
            if(success){
                ToastUtils.showToast(BookContentActivity.this,"评论成功！");
                showCommentCount();
                showComment();
            }else {
                ToastUtils.showToast(BookContentActivity.this,"评论错误，失败！");
            }
        }

    }
    /**
     * 显示全部评论
     */
    public void showComment(){
        List<Comment> commentList = db.getAllComment(book.get_bookName());
        CommentAdapter commentAdapter = new CommentAdapter(commentList, BookContentActivity.this);
        book_content_comment_listView.setAdapter(commentAdapter);
    }

    /**
     * 显示评论数
     */
   public void showCommentCount(){
        /**************评论相关***************/
        List<Comment> commentList = db.getAllComment(book.get_bookName());// 查询评论数
        int commentCount = commentList.size();
//       Log.wtf(book.get_bookName()+"这本书的评论数为", String.valueOf(commentCount));
        book_content_comment_count.setText(String.valueOf(commentCount));
    }

    /**
     * 显示分享数
     */
    public void showShareCount(){
        /**************分享相关***************/
        int shareCount = db.getShareCount(book.get_bookName());// 查询分享数
//        Log.wtf("此时的分享数为",String.valueOf(shareCount));
        book_content_share_count.setText(String.valueOf(shareCount));
    }

    /**
     * 分享按钮操作事件
     */
    public void book_content_share_button_onclick(){
        String isShare = book_content_share_button.getText().toString();
        if("分享".equals(isShare)){
            boolean success = db.addShare(new Share(username, book.get_bookName()));
            if(success){
                book_content_share_button.setText("取消分享");
                showShareCount();// 改变分享数
                ToastUtils.showToast(BookContentActivity.this,"分享成功");
            }else {
                ToastUtils.showToast(BookContentActivity.this,"分享失败");
            }
        }else if("取消分享".equals(isShare)){
            // 分享了，现在点击表示取消分享
            db.deleteShare(username, book.get_bookName());
            book_content_share_button.setText("分享");
            showShareCount();// 改变分享数
        }
    }
}
