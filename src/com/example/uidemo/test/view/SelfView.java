package com.example.uidemo.test.view;

import com.example.uidemo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;

public class SelfView extends RelativeLayout implements OnClickListener{
	TextView mText;
	ImageView mImage;

	public SelfView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	public SelfView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	public SelfView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	private void init(Context context){
		log("init");
	}

	@Override
	protected void onFinishInflate() {
		log("onFinishInflate");
		super.onFinishInflate();
		
		mImage = (ImageView) findViewById(R.id.image);
		mText = (TextView) findViewById(R.id.text);
		
		mImage.setOnClickListener(this);
	}
	
	public void setText(String text){
		mText.setText(text);
	}
	
	public void setImage(int resid){
		mImage.setBackgroundResource(resid);
	}
	
	public void updateViews(){
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch(id){
		case R.id.image:
			log("click image");
			break;
		default :
			break;
		}
	}
	
	private void log(String s){
		Log.d("zhou",s);
	}
}