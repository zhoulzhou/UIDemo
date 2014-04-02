package com.example.uidemo.logic.render;


/**
 * 鍩轰簬鏃堕棿鍔ㄤ綔
 * @author yangzc
 *
 */
public class CFiniteTimeAction extends CAction {

    protected float mDuration;

    public static CFiniteTimeAction action(float d) {
        return new CFiniteTimeAction(d);
    }

    protected CFiniteTimeAction(float d) {
        mDuration = d;
    }

    /**
     * 鑾峰緱鎸佺画鏃堕棿
     * @return
     */
    public float getDuration() {
        return mDuration;
    }

    /**
     * 璁剧疆鎸佺画鏃堕棿
     * @param duration
     */
    public void setDuration(float duration) {
        this.mDuration = duration;
    }
}
