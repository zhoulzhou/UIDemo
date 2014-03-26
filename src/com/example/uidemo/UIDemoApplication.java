package com.example.uidemo;

import android.app.Application;
import android.content.Context;

public class UIDemoApplication extends Application{
	private static Context mAppContext;
	
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mAppContext = this;
	}

	public static Context getAppContext() {
		return mAppContext;
	}
}