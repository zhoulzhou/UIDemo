package com.example.uidemo.ui.local;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uidemo.base.BaseFragment;

public class LocalArtistFragment extends BaseFragment{
	private Context mContext;

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
		TextView tv = new TextView(mContext);
		tv.setText("LOCAL ARTIST");
		return tv;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onStartLoadData() {
		// TODO Auto-generated method stub
		super.onStartLoadData();
	}
	
}