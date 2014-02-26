package com.example.uidemo.ui.local;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uidemo.base.BaseFragment;
import com.example.uidemo.logic.local.PlayStateChangeObserver;
import com.example.uidemo.logic.local.StorageChangeObserver;

public abstract class BaseLocalFragment extends BaseFragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	StorageChangeObserver observer = new StorageChangeObserver(){

		@Override
		public void onStorageChange(boolean mounted, Bundle data) {
			// TODO Auto-generated method stub
			BaseLocalFragment.this.onStorageChange(mounted, data);
		}
		
	};
	
	PlayStateChangeObserver pscobserver = new PlayStateChangeObserver(){
		@Override
		public void onPlayStateChange(String what, Bundle data) {
			if(!mInited){
				return;
			}
			BaseLocalFragment.this.onPlayStateChange(what, data);
		}
	};

	@Override
	public void onStart() {
		//注册一些监听 通过接口实现
		super.onStart();
	}

	@Override
	public void onStop() {
		//释放一些监听
		super.onStop();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	
    public abstract void onStorageChange(boolean mounted, Bundle data);
	
	public void onPlayStateChange(String what, Bundle data) {
	
	}
	
}