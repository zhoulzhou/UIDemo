package com.example.uidemo;

import com.example.uidemo.ui.local.LocalAlbumnFragment;
import com.example.uidemo.ui.local.LocalArtistFragment;
import com.example.uidemo.ui.local.LocalFavFragment;
import com.example.uidemo.ui.local.LocalFolderFragment;
import com.example.uidemo.ui.local.LocalSongFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

public class TabTwoPagerAdapter extends FragmentPagerAdapter{
	private static SparseArray<Fragment> fragments = new SparseArray<Fragment>();
	private FragmentManager mManager;
	private TabTwoView mTabTwoView;
	
	public TabTwoPagerAdapter(FragmentManager fm, TabTwoView tabTwoView) {
		super(fm);
		// TODO Auto-generated constructor stub
		mManager = fm;
		mTabTwoView = tabTwoView;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		Fragment f = null;
		switch(position){
		case 0:
			f = new LocalFavFragment();
			break;
		case 1:
			f = new LocalSongFragment();
			break;
		case 2:
			f = new LocalArtistFragment();
			break;
		case 3:
			f = new LocalAlbumnFragment();
			break;
		case 4:
			f = new LocalFolderFragment();
			break;
		default :
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
	
	public void clearFragment() {
		FragmentTransaction ft = mManager.beginTransaction();

		// Iterator iter = fragments.entrySet().iterator();
		// while(iter.hasNext()){
		// Map.Entry entry = (Map.Entry)iter.next();
		// Fragment f = (Fragment)entry.getValue();
		// ft.remove(f);
		// }

		for (int i = 0; i < 5; i++) {
			Fragment f = mManager.findFragmentByTag(makeFragmentTag(i));
			if (f != null) {
				ft.remove(f);
			}
		}

		ft.commitAllowingStateLoss();

		ft = null;
		// mManager.executePendingTransactions();

		fragments.clear();

	}
	
	/**
	 * android 源码中构造fragment的tag办法
	 * 
	 * @param index
	 * @return
	 */
	private static String makeFragmentTag(int index) {
		return "android:switcher:" + R.id.main_pager + ":" + index;
	}

	
}