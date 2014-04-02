package com.example.uidemo.logic.render;

import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * 鍩虹绾圭悊
 * @author yangzc
 */
public class CBaseTexture extends CNode {

	Matrix mMatrix;
	Paint mPaint;
	
	int mAlpha = 255;
	float mScaleX = 1;
	float mScaleY = 1;
	float mAnchorX = 0;
	float mAnchorY = 0;
	float mDegrees = 0;
	float mSkewX = 0f;
	float mSkewY = 0f;
//	int mX = 0;
//	int mY = 0;
	
	public CBaseTexture(){
		initTexture();
	}
	
	/**
	 * 鍒濆鍖朤exture
	 */
	private void initTexture(){
		mMatrix = new Matrix();
		mPaint = new Paint();
	}
	
	/**
	 * 璁剧疆alpha
	 * @param alpha
	 */
	public void setAlpha(int alpha){
		this.mAlpha = alpha;
		invalidate();
	}
	
	/**
	 * 鑾峰緱閫忔槑搴�
	 * @return
	 */
	public int getAlpha(){
		return mAlpha;
	}
	
	/**
	 * 缂╂斁鍥剧墖
	 * @param sx
	 * @param sy
	 */
	public void setScale(float sx, float sy){
		this.mScaleX = sx;
		this.mScaleY = sy;
		invalidate();
	}
	
	/**
	 * 璁剧疆閿氱偣
	 * @param x
	 * @param y
	 */
	public void setAnchor(int x, int y){
		this.mAnchorX = x;
		this.mAnchorY = y;
		invalidate();
	}
	
	/**
	 * 璁剧疆鍊炬枩瑙掑害
	 * @param skewX
	 * @param skewY
	 */
	public void setSkew(float skewX, float skewY){
		this.mSkewX = skewX;
		this.mSkewY = skewY;
	}
	
	/**
	 * 鏃嬭浆
	 * @param degrees
	 */
	public void rotate(float degrees){
		this.mDegrees = degrees;
		invalidate();
	}
	
	/**
	 * 璁剧疆浣嶇疆
	 * @param x
	 * @param y
	 */
//	public void setPosition(int x, int y){
//		this.mX = x;
//		this.mY = y;
//		invalidate();
//	}
	
	/**
	 * 鏇存柊鏁版嵁
	 */
	private void invalidate(){
		if(mPaint == null)
			mPaint = new Paint();
		mPaint.reset();
		mPaint.setAlpha(mAlpha);
		
		if(mMatrix == null){
			mMatrix = new Matrix();
		}
		mMatrix.reset();
//		mMatrix.postTranslate(mX, mY);//璁剧疆浣嶇疆
		mMatrix.postScale(mScaleX, mScaleY, mAnchorX, mAnchorY);//缂╂斁
		mMatrix.postRotate(mDegrees, mAnchorX, mAnchorY);//鏃嬭浆
		mMatrix.postSkew(mSkewX, mSkewY, mAnchorX, mAnchorY);//瑙嗛噹
	}
	
	/**
	 * 鍒濆鍖栧彉閲�
	 */
	public void reset(){
		this.mAlpha = 255;
		this.mScaleX = 1;
		this.mScaleY = 1;
		this.mAnchorX = 0;
		this.mAnchorY = 0;
		this.mDegrees = 0f;
//		this.mX = 0;
//		this.mY = 0;
		invalidate();
	}
}
