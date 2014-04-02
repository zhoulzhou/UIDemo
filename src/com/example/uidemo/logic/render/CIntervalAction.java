package com.example.uidemo.logic.render;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;


/**
 * 鏃堕棿鐗囧姩浣�
 * @author yangzc
 */
public class CIntervalAction extends CFiniteTimeAction {
	
	protected volatile float elapsed;
	protected volatile boolean firstTick;
	protected volatile Interpolator mInterpolator = new LinearInterpolator();

    /**
     * 鑾峰緱宸茬粡浣跨敤鏃堕棿
     * @return
     */
    public float getElapsed() {
        return elapsed;
    }

    protected CIntervalAction(float d) {
        super(d);
        elapsed = 0.0f;
        firstTick = true;
    }

    @Override
    public synchronized boolean isDone() {
        return super.isDone() || (elapsed >= mDuration);
    }
    
    @Override
    public void stop() {
    	super.stop();
    }
    
    @Override
    public synchronized void update(float dt) {
    	super.update(dt);
    	if (firstTick) {
            firstTick = false;
            elapsed = 0;
        } else
            elapsed += dt;
    }

    @Override
    public synchronized void start(CActionNode actionNode) {
        super.start(actionNode);
        elapsed = 0.0f;
        firstTick = true;
    }
    
    public synchronized void reset(){
        elapsed = 0.0f;
        firstTick = true;
    }
    
    /**
     * 鑾峰緱搴﹁繃鐧惧垎姣�
     * @return
     */
    public float getElapsePercent(){
    	if(mInterpolator != null){
    		float result = mInterpolator.getInterpolation(getElapsed()/getDuration());
//    		if(result <=0)
//    			return 0;
//    		if(result > 1)
//    			return 1;
    		
    		return result;
    	}
    	return getElapsed()/getDuration();
    }
    
    /**
     * 璁剧疆鎻掑叆鍣�
     * @param interpolator
     */
    public void setInterpolator(Interpolator interpolator){
    	this.mInterpolator = interpolator;
    }
}
