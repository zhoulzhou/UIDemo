package com.example.uidemo.ui.local;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.example.uidemo.MainActivity;
import com.example.uidemo.base.BaseFragment;

public class LocalAlbumnFragment extends BaseFragment implements OnClickListener{
	private Context mContext;
	private Button mBtn;

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
		tv.setText("LOCAL ALBUMN");
		
		mBtn = new Button(mContext);
		mBtn.setText("btn");
		LayoutParams params = new LayoutParams(60,60);
		mBtn.setLayoutParams(params);
		
		mBtn.setOnClickListener(this);
		return mBtn;
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

	@Override
	public void onClick(View v) {
		if(v == mBtn){
			BaseFragment fragment = new LocalFolderFragment();
			MainActivity.getMain().doShowAction(fragment, true, null);
		}
	}
	
}