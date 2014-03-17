package com.example.uidemo.ui.online;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uidemo.R;
import com.example.uidemo.base.BaseFragment;
import com.example.uidemo.ui.loadingview.CellListLoading;
import com.example.uidemo.view.widget.BDListView;

public class OnlineBaseListFragment extends BaseFragment{
    protected  BDListView mListView;
    protected CellListLoading mLoadView;
    private boolean mIsInited = false;
    
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		mListView = null;
		mLoadView = null;
		mIsInited = false;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		mLoadView = (CellListLoading) view.findViewById(R.id.loadingview);
		mLoadView.showLoading();
		mListView = (BDListView) view.findViewById(R.id.listview);
		
		mIsInited = true;
	}

	@Override
	public void onStartLoadData() {
		// TODO Auto-generated method stub
		super.onStartLoadData();
		startLoading();
	}
	
	public void startLoading() {
		if(!mIsInited){
			return ;
		}
		if (mLoadView != null && mListView != null) {
			mLoadView.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			mLoadView.showLoading();
		}
	}

	public void endLoading() {
		mLoadView.setVisibility(View.GONE);
		mListView.setVisibility(View.VISIBLE);
	}
	
	public void onLoadFinish(){
		if(!mIsInited){
			return ;
		}
		endLoading();
	}
}