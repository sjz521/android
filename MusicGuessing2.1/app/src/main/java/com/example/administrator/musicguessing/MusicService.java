package com.example.administrator.musicguessing;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;


public class MusicService extends Service {
    public static final String INDEX_STONE_CANCEL = "cancel.mp3";
    public static final String INDEX_STONE_ENTER = "enter.mp3";
    public static final String INDEX_STONE_COIN = "coin.mp3";
    private static final String TAG = MusicService.class.getSimpleName();

    private MediaPlayer mediaPlayer;

    public MusicService() {
    }

    public class MyBindle extends Binder{


        public void play(Context context,String songName){MusicService.this.play(context,songName);}


        public void stop(){
            MusicService.this.stop();
        }
        
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        return new MyBindle();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    public void play(Context context,String songFileName){
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }
        mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor fileDescriptor = context.getAssets().openFd(songFileName);
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),fileDescriptor.getLength());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();

    }


    public void stop(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }

        mediaPlayer.release();//不加这句的话会内存泄露，下面3首歌放不出来
        mediaPlayer = null;
    }
}
