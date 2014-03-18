package com.example.uidemo.view.widget;

import com.example.uidemo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class BDPullHeaderLayout extends RelativeLayout {
	BDImageView imageView;

	public BDPullHeaderLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		imageView = (BDImageView) findViewById(R.id.image);
		try{
			imageView.setImageResource(R.drawable.ic_launcher);
		}catch(Throwable e){}
	}

	public ImageView getPullImage() {
		return imageView;
	}
}
