package com.example.uidemo.logic.render;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 鍥剧墖閫氱敤澶勭悊
 * @author yangzc
 *
 */
public class BitmapUtils {

	/**
	 * 浠庤祫婧愪腑鑾峰彇鍥剧墖
	 * @param res
	 * @param resId
	 * @return
	 */
	public static Bitmap decodeResource(Resources res, int resId){
		return decodeResource(res, resId, -1, -1);
//		return BitmapManager.getInstance().getBitmap(res, resId);
	}
	
	/**
	 * 浠庤祫婧愪腑鑾峰彇鍥剧墖
	 * @param res
	 * @param resId
	 * @return
	 */
	public static Bitmap decodeResourceInternal(Resources res, int resId){
		return decodeResource(res, resId, -1, -1);
	}
	
	/**
	 * 浠庤祫婧愪腑鑾峰彇鍥剧墖
	 * @param res
	 * @param resId
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	@SuppressLint("NewApi")
	public static Bitmap decodeResource(Resources res, int resId, int reqWidth, int reqHeight) {
		try {
			if(res == null)
				return null;
			
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(res, resId, options);

			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			//缂╂斁淇濊瘉鍐呭瓨寮�攢
			options.inSampleSize = calculateInSampleSize(options, res.getDisplayMetrics().widthPixels, 
					res.getDisplayMetrics().heightPixels, reqWidth, reqHeight);
			//鏍规嵁鍒嗚鲸鐜囩缉鏀�
//			int density = res.getDisplayMetrics().densityDpi;
//			switch(density){
//			case DisplayMetrics.DENSITY_HIGH:
//				options.inSampleSize = 1;
//				break;
//			case DisplayMetrics.DENSITY_LOW:
//				options.inSampleSize = 2;
//				break;
//			case DisplayMetrics.DENSITY_MEDIUM:
//				options.inSampleSize = 2;
//				break;
//			case DisplayMetrics.DENSITY_TV:
//				options.inSampleSize = 1;
//				break;
//			case DisplayMetrics.DENSITY_XHIGH:
//				options.inSampleSize = 1;
//				break;
//			case DisplayMetrics.DENSITY_XXHIGH:
//				options.inSampleSize = 1;
//				break;
//			}
			options.inJustDecodeBounds = false;
			if(android.os.Build.VERSION.SDK_INT >= 11){
				options.inMutable = false;
			}
			
			return BitmapFactory.decodeResource(res, resId, options);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	/**
	 * 鑾峰緱缂╂斁姣斾緥
	 * @param options
	 * @param winWidth
	 * @param winHeight
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	private static int calculateInSampleSize(BitmapFactory.Options options, int winWidth, 
			int winHeight, int reqWidth, int reqHeight) {
		if(reqWidth < 0 || reqHeight < 0){
			return 1;
		}
		final int height = options.outHeight;
		final int width = options.outWidth;
		
		int inSampleSize = 1;
		if (height > winHeight || width > winWidth) {
			float raito = height * 1f / width;
			reqWidth = winWidth;
			reqHeight = (int) (reqWidth * raito);
		}
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
			
			//閲嶆柊楠岃瘉锛屼繚璇佸唴瀛樺紑閿�緝灏�
			final float totalPixels = width * height;
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;
			if(totalReqPixelsCap <= 0) return 1;
			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
	}
}
