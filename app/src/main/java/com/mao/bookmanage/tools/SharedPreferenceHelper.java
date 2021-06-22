package com.mao.bookmanage.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2021/6/12.
 */

public class SharedPreferenceHelper {
    private static SharedPreferenceHelper instance;
    private Context mContext;
    private SharedPreferences sp;

    private SharedPreferenceHelper(Context mContext) {
        this.mContext = mContext;
        this.sp = mContext.getSharedPreferences("user",mContext.MODE_PRIVATE);
    }
    public static SharedPreferenceHelper getInstance(Context mContext){
        if(instance == null){
            synchronized (SharedPreferenceHelper.class){
                if(instance == null){
                    instance = new SharedPreferenceHelper(mContext);
                }
            }
        }
        return instance;
    }

    /**
     * 在 SharedPreferenceHelper 保存用户名
     * @param username
     */
    public void saveUsername(String username){
        SharedPreferences.Editor edit = sp.edit();
        // 先判断是否存在这个用户名
        String userN= sp.getString("username", null);
        if("".equals(userN)){
            edit.putString("username",username);
            edit.commit();
        }else {
            edit.clear();
            edit.putString("username",username);
            edit.commit();
        }
    }

    /**
     * 从 SharedPreferences 中将用户名读出
     * @return
     */
    public String getUsername(){
        String username = sp.getString("username", null);
        return username;
    }

    /**
     * 删除 SharedPreferences 中的username数据，实现注销功能
     */
    public void deleteUsername(){
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.commit();
    }
}
