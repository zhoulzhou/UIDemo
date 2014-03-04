package com.example.uidemo.ui;

import java.lang.ref.WeakReference;

import com.example.uidemo.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class HorizontalAnimView extends ViewGroup{
	private final static String TAG = "HorizontalAnimView";

	ViewGroup mContainer;
	View mShadowView;
	Scroller mScroller;
	float density;

	public HorizontalAnimView(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		init();
	}

	public HorizontalAnimView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();

	}

	int mScreenWidth;
	int mEdageWidth;

	void init() {
		mScreenWidth = getResources().getDisplayMetrics().widthPixels;
		mShadowView = LayoutInflater.from(getContext()).inflate(R.layout.layout_pop_left, null);
		density = getResources().getDisplayMetrics().density;
		mContainer = new LinearLayout(getContext());
		mContainer.setBackgroundColor(Color.WHITE);
		addView(mShadowView);
		addView(mContainer);

		mScroller = new Scroller(getContext());// new
												// Scroller(getContext());
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

		mHandler = new DismissHandler(this);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		mShadowView.layout(l, t, r, b);
		mContainer.layout(r, t, r + r - l, b);
	}

	public void show(boolean isScroll) {

		if (isScroll) {

			if (!mScroller.isFinished()) {
				return;
			}
			isShow = true;
			enableChildrenCache();
			// int duration = 5000;
			// Log.d(TAG, "show >>" + getScrollX() + "," + getMeasuredWidth());
			// int oldScrollX = getScrollX();
			// mScroller.startScroll(oldScrollX, getScrollY(), mScreenWidth,
			// getScrollY(), duration);
			// postInvalidate();
			snapToScreen(1);
		} else {
			isShow = true;
			scrollTo(mScreenWidth, 0);
			mScrollX = mScreenWidth;
			postDelayed(new StakeOutRunable(), 5);

		}
	}

	class StakeOutRunable implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			VerticalAnimationView vav = VerticalAnimationView.getInstance();

			if (vav != null && vav.getScrollY() != 0) {
				postDelayed(new StakeOutRunable(), 5);
			} else {
				hasCallLoad = true;
				if (onSlidFinishedLinstener != null)
					onSlidFinishedLinstener.onSlidFinished();
			}
		}

	}

	public void dismiss() {
		if (!mScroller.isFinished()) {
			return;
		}
		mHandler.sendEmptyMessageDelayed(0, 1000);
		enableChildrenCache();
		snapToScreen(0);
	}

	boolean isShow;

	@Override
	public void scrollTo(int x, int y) {
		// TODO Auto-generated method stub
		super.scrollTo(x, y);
	}

	// protected void dispatchDraw(Canvas canvas) {
	// final long drawingTime = getDrawingTime();
	//
	// final int count = getChildCount();
	// try { // 消除Canvas: trying to use a recycled bitmap引起的爆机
	// for (int i = 0; i < count; i++) {
	// drawChild(canvas, getChildAt(i), drawingTime);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		try {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			final int width = MeasureSpec.getSize(widthMeasureSpec);
			final int count = getChildCount();
			for (int i = 0; i < count; i++) {
				getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
			}

			// scrollTo(mCurrentScreen * width, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void computeScroll() {

		if (!mScroller.isFinished()) {
			// mContainer.setEnabled(false);

			if (mScroller.computeScrollOffset()) {
				int oldX = getScrollX();
				int oldY = getScrollY();
				int x = mScroller.getCurrX();
				int y = mScroller.getCurrY();
				mScrollX = x;
				if (oldX != x || oldY != y) {
					scrollTo(x, y);
				}
				// Keep on drawing until the animation has finished.
				// invalidate();
				postInvalidate();
			} else {
				clearChildrenCache();
			}
		} else {
			clearChildrenCache();
		}

	}

	void performBackPress() {

	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		super.onScrollChanged(l, t, oldl, oldt);
		if (!isShow && l <= 3 * density && (oldl == 0 || mScrollX <= 3 * density) && oldl > l) {

			if (!isDismissed) {
				isDismissed = true;
				mScroller.abortAnimation();
				scrollTo(0, 0);
				if (mOnDismissListener != null) {
					mOnDismissListener.onDismiss();
					Log.e(TAG, "dismiss");
					mHandler.removeMessages(0);
				}
			}
		} else {
			// isDismissed = false;
		}
		if (l < oldl) {
			isShow = false;
		}

		if (!hasCallLoad && l > oldl && l == mScreenWidth && mScrollX == l) {
			// LogUtil.e(TAG, "start loadData");
			hasCallLoad = true;
			if (onSlidFinishedLinstener != null)
				onSlidFinishedLinstener.onSlidFinished();

		}

		setShadowAlpha(l);
		invalidate();
	}

	boolean hasCallLoad = false;

	boolean isDismissed = false;
	OnSlidFinishedLinstener onSlidFinishedLinstener;

	public void setOnSlidFinishedLinstener(OnSlidFinishedLinstener onSlidFinishedLinstener) {
		this.onSlidFinishedLinstener = onSlidFinishedLinstener;
	}

	public static interface OnSlidFinishedLinstener {
		public void onSlidFinished();
	}

	void setShadowAlpha(int currentPosition) {
		float d = currentPosition * 1f / mScreenWidth;
		// d = 1 - d;

		// d = (float) Math.sqrt(d);
		mShadowView.setBackgroundColor(Color.argb((int) (150 * d), 0, 0, 0));
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

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}

	// @Override
	// public boolean onInterceptTouchEvent(MotionEvent ev) {
	// // TODO Auto-generated method stub
	// final int action = ev.getAction();
	// if ((action == MotionEvent.ACTION_MOVE) && (mTouchState !=
	// TOUCH_STATE_REST)) {
	// return true;
	// }
	// final float x = ev.getX();
	// final float y = ev.getY();
	// switch (action) {
	// case MotionEvent.ACTION_MOVE:
	// final int xDiff = (int) Math.abs(x - mLastMotionX);
	// final int yDiff = (int) Math.abs(y - mLastMotionY);
	// boolean xMoved = xDiff > mTouchSlop;
	// boolean yMoved = yDiff > mTouchSlop;
	// if (xMoved || yMoved) {
	// if (xMoved && xDiff >= yDiff * 3) {
	// mTouchState = TOUCH_STATE_SCROLLING;
	// }
	// }
	//
	// if (yDiff > xDiff) {
	// return false;
	// }
	// break;
	// case MotionEvent.ACTION_DOWN:
	// mLastMotionX = x;
	// mLastMotionY = y;
	// // mScroller.abortAnimation();
	// Log.d(TAG, "onInterceptTouchEvent " + mScroller.isFinished());
	//
	// mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST :
	// TOUCH_STATE_SCROLLING;
	// break;
	// case MotionEvent.ACTION_CANCEL:
	// case MotionEvent.ACTION_UP:
	// mTouchState = TOUCH_STATE_REST;
	// break;
	// }
	//
	// return mTouchState != TOUCH_STATE_REST;
	// }
	private boolean mIsBeingDragged;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// if (!isTouchAtContainer(ev) || !hasCallLoad) {
		// return true;
		// }
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
			if (xDiff > 0 && xDiff > yDiff * 3.7f && isTouchAtContainer(ev)) {
				mIsBeingDragged = true;
				mLastMotionX = x;
			} else {

			}
			break;

		}
		return mIsBeingDragged;
	}

	public static class DismissHandler extends Handler {
		WeakReference<HorizontalAnimView> mView;

		public DismissHandler(HorizontalAnimView mView) {
			super();
			this.mView = new WeakReference<HorizontalAnimView>(mView);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (mView == null || mView.get() == null) {
				return;
			}
			HorizontalAnimView view = mView.get();
			view.dismiss();
		}

		public void clear() {
			mView.clear();
			mView = null;
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		if (!hasCallLoad) {
			Log.e(TAG, "------>");
			return true;
		}

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
					return true;
				}
				if (!isTouchAtContainer(event)) {
					return true;
				}

				mLastMotionX = x;
				mLastMotionY = y;

				break;
			case MotionEvent.ACTION_MOVE:
				if (mIsBeingDragged) {

					enableChildrenCache();
					final int deltaX = (int) (mLastMotionX - x);
					final int deltaY = (int) (mLastMotionY - y);
					mLastMotionX = x;
					mLastMotionY = y;
					if (Math.abs(deltaX) < Math.abs(deltaY) * 3) {
						return false;
					}

					if (deltaX < 0) {
						if (mScrollX > 0) {
							scrollBy(Math.max(-mScrollX, deltaX), 0);
						}
					} else if (deltaX > 0) {
						final int availableToScroll = getChildAt(getChildCount() - 1).getRight() - mScrollX - getWidth();
						if (availableToScroll > 0) {
							scrollBy(Math.min(availableToScroll, deltaX), 0);
						}
					}
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(1000);
				int velocityX = (int) velocityTracker.getXVelocity();
				if (velocityX > SNAP_VELOCITY && mCurrentScreen > 0) {
					snapToScreen(mCurrentScreen - 1);
				} else if (velocityX < -SNAP_VELOCITY && mCurrentScreen < getChildCount() - 1) {
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
				mTouchState = TOUCH_STATE_REST;
				break;

			// mTouchState = TOUCH_STATE_REST;
			}
			mScrollX = this.getScrollX();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private int mScrollX = 0;
	protected int mCurrentScreen = 0;
	private VelocityTracker mVelocityTracker;
	private float mLastMotionX;
	private float mLastMotionY;

	private static final int SNAP_VELOCITY = 600;

	private final static int TOUCH_STATE_REST = 0;
	private final static int TOUCH_STATE_SCROLLING = 1;

	private int mTouchState = TOUCH_STATE_REST;

	private int mTouchSlop = 0;

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

	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		super.onDetachedFromWindow();
		mHandler.clear();
		mHandler = null;
	}

	private DismissHandler mHandler;
	OnDismissListener mOnDismissListener;

	public void setOnDismissListener(OnDismissListener onDismissListener) {
		this.mOnDismissListener = onDismissListener;
	}

	public static interface OnDismissListener {
		public void onDismiss();
	}

	boolean isTouchAtContainer(MotionEvent ev) {
		int x = (int) ev.getRawX();
		int y = (int) ev.getRawY();
		Rect contentRect = new Rect();
		int[] contentArray = new int[2];
		mContainer.getLocationOnScreen(contentArray);
		mContainer.getDrawingRect(contentRect);
		contentRect.offsetTo(contentArray[0], contentArray[1]);
		boolean result = contentRect.contains(x, y);
		return result;
	}

	public ViewGroup getContainer() {
		return mContainer;
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

	
}