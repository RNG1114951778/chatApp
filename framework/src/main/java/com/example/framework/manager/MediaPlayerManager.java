package com.example.framework.manager;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.framework.utils.LogUtils;

import java.io.IOException;


public class MediaPlayerManager {


    //播放
    public static final int MEDIA_STATUS_PLAY = 0;
    //暂停
    public static final int MEDIA_STATUS_PAUSE = 1;
    //停止
    public static final int MEDIA_STATUS_STOP= 2;
    //当前状态
    public int MEDIA_STATUS = MEDIA_STATUS_STOP;

    private MediaPlayer mMediaPlayer;

    private OnMusicProgressListener musicProgressListener;

    private static final int H_PROGRESS = 1000;


    //计算歌曲进度
    //1 开始播放时计算市场
    //2 将进度结果向外抛出
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            switch (msg.what){

                case H_PROGRESS:
                    if(musicProgressListener != null){
                        int currentPosition = getCurrentPosition();

                        int pos = (int)(((float)currentPosition) / ((float)getDuration())*100);

                        musicProgressListener.OnProgress(currentPosition,pos);
                        mHandler.sendEmptyMessageDelayed(H_PROGRESS,1000);

                    }
            }

            return false;
        }

    });

    public MediaPlayerManager(){

        mMediaPlayer = new MediaPlayer();

    }


    public boolean isPlaying(){

        return mMediaPlayer.isPlaying();
    }



    public void startPlay(AssetFileDescriptor path){

        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            MEDIA_STATUS = MEDIA_STATUS_PLAY;
            mHandler.sendEmptyMessage(H_PROGRESS);
        }
        catch (IOException e)
        {
            LogUtils.e(e.toString());
            e.printStackTrace();
        }
    }

    public void pausePlay(){
        if(isPlaying()) {


            mMediaPlayer.pause();
            MEDIA_STATUS =MEDIA_STATUS_PAUSE;
            mHandler.removeMessages(H_PROGRESS);

        }
    }


    public void continuePlay(){

        mMediaPlayer.start();
        MEDIA_STATUS = MEDIA_STATUS_PLAY;
        mHandler.sendEmptyMessage(H_PROGRESS);
    }



    public void stopPlay(){
        mMediaPlayer.stop();
        MEDIA_STATUS = MEDIA_STATUS_STOP;
        mHandler.removeMessages(H_PROGRESS);

    }


    //获取当前位置
    public int getCurrentPosition(){

        return mMediaPlayer.getCurrentPosition();

    }


    //总时长
    public int getDuration(){

        return mMediaPlayer.getDuration();

    }

    public void setLooping(boolean isLooping){
        mMediaPlayer.setLooping(isLooping);
    }

    //监听结束
    public void  setOnCompleteListner(MediaPlayer.OnCompletionListener listener){

        mMediaPlayer.setOnCompletionListener(listener);

    }


    //播放错误
    public void  setOnErrorListener(MediaPlayer.OnErrorListener listener){

        mMediaPlayer.setOnErrorListener(listener);

    }


    //跳转
    public void seekTo(int ms){

        mMediaPlayer.seekTo(ms);

    }


    //进度

    public void  setOnProgressListener(OnMusicProgressListener listener){

        musicProgressListener = listener;

    }


    //进度接口
    public interface OnMusicProgressListener{

        void OnProgress(int progress,int pos);

    }

}
