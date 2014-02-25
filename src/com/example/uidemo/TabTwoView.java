package com.example.uidemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uidemo.base.BaseView;

public class TabTwoView extends BaseView implements OnPageChangeListener, OnClickListener{
	private Context mContext;
	private TextView mFav;
	private TextView mSony;
	private TextView mArtist;
	private TextView mAlbumn;
	private TextView mFolder;
	private ViewPager mPager;
	private TabTwoPagerAdapter mAdapter;
	private static int mCurrentPager = 1;
	
	private Handler mHandler = new Handler();

	public TabTwoView(Context context) {
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
		View view = inflater.inflate(R.layout.ui_local_main_fragment, null);

		// TODO Auto-generated method stub
		mFav = (TextView) view.findViewById(R.id.local_favsongs);
		mSony = (TextView) view.findViewById(R.id.local_allsongs);
		mArtist = (TextView) view.findViewById(R.id.local_artist);
		mAlbumn = (TextView) view.findViewById(R.id.local_ablum);
		mFolder = (TextView) view.findViewById(R.id.local_folder);
		
		mFav.setOnClickListener(this);
		mSony.setOnClickListener(this);
		mArtist.setOnClickListener(this);
		mAlbumn.setOnClickListener(this);
		mFolder.setOnClickListener(this);
		
		mPager = (ViewPager) view.findViewById(R.id.main_pager);
		mAdapter = new TabTwoPagerAdapter(getFragment().getChildFragmentManager(),this);
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mPager.setAdapter(mAdapter);
				mPager.setCurrentItem(mCurrentPager);
			}
		});
		
		mPager.setOffscreenPageLimit(1);
		mPager.setOnPageChangeListener(this);
	
		return view;
		
	}

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
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
	public void onClick(View v) {
		
		Log.d("zhou","onclick");
		switch (v.getId()) {

		case R.id.local_favsongs:
			mPager.setCurrentItem(0);
			setCurrentTab(0);
			break;
		case R.id.local_allsongs:
			mPager.setCurrentItem(1);
			setCurrentTab(1);
			break;
		case R.id.local_artist:
			mPager.setCurrentItem(2);
			setCurrentTab(2);
			break;
		case R.id.local_ablum:
			mPager.setCurrentItem(3);
			setCurrentTab(3);
			break;
		case R.id.local_folder:
			mPager.setCurrentItem(4);
			setCurrentTab(4);
			break;

		default:
			break;
		}

	}

	@Override
	public void setFragment(Fragment fragment) {
		// TODO Auto-generated method stub
		super.setFragment(fragment);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setCurrentTab(arg0);
		mCurrentPager = arg0;
	}
	
	private void setCurrentTab(int page){
		TextView[] tv_tabs = {mFav, mSony, mArtist, mAlbumn, mFolder};
		for(int i=0; i< tv_tabs.length; i++){
			if(i != page){
				tv_tabs[i].setSelected(false);
			}else{
				tv_tabs[i].setSelected(true);
			}
		}
	}
	
	
	
}