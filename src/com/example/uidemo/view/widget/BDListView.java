package com.example.uidemo.view.widget;

import java.util.ArrayList;
import java.util.List;

import com.example.uidemo.R;
import com.example.uidemo.ui.loadingview.CellListLoading;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class BDListView extends ListView {
	private final static String TAG = "BDListView";
	View mFootView;
	CellListLoading mLoadingView;
	boolean needFoot = true;

	public BDListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	String hugo_mFragmentName;

	public void setFragment(String name) {
		hugo_mFragmentName = name;
	}

	public void setNeedFoot(boolean b) {
		needFoot = b;
	}

	public BDListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public BDListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	@Override
	protected void layoutChildren() {
		// TODO Auto-generated method stub
		try {

			super.layoutChildren();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}

	private void init() {
		mFootView = new View(getContext());
		AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(-1, getResources().getDimensionPixelSize(R.dimen.top_one_view_height));
		mFootView.setLayoutParams(layoutParams);
	}

	// int mLoadingHeight = 0;

	// @Override
	// protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	// // TODO Auto-generated method stub
	// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	// int height = getMeasuredHeight();
	// if (height <= 0) {
	// return;
	// }
	// if (mLoadingHeight == 0) {
	//
	// final int count = getChildCount();
	// int h = 0;
	// for (int i = 0; i < count; i++) {
	// View v = getChildAt(i);
	// if (v instanceof CalcHeightView) {
	// h += v.getMeasuredHeight();
	// }
	// }
	// if (h == 0) {
	// return;
	// }
	// mLoadingHeight = height - h;
	// if (mLoadingView.getLayoutParams() == null) {
	// mLoadingView.setLayoutParams(new AbsListView.LayoutParams(-1, -2));
	// }
	// mLoadingView.getLayoutParams().height = mLoadingHeight;
	// Log.d(TAG, ">>" + mLoadingHeight);
	// }
	//
	// }

	List<View> mOtherViews = new ArrayList<View>();

	@Override
	public void addHeaderView(View v, Object data, boolean isSelectable) {
		// TODO Auto-generated method stub
		super.addHeaderView(v, data, isSelectable);
		if (!mOtherViews.contains(v)) {
			mOtherViews.add(v);
		}
	}

	@Override
	public void addFooterView(View v, Object data, boolean isSelectable) {
		// TODO Auto-generated method stub
		super.addFooterView(v, data, isSelectable);
		if (!mOtherViews.contains(v)) {
			mOtherViews.add(v);
		}
	}

	ListAdapter mAdapter;

	@Override
	public void setAdapter(ListAdapter adapter) {
		// TODO Auto-generated method stub
		try {
			removeFooterView(mFootView);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		if (needFoot) {
			addFooterView(mFootView);
		}
		// if (mAdapter instanceof LoadingAdapter) {
		// hideOtherViews();
		// } else {
		// showOtherViews();
		// }

		mAdapter = adapter;
		super.setAdapter(adapter);

	}

	// public CellListLoading getLoadingView() {
	// return mLoadingView;
	// }
	//
	// public void restoreLoading(View.OnClickListener retry,
	// View.OnClickListener closewifi) {
	// setAdapter(new LoadingAdapter());
	// switch (mLoadingView.getCurrentState()) {
	// case CellListLoading.LOADING:
	// mLoadingView.showLoading();
	// break;
	// case CellListLoading.NOTHING:
	// mLoadingView.showNothing(R.drawable.ic_blank_empty,
	// getString(R.string.blank_nothing), "",
	// getString(R.string.blank_retry_btn), retry);
	// break;
	// case CellListLoading.ONLINE_WIFI:
	// mLoadingView.showNoNetwork(R.drawable.ic_blank_networkproblem,
	// getString(R.string.blank_only_wifi), "",
	// getString(R.string.blank_only_wifi_btn), closewifi);
	// break;
	// case CellListLoading.NONE_NETWORK:
	// mLoadingView.showNoNetwork(R.drawable.ic_blank_networkproblem,
	// getString(R.string.blank_not_network), "",
	// getString(R.string.blank_retry_btn), retry);
	// break;
	// default:
	// break;
	// }
	// }

	String getString(int res) {
		return getResources().getString(res);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		// if (mAdapter instanceof LoadingAdapter) {
		// return false;
		// }
		return super.onTouchEvent(ev);
	}

	// void hideOtherViews() {
	//
	// int count = getChildCount();
	// for (int i = 0; i < count; i++) {
	// View v = getChildAt(i);
	// if (v.getLayoutParams() != null && !(v instanceof CalcHeightView) && v !=
	// mLoadingView) {
	// v.setVisibility(View.GONE);
	// v.getLayoutParams().height = 0;
	// }
	// }
	// requestLayout();
	// }
	//
	// void showOtherViews() {
	//
	// int count = getChildCount();
	// for (int i = 0; i < count; i++) {
	// View v = getChildAt(i);
	// if (v.getLayoutParams() != null && !(v instanceof CalcHeightView) && v !=
	// mLoadingView) {
	// v.setVisibility(View.VISIBLE);
	// v.getLayoutParams().height = -2;
	// }
	// }
	//
	// requestLayout();
	// }

	class LoadingAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return mLoadingView;
		}

	}

}
