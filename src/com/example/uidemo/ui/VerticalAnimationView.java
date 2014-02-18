package com.example.uidemo.ui;

import com.example.uidemo.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class VerticalAnimationView extends ViewGroup{


	ViewGroup mContainer;
	View mShadowView;
	Scroller mScroller;
	float density;

	private VelocityTracker mVelocityTracker;
	private float mLastMotionX;
	private float mLastMotionY;
	private boolean mIsBeingDragged;

	private static final int SNAP_VELOCITY = 600;
	
	// 播放界面是否展现中
	private VerticalAnimationViewScrollListener listener;
	public interface VerticalAnimationViewScrollListener {
		public void onScrollStatusChanged(boolean isShow);
	}
	public void setListener(VerticalAnimationViewScrollListener listener) {
		this.listener = listener;
	}

	public VerticalAnimationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
		mInstance = this;
	}

	private static VerticalAnimationView mInstance;

	public static VerticalAnimationView getInstance() {
		return mInstance;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		// if (changed) {
		// Log.e("hugo", "onLayout -- " + t + "," + b);
		mShadowView.layout(l, 0, r, b - t);
		mContainer.layout(l, b - t, r, b + b - t - t);
		// }
	}

	View mView1;
	View mView2;

	void init() {
		mScroller = new Scroller(getContext());

		mShadowView = new View(getContext());
		density = getResources().getDisplayMetrics().density;
		mContainer = new LinearLayout(getContext());
		mView1 = new LinearLayout(getContext());
		mView2 = new LinearLayout(getContext());
		mContainer.addView(mView1, -1, -1);
		mContainer.addView(mView2, -1, -1);
		mView1.setId(R.id.player_container_id);
		mView2.setId(R.id.radio_container_id);
		mContainer.setLongClickable(true);
		mContainer.setVisibility(View.GONE);
		mView1.setVisibility(View.GONE);
		mView2.setVisibility(View.GONE);
		
		addView(mShadowView);
		addView(mContainer);

		mScroller = new Scroller(getContext());

	}

	public void showPlayerView() {
		mContainer.setVisibility(View.VISIBLE);
		mView1.setVisibility(View.VISIBLE);
		mView2.setVisibility(View.GONE);
	}

	public void showRadioView() {
		mContainer.setVisibility(View.VISIBLE);
		mView1.setVisibility(View.GONE);
		mView2.setVisibility(View.VISIBLE);
	}

	public boolean isPlayerShown() {
		return mView1.getVisibility() == View.VISIBLE;
	}

	int mScreenHeight;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {

		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:

			mLastMotionX = x;
			mLastMotionY = y;
			mIsBeingDragged = false;
			break;

		case MotionEvent.ACTION_MOVE:
			final float dx = x - mLastMotionX;
			final float xDiff = Math.abs(dx);
			final float yDiff = Math.abs(y - mLastMotionY);
			// mTouchSlop
			if (yDiff > 0 && yDiff > xDiff * 1.7f && isTouchAtContainer(ev)) {
				mIsBeingDragged = true;
				mLastMotionX = x;
				mLastMotionY = y;
			} else {
				mIsBeingDragged = false;
			}
			if (!mScroller.isFinished()) {
				// mScroller.abortAnimation();
				mIsBeingDragged = false;
			}
			break;

		}
		return mIsBeingDragged;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		try {

			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
			}
			mVelocityTracker.addMovement(event);
			final int action = event.getAction();
			final float x = event.getX();
			final float y = event.getY();

			switch (action) {
			case MotionEvent.ACTION_DOWN:

				if (!mScroller.isFinished()) {
					// mScroller.abortAnimation();
					return false;
				}
				if (!isTouchAtContainer(event)) {
					return false;
				}

				mLastMotionX = x;
				mLastMotionY = y;

				break;
			case MotionEvent.ACTION_MOVE:

				log("ACTION_MOVE >" + mIsBeingDragged);
				// if (!isTouchAtContainer(event)) {
				// return false;
				// }
				if (mIsBeingDragged) {

					enableChildrenCache();
					final int deltaX = (int) (mLastMotionX - x);
					final int deltaY = (int) (mLastMotionY - y);
					mLastMotionX = x;
					mLastMotionY = y;
					if (Math.abs(deltaY) < Math.abs(deltaX) * 3) {
						return false;
					}

					if (deltaY < 0) {
						if (mScrollY > 0) {
							scrollBy(0, Math.max(-mScrollY, deltaY));
						}
					} else if (deltaY > 0) {
						final int availableToScroll = getChildAt(getChildCount() - 1).getBottom() - mScrollY - getHeight();
						if (availableToScroll > 0) {
							scrollBy(0, Math.min(availableToScroll, deltaY));
						}
					}
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				// if (!isTouchAtContainer(event)) {
				// return false;
				// }
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000);
				int velocityY = (int) velocityTracker.getYVelocity();
				if (velocityY > SNAP_VELOCITY && mCurrentScreen > 0) {
					snapToScreen(mCurrentScreen - 1);
				} else if (velocityY < -SNAP_VELOCITY && mCurrentScreen < getChildCount() - 1) {
					snapToScreen(mCurrentScreen + 1);
				} else {
					snapToDestination();
				}
				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
				// }
				clearChildrenCache();
				break;

			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		mScrollY = this.getScrollY();
		return true;
	}

	int mCurrentScreen = 0;
	private int mScrollY = 0;

	void setShadowAlpha(int currentPosition) {
		float d = currentPosition * 1f / mScreenHeight;
		// d = 1 - d;

		d = (float) Math.sqrt(d);
//		mShadowView.setBackgroundColor(Color.argb((int) (150 * d), 0, 0, 0));
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		try {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			final int width = MeasureSpec.getSize(widthMeasureSpec);
			final int height = MeasureSpec.getSize(heightMeasureSpec);
			mScreenHeight = height;
			final int count = getChildCount();
			for (int i = 0; i < count; i++) {
				getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();

		// if (!mScroller.isFinished()) {
		// mContainer.setEnabled(false);

		if (mScroller.computeScrollOffset()) {
			int oldX = getScrollX();
			int oldY = getScrollY();
			int x = mScroller.getCurrX();
			int y = mScroller.getCurrY();
			mScrollY = y;
			// if (oldX != x || oldY != y) {
			scrollTo(x, y);
			// }

			// Keep on drawing until the animation has finished.
			postInvalidate();
		} else {
			clearChildrenCache();
		}
		// } else {
		//
		// mContainer.setEnabled(true);
		// clearChildrenCache();
		// }
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

	private void snapToDestination() {
		int h = getHeight();
		if (h == 0) {
			h = mScreenHeight;
		}
		final int screenHeight = h;
		final int whichScreen = (mScrollY + (screenHeight / 2)) / screenHeight;

		snapToScreen(whichScreen);

	}

	public void snapToScreen(int whichScreen) {
		whichScreen = Math.max(0, whichScreen);
		// enableChildrenCache();
		mCurrentScreen = whichScreen;
		int h = getMeasuredHeight();
		if (h == 0) {
			h = mScreenHeight;
		}
		final int newY = whichScreen * h;
		final int delta = newY - getScrollY();

		int durrent = Math.min((int) (Math.abs(delta) * 2f / density), 800);
		mScroller.startScroll(0, getScrollY(), 0, delta, durrent);
		invalidate();
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		log( "onScrollChanged, pos=" + l + "|" + t + "|" + oldl + "|" + oldt);
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		setShadowAlpha(t);
		// scroll回调, 播放界面是否可见状态切换
		if (listener != null) {
			boolean showStatus;
			// 拖动过程中, 如在中间作为可见
			if (t > 0) {
				showStatus = true;
			} else {
				showStatus = false;
			}
			listener.onScrollStatusChanged(showStatus);
		}
	}

	boolean isTouchAtContainer(MotionEvent ev) {
		boolean result = false;
		//歌词触摸调整 暂时未用到。
//		View proxy = mContainer.findViewById(R.id.touchproxy);
		View proxy = null ;
		if (proxy == null) {
			return false;
		}
		int x = (int) ev.getRawX();
		int y = (int) ev.getRawY();
		Rect contentRect = new Rect();
		int[] contentArray = new int[2];

		proxy.getLocationOnScreen(contentArray);
		proxy.getDrawingRect(contentRect);
		contentRect.offsetTo(contentArray[0], contentArray[1]);
		result = contentRect.contains(x, y);
		return result;
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		final long drawingTime = getDrawingTime();
		// final int c = canvas.save();
		final int count = getChildCount();
		View child = null;
		for (int i = 0; i < count; i++) {
			child = getChildAt(i);
			// child.draw(canvas);
			if (child.getVisibility() == View.VISIBLE) {
				// continue;
				try {
					drawChild(canvas, child, drawingTime);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}
    private void log(String log){
    	Log.d("zhou",log);
    }

}