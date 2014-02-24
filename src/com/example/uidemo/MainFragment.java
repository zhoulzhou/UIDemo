package com.example.uidemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.uidemo.base.BaseFragment;

public class MainFragment extends BaseFragment implements OnClickListener{
	
	public final int TAB_MYMUSIC = 0;
	public final int TAB_LOCALMUSIC = 1;
	public final int TAB_ONLINEMUSIC = 2;
	
	private TextView mTab1;
	private TextView mTab2;
	private TextView mTab3;
	
	private ViewGroup mTabOneViewContainer;
	private ViewGroup mTabTwoViewContainer;
	private ViewGroup mTabThreeViewContainer;
	
	private TabOneView mTabOneView;
	private TabTwoView mTabTwoView;
	private TabThreeView mTabThreeView;
	
	// 标记
    private int mCurrentTab = TAB_LOCALMUSIC;
	
	public static MainFragment getInstance(boolean show_online){
		MainFragment fragment = new MainFragment();
		return fragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.ui_main_fragment, null);
		return root;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mTab1 = (TextView) view.findViewById(R.id.tab_my_music);
		mTab2 = (TextView) view.findViewById(R.id.tab_local_music);
		mTab3 = (TextView) view.findViewById(R.id.tab_online_music);
		
		
		mTab1.setOnClickListener(this);
		mTab2.setOnClickListener(this);
		mTab3.setOnClickListener(this);
		
		mTabOneViewContainer = (ViewGroup) view.findViewById(R.id.mymusic_container);
		mTabTwoViewContainer = (ViewGroup) view.findViewById(R.id.local_container);
		mTabThreeViewContainer = (ViewGroup) view.findViewById(R.id.online_container);
		
		if(mTabOneView == null ){
			mTabOneView = new TabOneView(getActivity());
		}
		mTabOneView.setFragment(this);
		mTabOneView.onAttach(getActivity());
		mTabOneView.onCreate(savedInstanceState);
		mTabOneViewContainer.addView(mTabOneView.onCreateView(getActivity().getLayoutInflater(), null, savedInstanceState),-1,-1);
	
		if(mTabTwoView == null ){
			mTabTwoView = new TabTwoView(getActivity());
		}
		mTabTwoView.setFragment(this);
		mTabTwoView.onAttach(getActivity());
		mTabTwoView.onCreate(savedInstanceState);
		mTabTwoViewContainer.addView(mTabTwoView.onCreateView(getActivity().getLayoutInflater(), null, savedInstanceState),-1,-1);
		
		if(mTabThreeView == null ){
			mTabThreeView = new TabThreeView(getActivity());
		}
		mTabThreeView.setFragment(this);
		mTabThreeView.onAttach(getActivity());
		mTabThreeView.onCreate(savedInstanceState);
		mTabThreeViewContainer.addView(mTabThreeView.onCreateView(getActivity().getLayoutInflater(), null, savedInstanceState),-1,-1);
		
		
		if(mCurrentTab == TAB_MYMUSIC){
			mTab1.setSelected(true);
			mTab2.setSelected(false);
			mTab3.setSelected(false);
			
			mTabOneViewContainer.setVisibility(View.VISIBLE);
			mTabTwoViewContainer.setVisibility(View.GONE);
			mTabThreeViewContainer.setVisibility(View.GONE);
			
		}else if(mCurrentTab == TAB_LOCALMUSIC){
			mTab1.setSelected(false);
			mTab2.setSelected(true);
			mTab3.setSelected(false);
			
			mTabOneViewContainer.setVisibility(View.GONE);
			mTabTwoViewContainer.setVisibility(View.VISIBLE);
			mTabThreeViewContainer.setVisibility(View.GONE);
		}else {
			mTab1.setSelected(false);
			mTab2.setSelected(false);
			mTab3.setSelected(true);
			
			mTabOneViewContainer.setVisibility(View.GONE);
			mTabTwoViewContainer.setVisibility(View.GONE);
			mTabThreeViewContainer.setVisibility(View.VISIBLE);
		}
	
	}

	@Override
	public void onStartLoadData() {
		super.onStartLoadData();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tab_my_music:
			onMyMusicClick();
			break;
		case R.id.tab_local_music:
			onLocalMusicClick();
			break;
		case R.id.tab_online_music:
			onOnlineMusicClick();
			break;
		default:
			break;
		}
	}
	
	
	private void onMyMusicClick(){
		if(mCurrentTab == TAB_MYMUSIC){
			return ;
		}
		mTab1.setSelected(true);
		mTab2.setSelected(false);
		mTab3.setSelected(false);
		
		mTabOneViewContainer.setVisibility(View.VISIBLE);
		mTabTwoViewContainer.setVisibility(View.GONE);
		mTabThreeViewContainer.setVisibility(View.GONE);
		
		mCurrentTab = TAB_MYMUSIC;
	}
	
	private void onLocalMusicClick(){
		if(mCurrentTab == TAB_LOCALMUSIC){
			return ;
		}
		mTab1.setSelected(false);
		mTab2.setSelected(true);
		mTab3.setSelected(false);
		
		mTabOneViewContainer.setVisibility(View.GONE);
		mTabTwoViewContainer.setVisibility(View.VISIBLE);
		mTabThreeViewContainer.setVisibility(View.GONE);
		
		mCurrentTab = TAB_LOCALMUSIC;
	}
	
	private void onOnlineMusicClick(){
		if(mCurrentTab == TAB_ONLINEMUSIC){
			return ;
		}
		mTab1.setSelected(false);
		mTab2.setSelected(false);
		mTab3.setSelected(true);
		
		mTabOneViewContainer.setVisibility(View.GONE);
		mTabTwoViewContainer.setVisibility(View.GONE);
		mTabThreeViewContainer.setVisibility(View.VISIBLE);
		
		mCurrentTab = TAB_ONLINEMUSIC;
	}
	
}