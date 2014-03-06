package com.example.uidemo.ui.playview;

import com.example.uidemo.R;
import com.example.uidemo.logic.player.BasePlayer;
import com.example.uidemo.logic.player.MusicPlayer;
import com.example.uidemo.logic.player.Song;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayingActivity extends Activity{
	private Button mPlayBtn;
	private SeekBar mSeekBar;
	private TextView mCurrentTime;
	private TextView mTotalTime;
	private MusicPlayer mPlayer;
	private Context mContext;
	
	private final static int REFRESH = 1;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int what = msg.what	;
			switch(what){
			case REFRESH:
				refresh();
				break;
			default:
				break;
			}
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.play_main);
		
		mContext = this;
		
		mPlayBtn = (Button)	 findViewById(R.id.playcontrolbtn);
		mSeekBar = (SeekBar) findViewById(R.id.seekbar);
		mCurrentTime = (TextView) findViewById(R.id.currenttime);
		mTotalTime = (TextView) findViewById(R.id.totaltime);
		
//		initPlayer();
		
		new Thread(r).start();
	}
	
	private Runnable r = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			initPlayer();
		}
		
	};
	
	private BasePlayer.onCompletionListener mCompletionListener = new BasePlayer.onCompletionListener() {
		
		@Override
		public void onCompletion() {
			// TODO Auto-generated method stub
			log("play end");
		}
	};
	
	private void initPlayer(){
		mPlayer = new MusicPlayer(mContext);
		mPlayer.setOnCompletionListener(mCompletionListener);
		Song ss = new Song();
		ss.path = getPath();
		log("path= " + ss.path);
		ss.type = Song.TYPE_LOCAL;
		mPlayer.setDataSource(ss);
		mPlayer.start();
	}
	
	private String getPath(){
		String[] projection = {
				MediaStore.Audio.AudioColumns._ID,
				MediaStore.Audio.AudioColumns.TITLE,
				MediaStore.Audio.AudioColumns.DATA
		};
		Cursor c = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
		c.moveToFirst();
		String path = c.getString(c.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
//		if(c != null){
//			c = null;
//			c.close();
//		}
		return path;
	}
	
	private void refresh(){
		mHandler.obtainMessage(REFRESH);
		mHandler.sendEmptyMessageDelayed(REFRESH, 1000);
	}
	
	private void log(String s){
		Log.d("zhou",s);
	}
}