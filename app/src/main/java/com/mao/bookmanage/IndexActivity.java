package com.mao.bookmanage;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.mao.bookmanage.adapter.BookAdapter;
import com.mao.bookmanage.entity.Book;
import com.mao.bookmanage.service.AudioService;
import com.mao.bookmanage.tools.DataBaseHandler;
import com.mao.bookmanage.tools.SharedPreferenceHelper;
import com.mao.bookmanage.tools.ToastUtils;

import java.util.List;

public class IndexActivity extends Activity {
    /************视频相关****************/
    private AudioService audioService = null;
    private SeekBar audio_seekbar; //播放进度条
    private Handler handler = new Handler();
    private boolean isUpdateSeekbar = true; //是否更新播放进度条
    public static SurfaceView surfaceView;
    private ImageView playImage;
    /************自己就要的按钮相关****************/
    private ListView bookListView;
    private Button to_add_book_button;
    private Spinner book_query_spinner;
    private EditText book_query_name;
    private Button book_query_button;
    private Button display_allBook_button;
    private DataBaseHandler db;
    private SharedPreferenceHelper sharedPreferenceHelper;
    /************菜单相关****************/
    private MenuItem username;
    private MenuItem update;
    private MenuItem exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initView();
        db = DataBaseHandler.getInstance(this);
        final List<Book> bookList = db.getAllBooks();
        Context mContext = IndexActivity.this;
        BookAdapter bookAdapter = new BookAdapter(bookList, mContext);
        bookListView.setAdapter(bookAdapter);
        to_add_book_onclick();
        book_query_button_onclick();
        display_allBook_button_onclick();
        bookListViewOnclick();

        final Intent serviceIntent = new Intent(this, AudioService.class);
        bindService(serviceIntent, mConnection, Context.BIND_AUTO_CREATE);
        setListener(); //设置各按钮的监听器
        handler.post(updateThread);
    }
    void initView(){
        bookListView = this.findViewById(R.id.book_item);
        to_add_book_button = findViewById(R.id.to_add_book_button);
        book_query_spinner = findViewById(R.id.book_query_spinner);
        book_query_name = findViewById(R.id.book_query_name);
        book_query_button = findViewById(R.id.book_query_button);
        display_allBook_button  = findViewById(R.id.display_allBook_button);
        sharedPreferenceHelper = SharedPreferenceHelper.getInstance(IndexActivity.this);
        /************视频相关****************/
        this.surfaceView = (SurfaceView)findViewById(R.id.sf);
        this.audio_seekbar = (SeekBar)findViewById(R.id.audio_seekbar);
        playImage = findViewById(R.id.playImage);

    }
    // 将各个按钮事件封装

    /**
     * 去添加书籍
     */
    void to_add_book_onclick(){
        // 添加按钮
        to_add_book_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexActivity.this, AddBookActivity.class);
                intent.putExtra("id",-1);
                startActivity(intent);
            }
        });
    }

    /**
     * 查询书籍
     */
    void book_query_button_onclick(){
        // 查询按钮
        book_query_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 获取spinner里的值，知道查询的类型==通过作者或者书籍名
                int selectedItemPosition = book_query_spinner.getSelectedItemPosition();
                String[] spinnerArray = getResources().getStringArray(R.array.spinner);
                String queryType = spinnerArray[selectedItemPosition];
                // 获取查询条件
                String queryName = book_query_name.getText().toString();
                // 执行查询操作
                if("作者名".equals(queryType)){
                    List<Book> bookListAuthor = db.queryBookByAuthor(queryName);
                    // 如果没有该作者的书籍
                    if(bookListAuthor.size() == 0){
                        ToastUtils.showToast(IndexActivity.this,"没有该作者的书籍");
                    }else {
                        BookAdapter bookAdapterAuthor = new BookAdapter(bookListAuthor, IndexActivity.this);
                        bookListView.setAdapter(bookAdapterAuthor);
                    }
                }else if("书籍名".equals(queryType)){
                    List<Book> bookListBookName = db.queryBookByBookName(queryName);
                    BookAdapter bookAdapterBookName = new BookAdapter(bookListBookName, IndexActivity.this);
                    bookListView.setAdapter(bookAdapterBookName);
                }else {
                    List<Book> bookListCategory = db.queryBookByBookCategory(queryName);
                    BookAdapter bookAdapterCategory = new BookAdapter(bookListCategory, IndexActivity.this);
                    bookListView.setAdapter(bookAdapterCategory);
                }
            }
        });
    }

    /**
     * 显示全部书籍
     */
    void display_allBook_button_onclick(){
        // 显示全部书籍按钮
        display_allBook_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<Book> bookList = db.getAllBooks();
                Context mContext = IndexActivity.this;
                BookAdapter bookAdapter = new BookAdapter(bookList, mContext);
                bookListView.setAdapter(bookAdapter);
            }
        });

    }

    void bookListViewOnclick(){
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book) bookListView.getItemAtPosition(position);
                Intent intent = new Intent(IndexActivity.this, BookContentActivity.class);
                intent.putExtra("id",book.get_id());
                startActivity(intent);
            }
        });
    }

    /************视频相关****************/
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        isUpdateSeekbar = false;
        super.onDestroy();
    }

    // 连接服务
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            audioService = ((AudioService.LocalBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            audioService = null;

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            default:
                return false;
        }
    }

    /**
     * 设置监听器
     */
    private void setListener()
    {
        audio_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (audioService !=null) {
                    try {
                        audioService.seekTo(seekBar.getProgress());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        });

        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (audioService !=null) {
                    if (audioService.isPlay()) {
                        audioService.pause();
                        playImage.setVisibility(0x00000000);
                    }
                    else {
                        audioService.play();
                        playImage.setVisibility(0x00000004);
                    }
                }
            }
        });
    }

    private Runnable updateThread = new Runnable() {
        @Override
        public void run() {
            if (audioService != null) {
                try {
                    audio_seekbar.setMax(audioService.getDuration());
                    audio_seekbar.setProgress(audioService.getCurrentPosition());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            //使得线程能够循环进行
            if (isUpdateSeekbar)
                handler.post(updateThread);
        }
    };
    /************视频相关结束****************/
    // 解析菜单资源文件
    @Override
    /**
     * 因为用户名和修改个人信息都在actionBar上，所以要在这里定义
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();// 实例化一个MenuInflater对象
        inflater.inflate(R.menu.menu,menu);//解析菜单文件
        MenuItem username = menu.findItem(R.id.userN);
        final String name = sharedPreferenceHelper.getUsername();
        username.setTitle(name);
        exit = menu.findItem(R.id.exit);
        // 实现注销功能
        exit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sharedPreferenceHelper.deleteUsername();
                Intent intent = new Intent(IndexActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
