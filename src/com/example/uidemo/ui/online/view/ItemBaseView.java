package com.example.uidemo.ui.online.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ItemBaseView extends RelativeLayout{
	ImageView mClickImage = null;
	ImageView mBackground = null;
	ImageView mTagImage = null;
	
	String mPicUrl = null;
	
	ItemData mData;
	
	//image loader
//   ImageFetcher mImageFetcher;

	public ItemBaseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public ItemBaseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public ItemBaseView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	void init(){
		//initialize ImageFetcher
		
	}
	
	public void updateViews(ItemData data, boolean big){
		mData = data;
		mPicUrl = data.picUrl;
		
		updateImages();
	}
	
	boolean b_updated = false;
	
	public void updateImages(){
		b_updated = true;
		if(b_onsized && b_updated){
			LoadImage(mPicUrl);
		}
	}
	
	private void LoadImage(String url){
		if(url == null){
			return ;
		}
		//load background image
		
		
//		ImageParam param = new ImageParam(url, ImageFileNameGenerator.getOnlineImageName(MD5Util.encode(url)), 2);
//		param.setDefaultResDrawableId(R.drawable.default_recommend);
//		param.setWidth(mBackground.getMeasuredWidth());
//		param.setHeight(mBackground.getMeasuredHeight());
//		LogUtil.d(TAG, "width: " + mBackground.getMeasuredWidth() + ", height: " + mBackground.getMeasuredHeight());
//		mImageFetcher.loadImage(param, mBackground);
	}
	
	boolean b_onsized = false;
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		super.onSizeChanged(w, h, oldw, oldh);

		b_onsized = true;

		if (b_onsized && b_updated)

			LoadImage(mPicUrl);
	}
}