package com.example.uidemo.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseView{

	Activity mActivity;
	Fragment mFragment;
	public boolean mInited = false;
	public boolean mIsCreated = false;
	
	private static final String TAG = "BaseView";

	protected Context mContext;
	public BaseView(Context context) {
		this.mContext = context;
//		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public boolean isCreated(){
		return mIsCreated;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		Log.e(TAG, "onCreate >>" + this);
		mIsCreated = true;
	}

	public void onAttach(Activity activity) {
		mActivity = activity;
		Log.e(TAG, "onAttach >>" + this);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e(TAG, "onCreateView >>" + this);
		return null;
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.e(TAG, "onViewCreated >>" + this);
		mInited = true;
	}

	public void onResume() {
		Log.e(TAG, "onResume >>" + this);
	}

	public void onDestroyView() {
		Log.e(TAG, "onDestroyView >>" + this);
		mInited = false;
	}
	
	public void onDestory(){
		mIsCreated = false;
	}

	public void setFragment(Fragment fragment) {
		Log.e(TAG, "setFragment >>" + this);
		mFragment = fragment;
	}

	public Activity getActivity() {
		return mActivity;
	}

	public LoaderManager getLoaderManager() {
		return mFragment.getLoaderManager();
	}

	public Fragment getFragment() {
		return mFragment;
	}

}