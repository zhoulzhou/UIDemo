package com.example.uidemo.logic.player;

import com.example.uidemo.logic.player.BasePlayer.onBufferingUpdateListener;
import com.example.uidemo.logic.player.BasePlayer.onCompletionListener;
import com.example.uidemo.logic.player.BasePlayer.onErrorListener;
import com.example.uidemo.logic.player.BasePlayer.onPlayStateChangedListener;
import com.example.uidemo.logic.player.BasePlayer.onPreparedListener;
import com.example.uidemo.logic.player.BasePlayer.onSeekCompletedListener;
import com.example.uidemo.logic.player.BasePlayer.onStartPlayListener;
import com.example.uidemo.utils.CustomHandler;

import android.content.Context;
import android.os.Message;

public class MusicPlayer{
	private Context mContext;
	
	private BasePlayer mCurrentPlayer;
	private BasePlayer mLocalPlayer;
	private BasePlayer mOnlinePlayer;
	
	private BasePlayer.onPreparedListener mPrepareListener;
	private BasePlayer.onSeekCompletedListener mSeekCompletedListener;
	private BasePlayer.onBufferingUpdateListener mBufferingUpdateListener;
	private BasePlayer.onCompletionListener mCompletionListener;
	private BasePlayer.onErrorListener mErrorListener;
	private BasePlayer.onPlayStateChangedListener mPlayStateChangedListener;
	private BasePlayer.onStartPlayListener mStartPlayListener;
	
	/** 消息类型 */
	private static final int COMMAND_SETDATASOURCE = 1;
	private static final int COMMAND_START = 2;
	private static final int COMMAND_PAUSE = 3;
	private static final int COMMAND_RELEASE = 4;
	private static final int COMMAND_SEEK = 6;
	private static final int COMMAND_RESET = 7;
	private static final int COMMAND_STOP = 8;
	
	/**
	 * 状态
	 */
	public static final int STATUS_UNINITED = 0;
	public static final int STATUS_BUFFING = 1;
	public static final int STATUS_INITED = 2;
	public static final int STATUS_PLAYING = 3;
	public static final int STATUS_PAUSE = 4;
	public static final int STATUS_RELEASE = 5;
	public static final int STATUS_ERROR = 6;
	protected int mStatus = STATUS_UNINITED;
	
	/**
	 * END_PLAY_REASON
	 */
	public static final int REASON_COMPLETED = 2;
	public static final int REASON_ERROR = 1;
	public static final int REASON_USER_END = 0;
	
	private Song mPlayingSong;
	
	private CustomHandler mHandler;
	
	public MusicPlayer(Context context){
		mHandler = new CustomHandler("player"){

			@Override
			public void handleMessage(Message msg) {
				handMessageImpl(msg);
			}
		};
		
		mLocalPlayer = new LocalPlayer(context);
		mOnlinePlayer = new OnlinePlayer(context);
		mCurrentPlayer = mLocalPlayer;
	}
	
	private void handMessageImpl(Message msg){
		int what = msg.what;
		switch(what){
		case COMMAND_SETDATASOURCE:
			setDataSourceImpl((Song) msg.obj);
			break;
		case COMMAND_START:
			startImpl();
			notifyPlayStateChanged();
			break;
		case COMMAND_SEEK:
			seekToImpl(msg.arg1);
			break;
		case COMMAND_PAUSE:
			pauseImpl();
			notifyPlayStateChanged();
			break;
		case COMMAND_RELEASE:
			releaseImpl();
			break;
		case COMMAND_RESET:
			resetImpl(msg.arg1);
			break;
		case COMMAND_STOP:
			stopImpl();
			break;
		default:
			;
		
		}
	}
	
	private void setDataSourceImpl(Song s){
		if(s.type == Song.TYPE_ONLINE){
			mCurrentPlayer = mOnlinePlayer;
		}else if(s.type == Song.TYPE_LOCAL){
			mCurrentPlayer = mLocalPlayer;
		}
		resetImpl(REASON_USER_END);
		mCurrentPlayer.setDataSource(s);
		mPlayingSong = s;
	}
	
	private void startImpl(){
		mCurrentPlayer.start();
	}
	
	private void seekToImpl(int msec){
		mCurrentPlayer.seekTo(msec);
		//seek结束的广播
	}
	
	private void pauseImpl(){
		mCurrentPlayer.pause();
	}
	
	private void releaseImpl(){
		mCurrentPlayer.release();
	}
	
	private void stopImpl(){
		mCurrentPlayer.stop();
	}
	
