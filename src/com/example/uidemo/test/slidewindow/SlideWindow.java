package com.example.uidemo.test.slidewindow;

import com.example.uidemo.R;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class SlideWindow extends ViewGroup{
	View mShadowView;
	ViewGroup mContainer;
	Scroller mScroller;
	VelocityTracker mVTracker;
	int mCurrentScreen = 0;
	int mScreenWidth;
	float density;

	public SlideWindow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	public SlideWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	public SlideWindow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	private void init(Context context){
		mScreenWidth = getResources().getDisplayMetrics().widthPixels;
		density = getResources().getDisplayMetrics().density;
		
		mShadowView = LayoutInflater.from(getContext()).inflate(R.layout.layout_pop_left, null);
		mContainer = new LinearLayout(getContext());
		
		mScroller = new Scroller(getContext());
		
		addView(mShadowView);
		addView(mContainer);
		
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		mShadowView.layout(l, t, r, b);
		mContainer.layout(r, t, r+r - l, b);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}
	
	
    private boolean mIsBeingdragged = false;
    private float mLastMotionX ;
    private float mLastMotionY;
    private int mScrollX;
    private final int SNAP_VELOCITY = 600;
    
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();
		switch(action){
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			mIsBeingdragged = false;
			break;
		case MotionEvent.ACTION_MOVE:
			float dx = x - mLastMotionX;
			float dy = y - mLastMotionY;
			float xDiff = Math.abs(dx);
			float yDiff = Math.abs(dy);
			if(xDiff>0 && xDiff > yDiff*3.7f && isTouchContainer(ev)){
				mIsBeingdragged = true;
				mLastMotionX = x;
			}
			break;
		case MotionEvent.ACTION_UP:
			
			break;
		}
		return mIsBeingdragged;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		try{
			if(mVTracker == null){
				mVTracker = VelocityTracker.obtain();
			}
			mVTracker.addMovement(event);
			int action = event.getAction();
			float x = event.getX();
			float y = event.getY();
			
			switch(action){
			case MotionEvent.ACTION_DOWN:
				if(!mScroller.isFinished()){
					return true;
				}
				if(!isTouchContainer(event)){
					return true;
				}
				mLastMotionX = x;
				mLastMotionY = y;
				break;
			case MotionEvent.ACTION_MOVE:
				if(mIsBeingdragged){
					
					enanbleChildrenCache();
					final int dx = (int) (mLastMotionX - x);
					final int dy = (int) (mLastMotionY - y);
					mLastMotionX = x;
					mLastMotionY = y;
					if(Math.abs(dx) < Math.abs(dy) * 3){
						return false;
					}
					if(dx < 0){
						if(mScrollX > 0){
							scrollBy(Math.max(mScrollX, dx), 0);
						}
					}else if(dx > 0){
						final int availableToScroll = getChildAt(getChildCount() - 1).getRight() - mScrollX - getWidth();
						if (availableToScroll > 0) {
							scrollBy(Math.min(availableToScroll, dx), 0);
						}
					}
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				final VelocityTracker vt = mVTracker;
				vt.computeCurrentVelocity(1000);
				int vx = (int) vt.getXVelocity();
				if(vx > SNAP_VELOCITY && mCurrentScreen >0){
					snapToScreen(mCurrentScreen - 1);
				}else if( vx < - SNAP_VELOCITY && mCurrentScreen < getChildCount() - 1){
					snapToScreen(mCurrentScreen +1);
				}else {
					snapToDestination();
				}
				
				if (mVTracker != null) {
					mVTracker.recycle();
					mVTracker = null;
				}
				// }
				clearChildrenCache();
				
				break;
				
			}
			
			mScrollX = this.getScrollX();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return super.onTouchEvent(event);
	}
	
	@Override
	public void computeScroll() {
		if(!mScroller.isFinished()){
			if(mScroller.computeScrollOffset()){
				int oldX = getScrollX();
				int oldY = getScrollY();
				int x = mScroller.getCurrX();
				int y = mScroller.getCurrY();
				mScrollX = x;
				if(oldX != x || oldY != y){
					scrollTo(x, y);
				}
				postInvalidate();
			}else {
				clearChildrenCache();
			}
		}else {
			clearChildrenCache();
		}
	}
	
	private boolean isTouchContainer(MotionEvent ev){
		int x = (int) ev.getRawX();
		int y = (int) ev.getRawY();
		Rect container = new Rect();
		int[] contentArray =new int[2];
		mContainer.getLocationOnScreen(contentArray);
		mContainer.getDrawingRect(container);
		container.offsetTo(contentArray[0], contentArray[1]);
		boolean result = container.contains(x, y);
		return result;
	}
	
	private void snapToDestination() {
		int w = getWidth();
		if (w == 0) {
			w = mScreenWidth;
		}
		final int screenWidth = w;
		final int whichScreen = (mScrollX + (screenWidth / 2)) / screenWidth;

		snapToScreen(whichScreen);

	}
	
	public void snapToScreen(int whichScreen) {
		whichScreen = Math.min(1, whichScreen);

		mCurrentScreen = whichScreen;
		int w = getWidth();
		if (w == 0) {
			w = mScreenWidth;
		}
		final int newX = whichScreen * w;
		final int delta = newX - mScrollX;

		int durrent = Math.max((int) (Math.abs(delta) * 2f / density), -1);//将1000改成-1，修复返回按钮成功率低的问题。
		mScroller.startScroll(mScrollX, 0, delta, 0, durrent);
		postInvalidate();
	}
	
	void enanbleChildrenCache(){
		int count = getChildCount();
		for(int i=0; i<count; i++){
			View layout = getChildAt(i);
			layout.setDrawingCacheEnabled(true);
		}
	}
	
	void clearChildrenCache(){
		int count = getChildCount();
		for(int i=0; i<count; i++){
			View layout = getChildAt(i);
			layout.setDrawingCacheEnabled(false);
		}
	}
	
}