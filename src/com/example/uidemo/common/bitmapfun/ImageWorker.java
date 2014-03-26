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

import java.lang.ref.WeakReference;

import com.example.uidemo.utils.LogUtil;
import com.example.uidemo.utils.StringUtils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ImageView;

/**
 * This class wraps up completing some arbitrary long running work when loading
 * a bitmap to an ImageView. It handles things like using a memory and disk
 * cache, running the work in a background thread and setting a placeholder
 * image.
 */
public abstract class ImageWorker {
	private static final String TAG = "ImageWorker";
	private static final int FADE_IN_TIME = 200;

	private ImageCache mImageCache;
	private ImageCache.ImageCacheParams mImageCacheParams;
	private Bitmap mLoadingBitmap;
	public boolean mFadeInBitmap = true;
	private boolean mExitTasksEarly = false;
	protected boolean mPauseWork = false;
	private final Object mPauseWorkLock = new Object();

	protected Resources mResources;

	private static final int MESSAGE_CLEAR = 0;
	private static final int MESSAGE_INIT_DISK_CACHE = 1;
	private static final int MESSAGE_FLUSH = 2;
	private static final int MESSAGE_CLOSE = 3;

	protected ImageWorker(Context context) {
		mResources = context.getResources();
	}

	/**
	 * 重新封装加载图片的方法
	 * 
	 * 解决加载本地图片或者安装包内图片引起的内存崩溃问题
	 * 
	 * @param param
	 * @param imageView
	 */
	public void loadImage(ImageParam param, ImageView imageView) {
		try {
			loadImageCore(param, imageView);
		} catch (Throwable e) {
			getImageCache().clearCache();
			e.printStackTrace();
		}
	}

	private void loadImageCore(ImageParam param, ImageView imageView) {
		LogUtil.d(TAG, "load >loadImageCore>" + imageView);
		if (imageView == null || param == null)
			return;

		if (!param.hasSize()) {
			LogUtil.v("ImageWorker", "width: " + imageView.getMeasuredWidth() + ", height: " + imageView.getMeasuredHeight());
			param.setWidth(imageView.getMeasuredWidth());
			param.setHeight(imageView.getMeasuredHeight());
		}

		String data = param.getKey();
		
		if (param.getDefaultResDrawableId() != 0) {
			imageView.setImageResource(param.getDefaultResDrawableId());
		} else {
			imageView.setImageDrawable(new ColorDrawable(0x00000000));
		}

		if (StringUtils.isEmpty(data) && param.getResid() == -1 && StringUtils.isEmpty(param.getLocalPath())) {

			return;
		}

		BitmapDrawable value = null;

		// if (data.equals(imageView.getTag())) {
		// return;
		// }
		// imageView.setTag(data);

		if (mImageCache != null) {
			value = mImageCache.getBitmapFromMemCache(param);
			LogUtil.i(TAG, "load >>" + value);
		}

		if (value != null) {
			// Bitmap found in memory cache
			imageView.setImageDrawable(value);

			// setImageDrawable(imageView, value);

		} else if (cancelPotentialWork(data, imageView)) {

			final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			Drawable drawable = null;
			if (param.getDefaultResDrawableId() != 0) {
				drawable = mResources.getDrawable(param.getDefaultResDrawableId());

			} else {
				drawable = new ColorDrawable(0x00000000);
			}
			final AsyncDrawable asyncDrawable = new AsyncDrawable(new Drawable[] { drawable }, task);
			imageView.setImageDrawable(asyncDrawable);

			// NOTE: This uses a custom version of AsyncTask that has been
			// pulled from the
			// framework and slightly modified. Refer to the docs at the top of
			// the class
			// for more info on what was changed.
			task.executeOnExecutor(AsyncTask.DUAL_THREAD_EXECUTOR, param);
		}
	}

	/**
	 * Set placeholder bitmap that shows when the the background thread is
	 * running.
	 * 
	 * @param bitmap
	 */
	public void setLoadingImage(Bitmap bitmap) {
		mLoadingBitmap = bitmap;
	}

	/**
	 * Set placeholder bitmap that shows when the the background thread is
	 * running.
	 * 
	 * @param resId
	 */
	public void setLoadingImage(int resId) {
		mLoadingBitmap = BitmapFactory.decodeResource(mResources, resId);
	}

