package com.example.uidemo.ui.animation;

import com.example.uidemo.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class AnimationTest extends Activity implements OnClickListener{
	TextView tv;
	Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animation_test);
		
		tv = (TextView) findViewById(R.id.textview);
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Animation anima = AnimationUtils.loadAnimation(this,R.anim.myanima);
        findViewById(R.id.textview).startAnimation(anima);
	}
	
}