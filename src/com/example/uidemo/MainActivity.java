package com.example.uidemo;

import com.example.uidemo.ui.VerticalAnimationView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView mBottomText;
	private VerticalAnimationView mPlayView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mBottomText = (TextView) findViewById(R.id.bottomview);
		mPlayView = (VerticalAnimationView) findViewById(R.id.vav);
	}

}
