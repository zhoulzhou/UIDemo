package com.example.uidemo;

import com.example.uidemo.base.BaseFragmentActivity;
import com.example.uidemo.ui.VerticalAnimationView;
import com.example.uidemo.ui.playview.PlayingFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

public class MainActivity extends BaseFragmentActivity {
	private TextView mBottomText;
	private VerticalAnimationView mPlayView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mBottomText = (TextView) findViewById(R.id.bottomview);
		mPlayView = (VerticalAnimationView) findViewById(R.id.vav);
		
		showPlayerView();
		
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
