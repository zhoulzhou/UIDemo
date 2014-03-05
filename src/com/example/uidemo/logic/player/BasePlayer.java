package com.example.uidemo.logic.player;

import android.content.Context;
import android.media.MediaPlayer;

public class BasePlayer {
	protected MediaPlayer mMediaPlayer;
	protected Song mSong;
	protected long mSongId = -1;
	
	protected boolean mIsPrepared;
	
	public static final int ERROR_INTERNAL = -101;
	
	protected onPreparedListener mOnPreparedListener;
	protected onSeekCompletedListener mOnSeekCompletedListener;
	protected onBufferingUpdateListener mOnBufferingUpdateListener;
	protected onErrorListener mOnErrorListener;
	protected onCompletionListener mOnCompletionListener;
	protected onStartPlayListener mOnStartPlayListener;
	protected onPlayStateChangedListener mOnPlayStateChangedListener;

	public interface onPreparedListener{
		public void onPrepared();
	}
	
	public interface onSeekCompletedListener{
		public void onSeekCompleted();
	}
	
	public interface onBufferingUpdateListener{
		public void onBufferingUpdate(int percent);
	}
	
	public interface onErrorListener{
		public void onError(int error);
	}
	
	public interface onCompletionListener{
		public void onCompletion();
	}
	
	public interface onStartPlayListener{
		public void onStartPlay(Song song);
	}
	
	public interface onPlayStateChangedListener{
		public void onPlayStateChanged();
	}
	
	public void setOnPreparedListener(onPreparedListener prepareListener){
		mOnPreparedListener = prepareListener;
	}
	
	public void setOnSeekCompletedListener(onSeekCompletedListener seekListener){
		mOnSeekCompletedListener = seekListener;
	}
	
	public void setOnBufferingUpdateListener(onBufferingUpdateListener bufferingListener){
		mOnBufferingUpdateListener  = bufferingListener;
	}
	
	public void setOnErrorListener(onErrorListener errorListener){
		mOnErrorListener = errorListener;
	}
	
	public void setOnCompletionListener(onCompletionListener completionListener){
		mOnCompletionListener = completionListener;
	}
	
	public void setOnStartPlayListener(onStartPlayListener startplayListener){
		mOnStartPlayListener = startplayListener;
	}
	
	public void setOnPlayStateChangedListener(onPlayStateChangedListener statechangeListener){
		mOnPlayStateChangedListener = statechangeListener;
	}
	
	public BasePlayer(){
		mMediaPlayer = new MediaPlayer();
	}
	
	public int getCurrentPosition(){
		return mMediaPlayer.getCurrentPosition();
	}
	
	public int getDuration(){
		return mMediaPlayer.getDuration();
	}
	
	public boolean isPlaying(){
		return mMediaPlayer.isPlaying();
	}
	
	public boolean isPrepared(){
		return mIsPrepared;
	}
	
	public void pause(){
		mMediaPlayer.pause();
	}
	
	public void prepareAsync(){
		mMediaPlayer.prepareAsync();
	}
	
	public void release(){
		mMediaPlayer.release();
	}
	
	public void reset(int reason){
		mMediaPlayer.reset();
		mSong = null;
		mSongId = -1;
	}
	
	public void seekTo(int msec){
		mMediaPlayer.seekTo(msec);
	}
	
	public void setDataSource(Song song){
		mSong = song;
		if(song != null){
			mSongId = song.songId;
		}
	}
	
	public void setVolume(int volume){
		mMediaPlayer.setVolume(volume, volume);
	}
	
	public void setWakeMode(Context context,int mode){
		mMediaPlayer.setWakeMode(context, mode);
	}
	
	public void start(){
		try {
			mMediaPlayer.start();
		} catch (Throwable e) {
			notifyError(ERROR_INTERNAL);
		}
	}
	
	public void stop(){
		mMediaPlayer.stop();
	}
	
	protected void notifyBufferingUpdate(int percent){
		if(mOnBufferingUpdateListener != null){
			mOnBufferingUpdateListener.onBufferingUpdate(percent);
		}
	}
	
	protected void notifyError(int error){
		if(mOnErrorListener != null){
			mOnErrorListener.onError(error);
		}
	}
	
	protected void notifyPlayCompleted(){
		if (mOnCompletionListener != null){
			mOnCompletionListener.onCompletion();
		}
	}
	
	protected void notifySeekCompleted(){
		if(mOnSeekCompletedListener != null){
			mOnSeekCompletedListener.onSeekCompleted();
		}
	}
	
	protected void notifyStartPlay(){
		if(mOnStartPlayListener != null){
			mOnStartPlayListener.onStartPlay(mSong);
		}
	}
	
	protected void notifyPlayStateChanged(){
		if(mOnPlayStateChangedListener != null) {
			mOnPlayStateChangedListener.onPlayStateChanged();
		}
	}
}