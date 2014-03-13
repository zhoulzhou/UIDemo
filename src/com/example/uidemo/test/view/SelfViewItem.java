package com.example.uidemo.test.view;

import com.example.uidemo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class SelfViewItem extends LinearLayout implements OnClickListener{
	ImageView mImage;
	TextView mText;
	ViewGroup mContainer;

	public SelfViewItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public SelfViewItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public SelfViewItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		
		mContainer = (ViewGroup) findViewById(R.id.content);
		mImage = (ImageView) findViewById(R.id.image_item);
		mText = (TextView) findViewById(R.id.text_item);
		
		mContainer.setOnClickListener(this);
	}
	
	public void setText(String text){
		mText.setText(text);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch(id){
		case R.id.content:
			log("item click");
			break;
		default:
			break;
		}
	}
	
	private void log(String s){
		Log.d("zhou",s);
	}
	
}