package com.example.uidemo.logic.render;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

/**
 * 瑙嗗浘鍩虹被
 * @author yangzc
 */
public class CGLView extends View {

	private static final int MSG_REFRESH_VIEW = 0x00000001;
	private int mRefreshDelay = 50;
	private Handler mHandler;
	
	public CGLView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public CGLView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CGLView(Context context) {
		super(context);
		init();
	}
	
	private void init(){
		mHandler = new Handler(Looper.getMainLooper()){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				int what = msg.what;
				switch (what) {
				case MSG_REFRESH_VIEW:
					Director director = Director.getSharedDirector();
					if(director.isViewVisable()){
//						postInvalidate();
						invalidate();
					}
					
					sendEmptyMessageDelayed(MSG_REFRESH_VIEW, mRefreshDelay);
					break;
				default:
					break;
				}
			}
		};
		mSetfil = new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG);
	}
	
	private PaintFlagsDrawFilter mSetfil;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		try {
			if(mSetfil != null)
				canvas.setDrawFilter(mSetfil);
			
			drawEngine(canvas);
		} catch (Throwable e) {
		}
	}
	
	/**
	 * 缁樼敾寮曟搸
	 * @param canvas
	 */
	protected void drawEngine(Canvas canvas){
		CScene scene = Director.getSharedDirector().getActiveScene();
		if(scene != null){
//			long start = System.currentTimeMillis();
			scene.render(canvas);
//			DebugUtils.debug("yangzc", "cost: " + (System.currentTimeMillis() - start));
		}
	}
	
	/**
	 * 璁剧疆鍒锋柊寤惰繜
	 * @param delay
	 */
	public void setRefreshDelay(int delay){
		this.mRefreshDelay = delay;
	}
	
	/**
	 * 寮�鍒锋柊
	 */
	public void startRefresh(){
		if(mHandler != null){
			mHandler.sendEmptyMessage(MSG_REFRESH_VIEW);
		}
	}
	
	/**
	 * 鍋滄鍒锋柊
	 */
	public void stopReferesh(){
		if(mHandler != null){
			mHandler.removeMessages(MSG_REFRESH_VIEW);
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Rect rect = new Rect(0, 0, getWidth(), getHeight());
		if(mSizeChangeListener != null){
			mSizeChangeListener.onSizeChange(rect);
		}
	}
	
	private SizeChangeListener mSizeChangeListener;
	public void setSizeChangeListener(SizeChangeListener sizeChangeListener){
		this.mSizeChangeListener = sizeChangeListener;
		
		if(getWidth() >0 && getHeight() > 0){
			Rect rect = new Rect(0, 0, getWidth(), getHeight());
			if(mSizeChangeListener != null){
				mSizeChangeListener.onSizeChange(rect);
			}
		}
	}
	
	public static interface SizeChangeListener {
		public void onSizeChange(Rect rect);
	}
}
