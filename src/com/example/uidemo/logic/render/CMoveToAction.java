package com.example.uidemo.logic.render;

import android.graphics.Point;
import android.view.animation.Interpolator;

/**
 * 绉诲姩鍔ㄤ綔
 * @author yangzc
 *
 */
public class CMoveToAction extends CIntervalAction {

	private Point mStart;
	private Point mTarget;
	
	protected CMoveToAction(int x, int y, float d, Interpolator interpolator) {
		super(d);
		
		mTarget = new Point(x, y);
		
		if(interpolator != null)
			setInterpolator(interpolator);
	}
	
	/**
	 * 鍒涘缓鍔ㄤ綔
	 * @param x 鐩爣浣嶇疆X
	 * @param y 鐩爣浣嶇疆Y
	 * @param d 鎸佺画鏃堕棿
	 * @param interpolator 鎻掑叆鍣�
	 * @return
	 */
	public static CMoveToAction create(int x, int y, float d, Interpolator interpolator){
		return new CMoveToAction(x, y, d, interpolator);
	}
	
	/**
	 * 鍒涘缓鍔ㄤ綔
	 * @param x 鐩爣浣嶇疆X
	 * @param y 鐩爣浣嶇疆Y
	 * @param d 鎸佺画鏃堕棿
	 * @return
	 */
	public static CMoveToAction create(int x, int y, float d){
		return new CMoveToAction(x, y, d, null);
	}

	@Override
	public void update(float dt) {
		if(mStart == null){
			mStart = actionNode.getPosition();
			return;
		}
		
		super.update(dt);
		
		if(actionNode == null || mStart == null 
				|| !mIsStarted || isDone()){
			return;
		}
		
		float elapsePercent = getElapsePercent();
		int x = 0, y =0;
		if(mTarget.x < mStart.x){
			x = (int) (mStart.x - (mStart.x - mTarget.x) * elapsePercent);
			if(mTarget.y < mStart.y){
				y = (int) (mStart.y - (mStart.y - mTarget.y) * elapsePercent);
			}else{
				y = (int) ((mTarget.y - mStart.y) * elapsePercent + mStart.y);
			}
		}else{
			x = (int) ((mTarget.x - mStart.x) * elapsePercent + mStart.x);
			if(mTarget.y < mStart.y){
				y = (int) (mStart.y - (mStart.y - mTarget.y) * elapsePercent);
			}else{
				y = (int) ((mTarget.y - mStart.y) * elapsePercent + mStart.y);
			}
		}
		
		if(actionNode != null){
			actionNode.setPosition(new Point(x, y));
		}
	}
}
