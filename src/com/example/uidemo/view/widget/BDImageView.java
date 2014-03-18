package com.example.uidemo.view.widget;

import com.example.uidemo.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.FloatMath;

public class BDImageView extends RecyclingImageView {

//	public static final float PLAYLIST_RATIO = 0.54F;
//	public static final float ARTIST_RATIO = 0.64F;
//	public static final float ALBUM_RATIO = 0.64F;

	protected Matrix mMatrix = new Matrix();

	private int mMinHeight;
	private float mHeightRatio = 0.5F;
	
	private Integer initHeight = DEFAULT_HEIGHT;
	private final static Integer DEFAULT_HEIGHT = 186;

	public BDImageView(Context context, AttributeSet attrs) {

		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.pullImage);
		initHeight = a.getInt(R.styleable.pullImage_initHeight, DEFAULT_HEIGHT);
		a.recycle();
	}

	protected Drawable mDrawable;
	protected int mLeft;
	protected int mTop;
	protected int mRight;
	protected int mBottom;
	protected int mDrawableWidth;
	protected int mDrawableHeight;

	public void setHeightRatio(float mHeightRatio) {
		this.mHeightRatio = mHeightRatio;
		invalidate();
	}

	@Override
	protected boolean setFrame(int l, int t, int r, int b) {
		// TODO Auto-generated method stub

		boolean result = super.setFrame(l, t, r, b);
		int dwidth = 0;
		int dheight = 0;
		if (mDrawable == null) {
			dwidth = dheight = getMeasuredWidth();
		} else {
			dwidth = mDrawable.getIntrinsicWidth();
			dheight = mDrawable.getIntrinsicHeight();
		}

		mLeft = l;
		mTop = t;
		mRight = r;
		mBottom = b;

		int vwidth = getWidth() - getPaddingLeft() - getPaddingRight();
		int vheight = getHeight() - getPaddingTop() - getPaddingBottom();
		float scale;
		float dx = 0, dy = 0;

		if (dwidth * vheight > vwidth * dheight) {
			scale = (float) vheight / (float) dheight;
			dx = (vwidth - dwidth * scale) * 0.5f;
		} else {
			scale = (float) vwidth / (float) dwidth;

			float dd = dheight * scale/2 - dheight * scale * 0.17f - getHeight()/2;
			if(dd > 0){
				float marginTop = dheight * scale * 0.17f;
				dy = (vheight - dheight * scale) * 0.5f + marginTop;
			}else{
				dy = - getPaddingTop();
			}
//			LogUtil.v("dheight: " + dheight + ", getHeight: " + getHeight());
//			LogUtil.v("dd: " + dd);
//			LogUtil.v("dy:" + dy + "");
		}
		mMatrix.reset();
		mMatrix.setScale(scale, scale);
		mMatrix.postTranslate((int) dx, (int) dy);

		return result;
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		// TODO Auto-generated method stub
		if (mDrawable != null) {
			mDrawable.setCallback(null);
		}
		if (drawable == null) {
			super.setImageDrawable(drawable);
			return;
		}

		mDrawable = drawable;
		mDrawableWidth = mDrawable.getIntrinsicWidth();
		mDrawableHeight = mDrawable.getIntrinsicHeight();

		// mDrawable.setBounds(0, 0, 720, 720);
		super.setImageDrawable(drawable);
		invalidate();
		if (getDrawable() != null && getDrawable() instanceof BitmapDrawable) {
			Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
			if (bitmap != null) {
				bw = bitmap.getWidth();
				bh = bitmap.getHeight();

			}
		}
	}

	private int bw = 1;
	private int bh = 1;

	public float getImageWHRaito() {
		return mDrawableWidth * 1f / mDrawableHeight;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub

		int w;
		int h;

		if (mDrawable == null) {
			mDrawableWidth = -1;
			mDrawableHeight = -1;
			w = h = getMeasuredWidth();
		} else {
			w = getMeasuredWidth();
			h = getMeasuredWidth();
			if (w <= 0)
				w = 1;
			if (h <= 0)
				h = 1;
		}

		int pleft = getPaddingLeft();
		int pright = getPaddingRight();
		int ptop = getPaddingTop();
		int pbottom = getPaddingBottom();
		// 初始高度可配置
//		LogUtil.d("BDImageView", "initHeight=" + initHeight);
		mMinHeight = (int) (getResources().getDisplayMetrics().density * initHeight);// (int) FloatMath.ceil(getMeasuredWidth() * mHeightRatio);
		int widthSize;
		int heightSize;

		w += pleft + pright;
		h += ptop + pbottom;
		int sh = mMinHeight + ptop + pbottom;
		w = Math.max(w, getSuggestedMinimumWidth());
		h = Math.min(h, sh);
		if (h > getMeasuredWidth() * 1f / getImageWHRaito()) {
			h = (int) FloatMath.ceil(getMeasuredWidth() * 1f / getImageWHRaito());
		}
		// h = Math.max(h, mMinHeight);
		widthSize = resolveSize(w, widthMeasureSpec);
		heightSize = resolveSize(h, heightMeasureSpec);

		setMeasuredDimension(widthSize, heightSize);
		postInvalidate();

	}

	@Override
	public void setPadding(int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		int h = getMeasuredHeight();
		if (h < mMinHeight) {
			return;
		}

		super.setPadding(left, top, right, bottom);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		// TODO Auto-generated method stub
		super.setImageBitmap(bm);
		bw = bm.getWidth();
		bh = bm.getHeight();
	}

	public float getHeightRatio() {
		return mHeightRatio;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		// super.onDraw(canvas);
		if (mDrawable == null) {
			return;
		}

		try {
			int saveCount = canvas.getSaveCount();
			canvas.save();
			canvas.translate(getPaddingLeft(), getPaddingTop());

			if (mMatrix != null) {
				canvas.concat(mMatrix);
			}
			mDrawable.draw(canvas);
			canvas.restoreToCount(saveCount);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
