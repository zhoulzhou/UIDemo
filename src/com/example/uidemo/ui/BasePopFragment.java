package com.example.uidemo.ui;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uidemo.base.BaseFragment;
import com.example.uidemo.ui.HorizontalAnimView.OnDismissListener;
import com.example.uidemo.ui.HorizontalAnimView.OnSlidFinishedLinstener;

public class BasePopFragment extends BaseFragment implements OnDismissListener, OnSlidFinishedLinstener{


	private final static String TAG = "BasePopFragment";

	HorizontalAnimView mView;

	Fragment mFragment;

	public void setFragment(Fragment fragment) {
		mFragment = fragment;
		if(fragment instanceof BaseFragment){
			((BaseFragment)fragment).setPopFragment(this);
		}
	}

	public Fragment getFragment() {
		return mFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = new HorizontalAnimView(getActivity());
		mView.setOnSlidFinishedLinstener(this);
		mView.setOnDismissListener(this);
		View view = mFragment.onCreateView(inflater, container, savedInstanceState);
		mView.getContainer().addView(view, -1, -1);
		setFragmentCached();
		return mView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);

		mFragment.onViewCreated(view, savedInstanceState);
		
//		RightSlideGuideDialog.showSlideGuide(mContext);//显示帮助界面的
	}

	boolean isScroll = true;

	public void setIsScroll(boolean b) {
		isScroll = b;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mView.show(isScroll);
		mFragment.onResume();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		mFragment.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		try {
			mFragment.onDestroyView();
			mView.removeAllViews();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		try {
			super.onDestroy();
			mFragment.onDestroy();

		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		mFragment.onDetach();
		//mFragment = null;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		mFragment.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		try {
			Field f = Fragment.class.getDeclaredField("mActivity");
			f.setAccessible(true);
			f.set(mFragment, activity);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mFragment.onAttach(getActivity());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		mFragment.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mFragment.onCreate(savedInstanceState);

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mFragment.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
		// PLAY_INFO监听
		mFragment.onStop();
	}

	@Override
	public boolean dispatchBackPressed() {
		// TODO Auto-generated method stub

		// 这个 不需要在 back 按下时调用 在 onDismiss 时会被调用
		// super.dispatchBackPressed();
		mView.dismiss();
		// return super.dispatchBackPressed();
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getKey();
	}

	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub

		super.dispatchBackPressed();
	}

	@Override
	public void onSlidFinished() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onSlidFinished >>>" + getKey());
		if (mFragment instanceof BaseFragment) {
			((BaseFragment) mFragment).onStartLoadData();
		} else {

			if(mFragment==null) return;
			
			try {
				Method m = mFragment.getClass().getDeclaredMethod("onStartLoadData", null);
				m.setAccessible(true);
				m.invoke(mFragment, null);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public String getKey() {
		// TODO Auto-generated method stub'
		if (mFragment != null && mFragment instanceof BaseFragment) {
			return ((BaseFragment) mFragment).getKey();
		}

		return super.getKey();
	}

	public String getFragmentTag() {
		return ((BaseFragment) mFragment).getFragmentTag();
	}


}