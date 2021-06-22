package com.mao.bookmanage;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mao.bookmanage.entity.User;
import com.mao.bookmanage.tools.Constants;
import com.mao.bookmanage.tools.JsonParse;
import com.mao.bookmanage.tools.SharedPreferenceHelper;
import com.mao.bookmanage.tools.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity {
    private EditText mUsername;
    private EditText mPassword;
    private Button login;
    private SharedPreferenceHelper sharedPreferenceHelper;
    private static final int UPDATE_UI = 1;// handler更新UI
    private static final int ERROR = 2;// handler更新UI
    private List<User> userList;// 连接远程获取用户数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        rememberMe();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
    }
    void initView(){
        mUsername = findViewById(R.id.username);
        mPassword = findViewById(R.id.password);
        login = findViewById(R.id.login);
        sharedPreferenceHelper = SharedPreferenceHelper.getInstance(this);
    }
    /**
     * 获取用户数据
     */
    void initData(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(Constants.WEB_SITE).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.wtf("失败",e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String userJson = response.body().string();
//                        Log.wtf("json",userJson);
                userList = JsonParse.getInstanse().getUserList(userJson);
                // 验证用户是否登录成功
                verifyUser();
            }
        });
    }
    /**
     * 验证用户登录是否成功
     */
    void verifyUser(){
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        if("".equals(username) || "".equals(password)) {
            Message message = new Message();
            message.what  = UPDATE_UI;
            handler.sendMessage(message);
        } else{
            boolean success = false;
            for (User user : userList) {
                if(username.equals(user.getUsername())&&password.equals(user.getPassword())){
                    success = true;
                }
            }
            if(success){
                sharedPreferenceHelper.saveUsername(username);
                Intent intent = new Intent(MainActivity.this, IndexActivity.class);
                startActivity(intent);
            }else {
                Message message = new Message();
                message.what  = ERROR;
                handler.sendMessage(message);
            }
        }
    }

    /**
     * 记住密码功能，下次不用再登陆
     */
    void rememberMe(){
        String username = sharedPreferenceHelper.getUsername();
        if(username != null){
//            Log.wtf("结果",username);
            Intent intent = new Intent(MainActivity.this, IndexActivity.class);
            startActivity(intent);
        }

    }
    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE_UI:
                    ToastUtils.showToast(MainActivity.this,"请输入用户名或密码");
                    break;
                case ERROR:
                    ToastUtils.showToast(MainActivity.this,"用户名或密码错误");
                    break;
            }
        }
    };
}
