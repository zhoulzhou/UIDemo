package com.example.uidemo.ui.animation;

import com.example.uidemo.R;
import com.example.uidemo.logic.render.BitmapUtils;
import com.example.uidemo.logic.render.CFrameAction;
import com.example.uidemo.logic.render.CGLView;
import com.example.uidemo.logic.render.CMethodAction;
import com.example.uidemo.logic.render.CMethodAction.CMethodCallBack;
import com.example.uidemo.logic.render.CMoveToAction;
import com.example.uidemo.logic.render.CRepeatAction;
import com.example.uidemo.logic.render.CScene;
import com.example.uidemo.logic.render.CSequenceAction;
import com.example.uidemo.logic.render.CSprite;
import com.example.uidemo.logic.render.CTexture;
import com.example.uidemo.logic.render.Director;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AnimationTest extends Activity implements OnClickListener{
	TextView tv;
	Button btn;
	CGLView mCGLView;
	ImageView mBgImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.animation_test);
		
		tv = (TextView) findViewById(R.id.textview);
		btn = (Button) findViewById(R.id.btn);
		btn.setOnClickListener(this);
		mBgImageView = (ImageView) findViewById(R.id.guide_img_song);
		mCGLView = (CGLView) findViewById(R.id.glview);
		
		Director.getSharedDirector().setAttachView(mCGLView);
		Director.getSharedDirector().showScene(new Scene(this));
		Director.getSharedDirector().start();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		LogController.createInstance(getApplicationContext()).endRecordLaunch();
		mCGLView.startRefresh();
		Director.getSharedDirector().resume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mCGLView.stopReferesh();
		Director.getSharedDirector().pause();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	};
	
	private class Scene extends CScene{

		
		private Context mContext;
		private CSprite mCloth;
		private CSprite mStars;
		private CSprite mSinger;
		private int mWidth = 480;
		
		public Scene(Context context){
			this.mContext = context;
			mWidth = context.getResources().getDisplayMetrics().widthPixels;
			initScene();
		}
		
		@Override
		public synchronized void update(float dt) {
			super.update(dt);
		}
		
		private void initScene(){
			mCloth = CSprite.create(CTexture.create(BitmapUtils.decodeResource(mContext.getResources(), R.drawable.guide_img_bg)));
			int clothx = (mWidth - mCloth.getWidth())/2;
			mCloth.setPosition(new Point(clothx, 0));
			addNode(mCloth, 3);

			int width = 480;
			try {
				int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
				int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
				mBgImageView.measure(w, h); 
				width =mBgImageView.getMeasuredWidth();
//				width = mCloth.getWidth();
			} catch (Exception e) {
			}
			
			int x = (mWidth - width)/2;
			mStars = CSprite.create();
			mStars.setPosition(new Point(x, 0));
			addNode(mStars, 2);
			
			mSinger = CSprite.create(CTexture.create(BitmapUtils.decodeResource(mContext.getResources(), R.drawable.guide_singer_1)));
			mSinger.setPosition(new Point(x, 0));
			addNode(mSinger, 1);
			
			CMoveToAction moveToAction = CMoveToAction.create(clothx, - mCloth.getHeight() + dip2px(50), 1000, new AccelerateDecelerateInterpolator());
			mCloth.runAction(CSequenceAction.create(moveToAction, CMethodAction.create(new CMethodCallBack() {
				@Override
				public void onCallBack() {
//					LogUtil.v("GuiderActivity", "onCallBack");
//					runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							startShowIcon();
//						}
//					});
					//手机党都爱用的听歌、K歌神器
					
					CFrameAction frameAction = CFrameAction.create();
					frameAction.addFrame(BitmapUtils.decodeResource(mContext.getResources(), R.drawable.guide_img_stars1), 500);
					frameAction.addFrame(BitmapUtils.decodeResource(mContext.getResources(), R.drawable.guide_img_stars2), 500);
					frameAction.addFrame(BitmapUtils.decodeResource(mContext.getResources(), R.drawable.guide_img_stars3), 500);
					mStars.runAction(CRepeatAction.create(frameAction));
					
					CFrameAction singerAction = CFrameAction.create();
					Bitmap bitmap1 = BitmapUtils.decodeResource(mContext.getResources(), R.drawable.guide_singer_1);
					Bitmap bitmap2 = BitmapUtils.decodeResource(mContext.getResources(), R.drawable.guide_singer_2);
					Bitmap bitmap3 = BitmapUtils.decodeResource(mContext.getResources(), R.drawable.guide_singer_3);
					Bitmap bitmap4 = BitmapUtils.decodeResource(mContext.getResources(), R.drawable.guide_singer_4);
					
					singerAction.addFrame(bitmap1, 200);
					singerAction.addFrame(bitmap2, 100);
					singerAction.addFrame(bitmap3, 300);
					singerAction.addFrame(bitmap2, 100);
					singerAction.addFrame(bitmap1, 200);
					singerAction.addFrame(bitmap4, 300);
					mSinger.runAction(CRepeatAction.create(singerAction));
				}
			})));
		}
	
	}

	@Override
	public void onClick(View v) {
		Animation anima = AnimationUtils.loadAnimation(this,R.anim.myanima);
        findViewById(R.id.textview).startAnimation(anima);
	}
	
}