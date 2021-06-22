package com.mao.bookmanage.tools;

import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mao.bookmanage.entity.User;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2021/6/12.
 */
// 单例模式
public class JsonParse {
    private static JsonParse instance;
    private JsonParse() {
    }

    public static JsonParse getInstanse(){
        if(instance == null){
            synchronized (JsonParse.class){
                if(instance == null){
                    return instance = new JsonParse();
                }
            }
        }
        return instance;
    }

    public List<User> getUserList(String json){
        Type userType = new TypeToken<List<User>>(){}.getType();
        return new Gson().fromJson(json,userType);
    }
}
