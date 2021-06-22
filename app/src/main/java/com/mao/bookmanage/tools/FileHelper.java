package com.mao.bookmanage.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2021/6/11.
 */

public class FileHelper {

    private static FileHelper instance;

    private FileHelper() {
    }
    public static FileHelper getInstance(){
        if(instance == null){
            synchronized (JsonParse.class){
                if(instance == null){
                    instance = new FileHelper();
                }
            }
        }
        return instance;
    }
    public void saveImage(Context context, Bitmap bitmap, String name){
        name = name + ".png";
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
            Log.wtf("进入了保存图片的方法",name);
//            Log.d("路径是", context.getFileStreamPath("name").toString());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            Log.wtf("保存图片出错",e.toString());
            Log.wtf("图片有错吗",bitmap.toString());
        }
    }
    public Bitmap loadImageBitmap(Context context,String name){
        name = name + ".png";
        FileInputStream fileInputStream;
        Bitmap bitmap = null;
        try{
            fileInputStream = context.openFileInput(name);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
