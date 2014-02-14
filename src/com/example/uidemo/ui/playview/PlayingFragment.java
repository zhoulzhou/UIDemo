package com.example.uidemo.ui.playview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.uidemo.base.BaseFragment;

public class PlayingFragment extends BaseFragment{
	PlayingView mPlayerView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mPlayerView = new PlayingView(getActivity());
		return mPlayerView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	
}