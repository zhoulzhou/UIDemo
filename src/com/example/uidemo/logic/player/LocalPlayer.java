package com.example.uidemo.logic.player;

import java.io.IOException;

import com.example.uidemo.utils.StringUtils;

import android.R.string;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class LocalPlayer extends BasePlayer{
	private Context mContext ;
	
	private boolean mIsError;
	private boolean mIsPrepared;
	
	public LocalPlayer(Context context){
		mContext = context;
		//logcontroller
	}
	
	private MediaPlayer.OnCompletionListener mCopletionListener = new MediaPlayer.OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer mp) {
			// TODO Auto-generated method stub
			try {
			if(mMediaPlayer != null){
				mMediaPlayer.reset();
				if(mSong != null){
					mMediaPlayer.setDataSource(mSong.path);
					mMediaPlayer.prepare();
					mIsPrepared = true;
				  }
			}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				notifyPlayCompleted();
			}
		};
		
		private MediaPlayer.OnSeekCompleteListener mSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {
			
			@Override
			public void onSeekComplete(MediaPlayer mp) {
				notifySeekCompleted();
			}
		};
		
		private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				notifyError(what);
				return true;
			}
		};

		@Override
		public void setDataSource(Song song) {
			// TODO Auto-generated method stub
			mSong = song;
			if(StringUtils.isEmpty(mSong.path)){
				return ;
			}
			try {
			if(song.path.startsWith("content://")){
				mMediaPlayer.setDataSource(mContext, Uri.parse(mSong.path));
			}else{
				mMediaPlayer.setDataSource(mSong.path);
			}
			
			mMediaPlayer.prepare();
			mIsPrepared = true;
			notifyBufferingUpdate(100);
			
			
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		@Override
		public void pause() {
			// TODO Auto-generated method stub
			super.pause();
			if(mSong != null){
				//try log or something
			}
		}

		@Override
		public void release() {
			// TODO Auto-generated method stub
			super.release();
		}

		@Override
		public void reset(int reason) {
			// TODO Auto-generated method stub
			super.reset(0);
			
			if(mSong != null){
				//try ...
			}
			
			mMediaPlayer.setOnCompletionListener(mCopletionListener);
			mMediaPlayer.setOnErrorListener(mErrorListener);
			mMediaPlayer.setOnSeekCompleteListener(mSeekCompleteListener);
			mIsPrepared = false;
			mIsError = false;
		}

		@Override
		public void seekTo(int msec) {
			// TODO Auto-generated method stub
			super.seekTo(msec);
		}

		@Override
		public void setVolume(int volume) {
			// TODO Auto-generated method stub
			super.setVolume(volume);
		}

		@Override
		public void start() {
			// TODO Auto-generated method stub
			if(mIsError){
				notifyError(ERROR_INTERNAL);
				return ;
			}
			
			super.start();
			
			notifyStartPlay();
			
			if(mSong != null){
				//try log
			}
		}

		@Override
		public void stop() {
			// TODO Auto-generated method stub
			super.stop();
		}
		
		
}