	/**
	 * Adds an {@link ImageCache} to this {@link ImageWorker} to handle disk and
	 * memory bitmap caching.
	 * 
	 * @param fragmentManager
	 * @param cacheParams
	 *            The cache parameters to use for the image cache.
	 */
	protected void addImageCache(FragmentManager fragmentManager, ImageCache.ImageCacheParams cacheParams) {
		mImageCacheParams = cacheParams;
		mImageCache = ImageCache.getInstance(fragmentManager, mImageCacheParams);
		new CacheAsyncTask().execute(MESSAGE_INIT_DISK_CACHE);
	}

	/**
	 * Adds an {@link ImageCache} to this {@link ImageWorker} to handle disk and
	 * memory bitmap caching.
	 * 
	 * @param activity
	 * @param diskCacheDirectoryName
	 *            See
	 *            {@link ImageCache.ImageCacheParams#ImageCacheParams(Context, String)}
	 *            .
	 */
	// public void addImageCache(FragmentActivity activity, String
	// diskCacheDirectoryName) {
	// mImageCacheParams = new ImageCache.ImageCacheParams(activity,
	// diskCacheDirectoryName);
	// mImageCache =
	// ImageCache.getInstance(activity.getSupportFragmentManager(),
	// mImageCacheParams);
	// new CacheAsyncTask().execute(MESSAGE_INIT_DISK_CACHE);
	// }

	/**
	 * If set to true, the image will fade-in once it has been loaded by the
	 * background thread.
	 */
	public void setImageFadeIn(boolean fadeIn) {
		mFadeInBitmap = fadeIn;
	}

	public void setExitTasksEarly(boolean exitTasksEarly) {
		mExitTasksEarly = exitTasksEarly;
		setPauseWork(false);
	}

	protected abstract Bitmap processBitmap(Object data);

	/**
	 * @return The {@link ImageCache} object currently being used by this
	 *         ImageWorker.
	 */
	public ImageCache getImageCache() {
		return mImageCache;
	}

	/**
	 * Cancels any pending work attached to the provided ImageView.
	 * 
	 * @param imageView
	 */
	public static void cancelWork(ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
		if (bitmapWorkerTask != null) {
			bitmapWorkerTask.cancel(true);
			if (ImageUtils.DEBUG) {
				final Object bitmapData = bitmapWorkerTask.param.getUrl();
				LogUtil.d(TAG, "cancelWork - cancelled work for " + bitmapData);
			}
		}
	}

	/**
	 * Returns true if the current work has been canceled or if there was no
	 * work in progress on this image view. Returns false if the work in
	 * progress deals with the same data. The work is not stopped in that case.
	 */
	public static boolean cancelPotentialWork(Object data, ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
		if (bitmapWorkerTask != null) {

			final ImageParam bitmapData = bitmapWorkerTask.param;
			if (bitmapData == null || StringUtils.isEmpty(bitmapData.getUrl()) || !bitmapData.getUrl().equals(data)) {
				bitmapWorkerTask.cancel(true);
				if (ImageUtils.DEBUG) {
					LogUtil.d(TAG, "cancelPotentialWork - cancelled work for " + data);
				}
			} else {
				// The same work is already in progress.
				return false;
			}
		}
		return true;
	}

