package com.example.uidemo.logic.render;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * 鍦烘櫙
 * @author yangzc
 */
public class CScene extends CLayer {
	
	private long mLastRefreshTs = -1;
	
	private CTextNode mTextNode;
	boolean DEBUG_ABLE = false;
	
	protected CScene(){
		super();
		if(DEBUG_ABLE){
			mTextNode = CTextNode.create();
			addNode(mTextNode, Integer.MAX_VALUE);
		}
	}
	
	public static CScene create(){
		return new CScene();
	}
	
	/**
	 * 鍒锋柊鍦烘櫙
	 */
	public void refresh(){
		if(!Director.getSharedDirector().isViewVisable())
			return;
		
		if(Director.getSharedDirector().isPaused()){
			mLastRefreshTs = -1;
			return;
		}
		
		long ts = System.currentTimeMillis();
		if(mLastRefreshTs > 0){
			update(ts - mLastRefreshTs);
			
			if(DEBUG_ABLE && mTextNode!= null){
				mTextNode.setText(1000/(ts - mLastRefreshTs) + " FPS");
			}
		}
		mLastRefreshTs = ts;
	}
	
	@Override
	public synchronized void update(float dt) {
		if(!Director.getSharedDirector().isViewVisable())
			return;
		
		if(mTextNode != null)
			mTextNode.setPosition(new Point(0, getHeight() - dip2px(20)));

//		DebugUtils.debug("yangzc", "scene: update");
//		long start = System.currentTimeMillis();
		super.update(dt);
//		DebugUtils.debug("yangzc", "cost: " + (System.currentTimeMillis() - start));
	}	

	public void onSceneStart(){}
	public void onSceneStop(){}
	
	public void onSceneResume(){}
	public void onScenePause(){}
	
	/**
	 * View澶у皬鏀瑰彉
	 * @param view
	 * @param rect
	 */
	public void onSizeChange(CGLView view, Rect rect){}
	
	@Override
	public boolean isActived() {
		return super.isActived();
	}
}
