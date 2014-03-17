package com.example.uidemo.ui.online.view;

import java.lang.reflect.TypeVariable;

import com.example.uidemo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemView extends ItemBaseView{
	
	public ItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		mContext = context;
		initViews(0);
	}

	private Context mContext;
	private LayoutInflater mInflater;
	
	private TextView mTitle;
	private TextView mListNum;
	
	
	public ItemView(Context context, float b_length) {
		super(context);
		log("init");
		mContext = context;
		initViews(b_length);
	}

	
	private void initViews(float length){
		log("init views");
		mInflater = LayoutInflater.from(mContext);
		View v = mInflater.inflate(R.layout.item_view, this,true);
		
		mBackground = (ImageView) v.findViewById(R.id.background);
		mImage = (ImageView) v.findViewById(R.id.image);
		mTagImage = (ImageView) v.findViewById(R.id.image1);
		
		mTitle = (TextView) v.findViewById(R.id.text2);
		mListNum = (TextView) v.findViewById(R.id.text1);
		
		mBackground.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				onItemClick();
				
			}
		});
	}
	
	public void onItemClick(){
		log("item click");
		// mData 获取数据展示点击后的view
	}
	
	public void updateViews(ItemData data, boolean big){
		log("update views data= " + data.toString());
		
		super.updateViews(data, big);
		
		mTitle.setText(data.title);
		mTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP,12);
		
		mListNum.setText(data.listnum);
	}
	
	private void log(String s){
		Log.d("zhou",s);
	}
}