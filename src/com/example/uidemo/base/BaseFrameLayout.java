package com.example.uidemo.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class BaseFrameLayout extends FrameLayout {
	protected float density;

	public BaseFrameLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initDisplay();
	}

	public BaseFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		initDisplay();
	}

	public BaseFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initDisplay();
	}

	void initDisplay() {
		density = getResources().getDisplayMetrics().density;
		setBackgroundColor(0x00000000);
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		final Drawable backgraound = getBackground();
		if (backgraound != null) {
			backgraound.draw(canvas);
		}
		long drawTime = getDrawingTime();
		final int count = getChildCount();
		View child = null;
		for (int i = 0; i < count; i++) {
			child = getChildAt(i);
			if (child.getVisibility() == View.VISIBLE) {
				drawChild(canvas, child, drawTime);
			}
		}
	}
	
}