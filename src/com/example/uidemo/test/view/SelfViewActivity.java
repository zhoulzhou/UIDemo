package com.example.uidemo.test.view;

import com.example.uidemo.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;

public class SelfViewActivity extends Activity{
	LayoutInflater mInflater;
	SelfView mSelfView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		
		mSelfView = (SelfView) mInflater.inflate(R.layout.view_test_self, null);
		
		setContentView(mSelfView);
		
		mSelfView.setText("title***t");
	}
	
}