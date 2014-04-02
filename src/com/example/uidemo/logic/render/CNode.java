package com.example.uidemo.logic.render;

import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * 鍗曚釜瀵硅薄鑺傜偣
 * @author yangzc
 *
 */
public abstract class CNode implements OnTouchListener {

	private static final String LOG_TAG = CNode.class.getSimpleName();

	public static final int FILL_PARENT = -1;
	
	private CNode mParent;
	//瀵归綈鏂瑰紡
	protected CAlign mAlign = null;
	//鑺辫垂鐨勬椂闂�
	private float elapsed = 0;
	//z杞寸储寮�
	private int mZindex;

	private int mX = 0, mY = 0;
	private int mWidth = FILL_PARENT;
	private int mHeight = FILL_PARENT;
	
	private Paint mPaint;
	
	private static Random mRandom = new Random();
	
	public CNode(){}
	
	public void setParent(CNode parent){
		this.mParent = parent;
	}

	/**
	 * 娓叉煋
	 * @param canvas
	 */
	public void render(Canvas canvas){
		if(mPaint != null){
			canvas.drawRect(new Rect(getPosition().x, getPosition().y, 
					getPosition().x + getWidth(), getPosition().y + getHeight()), mPaint);
		}
	}
	
	/**
	 * 鍒锋柊甯�
	 * @param dt
	 */
	public void update(float dt){
//		DebugUtils.debug(LOG_TAG, "update");
		elapsed += dt;
	}
	
	/**
	 * 鑾峰緱宸茬粡浣跨敤鏃堕棿
	 * @return
	 */
	protected float getElapsed(){
		return elapsed;
	}
	
	/**
	 * 閲嶇疆鐘舵�
	 */
	public void reset(){
		elapsed = 0f;
	}
	
	/**
	 * 璁剧疆鑺傜偣浣嶇疆
	 * @param position
	 */
	public void setPosition(Point position){
		this.mX = position.x;
		this.mY = position.y;
	}
	
	/**
	 * 璁剧疆view澶у皬
	 * @param width
	 * @param height
	 */
	public void setViewSize(int width, int height){
		this.mWidth = width;
		this.mHeight = height;
	}
	
	/**
	 * 鍐呭瀹藉害
	 * @return
	 */
	public int getWidth() {
		if(mParent != null){
			if(mWidth == FILL_PARENT){
				return mParent.getWidth();
			}
			return mWidth;
		}
		if(Director.getSharedDirector().getViewSize() == null)
			return 0;
		
		return Director.getSharedDirector().getViewSize().width();
	}
	
	/**
	 * 鍐呭楂樺害
	 * @return
	 */
	public int getHeight() {
		if(mParent != null){
			if(mHeight == FILL_PARENT){
				return mParent.getHeight();
			}
			return mHeight;
		}
		if(Director.getSharedDirector().getViewSize() == null){
			return 0;
		}
		return Director.getSharedDirector().getViewSize().height();
	}
	
	/**
	 * 璁剧疆瀵归綈鏂瑰紡
	 * @param align
	 */
	public void setAlign(CAlign align){
		if(align == null)
			return;
		this.mAlign = align;
	}
	
	public enum CAlign{
		TOP_LEFT(0)
		,TOP_CENTER(1)
		,TOP_RIGHT(2)
		
		,CENTER_LEFT(3)
		,CENTER_CENTER(4)
		,CENTER_RIGHT(5)
		
		,BOTTOM_LEFT(6)
		,BOTTOM_CENTER(7)
		,BOTTOM_RIGHT(8);
		
		CAlign(int type){
			this.type = type;
		}
		
		private int type;
		
		public int getValue(){
			return type;
		}
	}
	
	/**
	 * 璁剧疆z杞寸储寮�
	 * @param zindex
	 */
	public void setZindex(int zindex){
		this.mZindex = zindex;
	}
	
	/**
	 * 鑾峰緱z杞寸储寮�
	 * @return
	 */
	public int getZindex(){
		return mZindex;
	}
	
    @Override
    public boolean onTouch(View v, MotionEvent event) {
    	return false;
    }
    
	/**
	 * 鑾峰緱闅忔満鏁板璞�
	 * @return
	 */
	public static Random getRandomObj(){
		if(mRandom == null)
			mRandom = new Random();
		return mRandom;
	}
	
	/** 
     * 鏍规嵁鎵嬫満鐨勫垎杈ㄧ巼浠�dp 鐨勫崟浣�杞垚涓�px(鍍忕礌) 
     */  
	public int dip2px(float dpValue) {  
        return Director.getSharedDirector().dp2px(dpValue);
    }
    
	/**
	 * 鑾峰緱鑺傜偣浣嶇疆
	 * @return
	 */
	public Point getPosition(){
		if(Director.getSharedDirector().getViewSize() == null)
			return new Point(mX, mY);
		
		int pleft = 0, ptop = 0;
		int pwidth = Director.getSharedDirector().getViewSize().width();
		int pheight = Director.getSharedDirector().getViewSize().height();
		if(mParent != null){
			pleft = mParent.getPosition().x;
			ptop = mParent.getPosition().y;
		}
		
		if(mAlign != null){
			if(mParent != null){
				pwidth = mParent.getWidth();
				pheight = mParent.getHeight();
			}
			
			switch (mAlign) {
			case TOP_LEFT:
				return new Point(pleft, ptop);
			case TOP_CENTER:
				return new Point(pleft + (pwidth - getWidth())/2, ptop + 0);
			case TOP_RIGHT:
				return new Point(pleft + pwidth - getWidth(), ptop + 0);
				
			case CENTER_LEFT:
				return new Point(pleft, ptop + (pheight - getHeight())/2);
			case CENTER_CENTER:
				return new Point(pleft + (pwidth - getWidth())/2, ptop + (pheight - getHeight())/2);
			case CENTER_RIGHT:
				return new Point(pleft + pwidth - getWidth(), ptop + (pheight - getHeight())/2);
				
			case BOTTOM_LEFT:
				return new Point(pleft + 0, ptop + pheight - getHeight());
			case BOTTOM_CENTER:
				return new Point(pleft + (pwidth - getWidth())/2, ptop + pheight - getHeight());
			case BOTTOM_RIGHT:
				return new Point(pleft + pwidth - getWidth(), ptop + pheight - getHeight());
			default:
				break;
			}
		}
		return new Point(pleft + mX, ptop + mY);
	}
	
	/**
	 * 璁剧疆鑳屾櫙棰滆壊
	 * @param color
	 */
	public void setColor(int color){
		if(mPaint == null)
			mPaint = new Paint();
		mPaint.setColor(color);
	}
	
	/**
	 * 妫�煡鎵�湁鑺傜偣鏄惁鏈夊彉鍖�
	 * @return
	 */
	public boolean isActived(){
		return true;
	}
}
