package com.example.uidemo.ui;

import java.util.Stack;

import com.example.uidemo.R;
import com.example.uidemo.base.BaseFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class ViewManager{


	private final static String TAG = "ViewManager";

//	private List<View> mViews = new ArrayList<View>();
	private FragmentManager mFragmentManager;
	private ViewGroup mCoreView;

	private Stack<Fragment> mStack = new Stack<Fragment>();

	private static ViewManager mViewManager = new ViewManager();

	private ViewManager() {
	}

	public static ViewManager getInstance() {
		return mViewManager;
	}

	public void setCoreView(ViewGroup coreView) {
//		int count = coreView.getChildCount();
		mCoreView = (ViewGroup) coreView;
		mPreView = (ViewGroup) mCoreView.findViewById(R.id.container1);
		mCurrentView = (ViewGroup) mCoreView.findViewById(R.id.container2);
		mNextView = (ViewGroup) mCoreView.findViewById(R.id.container3);

	}

	public void clearAll() {
		mStack.clear();
		destroyFragment(mCurrentView);
		destroyFragment(mNextView);
		destroyFragment(mPreView);
		mPreView = (ViewGroup) mCoreView.findViewById(R.id.container1);
		mCurrentView = (ViewGroup) mCoreView.findViewById(R.id.container2);
		mNextView = (ViewGroup) mCoreView.findViewById(R.id.container3);
	}

	public void setFragmentManager(FragmentManager fragmentManager) {
		mFragmentManager = fragmentManager;
	}

	ViewGroup mCurrentView;
	ViewGroup mNextView;
	ViewGroup mPreView;

	public View getCurrentView() {
		return mCurrentView;
	}

	public synchronized void showPage(Fragment fragment, boolean saveToStack) {

		showPage(fragment, saveToStack, true);

	}

	long mLastShowTime;
	
	public void clearLastShowTime() {
		mLastShowTime = 0;
	}

	public synchronized void showPage(Fragment fragment, boolean saveToStack, boolean scroll) {
		Log.i(TAG, "showPage " + fragment + ">>>>>" + mStack);
		if (System.currentTimeMillis() - mLastShowTime < 500) {
			return;
		}

		mStack.push(fragment);
		mNextView.bringToFront();
		showFragment(fragment, saveToStack, scroll);
		destroyPreFragment();

		ViewGroup temp = mCurrentView;

		mCurrentView = mNextView;
		mNextView = mPreView;
		mPreView = temp;

		mPreView.setEnabled(false);
		mLastShowTime = System.currentTimeMillis();

	}

	public synchronized boolean dismissPage() {
		if (mStack.isEmpty()) {
			return false;
		}

		Fragment fragment = mStack.pop();
		if (mStack.isEmpty()) {
			mStack.push(fragment);
			return false;
		}

		destroyCurrentFragment();

		mPreView.setEnabled(true);
		mPreView.bringToFront();

		// mCoreView.postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// TODO Auto-generated method stub
		Fragment baseFragment = mStack.pop();

		if (!mStack.isEmpty()) {
			Fragment preloadFragment = mStack.peek();

			showFragment(preloadFragment, false, false);
		}
		mStack.push(baseFragment);

		ViewGroup temp = mPreView;
		mPreView = mNextView;
		mNextView = mCurrentView;
		mCurrentView = temp;

		// mNextView.bringToFront();
		// }
		// }, 20);
		return true;

	}

	public BaseFragment getCurrentFragment() {
		BaseFragment fragment = (BaseFragment) mFragmentManager.findFragmentById(mCurrentView.getId());
		return fragment;
	}

	void destroyPreFragment() {
		Fragment fragment = mFragmentManager.findFragmentById(mPreView.getId());
		if (fragment == null) {
			return;
		}
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.detach(fragment);
		ft.commitAllowingStateLoss();
	}

	void destroyFragment(View view) {
		Fragment fragment = mFragmentManager.findFragmentById(view.getId());
		if (fragment == null) {
			return;
		}
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.detach(fragment);
		ft.commitAllowingStateLoss();
	}

	void destroyCurrentFragment() {
		Fragment fragment = mFragmentManager.findFragmentById(mCurrentView.getId());
		if (fragment == null) {
			return;
		}
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		ft.detach(fragment);
		ft.remove(fragment);
		ft.commitAllowingStateLoss();

		mFragmentManager.popBackStack();

	}

	void preShowFragment() {

	}

	void showFragment(Fragment fragment, boolean saveToStack, boolean scroll) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		if (fragment.isDetached()) {
			ft.attach(fragment);
		}

		String name = null;

		if (fragment instanceof BasePopFragment) {
			BasePopFragment popFragment = (BasePopFragment) fragment;
			popFragment.setIsScroll(scroll);
			name = popFragment.getFragmentTag();
		}
		//ft.setCustomAnimations(R.anim.noting_anim, R.anim.noting_anim);
		ft.replace(mNextView.getId(), fragment, name);
		// if (saveToStack)
		// ft.addToBackStack(name);

		ft.commitAllowingStateLoss();
	}

	void showFragment(Fragment fragment, boolean saveToStack) {
		showFragment(fragment, saveToStack, true);
	}


	
}