	private void resetImpl(int reason){
		mCurrentPlayer.reset(reason);
		mCurrentPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
		mCurrentPlayer.setOnCompletionListener(mCompletionListener);
		mCurrentPlayer.setOnErrorListener(mErrorListener);
		mCurrentPlayer.setOnPlayStateChangedListener(mPlayStateChangedListener);
		mCurrentPlayer.setOnPreparedListener(mPrepareListener);
		mCurrentPlayer.setOnSeekCompletedListener(mSeekCompletedListener);
		mCurrentPlayer.setOnStartPlayListener(mStartPlayListener);
	}
	
	public void setDataSource(Song ss){
		Message msg = mHandler.obtainMessage(COMMAND_SETDATASOURCE, ss);
		mHandler.sendEmptyMessage(COMMAND_SETDATASOURCE);
	}
	
	public void start(){
		mStatus = STATUS_PLAYING;
		mHandler.removeMessages(COMMAND_START);
		mHandler.removeMessages(COMMAND_PAUSE);
		mHandler.removeMessages(COMMAND_STOP);
		mHandler.sendEmptyMessage(COMMAND_START);
	}
	
	public void pause(){
		mStatus = STATUS_PAUSE;
		mHandler.removeMessages(COMMAND_START);
		mHandler.removeMessages(COMMAND_PAUSE);
		mHandler.removeMessages(COMMAND_STOP);
		mHandler.sendEmptyMessage(COMMAND_PAUSE);
	}
	
	public void release(){
		mStatus = STATUS_RELEASE;
		mHandler.removeCallbacksAndMessages(null);
		mHandler.sendEmptyMessage(COMMAND_RELEASE);
	}
	
	public void reset(int reason){
		mStatus = STATUS_INITED;
		mHandler.removeCallbacksAndMessages(null);
		Message msg = mHandler.obtainMessage(COMMAND_RESET,reason, 0);
		mHandler.sendMessage(msg);
	}
	
	public void seekTo(int msec){
		mHandler.removeMessages(COMMAND_SEEK);
		Message msg = mHandler.obtainMessage(COMMAND_SEEK);
		msg.arg1 = msec;
		mHandler.sendMessage(msg);
	}
	
	public void stop(){
		mHandler.removeMessages(COMMAND_START);
		mHandler.removeMessages(COMMAND_PAUSE);
		mHandler.removeMessages(COMMAND_STOP);
		Message msg = mHandler.obtainMessage(COMMAND_STOP);
		mHandler.sendMessage(msg);
	}
	
	public int getCurrentPosition(){
		return mCurrentPlayer.getCurrentPosition();
	}
	
	public int getDuration(){
		return mCurrentPlayer.getDuration();
	}
	
	public boolean isPlaying(){
		return mCurrentPlayer.isPlaying();
	}
	
	public void setOnCompletionListener(onCompletionListener listener){
		mCompletionListener = listener;
		mCurrentPlayer.setOnCompletionListener(mCompletionListener);
	}
	
	public void setOnPreparedListener(onPreparedListener listener){
		mPrepareListener = listener;
		mCurrentPlayer.setOnPreparedListener(mPrepareListener);
	}
	
	public void setOnErrorListener(onErrorListener listener) {
		mErrorListener = listener;
		mCurrentPlayer.setOnErrorListener(listener);
	}
	
	public void setOnSeekCompleteListener(onSeekCompletedListener listener) {
		mSeekCompletedListener = listener;
		mCurrentPlayer.setOnSeekCompletedListener(listener);
	}
	
	public void setOnBufferingUpdateListener(onBufferingUpdateListener listener) {
		mBufferingUpdateListener = listener;
		mCurrentPlayer.setOnBufferingUpdateListener(listener);
	}
	
	public void setOnStartPlayListener(onStartPlayListener listener) {
		mStartPlayListener = listener;
		mCurrentPlayer.setOnStartPlayListener(listener);
	}
	
	public void setOnPlayStateChangedListener(onPlayStateChangedListener listener) {
		mPlayStateChangedListener = listener;
		mCurrentPlayer.setOnPlayStateChangedListener(listener);
	}
	
	public void setVolume(int volume){
		mCurrentPlayer.setVolume(0);
	}
	
	protected void notifyError(int errcode) {
		if (mErrorListener != null) {
			mErrorListener.onError(errcode);
		}
	}

	protected void notifyBufferingUpdate(int percent) {
		if (mBufferingUpdateListener != null) {
			mBufferingUpdateListener.onBufferingUpdate(percent);
		}
	}

	protected void notifyCompletion() {
		if (mCompletionListener != null) {
			mCompletionListener.onCompletion();
		}
	}

	protected void notifySeekCompleted() {
		if (mSeekCompletedListener != null) {
			mSeekCompletedListener.onSeekCompleted();
		}
	}

	protected void notifyPrepared() {
		if (mPrepareListener != null) {
			mPrepareListener.onPrepared();
		}
	}
	
	protected void notifyPlayStateChanged(){
		if(mPlayStateChangedListener != null) {
			mPlayStateChangedListener.onPlayStateChanged();
		}
	}
	
}