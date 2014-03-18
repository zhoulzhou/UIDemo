/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.uidemo.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;


/**
 * Sub-class of ImageView which automatically notifies the drawable when it is
 * being displayed.
 */
public class RecyclingImageView extends ImageView {
	private final static String TAG = "RecyclingImageView";

	public RecyclingImageView(Context context) {
		super(context);
		if (getBackground() == null) {
			setBackgroundColor(0xffffffff);
		}
	}

	public RecyclingImageView(Context context, AttributeSet attrs) {
		super(context, attrs);

		if (getBackground() == null) {
			// setBackgroundColor(0xffe2e2e2);
			setBackgroundColor(0xffffffff);
		}
	}

	/**
	 * @see android.widget.ImageView#onDetachedFromWindow()
	 */
	@Override
	protected void onDetachedFromWindow() {
		// This has been detached from Window, so clear the drawable
//		LogUtil.d(TAG, "onDetachedFromWindow >>" + this);
		setImageDrawable(null);
		super.onDetachedFromWindow();
	}

	/**
	 * @see android.widget.ImageView#setImageDrawable(android.graphics.drawable.Drawable)
	 */
	@Override
	public void setImageDrawable(Drawable drawable) {
		// Keep hold of previous Drawable

		final Drawable previousDrawable = getDrawable();

		// Call super to set new Drawable
		super.setImageDrawable(drawable);

		// Notify new Drawable that it is being displayed

		if (previousDrawable != drawable) {
			notifyDrawable(drawable, true);

			// Notify old Drawable so it is no longer being displayed
			notifyDrawable(previousDrawable, false);
		}
	}

	public boolean requsetLayout = true;

	@Override
	public void requestLayout() {
		// TODO Auto-generated method stub
		if (requsetLayout) {
			super.requestLayout();
		}
	}

	/**
	 * Notifies the drawable that it's displayed state has changed.
	 * 
	 * @param drawable
	 * @param isDisplayed
	 */
	protected static void notifyDrawable(Drawable drawable, final boolean isDisplayed) {
		if (drawable instanceof RecyclingBitmapDrawable) {
			// The drawable is a CountingBitmapDrawable, so notify it
			((RecyclingBitmapDrawable) drawable).setIsDisplayed(isDisplayed);
		} else if (drawable instanceof LayerDrawable) {
			// The drawable is a LayerDrawable, so recurse on each layer
			LayerDrawable layerDrawable = (LayerDrawable) drawable;
			for (int i = 0, z = layerDrawable.getNumberOfLayers(); i < z; i++) {
				notifyDrawable(layerDrawable.getDrawable(i), isDisplayed);
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		try {
			super.onDraw(canvas);
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			LogUtil.e(TAG, "error >" + getTag());
			// e.printStackTrace();
		}
	}
}
