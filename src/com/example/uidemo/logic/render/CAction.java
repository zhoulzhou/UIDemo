package com.example.uidemo.logic.render;

/**
 * 鍔ㄤ綔鍩虹被
 * @author yangzc
 */
public class CAction {
	
	private static final String LOG_TAG = CAction.class.getSimpleName();
	//绮剧伒
	protected CActionNode actionNode;
	protected volatile boolean mIsStarted = false;
	
    public static CAction action() {
        return new CAction();
    }

    protected CAction() {
//    	DebugUtils.debug(LOG_TAG, "init");
    }

    /**
     * 寮�鎾斁鍔ㄧ敾
     */
    public void start(CActionNode actionNode) {
//    	DebugUtils.debug(LOG_TAG, "start");
    	mIsStarted = true;
    	this.actionNode = actionNode;
    }

    /**
     * 鍋滄鎾斁鍔ㄧ敾
     */
    public void stop() {
    	mIsStarted = false;
//    	DebugUtils.debug(LOG_TAG, "stop");
    }

    /**
     * 鏄惁瀹屾垚
     * @return
     */
    public boolean isDone() {
        return !mIsStarted;
    }

    /**
     * 鏇存柊鍔ㄤ綔
     * @param time
     */
    public void update(float dt) {
//    	DebugUtils.debug(LOG_TAG, "update");
    }
    
    public void reset(){
    	
    }
}
