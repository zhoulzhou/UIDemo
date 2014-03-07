package com.example.uidemo.ui.view;

import com.example.uidemo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class TopLayout extends LinearLayout{
	private Scroller mScroller;
	private int mScrollY;
	private int mDeltaY;
	private int mFinalY;

	public TopLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	public TopLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public TopLayout(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context){
		mScroller = new Scroller(context);
	}
	
	public void scroll2Bottom(){
		log("scroll2Bottom");
		mScroller.abortAnimation();
		log("getScrollY= " + getScrollY());
		mScroller.startScroll(0, getScrollY(), 0, 0-getScrollY());
		mFinalY = 0;
		invalidate();
	}
	
	public void scroll2Top(){
		log("scroll2Top");
		int y = getResources().getDimensionPixelSize(R.dimen.top_one_view_height);
		log("y= " + y);
		log("getScrollY= " + getScrollY());
		if(getScrollY() > y) {
			return ;
		}
		mScroller.abortAnimation();
		mFinalY = y;
		mScroller.startScroll(0, getScrollY(), 0, y-getScrollY(),1000);
		invalidate();
	}
	
	
	
	@Override
	public void computeScroll() {
		log("computeScroll");
		if (!mScroller.isFinished()) {
			if (mScroller.computeScrollOffset()) {
				int oldY = getScrollY();
				int oldX = getScrollX();
				log("oldX = " + oldX + " oldY= " + oldY);
				int x = mScroller.getCurrX();
				int y = mScroller.getCurrY();
				log("x= " + x + " y= " + y);
				if (oldX != x || oldY != y) {
					scrollTo(x, y);
				}
				invalidate();
			} else {
				clearChildrenCache();
			}
		}else{
			mFinalY = 0;
			clearChildrenCache();
		}
	}
	
	void enableChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View layout = (View) getChildAt(i);
			layout.setDrawingCacheEnabled(true);
		}
	}

	void clearChildrenCache() {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View layout = (View) getChildAt(i);
			layout.setDrawingCacheEnabled(false);
		}
	}

	public boolean isScrollFinished(){
		return mScroller.isFinished();
	}
	
	public void stopScroll(){
		mScroller.abortAnimation();
	}
	
	private void log(String s){
		Log.d("zhou",s);
	}
}