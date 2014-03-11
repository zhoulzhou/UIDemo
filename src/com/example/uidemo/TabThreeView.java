package com.example.uidemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uidemo.base.BaseView;

public class TabThreeView extends BaseView implements OnClickListener, OnPageChangeListener{
	private Context mContext;
	TextView mRecommadTv;
	TextView mPlaylistTv;
	TextView mRankTv;
	TextView mArtistTv;
	TextView mRadioTv;
	
	ViewPager mPager;
	TabThreePagerAdapter mAdapter;
	int mCurrentPager = 0;
	
	Handler mHandler = new Handler();

	public TabThreeView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mContext = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.online_main, null);
		mRecommadTv = (TextView) v.findViewById(R.id.recommdlist);
		mPlaylistTv = (TextView) v.findViewById(R.id.playlist);
		mRankTv = (TextView) v.findViewById(R.id.ranklist);
		mArtistTv = (TextView) v.findViewById(R.id.artistlist);
		mRadioTv = (TextView) v.findViewById(R.id.radiolist);
		
		mRecommadTv.setOnClickListener(this);
		mPlaylistTv.setOnClickListener(this);
		mRankTv.setOnClickListener(this);
		mArtistTv.setOnClickListener(this);
		mRadioTv.setOnClickListener(this);
		
		mPager = (ViewPager) v.findViewById(R.id.online_main_pager);
		mAdapter = new TabThreePagerAdapter(getFragment().getChildFragmentManager(), this);
		
		mHandler.post(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mPager.setAdapter(mAdapter);
				mPager.setCurrentItem(mCurrentPager);
			}
			
		});
		
		mPager.setOffscreenPageLimit(1);
		mPager.setOnPageChangeListener(this);
		
		return v;
		
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void setFragment(Fragment fragment) {
		// TODO Auto-generated method stub
		super.setFragment(fragment);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch(id){
		case R.id.recommdlist:
			setCurrentClickTab(0);
			break;
		case R.id.playlist:
			setCurrentClickTab(1);
			break;
		case R.id.ranklist:
			setCurrentClickTab(2);
			break;
		case R.id.artistlist:
			setCurrentClickTab(3);
			break;
		case R.id.radiolist:
			setCurrentClickTab(4);
			break;
		default :
			break;
		}
	}
	
	private void setCurrentClickTab(int index){
		mPager.setCurrentItem(index);
		setCurrentTab(index);
	}
	
	private void setCurrentTab(int index){
		View[] views =  { mRecommadTv, mPlaylistTv, mRankTv, mArtistTv, mRadioTv};
		for (int i = 0; i < views.length; i++) {
			if (index != mCurrentPager) {
				views[index].setSelected(false);
			} else if (index == mCurrentPager) {
				views[index].setSelected(true);
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		setCurrentTab(arg0);
		mCurrentPager = arg0;
	}
	
	
	
}