package com.example.uidemo.ui.playview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uidemo.R;
import com.example.uidemo.base.BaseFrameLayout;
import com.example.uidemo.ui.widget.DocIndicator;

public class PlayingView extends BaseFrameLayout{
	private PlayerPager mPager;
	private View mIndicatorLayout;
	private DocIndicator mDocIndicator;
	private PlayerAdapter mAdapter;
	private PlayerContainerView mContainerView;
	final static float BAR_HEIGHT = 92.0f; //底部播放控制。

	public PlayingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public PlayingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public PlayingView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private void init(){
		mPager = new PlayerPager(getContext());
		mIndicatorLayout = LayoutInflater.from(getContext()).inflate(R.layout.player_indicator, null);
		mDocIndicator = (DocIndicator) mIndicatorLayout.findViewById(R.id.indicator);
		
		if (mAdapter == null) {
			mAdapter = new PlayerAdapter();
			mPager.setOffscreenPageLimit(3);

			mPager.setAdapter(mAdapter);
			mPager.setCurrentItem(1);
		}
		mDocIndicator.setCurrent(1);
		
		mContainerView = new PlayerContainerView(getContext());
		
		addView(mContainerView);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
	    int w = getWidth();
	    int h = getHeight();
	    int barHeight = (int) (BAR_HEIGHT * density);

		mContainerView.layout(0, 0, w, h);
	}
	
	class PlayerPager extends ViewPager {

		public PlayerPager(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			setDrawingCacheEnabled(false);
			setAlwaysDrawnWithCacheEnabled(false);
			setChildrenDrawingCacheEnabled(false);
			setFadingEdgeLength(0);
			setChildrenDrawingOrderEnabled(false);
			setChildrenDrawnWithCacheEnabled(false);
			setClipChildren(false);
			//
			// setHorizontalFadingEdgeEnabled(false);
			// setVerticalFadingEdgeEnabled(false);
			//
			// setScrollbarFadingEnabled(false);
		}

		@Override
		protected void onMeasure(int arg0, int arg1) {
			// TODO Auto-generated method stub
			super.onMeasure(arg0, arg1);
			int i = 0;
			int count = getChildCount();
			for (i = 0; i < count; i++) {
				View child = getChildAt(i);
				child.measure(View.MeasureSpec.makeMeasureSpec(PlayerPager.this.getWidth(), View.MeasureSpec.EXACTLY),
						View.MeasureSpec.makeMeasureSpec(PlayerPager.this.getHeight(), View.MeasureSpec.EXACTLY));
			}

		}

		@Override
		protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
			// TODO Auto-generated method stub
			super.onLayout(arg0, arg1, arg2, arg3, arg4);
		}

		@Override
		protected void dispatchDraw(Canvas canvas) {
			// TODO Auto-generated method stub

			long time = getDrawingTime();
			int count = getChildCount();
			for (int i = 0; i < count; i++) {
				View child = getChildAt(i);
				drawChild(canvas, child, time);

			}
		}

		@Override
		protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
			// TODO Auto-generated method stub
			boolean b = super.drawChild(canvas, child, drawingTime);
			child.invalidate();
			return b;
		}

	}
	
	class PlayerAdapter extends PagerAdapter {

		public PlayerAdapter() {
			super();
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			View view = null;
			if (position == 0) {
//				view = mPlayQueueView;
			} else if (position == 1) {
//				view = mAlbumView;
			} else if (position == 2) {
//				view = mLyricView;
			}
			container.addView(view);
			return view;
		}

	}
	
	class PlayerContainerView extends BaseFrameLayout {
		public PlayerContainerView(Context context) {
			super(context);
			addView(mPager);
			addView(mIndicatorLayout);
		}
		
		@Override
		protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
			// TODO Auto-generated method stub
			int w = getWidth();
			int h = getHeight();
			
			int barHeight = (int) (BAR_HEIGHT * density);
			int barSpace = (int) (6 * density);
			
			int topMarginY = (int) (8 * density);
			int topBtnHeight = (int) (70 * density / 1.5f);
			
			mPager.layout(0, topMarginY + topBtnHeight, w, h - barHeight + barSpace);
			
			int indicatorHeight = (int) (20 * density);
			mIndicatorLayout.layout(0, 0, w, indicatorHeight);
			
		}
	}
	
}