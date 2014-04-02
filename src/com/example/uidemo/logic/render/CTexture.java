package com.example.uidemo.logic.render;


import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * 榛樿绾圭悊
 * 澶勭悊鍥剧墖灞曠幇
 * @author yangzc
 */
public class CTexture extends CBaseTexture {

	private Bitmap mBitmap;
	
	private CTexture(Bitmap bitmap){
		super();
		this.mBitmap = bitmap;
	}
	
	/**
	 * 鍒涘缓Texture
	 * @param bitmap 绾圭悊
	 * @return
	 */
	public static CTexture create(Bitmap bitmap){
		CTexture texture = new CTexture(bitmap);
		return texture;
	}
	
	@Override
	public void render(Canvas canvas) {
		super.render(canvas);
		if(mBitmap == null || mMatrix == null 
				|| mPaint == null || mBitmap.isRecycled())
			return;
		
		canvas.drawBitmap(mBitmap, mMatrix, mPaint);
	}

	/**
	 * 璁剧疆绾圭悊
	 * @param bitmap
	 */
	public void setTexture(Bitmap bitmap){
		this.mBitmap = bitmap;
	}
	
	@Override
	public int getWidth(){
		if(mBitmap != null){
			return mBitmap.getWidth();
		}
		return 0;
	}

	@Override
	public int getHeight(){
		if(mBitmap != null){
			return mBitmap.getHeight();
		}
		return 0;
	}
	
	/**
	 * 鑾峰緱绾圭悊鍥剧墖
	 * @return
	 */
	public Bitmap getTexture(){
		return mBitmap;
	}
}