	/**
	 * @param imageView
	 *            Any imageView
	 * @return Retrieve the currently active work task (if any) associated with
	 *         this imageView. null if there is no such task.
	 */
	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				LogUtil.e(TAG, "getBitmapWorkerTask  AsyncDrawable >> " + asyncDrawable.getBitmapWorkerTask());
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		LogUtil.e(TAG, "getBitmapWorkerTask  >> null");
		return null;
	}

	/**
	 * The actual AsyncTask that will asynchronously process the image.
	 */
	private class BitmapWorkerTask extends AsyncTask<Object, Void, BitmapDrawable> {
		private ImageParam param;
		private boolean fadeing;
		private final WeakReference<ImageView> imageViewReference;

		// private ImageView mImageView;

		public BitmapWorkerTask(ImageView imageView) {
			imageViewReference = new WeakReference<ImageView>(imageView);
			// mImageView = imageView;
		}

		/**
		 * Background processing.
		 */
		@Override
		protected BitmapDrawable doInBackground(Object... params) {
			if (ImageUtils.DEBUG) {
				LogUtil.d(TAG, "doInBackground - starting work");
			}

			param = (ImageParam) params[0];

			// final String dataString = param.getUrl();
			Bitmap bitmap = null;
			BitmapDrawable drawable = null;

			// Wait here if work is paused and the task is not cancelled

			// synchronized (mPauseWorkLock) {
			// while (mPauseWork && !isCancelled()) {
			// try {
			// mPauseWorkLock.wait();
			// } catch (InterruptedException e) {
			// break;
			// }
			// }
			// }

			// If the image cache is available and this task has not been
			// cancelled by another
			// thread and the ImageView that was originally bound to this task
			// is still bound back
			// to this task and our "exit early" flag is not set then try and
			// fetch the bitmap from
			// the cache
			ImageView iv = getAttachedImageView();
			if (mImageCache != null && !isCancelled() && iv != null && !mExitTasksEarly) {
				// param.setWidth(iv.getMeasuredWidth());
				// param.setHeight(iv.getMeasuredHeight());
				if (param.getResid() != -1) {
					bitmap = ImageResizer.decodeSampledBitmapFromResource(mResources, param.getResid(), param.getWidth(), param.getHeight(), mImageCache);
				} else {
					LogUtil.i(TAG, "------>>>>>>>>>>>>1<<<<<<<<");
					bitmap = mImageCache.getBitmapFromDiskCache(param);
					if (ImageUtils.DEBUG) {
						LogUtil.i(TAG, param.getUrl() + "get bitmap from disk -->" + bitmap);
					}
				}
			}

			// If the bitmap was not found in the cache and this task has not
			// been cancelled by
			// another thread and the ImageView that was originally bound to
			// this task is still
			// bound back to this task and our "exit early" flag is not set,
			// then call the main
			// process method (as implemented by a subclass)

			LogUtil.i(TAG, "------>>>>>>>>>>>>2<<<<<<<<" + bitmap + ">" + isCancelled() + ">" + iv + ">>" + mExitTasksEarly + ">>" + param.getResid());

			if (bitmap == null && !isCancelled() && iv != null && !mExitTasksEarly && param.getResid() == -1) {

				LogUtil.i(TAG, "------>>>>>>>>>>>>2<<<<<<<<");

				bitmap = processBitmap(params[0]);
				fadeing = true;
			}

			// If the bitmap was processed and the image cache is available,
			// then add the processed
			// bitmap to the cache for future use. Note we don't check if the
			// task was cancelled
			// here, if it was, and the thread is still running, we may as well
			// add the processed
			// bitmap to our cache as it might be used again in the future

			if (bitmap != null) {
				// if (param.getType() == ImageType.TYPE_PORTRAIT_CIRCLE ||
				// param.getType() == ImageType.TYPE_TAKE_PHOTO_PORTRAIT) {
				// bitmap = toRoundBitmap(bitmap);
				// } else if (param.getType() == ImageType.TYPE_ARTICEL) {
				// if (bitmap.getHeight() < bitmap.getWidth()) {
				// bitmap = createReflectedImage(bitmap);
				// // bitmap = createShadowBitmap(bitmap);
				// }
				// }
				if (param.getCornerDip() > 0) {
					float r = mResources.getDisplayMetrics().density * param.getCornerDip();
					// bitmap = toRoundBitmap(bitmap, r);
					bitmap = ImageUtil.round(bitmap, (int) r, true);
				}

				if (ImageUtils.hasHoneycomb()) {
					// Running on Honeycomb or newer, so wrap in a standard
					// BitmapDrawable

					// drawable = new BitmapDrawable(mResources, bitmap);

				} else {
					// Running on Gingerbread or older, so wrap in a
					// RecyclingBitmapDrawable
					// which will recycle automagically
				}
				drawable = new RecyclingBitmapDrawable(mResources, bitmap);
				// try {
				// ((RecyclingBitmapDrawable)
				// drawable).setName(imageViewReference.get().getTag().toString());
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// // e.printStackTrace();
				// }

				// 添加缓存控制
				/*
				 * 现在存在如下问题：有些图片较大但是并不是不是经常使用。这些图片始终在缓存中并不合理。 为控制以上问题，添加如下控制
				 */
				if (mImageCache != null && param.isCacheMemable()) {
					mImageCache.addBitmapToCache(param, drawable);
				}

			}

			if (ImageUtils.DEBUG) {
				LogUtil.d(TAG, "doInBackground - finished work>" + drawable);
			}
			// return null;
			return drawable;
		}

		/**
		 * Once the image is processed, associates it to the imageView
		 */
		@Override
		protected void onPostExecute(BitmapDrawable value) {
			// if cancel was called on this task or the "exit early" flag is set
			// then we're done
			if (isCancelled() || mExitTasksEarly) {
				value = null;
			}

			final ImageView imageView = getAttachedImageView();
			if (value != null && imageView != null) {
				if (ImageUtils.DEBUG) {
					LogUtil.d(TAG, "onPostExecute - setting bitmap");
				}
				setImageDrawable(imageView, value, fadeing, param);
			}
		}

		@Override
		protected void onCancelled(BitmapDrawable value) {
			super.onCancelled(value);
			synchronized (mPauseWorkLock) {
				mPauseWorkLock.notifyAll();
			}
		}

		/**
		 * Returns the ImageView associated with this task as long as the
		 * ImageView's task still points to this task as well. Returns null
		 * otherwise.
		 */
		private ImageView getAttachedImageView() {
			final ImageView imageView = imageViewReference.get();// mImageView;//
			final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
			LogUtil.e(TAG, "getAttachedImageView >>" + bitmapWorkerTask);
			if (this == bitmapWorkerTask) {
				return imageView;
			}

			return null;
		}
	}

	/**
	 * A custom Drawable that will be attached to the imageView while the work
	 * is in progress. Contains a reference to the actual worker task, so that
	 * it can be stopped if a new binding is required, and makes sure that only
	 * the last started worker process can bind its result, independently of the
	 * finish order.
	 */

	private static class DefaultDrawable extends TransitionDrawable {

		public DefaultDrawable(Drawable[] layers) {
			super(layers);
			// TODO Auto-generated constructor stub
		}

	}

	private static class AsyncDrawable extends TransitionDrawable {
//		private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

		 BitmapWorkerTask mBitmapWorkerTask;
		public AsyncDrawable(Drawable[] layers, BitmapWorkerTask bitmapWorkerTask) {

			super(layers);

			// super(res, bitmap);
//			bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
			 mBitmapWorkerTask = bitmapWorkerTask;
		}

		public BitmapWorkerTask getBitmapWorkerTask() {
//			return bitmapWorkerTaskReference.get();
			 return mBitmapWorkerTask;
		}
	}

	/**
	 * Called when the processing is complete and the final drawable should be
	 * set on the ImageView.
	 * 
	 * @param imageView
	 * @param drawable
	 */
	private void setImageDrawable(ImageView imageView, Drawable drawable, boolean fadeing, ImageParam mParam) {
		if (mFadeInBitmap && mParam.isFading) {
			// Transition drawable with a transparent drawable and the final
			// drawable

			Drawable d = new ColorDrawable(android.R.color.transparent);
			if (mParam != null && mParam.getDefaultResDrawableId() > 0) {
				try {
					d = mResources.getDrawable(mParam.getDefaultResDrawableId());
				} catch (Error e) {
					// TODO Auto-generated catch block
				}
			}

			final TransitionDrawable td = new TransitionDrawable(new Drawable[] { d, drawable });
			// Set background to loading bitmap

			// imageView.setBackgroundDrawable(new BitmapDrawable(mResources,
			// mLoadingBitmap));

			td.startTransition(FADE_IN_TIME);
			imageView.setImageDrawable(td);
			// Message msg = uiHandler.obtainMessage(0, new
			// ImageStruct(imageView, td));
			// msg.sendToTarget();
		} else {
			imageView.setImageDrawable(drawable);
			// Message msg = uiHandler.obtainMessage(0, new
			// ImageStruct(imageView, drawable));
			// msg.sendToTarget();
		}

	}

	/**
	 * Pause any ongoing background work. This can be used as a temporary
	 * measure to improve performance. For example background work could be
	 * paused when a ListView or GridView is being scrolled using a
	 * {@link android.widget.AbsListView.OnScrollListener} to keep scrolling
	 * smooth.
	 * <p/>
	 * If work is paused, be sure setPauseWork(false) is called again before
	 * your fragment or activity is destroyed (for example during
	 * {@link android.app.Activity#onPause()}), or there is a risk the
	 * background thread will never finish.
	 */
	public void setPauseWork(boolean pauseWork) {
		synchronized (mPauseWorkLock) {
			mPauseWork = pauseWork;
			if (!mPauseWork) {
				mPauseWorkLock.notifyAll();
			}
		}
	}

	protected class CacheAsyncTask extends AsyncTask<Object, Void, Void> {

		@Override
		protected Void doInBackground(Object... params) {
			switch ((Integer) params[0]) {
			case MESSAGE_CLEAR:
				clearCacheInternal();
				break;
			case MESSAGE_INIT_DISK_CACHE:
				initDiskCacheInternal();
				break;
			case MESSAGE_FLUSH:
				flushCacheInternal();
				break;
			case MESSAGE_CLOSE:
				closeCacheInternal();
				break;
			}
			return null;
		}
	}

	protected void initDiskCacheInternal() {
		if (mImageCache != null) {
			mImageCache.initDiskCache();
		}
	}

	protected void clearCacheInternal() {
		if (mImageCache != null) {
			mImageCache.clearCache();
		}
	}

	protected void flushCacheInternal() {
		if (mImageCache != null) {
			mImageCache.flush();
		}
	}

	protected void closeCacheInternal() {
		if (mImageCache != null) {
			mImageCache.close();
			mImageCache = null;
		}
	}

	public void clearCache() {
		new CacheAsyncTask().execute(MESSAGE_CLEAR);
	}

	public void flushCache() {
		new CacheAsyncTask().execute(MESSAGE_FLUSH);
	}

	public void closeCache() {
		new CacheAsyncTask().execute(MESSAGE_CLOSE);
	}

	public static Bitmap toRoundBitmap(Bitmap source, float radius) {
		int width = source.getWidth();
		int height = source.getHeight();

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(0xff000000);

		Bitmap clipped = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(clipped);
		canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius, paint);

		paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(source, 0, 0, paint);

		source.recycle();
		return clipped;
	}

	public static Bitmap toRoundBitmap(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.RGB_565);
		Canvas canvas = new Canvas(output);

		final int color = 0xff000000;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	public static Bitmap createShadowBitmap(Bitmap originalImage) {
		final int reflectionGap = 0; // 原始图片和反射图片中间的间距
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		// 反转
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);
		// matrix.setScale(1, -1);
		// reflectionImage就是下面透明的那部分,可以设置它的高度为原始的3/4,这样效果会更好些
		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, 0, width, height, matrix, false);

		// 创建一个新的bitmap,高度为原来的两倍
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height), Config.RGB_565);
		Canvas canvasRef = new Canvas(bitmapWithReflection);

		// 先画原始的图片
		canvasRef.drawBitmap(originalImage, 0, 0, null);
		// 画间距
		Paint deafaultPaint = new Paint();
		canvasRef.drawRect(0, height, width, height + reflectionGap, deafaultPaint);

		// 画被反转以后的图片
		canvasRef.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		// 创建一个渐变的蒙版放在下面被反转的图片上面
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x80ffffff, 0x00ffffff, TileMode.CLAMP);
		// Set the paint to use this shader (linear gradient)
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvasRef.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
		// 调用ImageView中的setImageBitmap
		return bitmapWithReflection;
	}

	public static Bitmap createReflectedImage(Bitmap originalImage) {

		final int reflectionGap = 0;

		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height / 2, width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.RGB_565);

		Canvas canvas = new Canvas(bitmapWithReflection);

		canvas.drawBitmap(originalImage, 0, 0, null);

		Paint defaultPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0xc7000000, 0x00000000, TileMode.MIRROR);

		paint.setShader(shader);

		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
		reflectionImage.recycle();
		originalImage.recycle();
		return bitmapWithReflection;
	}

	static Handler uiHandler = new Handler(Looper.getMainLooper()) {
		public void handleMessage(android.os.Message msg) {
			ImageStruct imageStruct = (ImageStruct) msg.obj;
			imageStruct.imageView.setImageDrawable(imageStruct.drawble);
		}

		;
	};

	static class ImageStruct {
		ImageView imageView;
		Drawable drawble;

		public ImageStruct(ImageView imageView, Drawable drawble) {
			super();
			this.imageView = imageView;
			this.drawble = drawble;
		}

	}

}
