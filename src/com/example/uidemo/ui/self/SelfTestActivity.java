package com.example.uidemo.ui.self;

import android.app.Activity;
import android.os.Bundle;

public class SelfTestActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewContainer vc = new ViewContainer(this);
		setContentView(vc);
	}
	
}