package com.example.uidemo.ui.self;

import com.example.uidemo.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewContainer extends ViewGroup{
	Context mContext;
	
	TextView tv;
	ImageView im;
	Button btn;

	public ViewContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	public ViewContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public ViewContainer(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context){
		mContext =context;
		tv = new TextView(context);
		LayoutParams params = new LayoutParams(30, 60);
		tv.setLayoutParams(params);
		tv.setBackgroundColor(Color.RED);
		tv.setText("text view");
		
	    btn = new Button(mContext);
		params = new LayoutParams(40,70);
		btn.setLayoutParams(params);
		btn.setBackgroundColor(Color.GREEN);
		
		im = new ImageView(mContext);
		params = new LayoutParams(60,60);
		im.setBackgroundResource(R.drawable.ic_launcher);
		
		addView(tv);
		addView(btn);
		addView(im);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		log("onmeasure");
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));
		int count = getChildCount();
		for(int i=0; i<count; i++){
			View child = getChildAt(i);
			child.measure(View.MeasureSpec.makeMeasureSpec(widthMeasureSpec, MeasureSpec.EXACTLY),
					View.MeasureSpec.makeMeasureSpec(heightMeasureSpec, MeasureSpec.EXACTLY));
		}
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		log("changed= " + changed + " l= " + l + " t= " + t + " r= " + r + " b= " + b);
		int count = getChildCount();
		int t1 =0, r1=0 , l1=0, b1=0;
		for(int i=0; i<count; i++){
			View child = getChildAt(i);
		    int childW = child.getMeasuredWidth();
		    int childH = child.getMeasuredHeight();
		    log("childW= " +  childW + " childH= " + childH);
			child.layout(l1, t1, childW, childH+ t1);
			t1 = t1 + child.getHeight();
			log("t1= " + t1);
		}
	}
	
	private void log(String s){
		Log.d("zhou",s);
	}
}