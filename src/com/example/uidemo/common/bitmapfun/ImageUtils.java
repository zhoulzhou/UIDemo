/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.example.uidemo.common.bitmapfun;

import com.baidu.music.common.utils.DeviceUtil;
import com.baidu.music.common.utils.ImageUtil;
import com.baidu.music.common.utils.LogUtil;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.StrictMode;
import android.widget.ImageView;

/**
 * Class containing some static utility methods.
 */
public class ImageUtils {

	public final static boolean DEBUG = true;

	private ImageUtils() {
	};

	@TargetApi(11)
	public static void enableStrictMode() {
		if (ImageUtils.hasGingerbread()) {
			StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog();
			StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder().detectAll().penaltyLog();

			if (ImageUtils.hasHoneycomb()) {
				threadPolicyBuilder.penaltyFlashScreen();
				vmPolicyBuilder.setClassInstanceLimit(UIMain.class, 1).setClassInstanceLimit(UIMain.class, 1);
			}
			StrictMode.setThreadPolicy(threadPolicyBuilder.build());
			StrictMode.setVmPolicy(vmPolicyBuilder.build());
		}
	}

	public static boolean hasFroyo() {
		// Can use static final constants like FROYO, declared in later versions
		// of the OS since they are inlined at compile time. This is guaranteed
		// behavior.
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	public static boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	public static boolean isTheBitmapInImageView(Bitmap bitmap, ImageView iv) {
		if(!hasHoneycombMR1()){
			return false;
		}
		
		if (bitmap != null && iv != null) {
			if (iv.getDrawable() != null) {
				Drawable drawable = iv.getDrawable();

				if (drawable instanceof BitmapDrawable) {
					BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
					return bitmap.sameAs(bitmapDrawable.getBitmap());
				} else if (drawable instanceof LayerDrawable) {
					LayerDrawable ld = (LayerDrawable) drawable;
					for (int i = 0; i < ld.getNumberOfLayers(); i++) {
						Drawable d = ld.getDrawable(i);
						if (d != null && d instanceof BitmapDrawable) {
							BitmapDrawable bitmapDrawable = (BitmapDrawable) d;
							return bitmap.sameAs(bitmapDrawable.getBitmap());
						}
					}
				}
			} else {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 增加圆角
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @param radius
	 * @return
	 */
	public static Bitmap round(Bitmap bitmap, int width, int height,
			int radius, boolean recycleSource) {
		if (width == 0 || height == 0 || radius <= 0 || bitmap == null)
			return bitmap;

		Bitmap ret = null;
		try {
			ret = Bitmap.createBitmap(width, height, Config.RGB_565);
		} catch (OutOfMemoryError e) {
			LogUtil.e("OutOfMemoryError",
					"OutOfMemoryError in ImageUtils.round(): " + e.getMessage());
		}
		if (ret == null)
			return null;

		Canvas canvas = new Canvas(ret);
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, width, height);
		RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(0xff424242);
		canvas.drawRoundRect(rectF, radius, radius, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		if (recycleSource)
			ImageUtil.clear(bitmap);
		return ret;
	}

	/**
	 * 增加圆角
	 * 
	 * @param bitmap
	 * @param radius
	 * @return
	 */
	public static Bitmap round(Bitmap bitmap, int radius, boolean recycleSource) {
		if (radius <= 0 || bitmap == null)
			return bitmap;
		return round(bitmap, bitmap.getWidth(), bitmap.getHeight(), radius,
				recycleSource);
	}
	
	/**
	 * 释放bitmap
	 * 
	 * @param bitmap
	 */
	public static void clear(Bitmap bitmap) {
//		LogUtil.d(TAG, "recycling bitmap : " + bitmap);
		if (bitmap != null && getSdkVersionInt() < 14)
			bitmap.recycle();
	}
	
	private static int SDK_VERSION_CODE = -1;
	@SuppressWarnings("deprecation")
	public static int getSdkVersionInt() {
		if (SDK_VERSION_CODE <= 0)
			SDK_VERSION_CODE = Integer.parseInt(Build.VERSION.SDK);
		return SDK_VERSION_CODE;
	}
}
