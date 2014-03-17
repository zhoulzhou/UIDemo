package com.example.uidemo.ui.online;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uidemo.base.BaseFragment;
import com.example.uidemo.ui.online.view.ItemData;
import com.example.uidemo.ui.online.view.RecmdDailyView;
import com.example.uidemo.widget.mergeadapter.MergeAdapter;

public class OnlineRecommendFragment extends OnlineBaseListFragment{
    private Handler mhandler = new Handler();
    RecmdDailyView mRemdDailyView;
    MergeAdapter mAdapter;
    
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		TextView tv = new TextView(getActivity());
		tv.setText("recommend list");
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	private RecmdDailyView setRecmdDailyView(){
		RecmdDailyView v = new RecmdDailyView(getActivity());
		
		ArrayList<ItemData> datas = new ArrayList<ItemData>();
		for(int i=0; i<6; i++){
			ItemData data = new ItemData();
			data.title = " title ** " + i;
			data.listnum = " num *** " + i;
			if(i/2 == 0){
				data.type = 2;
			}else{
				data.type = 1;
			}
			datas.add(data);
		}
		
		v.updateViews(datas);
		return v;
	}
	
	private void setRecmdDailyViewData(RecmdDailyView v){
		
		ArrayList<ItemData> datas = new ArrayList<ItemData>();
		for(int i=0; i<6; i++){
			ItemData data = new ItemData();
			data.title = " title ** " + i;
			data.listnum = " num *** " + i;
			if(i/2 == 0){
				data.type = 2;
			}else{
				data.type = 1;
			}
			datas.add(data);
		}
		
		v.updateViews(datas);
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		//是否对用户可见 决定一些view的动画是否执行
//		if (isVisibleToUser) {
//			if (mFocusImgView != null) {
//				mFocusImgView.startScroll();
//			}
//		} else {
//			if (mFocusImgView != null) {
//				mFocusImgView.stopScroll();
//			}
//		}
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
		mRemdDailyView = new RecmdDailyView(getActivity());
		mAdapter = new MergeAdapter();
		
		mAdapter.addView(mRemdDailyView);
		
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onStartLoadData() {
		// TODO Auto-generated method stub
		super.onStartLoadData();
		setRecmdDailyViewData(mRemdDailyView);
		onLoadFinish();
	}
	
	

}