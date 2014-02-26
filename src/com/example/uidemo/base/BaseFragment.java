package com.example.uidemo.base;

import com.example.uidemo.ui.BasePopFragment;
import com.example.uidemo.ui.ViewManager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {
	protected boolean mInited = false;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		mInited = false;
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (!mFragmnetCached){
			
		}
		super.onResume();
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
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mInited = true;
	}
	
	public String getKey() {
		return getClass().getSimpleName();
	}

	public String getFragmentTag() {
		return getClass().getSimpleName();
	}
	
	private BasePopFragment mPopFragment = null;
	public void setPopFragment(BasePopFragment popFragment){
		mPopFragment = popFragment;
	}
	
	public boolean dispatchBackPressed() {

		return ViewManager.getInstance().dismissPage();

		// return false;
	}
	
	public void onStartLoadData() {

	}
	
	
	protected boolean mFragmnetCached = false;// 音乐的页面中首页面的几个都是存放在内存中的，加载uimain时，
	/**
	 * 首页面需要加载在内存中的页面需要在oncreatView中调用该方法
	 * */
	public void setFragmentCached() {
		mFragmnetCached = true;
	}
}