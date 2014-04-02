package com.example.uidemo.logic.render;

import java.util.Stack;

import com.example.uidemo.logic.render.CGLView.SizeChangeListener;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


/**

 */
public class Director implements SizeChangeListener, OnTouchListener {

	private static Director _instance = null;
	
	private MainLoop mMainLooper;
	private volatile boolean mPaused = true;
	protected CGLView mAttachView = null;
	private Rect mRect = null;
	private Stack<CScene> mScenes;
	
	private Director(){
		mScenes = new Stack<CScene>();
	}
	
	public static Director getSharedDirector(){
		if(_instance == null)
			_instance = new Director();
		return _instance;
	}
	
	/**
	 * 閲婃斁瀹炰緥
	 */
	public void releaseInstance(){
		_instance = null;
	}
	
	/**
	 * 璁剧疆鍏宠仈鐨刅iew
	 * @param view
	 */
	public void setAttachView(CGLView view){
		mAttachView = view;
		pause();
		if(view == null)
			return;
		
		mAttachView.setSizeChangeListener(this);
		mAttachView.setOnTouchListener(this);
	}
	
	private int mStartCnt = 0;
	
	/**
	 * 寮�惎寮曟搸
	 */
	public boolean start(){
		if(mStartCnt > 0) {
//			DebugUtils.debug("yangzc", "start fail, reason: running");
			mStartCnt ++;
			return false;
		}
//		DebugUtils.debug("yangzc", "start");
		reset();
		
		mPaused = false;
		mMainLooper = new MainLoop();
		mMainLooper.mStarted = true;
		mMainLooper.start();
		mStartCnt ++;
		return true;
	}
	
	/**
	 * 鏆傚仠寮曟搸
	 */
	public void pause(){
		mPaused = true;
	}
	
	/**
	 * 閲嶅惎寮曟搸
	 */
	public void resume(){
		mPaused = false;
	}
	
	/**
	 * 寮曟搸鏄惁鏆傚仠
	 * @return
	 */
	public boolean isPaused(){
		return mPaused;
	}
	
	/**
	 * 鍏抽棴寮曟搸
	 */
	public void stop(){
		mStartCnt --;
		if(mStartCnt < 0)
			mStartCnt = 0;
		if(mStartCnt > 0)
			return;
//		DebugUtils.debug("yangzc", "stop");
		reset();
		release();
	}
	
	/**
	 * 閲婃斁璧勬簮
	 */
	private void release(){
		if(mAttachView != null){
			mAttachView.stopReferesh();
			mAttachView = null;
		}
		if(mScenes != null){
			mScenes.clear();
		}
		mCurrentScene = null;
	}
	
	private void reset(){
		mPaused = true;
		mStartCnt = 0;
		if(mMainLooper != null){
			mMainLooper.mStarted = false;
			mMainLooper.interrupt();
//			while (true) {
//				try {
//					mMainLooper.join();
//				} catch (InterruptedException e) {
//					continue;
//				}
//				break;
//			}
		}
		mMainLooper = null;
	}
	
	private CScene mCurrentScene;
	
	/**
	 * 鏄剧ず鍦烘櫙
	 * @param scene
	 */
	public synchronized void showScene(CScene scene){
		if(scene == null)
			return;
		
		if(mCurrentScene != null){
			mCurrentScene.onScenePause();
		}
		
		this.mCurrentScene = scene;
		mScenes.push(mCurrentScene);
		
		mCurrentScene.onSceneStart();
		mCurrentScene.onSceneResume();
	}
	
	/**
	 * 鍏抽棴鍦烘櫙
	 */
	public synchronized void closeScene(){
		if(mScenes != null && mScenes.size() > 0){
			mScenes.pop();
			if(mCurrentScene != null){
				mCurrentScene.onScenePause();
				mCurrentScene.onSceneStop();
			}
			if(mScenes.size() > 0)
				mCurrentScene = mScenes.peek();
		}
	}
	
	/**
	 * 鏆傚仠鍦烘櫙
	 */
	public void pauseScene(){
		if(mCurrentScene != null){
			mCurrentScene.onScenePause();
		}
	}
	
	/**
	 * 閲嶅惎鍦烘櫙
	 */
	public void resumeScene(){
		if(mCurrentScene != null){
			mCurrentScene.onSceneResume();
		}
	}
	
	/**
	 * 鑾峰緱娲昏穬鐨勫満鏅�
	 * @return
	 */
	public CScene getActiveScene(){
		return mCurrentScene;
	}
	
	/**
	 * 绫诲瀷杞崲
	 * @param dpValue
	 * @return
	 */
	public int dp2px(float dpValue) {
		if(mAttachView == null){
			return 0;
		}
		return UIUtils.dp2px(mAttachView.getContext(), dpValue);
    }
	
	/**
	 * 鑾峰緱褰撳墠view澶у皬
	 * @return
	 */
	public Rect getViewSize(){
		return mRect;
	}
	
	/**
	 * 璁剧疆褰撳墠绐楀彛size
	 * @param rect
	 */
	public void setViewSize(Rect rect){
		this.mRect = rect;
		if(mCurrentScene != null){
			mCurrentScene.onSizeChange(mAttachView, rect);
		}
	}
	
	/**
	 * 閲嶆瀯鏄惁鏄剧ず
	 * @return
	 */
	public boolean isViewVisable(){
		return (mRect != null && mRect.width() > 0 && mRect.height() > 0 
				&& mAttachView != null && getActiveScene() != null && mAttachView.isShown());
	}

	@Override
	public void onSizeChange(Rect rect) {
		setViewSize(rect);
		resume();
		CScene scene = getActiveScene();
		if(scene != null){
			scene.refresh();
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		CScene scene = getActiveScene();
		if(scene != null){
			return scene.onTouch(v, event);
		}
		return false;
	}
	
	private class MainLoop extends Thread {
		
		public volatile boolean mStarted = true;
		
		@Override
		public void run() {
			while(mStarted){
				CScene scene = getActiveScene();
				if(scene != null){
					scene.refresh();
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
