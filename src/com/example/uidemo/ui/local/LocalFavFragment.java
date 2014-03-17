package com.example.uidemo.ui.local;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uidemo.R;
import com.example.uidemo.base.BaseFragment;
import com.example.uidemo.ui.loadingview.CellListLoading;

public class LocalFavFragment extends BaseFragment{
	private Context mContext;
	private TextView mText;
	private CellListLoading mLoadView;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int what = msg.what;
			if (what == 100) {
				Log.d("zhou", "handler show load view");
				mText.setVisibility(View.GONE);
				mLoadView.showLoading();
			}
		}
		
	};

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mContext = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.ui_local_fav_view, null);
		mText = (TextView) v.findViewById(R.id.text);
		mLoadView =(CellListLoading) v.findViewById(R.id.loading);

		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		Log.d("zhou","fav show load view");
		mHandler.obtainMessage(100);
		mHandler.sendEmptyMessageDelayed(100, 1000);
	}

	@Override
	public void onStartLoadData() {
		// TODO Auto-generated method stub
		super.onStartLoadData();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		mText = null;
		mLoadView = null;
	}
	
}