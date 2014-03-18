package com.example.uidemo.view.widget;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

public class BDPullListView extends BDListView implements OnScrollListener {

	private final static String TAG = "PullList";

	private int firstItemIndex;
	private boolean pullable = true;

	private boolean isRecored;
	private int startY;

	private int state;

	private View headView;

	private ImageView headImageView;

	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;

	private final static float RATIO = 2f;

	private ArrayList<View> headViews = new ArrayList<View>();

	public BDPullListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// TODO Auto-generated constructor stub
		init(context);

	}

//	public ImageView getHeadImageView() {
//		return headImageView;
//	}

	public BDPullListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub

		pullable = true;
		init(context);

	}

	public boolean isScrollUp;

	@Override
	public void setAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		// if (adapter != getAdapter())
		super.setAdapter(adapter);

	}

	public void setPullAble(boolean b) {
		pullable = b;
	}

	public BDPullListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	ArrayList<FixedViewInfo> mHeaderViewInfos;

	public ArrayList<FixedViewInfo> getHeaders() {
		return mHeaderViewInfos;
	}

	@Override
	public void addHeaderView(View v, Object data, boolean isSelectable) {
		// TODO Auto-generated method stub
		super.addHeaderView(v, data, isSelectable);
		headViews.add(v);
	}

	public ArrayList<View> getHeadViews() {
		return headViews;
	}

	public void setHeaderImageView(ImageView imagView) {
		this.headImageView = imagView;
	}

	@Override
	protected void layoutChildren() {
		// TODO Auto-generated method stub
		try {
			super.layoutChildren();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			try {
				ListAdapter adapter = getAdapter();
				if (adapter instanceof HeaderViewListAdapter) {
					HeaderViewListAdapter hAdapter = (HeaderViewListAdapter) adapter;
					ListAdapter wListAdapter = hAdapter.getWrappedAdapter();
					if (wListAdapter instanceof BaseAdapter) {
						BaseAdapter bAdapter = (BaseAdapter) wListAdapter;
						bAdapter.notifyDataSetChanged();
					}
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public static interface OnLayoutListener {
		public void onLayoutFinished();
	}

	@SuppressLint("NewApi")
	void init(Context context) {
		try {
			Class c = Class.forName("android.widget.ListView");
			Field f = c.getDeclaredField("mHeaderViewInfos");
			f.setAccessible(true);
			mHeaderViewInfos = (ArrayList<FixedViewInfo>) f.get(this);
			int sdk = android.os.Build.VERSION.SDK_INT;
			if (sdk > 10) {
				// 先去掉    本来是 去掉那些上下边缘泛光效果 但是他妈的 meizu 容易在滑动的时候 出现绘制不全得问题 操得勒
				// setOverScrollMode(ListView.OVER_SCROLL_NEVER);
			}
			if (sdk > android.os.Build.VERSION_CODES.GINGERBREAD_MR1) {

				setLayerType(LAYER_TYPE_NONE, null);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} catch (Error e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// headView = inflater.inflate(R.layout.test_cell_head, null);
		// headImageView = (ImageView)
		// headView.findViewById(R.id.headImageView);
		// // measureView(headView);
		// headContentHeight = 200;// headView.getMeasuredHeight();
		// headView.setPadding(0, -1 * headContentHeight, 0, 0);
		// headView.scrollTo(0, -1 * headContentHeight);
		setHeaderDividersEnabled(false);
		// headView.invalidate();
		// addHeaderView(headView);
		super.setOnScrollListener(this);

		state = DONE;
	}

	public void setHeadView(View v) {
		headView = v;
		addHeaderView(headView, null, false);
	}

	public void hideHeadView() {
		if (mHeaderViewInfos.size() > 0) {
			mHeaderViewInfos.remove(0);
		}
	}

	public void showHeadView() {
		if (mHeaderViewInfos.size() == 0) {
			FixedViewInfo fvi = new FixedViewInfo();
			fvi.data = null;
			fvi.isSelectable = false;
			fvi.view = headView;
			mHeaderViewInfos.add(fvi);
		}
	}

	public View getHeadView() {
		return headView;
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	private List<OnScrollListener> mScrollListener = new ArrayList<AbsListView.OnScrollListener>();

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		// TODO Auto-generated method stub
		mScrollListener.add(l);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (mScrollListener != null) {

			for (OnScrollListener l : mScrollListener) {

				l.onScrollStateChanged(view, scrollState);
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		firstItemIndex = firstVisibleItem;
		for (OnScrollListener l : mScrollListener) {
			try{
				l.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}catch(Exception e){}
		}
	}

	private int mode;

	void setOverScroll(int mode) {

		try {
			if (this.mode == mode) {
				return;
			}
			this.mode = mode;
			// setOverScrollMode(mode);

		} catch (Exception e) {
			// TODO: handle exception
		} catch (Error e) {

		}

	}

	private boolean isTouch = true;

	public void setTouchAble(boolean b) {
		isTouch = b;
	}

	float dY;

	int barTop;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		try{
			final int action = ev.getAction();
			float y = ev.getY();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
	
				if (getFirstVisiblePosition() == 0 && getChildCount() > 1) {
					barTop = getChildAt(1).getTop();
				} else {
					barTop = -1;
				}
	
			case MotionEvent.ACTION_MOVE:
				isScrollUp = dY > y;
				break;
			default:
				if (onTouchUpListener != null) {
					onTouchUpListener.onTouchUp(barTop);
				}
				break;
			}
	
			dY = y;
		}catch(Exception e){
			
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (!isTouch) {
			return true;
		}

		int action = ev.getAction();
		if (pullable && headView != null && headView.getTop() == 0 && firstItemIndex == 0) {

			switch (action) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0) {
					isRecored = true;

				} else {
					isRecored = false;
				}
				mHandler.removeMessages(0, this);
				break;
			case MotionEvent.ACTION_MOVE:
				int tempY = (int) ev.getY();

				if (tempY < startY && isRecored) {
					break;
				}
				if (firstItemIndex != 0) {
					break;
				}

				if (!isRecored && firstItemIndex == 0) {
					isRecored = true;

				}

				mOffset = (int) ((tempY - startY) / RATIO);

				if (mOffset < 0) {
					mOffset = 0;
				}

				if (mOffset > getMeasuredWidth()) {
					mOffset = getMeasuredWidth();
				}

				headImageView.setPadding(0, mOffset / 2, 0, mOffset / 2);

				if (pullListener != null) {
					pullListener.onPull(mOffset);
				}
				return false;
			case MotionEvent.ACTION_UP:
			default:

				// onRefresh();
				if (isRecored) {
					state = DONE;

					changeHeaderViewByState();

				}
				startY = 0;
				isRecored = false;
				MotionEvent event = MotionEvent.obtain(ev);
				if (mOffset != 0) {
					event.setAction(MotionEvent.ACTION_CANCEL);
				}
				try {
					return super.onTouchEvent(event);
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
			}
		}
		try {
			return super.onTouchEvent(ev);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		return false;
	}

	private int mOffset;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (!isTouch) {
			return false;
		}

		final int action = ev.getAction();
		if (action == MotionEvent.ACTION_DOWN && firstItemIndex == 0) {
			startY = (int) ev.getY();
		}

		return super.onInterceptTouchEvent(ev);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);

	}

	private OnPullListener pullListener;

	public void setOnPullListener(OnPullListener pullListener) {
		this.pullListener = pullListener;
	}

	public static interface OnPullListener {

		public void onPull(int yOffest);

	}

	private void changeHeaderViewByState() {
		switch (state) {

		case REFRESHING:
		case RELEASE_To_REFRESH:
		case PULL_To_REFRESH:
			headImageView.setPadding(0, 0, 0, 0);

			if (pullListener != null) {
				pullListener.onPull(0);
			}
			break;
		case DONE:
			mHandler.removeMessages(0, this);
			int distance = (int) (20 * getResources().getDisplayMetrics().density);
			Message msg = mHandler.obtainMessage(0, distance, 0);
			msg.obj = this;
			mHandler.sendMessageDelayed(msg, 10);
			break;
		}
	}

	private static Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			BDPullListView listView = (BDPullListView) msg.obj;
			listView.mOffset -= msg.arg1;
			if (listView.mOffset < 0) {
				listView.mOffset = 0;
			}

			if (listView.mOffset >= 0) {

				listView.headImageView.setPadding(0, listView.mOffset / 2, 0, listView.mOffset / 2);
				if (listView.pullListener != null) {
					listView.pullListener.onPull(listView.mOffset);
				}
				if (listView.mOffset > 0) {

					Message m = obtainMessage(0, msg.arg1, 0);
					m.obj = listView;
					sendMessageDelayed(m, 20);
				} else {
					if (listView.getOnFinishedListener() != null) {
						listView.getOnFinishedListener().finished();
					}
				}
			}

		};
	};
	private OnFinishedListener onFinishedListener;

	public OnFinishedListener getOnFinishedListener() {
		return onFinishedListener;
	}

	public void setOnFinishedListener(OnFinishedListener onFinishedListener) {
		this.onFinishedListener = onFinishedListener;
	}

	public static interface OnFinishedListener {
		public void finished();
	}

	OnTouchUpListener onTouchUpListener;

	public void setOnTouchUpListener(OnTouchUpListener onTouchUpListener) {
		this.onTouchUpListener = onTouchUpListener;
	}

	public static interface OnTouchUpListener {
		public void onTouchUp(int downTop);

	}

}
