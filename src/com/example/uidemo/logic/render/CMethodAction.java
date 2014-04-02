package com.example.uidemo.logic.render;


/**
 * 鏂规硶鍔ㄤ綔
 * 鐢ㄤ簬椤哄簭鍔ㄧ敾鐨勭姸鎬佸洖璋�
 * @author yangzc
 *
 */
public class CMethodAction extends CIntervalAction {

	private volatile boolean mIsDone = false;
	
	private CMethodCallBack mMethodCallBack;
	
	protected CMethodAction(CMethodCallBack callBack) {
		super(0);
		this.mMethodCallBack = callBack;
		mIsDone = false;
	}
	
	public static CMethodAction create(CMethodCallBack callBack){
		return new CMethodAction(callBack);
	}

	@Override
	public void update(float dt) {
		super.update(dt);
		if(!mIsStarted)
			return;
		
		try {
			if(mMethodCallBack != null){
				mMethodCallBack.onCallBack();
			}
		} catch (Exception e) {
		}
		
		mIsDone = true;
	}
	
	@Override
	public boolean isDone() {
		return mIsDone;
	}
	
	@Override
	public synchronized void reset() {
		super.reset();
		mIsDone = false;
	}
	
	@Override
	public void stop() {
		super.stop();
		mIsDone = true;
	}
	
	public static interface CMethodCallBack {
		public void onCallBack();
	}
}
