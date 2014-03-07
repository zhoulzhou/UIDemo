package com.example.uidemo.ui.view;

import com.example.uidemo.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

public class TopLayoutTestActivity extends Activity{
	TopLayout mTopLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.toplayout_test);
	    
	    mTopLayout = (TopLayout) findViewById(R.id.toplayout);
	    
	    mTopLayout.scroll2Top();
	}
	
}