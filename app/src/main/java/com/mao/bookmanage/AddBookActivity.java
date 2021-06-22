package com.mao.bookmanage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.mao.bookmanage.entity.Book;
import com.mao.bookmanage.tools.DataBaseHandler;
import com.mao.bookmanage.tools.FileHelper;
import com.mao.bookmanage.tools.ToastUtils;

import java.io.InputStream;

public class AddBookActivity extends Activity implements View.OnClickListener {
    private ImageView add_book_image;
    private Button add_book_image_button;
    private EditText add_book_name;
    private EditText add_book_author;
    private EditText add_book_category;
    private EditText add_book_info;
    private Button add_book_button;
    private DataBaseHandler db;
    private Bitmap selectedImage = null;
    private final FileHelper fileHelper = FileHelper.getInstance();
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        initView();
        Intent intent = getIntent();
        id = intent.getIntExtra("id",-1);
        if(id != -1) {
            Book book = db.queryBookById(id);
            add_book_name.setText(book.get_bookName());
            add_book_author.setText(book.get_author());
            add_book_category.setText(book.get_category());
            add_book_info.setText(book.get_info());
            add_book_image.setImageBitmap(fileHelper.loadImageBitmap(AddBookActivity.this,book.get_id()+""));
            selectedImage = fileHelper.loadImageBitmap(AddBookActivity.this,book.get_id()+"");
            add_book_button.setText("更新书籍信息");
        }

        add_book_image_button.setOnClickListener(this);
        add_book_button.setOnClickListener(this);
    }
    void initView(){
        add_book_image = findViewById(R.id.add_book_image);
        add_book_name = findViewById(R.id.add_book_name);
        add_book_author = findViewById(R.id.add_book_author);
        add_book_category = findViewById(R.id.add_book_category);
        add_book_info = findViewById(R.id.add_book_info);
        add_book_image_button = findViewById(R.id.add_book_image_button);
        add_book_button = findViewById(R.id.add_book_button);
        db = DataBaseHandler.getInstance(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_book_image_button:
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto,1);
                break;
            case R.id.add_book_button:
//                Log.wtf("id是：", String.valueOf(id));
//                ToastUtils.showToast(this,"hh");
                Book book = new Book();
                book.set_bookName(add_book_name.getText().toString());
                book.set_author(add_book_author.getText().toString());
                book.set_category(add_book_category.getText().toString());
                book.set_info(add_book_info.getText().toString());
                if(id == -1){
                    id = db.addBook(book);
//                    Log.wtf("添加后的id", String.valueOf(id));
                    if(id != -1){
                        ToastUtils.showToast(this,"添加成功");
                    }else {
                        ToastUtils.showToast(this, "添加失败");
                    }
                }else {
                    book.set_id(id);
                    boolean success = db.updateBook(book);
                    Log.wtf("更新后id是", String.valueOf(book.get_id()));
                    if(success){
                        ToastUtils.showToast(this,"更新成功");
                    }else {
                        ToastUtils.showToast(this, "更新失败");
                    }
                }
                fileHelper.saveImage(AddBookActivity.this,selectedImage,id + "");
                toIndexActivity();
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            final Uri imageUri = data.getData();
            final InputStream imageStream;
            try{
                imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                add_book_image.setImageBitmap(selectedImage);
            } catch(Exception e) {
                Log.wtf("上传图片错误",e.toString());
            }
        }
    }
    public void toIndexActivity(){
        Intent intent = new Intent(AddBookActivity.this, IndexActivity.class);
        startActivity(intent);
    }
}

