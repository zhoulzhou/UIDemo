package com.example.uidemo.ui.view;

import com.example.uidemo.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TopLayoutTestActivity extends Activity{
	TopLayout mTopLayout;
	Button mBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.toplayout_test);
	    
	    mTopLayout = (TopLayout) findViewById(R.id.toplayout);
	    mBtn = (Button) findViewById(R.id.btn);
	    mBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 mTopLayout.scroll2Bottom();
			}
		});
	    
	    mTopLayout.scroll2Top();
	    
	   
	}
	
}