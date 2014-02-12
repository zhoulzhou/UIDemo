package com.example.uidemo;

import com.example.uidemo.ui.UIController;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashActivity extends Activity{
	StartTask mLoadingTask;
	Bundle mExtraBundle = null;
	Context mContext = null;
	private static Handler mHandler = new Handler();
	private static final int DELAY_TIME = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mExtraBundle = getIntent().getExtras();
		mContext = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		mLoadingTask = new StartTask();
		mLoadingTask.execute("loading data ...");
		
	}
	
	private class StartTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			Log.d("splash","load data");
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			Log.d("splash","post execute");
			mHandler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					startMain();
				}
			}, DELAY_TIME);
		}
		
	}
	
	private void startMain(){
		
		UIController.showNewUIMain(mContext, mExtraBundle);
		finish();
	}
}