package com.example.uidemo.logic.render;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

/**
 *
 */
public class CFrameAction extends CIntervalAction {
	
	private List<ActionFrame> mActionFrames;
	
	protected CFrameAction() {
		super(0);
	}
	
	public static CFrameAction create(){
		return new CFrameAction();
	}

	/**
	 * 娣诲姞甯�
	 * @param bitmap
	 * @param duration
	 */
	public void addFrame(Bitmap bitmap, int duration){
		if(mActionFrames == null)
			mActionFrames = new ArrayList<ActionFrame>();

		if(bitmap == null)
			return;
		
		ActionFrame frame = new ActionFrame();
		frame.mBitmap = bitmap;
		frame.mDuration = duration;
		mActionFrames.add(frame);
		setDuration(getDuration() + duration);
	}
	
	@Override
	public void update(float dt) {
		super.update(dt);
		if(!mIsStarted || actionNode == null || !(actionNode instanceof CSprite) || ((CSprite)actionNode).getTexture() == null
				|| mActionFrames == null || mActionFrames.isEmpty()
				|| getDuration() <= 0){
			return;
		}
		
		CSprite sprite = (CSprite) actionNode;
		
		float elapse = getElapsed()%getDuration();
		int ts = 0;
		ActionFrame curFrame = null;
		for(int i=0; i< mActionFrames.size(); i++){
			ActionFrame frame = mActionFrames.get(i);
			if(ts < elapse){
				curFrame = frame;
			}else{
				break;
			}
			ts += frame.mDuration;
		}
		
		if(curFrame != null)
			sprite.getTexture().setTexture(curFrame.mBitmap);
	}
	
	private class ActionFrame {
		public Bitmap mBitmap;
		public int mDuration;
	}
}
