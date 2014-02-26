package com.example.uidemo.ui.local;

import com.example.uidemo.R;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TabLocalFragment extends BaseLocalFragment{
	protected ListView mList;
	protected View mContainView;
	protected View mHeadView;
	protected View mFootView;
	protected LocalListViewAbstractFactory mFactory;
	protected BaseAdapter mAdapter;
	protected TextView mFootText;
	protected boolean mInit = false;

	@Override
	public void onStorageChange(boolean mounted, Bundle data) {
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContainView = inflater.inflate(R.layout.ui_tab_local, null);
		mList = (ListView) mContainView.findViewById(R.id.local_list);
		
		if (!mInit) {
			mInit = true;
//			showLoadingDialog();
		}
		
		return mContainView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
//		mAdapter = mFactory.createAdapter();
		
		if(mAdapter == null){
			mAdapter = mFactory.createBaseAdapter();
		}
		
		mList.setAdapter(mAdapter);
		
		//显示loadingView
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (checkSdcard())
			refreshListView();
	}
	
	protected void refreshListView() {
		if (!isDetached() && getLoaderManager().getLoader(mFactory.getLoaderCallbackId()) != null) {
			getLoaderManager().getLoader(mFactory.getLoaderCallbackId()).forceLoad();
		}
	}
	
	private boolean checkSdcard() {
		boolean isSDCardMounted = false;
		if (!isSDCardMounted) {
		}

		return isSDCardMounted;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		
		if (mFactory != null) {
			getLoaderManager().destroyLoader(mFactory.getLoaderCallbackId());
		}
		mList = null;
		mContainView = null;
		mAdapter = null;
		mFootText = null;
	}
	
	protected void closeCursor(Cursor cursor) {
		if (cursor != null) {
			try {
				cursor.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected void restartCursorLoader() {
		getLoaderManager().destroyLoader(mFactory.getLoaderCallbackId());
		getLoaderManager().initLoader(mFactory.getLoaderCallbackId(), null, mFactory.createLoaderCallback());
	}
	
	protected void updateBottomBar(int number) {
		if (mFactory != null) {
			mFootText.setText(mFactory.getFooterText(number));
		} else {
			mFootText.setText("");
		}
	}
	
	protected void showContainerView(){
		//去掉loadingView 显示containerview
		//mLoadingView.setVisibility(View.GONE);
		mList.setVisibility(View.VISIBLE);
	}
	protected void showLoadingDialog() {

//		mLoadView.setVisibility(View.VISIBLE);
		mList.setVisibility(View.GONE);
//		mLoadView.showLoading();
   }
}