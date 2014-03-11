package com.example.uidemo;

import com.example.uidemo.ui.online.OnlineArtistFragment;
import com.example.uidemo.ui.online.OnlinePlayListFragment;
import com.example.uidemo.ui.online.OnlineRadioFragment;
import com.example.uidemo.ui.online.OnlineRankFragment;
import com.example.uidemo.ui.online.OnlineRecommendFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class TabThreePagerAdapter extends FragmentPagerAdapter{
	FragmentManager mfm;
	TabThreeView mTabView;

	public TabThreePagerAdapter(FragmentManager fm, TabThreeView tabview) {
		super(fm);
		// TODO Auto-generated constructor stub
		mfm = fm;
		mTabView = tabview;
	}

	@Override
	public Fragment getItem(int positon) {
		// TODO Auto-generated method stub
		Fragment f = null;
		switch(positon){
		case 0:
			f = new OnlineRecommendFragment();
			break;
		case 1:
			f = new OnlinePlayListFragment();
			break;
		case 2:
			f = new OnlineRankFragment();
			break;
		case 3:
			f = new OnlineArtistFragment();
			break;
		case 4:
			f = new OnlineRadioFragment();
			break;
		default:
			break;
			
		}
		return f;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 5;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		return super.instantiateItem(container, position);
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return super.isViewFromObject(view, object);
	}
	
	public String makeFragmentTag(int index){
		return "android:switcher:"  + R.id.online_main_pager + ":" + index;
	}
	
}