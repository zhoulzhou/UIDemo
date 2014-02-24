package com.example.uidemo;

import com.example.uidemo.base.BaseFragmentActivity;
import com.example.uidemo.ui.VerticalAnimationView;
import com.example.uidemo.ui.ViewManager;
import com.example.uidemo.ui.playview.PlayingFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends BaseFragmentActivity {
	private TextView mBottomText;
	private VerticalAnimationView mPlayView;
	ViewGroup mCoreView;
	ViewManager mViewManager;
	private MainFragment mHomeFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mBottomText = (TextView) findViewById(R.id.bottomview);
		mPlayView = (VerticalAnimationView) findViewById(R.id.vav);
		
//		showPlayerView();
		initViewManager();
	}
	
	private void initViewManager(){
		mCoreView = (ViewGroup) getWindow().getDecorView();
		mViewManager = ViewManager.getInstance();
		mViewManager.setCoreView(mCoreView);

		mViewManager.setFragmentManager(getSupportFragmentManager());
		
		mHomeFragment = MainFragment.getInstance(false);
		mViewManager.showPage(mHomeFragment, true);
	}
	
	public void showPlayerView(){
		mPlayView.postDelayed(new Runnable(){

			@Override
			public void run() {
				PlayingFragment mPlayFragment = new PlayingFragment();
				FragmentTransaction ft = null;
				ft = getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.player_container_id, mPlayFragment);
				ft.commit();
				
				mPlayView.showPlayerView();
				mPlayView.snapToScreen(1);
			}
			
		}, 100);
	}

}
