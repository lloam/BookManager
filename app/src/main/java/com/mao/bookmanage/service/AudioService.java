package com.mao.bookmanage.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.mao.bookmanage.IndexActivity;
import com.mao.bookmanage.R;

/**
 * Created by Administrator on 2021/6/12.
 */

public class AudioService extends Service {
    private SurfaceHolder surfaceHolder;
    private MediaPlayer mediaPlayer;
    private final IBinder mBinder = new LocalBinder();
    public class LocalBinder extends Binder
    {
        public AudioService getService() {
            //返回AudioService类的对象
            return AudioService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        if (this.mediaPlayer.isPlaying())
        {
            this.mediaPlayer.stop();
        }
        this.mediaPlayer.release();
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mediaPlayer = MediaPlayer.create(this, R.raw.book);
    }

    public boolean isPlay() {
        return this.mediaPlayer.isPlaying();
    }

    public void stop() {
        mediaPlayer.pause();
        mediaPlayer.seekTo(0);//将播放位置置于开始处
    }

    public void play() {
        mediaPlayer.setDisplay(IndexActivity.surfaceView.getHolder());
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void seekTo(int current) {
        //指定播放位置(以毫秒为单位)
        mediaPlayer.seekTo(current);
    }

    public int getDuration() {
        return mediaPlayer.getDuration();//获得播放文件的时间
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }
}
