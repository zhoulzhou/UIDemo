package com.example.uidemo;

import java.lang.ref.SoftReference;

import com.example.uidemo.base.BaseFragment;
import com.example.uidemo.base.BaseFragmentActivity;
import com.example.uidemo.ui.BasePopFragment;
import com.example.uidemo.ui.VerticalAnimationView;
import com.example.uidemo.ui.ViewManager;
import com.example.uidemo.ui.playview.PlayingFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends BaseFragmentActivity implements OnClickListener{
	private TextView mBottomText;
	private VerticalAnimationView mPlayView;
	ViewGroup mCoreView;
	ViewManager mViewManager;
	private MainFragment mHomeFragment;
	private static SoftReference<MainActivity> mMainRefrence;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mMainRefrence = new SoftReference<MainActivity>(this);
		
		mBottomText = (TextView) findViewById(R.id.bottomview);
		mBottomText.setOnClickListener(this);
		mPlayView = (VerticalAnimationView) findViewById(R.id.vav);
		
//		showPlayerView();
		initViewManager();
	}
	
	public static MainActivity getMain(){
		if(mMainRefrence != null){
			return mMainRefrence.get();
		}
		return null;
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
	
	public void doShowAction(BaseFragment fragment, boolean save, Bundle data){
		if(fragment == null){
			return ;
		}
		BasePopFragment popFragment = new BasePopFragment();
		popFragment.setArguments(data);
		if(data != null){
			Bundle extras = fragment.getArguments();
			extras.putAll(data);
		}
		
		popFragment.setFragment(fragment);
		mViewManager.showPage(popFragment, true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch(id){
		case R.id.bottomview:
			showPlayerView();
			break;
		default :
			break;
		}
	}

}
