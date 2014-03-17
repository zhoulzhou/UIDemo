package com.example.uidemo.ui.online.view;

import java.util.ArrayList;

import com.example.uidemo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class RecmdDailyView extends RelativeLayout{
	private Context mContext;
	private LayoutInflater mInflater;
	private ViewGroup mGroupOne, mGroupTwo, mGroupThree, mGroupFour, mGroupFive, mGroupSix;
	
	private float density;
	private float mWidth = 0;
	private float mBigImageWidth = 0;
	private float mSmallImageWidth = 0;

	public RecmdDailyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public RecmdDailyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public RecmdDailyView(Context context) {
		super(context);
		// TODO Auto-generated constructor 
		initViews(context);
	}
	
	private void initViews(Context context){
		
		mContext = context;
		
		getDensity();
		
		mInflater = LayoutInflater.from(context);
		View v = mInflater.inflate(R.layout.recmd_daily_view, this,true);
		
		mGroupOne = (ViewGroup) v.findViewById(R.id.group_one);
		mGroupTwo = (ViewGroup) v.findViewById(R.id.group_two);
		mGroupThree = (ViewGroup) v.findViewById(R.id.group_three);
		mGroupFour = (ViewGroup) v.findViewById(R.id.group_four);
		mGroupFive = (ViewGroup) v.findViewById(R.id.group_five);
		mGroupSix = (ViewGroup) v.findViewById(R.id.group_six);
		
		resizeBigView(mGroupOne);
		resizeSmallView(mGroupTwo);
		resizeSmallView(mGroupThree);
		resizeSmallView(mGroupFour);
		resizeSmallView(mGroupFive);
		resizeSmallView(mGroupSix);
		
		mGroupOne.addView(new ItemView(mContext,0));
		mGroupTwo.addView(new ItemView(mContext,0));
		mGroupThree.addView(new ItemView(mContext,0));
		mGroupFour.addView(new ItemView(mContext,0));
		mGroupFive.addView(new ItemView(mContext,0));
		mGroupSix.addView(new ItemView(mContext,0));
	}
	
	private void getDensity(){
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();
		density = dm.density;
		
		WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		Display ds = wm.getDefaultDisplay();
		
		@SuppressWarnings("deprecation")
		int width = ds.getWidth();
		int height = ds.getHeight();
		log("width= " + width + " height= " + height);
		
		mWidth = width;
		
		mSmallImageWidth = ((mWidth - 4*8*density) / 3.0f);//除以3 取一位小数
		log("smallwidth= " + mSmallImageWidth );
		mBigImageWidth = mSmallImageWidth * 2 + 8*density;
		log("bigwidth= " + mBigImageWidth);
		
	}
	
	private void resizeSmallView(View v){
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v.getLayoutParams();
	    lp.width = lp.height = (int) mSmallImageWidth;
	    v.setLayoutParams(lp);
	}
	
	private void resizeBigView(View v){
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) v.getLayoutParams();
	    lp.width = lp.height = (int) mBigImageWidth;
	    v.setLayoutParams(lp);
	}
	
	public void updateViews(ArrayList<ItemData> list){
		ViewGroup[] gs = {mGroupOne, mGroupTwo, mGroupThree, mGroupFour, mGroupFive, mGroupSix};
		if(list == null){
			return ;
		}
		
		for(int i=0; i<list.size(); i++){
			gs[i].removeAllViews();
			
			float length_p = mSmallImageWidth;
			if(i == 0) {
				length_p = mBigImageWidth;
			}
			
			ItemBaseView v = buildViewByType(i,list.get(i),length_p);
			gs[i].addView(v);
			
			if(i == 0){
				v.updateViews(list.get(i), true);
			}else{
				v.updateViews(list.get(i),false);
			}
			
		}
	}
	
	private ItemBaseView buildViewByType(int i, ItemData data, float length){
		int type = data.type;
		ItemBaseView v = null;
		switch(type){
		case 1:
			 v = new ItemView(mContext, length);
		    break;
		case 2:
			 v = new ItemView(mContext, length);
			break;
		default:
			break;
		
		}
		
		if(v != null){
			v.setViewPosition(i);
		}
		
		return v;
	}
	
	private void log(String s){
		Log.d("zhou",s);
	}
}