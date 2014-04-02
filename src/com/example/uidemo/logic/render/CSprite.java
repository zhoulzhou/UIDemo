package com.example.uidemo.logic.render;

import android.graphics.Canvas;
import android.graphics.Point;

/**
 * 绮剧伒
 * @author yangzc
 */
public class CSprite extends CActionNode {

	//绾圭悊
	private CTexture mTexture;
	
	private CSprite(CTexture texture){
		super();
		this.mTexture = texture;
	}
	
	/**
	 * 鍒涘缓绮剧伒
	 * @param texture
	 * @return
	 */
	public static CSprite create(CTexture texture){
		if(texture == null)
			texture = CTexture.create(null);
		CSprite sprite = new CSprite(texture);
		return sprite;
	}
	
	/**
	 * 鍒涘缓绮剧伒
	 * @return
	 */
	public static CSprite create(){
		return create(null);
	}
	
	@Override
	public synchronized void update(float dt) {
		super.update(dt);
		if(mTexture != null){
			mTexture.update(dt);
		}
	}
	
	@Override
	public synchronized void render(Canvas canvas) {
		super.render(canvas);
		if(mTexture == null)
			return;

		Point position = getPosition();
		canvas.save();
		
		if(position == null){
			canvas.translate(0, 0);
		}else{
			canvas.translate(position.x, position.y);
		}
		
		mTexture.render(canvas);
		canvas.restore();
	}
	
	@Override
	public int getWidth() {
		if(mTexture != null)
			return mTexture.getWidth();
		return 0;
	}
	
	@Override
	public int getHeight() {
		if(mTexture != null)
			return mTexture.getHeight();
		return 0;
	}
	
	/**
	 * 璁剧疆绾圭悊
	 * @param texture
	 */
	public void setTexture(CTexture texture){
		this.mTexture = texture;
	}
	
	/**
	 * 鑾峰緱绾圭悊
	 * @return
	 */
	public CTexture getTexture(){
		return mTexture;
	}
	
	@Override
	public void rotate(float degrees) {
		if(mTexture != null)
			mTexture.rotate(degrees);
	}

	@Override
	public void setSkew(float skewX, float skewY) {
		if(mTexture != null)
			mTexture.setSkew(skewX, skewY);
	}

	@Override
	public void setAnchor(int x, int y) {
		if(mTexture != null)
			mTexture.setAnchor(x, y);
	}

	@Override
	public void setScale(float sx, float sy) {
		if(mTexture != null)
			mTexture.setScale(sx, sy);
	}

	@Override
	public void setAlpha(int alpha) {
		if(mTexture != null)
			mTexture.setAlpha(alpha);
	}
}
