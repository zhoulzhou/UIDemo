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

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Iterator;

import com.example.uidemo.utils.LogUtil;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * This class handles disk and memory caching of bitmaps in conjunction with the
 * {@link ImageWorker} class and its subclasses. Use
 * {@link ImageCache#getInstance(FragmentManager, ImageCacheParams)} to get an
 * instance of this class, although usually a cache should be added directly to
 * an {@link ImageWorker} by calling
 * {@link ImageWorker#addImageCache(FragmentManager, ImageCacheParams)}.
 */
public class ImageCache {
	private static final String TAG = "ImageCache";
	private static final int MAX_MEM_CACEH_SIZE = 1024 * 1024 * 20;
	// Default memory cache size in kilobytes
	private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 5; // 5MB

	// Default disk cache size in bytes
	private static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB

	// Compression settings when writing images to disk cache
	private static final CompressFormat DEFAULT_COMPRESS_FORMAT = CompressFormat.JPEG;
	private static final int DEFAULT_COMPRESS_QUALITY = 100;
	private static final int DISK_CACHE_INDEX = 0;

	// Constants to easily toggle various caches
	private static final boolean DEFAULT_MEM_CACHE_ENABLED = true;
	private static final boolean DEFAULT_DISK_CACHE_ENABLED = true;
	private static final boolean DEFAULT_INIT_DISK_CACHE_ON_CREATE = false;

	private DiskLruCache mDiskLruCache;
	private LruCache<String, BitmapDrawable> mMemoryCache;
	private ImageCacheParams mCacheParams;
	private final Object mDiskCacheLock = new Object();
	private boolean mDiskCacheStarting = true;

	private HashSet<SoftReference<Bitmap>> mReusableBitmaps;

	/**
	 * Create a new ImageCache object using the specified parameters. This
	 * should not be called directly by other classes, instead use
	 * {@link ImageCache#getInstance(FragmentManager, ImageCacheParams)} to
	 * fetch an ImageCache instance.
	 * 
	 * @param cacheParams
	 *            The cache parameters to use to initialize the cache
	 */
	private ImageCache(ImageCacheParams cacheParams) {
		init(cacheParams);
	}

	/**
	 * Return an {@link ImageCache} instance. A {@link RetainFragment} is used
	 * to retain the ImageCache object across configuration changes such as a
	 * change in device orientation.
	 * 
	 * @param fragmentManager
	 *            The fragment manager to use when dealing with the retained
	 *            fragment.
	 * @param cacheParams
	 *            The cache parameters to use if the ImageCache needs
	 *            instantiation.
	 * @return An existing retained ImageCache object or a new one if one did
	 *         not exist
	 */
	public static ImageCache getInstance(FragmentManager fragmentManager, ImageCacheParams cacheParams) {

		final RetainFragment mRetainFragment = findOrCreateRetainFragment(fragmentManager);

		ImageCache imageCache = (ImageCache) mRetainFragment.getObject();

		if (imageCache == null) {
			imageCache = new ImageCache(cacheParams);// ImageCache
			mRetainFragment.setObject(imageCache);
		}

		return imageCache;
	}

	/**
	 * Initialize the cache, providing all parameters.
	 * 
	 * @param cacheParams
	 *            The cache parameters to initialize the cache
	 */
	private void init(ImageCacheParams cacheParams) {
		mCacheParams = cacheParams;

		// Set up memory cache
		if (mCacheParams.memoryCacheEnabled) {
			if (ImageUtils.DEBUG) {
				LogUtil.d(TAG, "Memory cache created (size = " + mCacheParams.memCacheSize + ")");
			}

			// If we're running on Honeycomb or newer, then
			if (ImageUtils.hasHoneycomb()) {
				mReusableBitmaps = new HashSet<SoftReference<Bitmap>>();
			}

			mMemoryCache = new LruCache<String, BitmapDrawable>(mCacheParams.memCacheSize) {

				/**
				 * Notify the removed entry that is no longer being cached
				 */
				@Override
				protected void entryRemoved(boolean evicted, String key, BitmapDrawable oldValue, BitmapDrawable newValue) {
					if (RecyclingBitmapDrawable.class.isInstance(oldValue)) {
						// The removed entry is a recycling drawable, so notify
						// it
						// that it has been removed from the memory cache

						((RecyclingBitmapDrawable) oldValue).setIsCached(false);
					} else {
						// The removed entry is a standard BitmapDrawable

						if (ImageUtils.hasHoneycomb()) {
							// We're running on Honeycomb or later, so add the
							// bitmap
							// to a SoftRefrence set for possible use with
							// inBitmap later
							mReusableBitmaps.add(new SoftReference<Bitmap>(oldValue.getBitmap()));
						}
					}
				}

				/**
				 * Measure item size in kilobytes rather than units which is
				 * more practical for a bitmap cache
				 */
				@Override
				protected int sizeOf(String key, BitmapDrawable value) {
					final int bitmapSize = getBitmapSize(value) / 1024;
					int size = bitmapSize == 0 ? calcMoreBitmapSize(value) : bitmapSize;
					return size;
				}
			};
		}

		// By default the disk cache is not initialized here as it should be
		// initialized
		// on a separate thread due to disk access.
		if (cacheParams.initDiskCacheOnCreate) {
			// Set up disk cache
			initDiskCache();
		}
	}

	int calcMoreBitmapSize(BitmapDrawable value) {
		if (value.getBitmap() == null) {
			return 0;
		}

		int w = value.getBitmap().getWidth();
		int h = value.getBitmap().getHeight();
		return w * h * 4 / 1024;
	}

	/**
	 * Initializes the disk cache. Note that this includes disk access so this
	 * should not be executed on the main/UI thread. By default an ImageCache
	 * does not initialize the disk cache when it is created, instead you should
	 * call initDiskCache() to initialize it on a background thread.
	 */
	public void initDiskCache() {
		// Set up disk cache
		synchronized (mDiskCacheLock) {

			if (Utils.USE_SELF_IMAGE) {

			} else {

				if (mDiskLruCache == null || mDiskLruCache.isClosed()) {
					File diskCacheDir = mCacheParams.diskCacheDir;
					if (mCacheParams.diskCacheEnabled && diskCacheDir != null) {
						if (!diskCacheDir.exists()) {
							diskCacheDir.mkdirs();
						}
						if (getUsableSpace(diskCacheDir) > mCacheParams.diskCacheSize) {
							try {
								mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, mCacheParams.diskCacheSize);
								if (ImageUtils.DEBUG) {
									LogUtil.d(TAG, "Disk cache initialized");
								}
							} catch (final IOException e) {
								mCacheParams.diskCacheDir = null;
								LogUtil.e(TAG, "initDiskCache - " + e);
							}
						}
					}
				}
			}
			mDiskCacheStarting = false;
			mDiskCacheLock.notifyAll();
		}
	}

	public void remove(ImageParam param) {
		if (param == null) {
			return;
		}
		if (mMemoryCache != null) {

			BitmapDrawable bd = mMemoryCache.get(param.getKey());
			if (bd != null && bd.getBitmap() != null && !bd.getBitmap().isRecycled()) {
				bd.getBitmap().recycle();
			}
			mMemoryCache.remove(param.getKey());
		}
	}

	/**
	 * Adds a bitmap to both memory and disk cache.
	 * 
	 * @param data
	 *            Unique identifier for the bitmap to store
	 * @param value
	 *            The bitmap drawable to store
	 */
	public void addBitmapToCache(ImageParam param, BitmapDrawable value) {
		if (param == null || value == null) {
			return;
		}

		// Add to memory cache
		if (mMemoryCache != null) {
			if (RecyclingBitmapDrawable.class.isInstance(value)) {
				// The removed entry is a recycling drawable, so notify it
				// that it has been added into the memory cache
				((RecyclingBitmapDrawable) value).setIsCached(true);
			}
			mMemoryCache.put(param.getKey(), value);
		}

		// synchronized (mDiskCacheLock) {
		// Add to disk cache

		if (Utils.USE_SELF_IMAGE) {
			return;
		} else {

			if (mDiskLruCache != null) {
				final String key = hashKeyForDisk(param.getKey());
				OutputStream out = null;
				try {
					DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
					if (snapshot == null) {
						final DiskLruCache.Editor editor = mDiskLruCache.edit(key);
						if (editor != null) {
							out = editor.newOutputStream(DISK_CACHE_INDEX);

							boolean png = false;// param.getType() ==
												// ImageType.TYPE_PORTRAIT_CIRCLE
												// ? true : false;
							value.getBitmap().compress(png ? CompressFormat.PNG : CompressFormat.JPEG, 100, out);
							editor.commit();
							out.close();
						}
					} else {
						snapshot.getInputStream(DISK_CACHE_INDEX).close();
					}
				} catch (final IOException e) {
					LogUtil.e(TAG, "addBitmapToCache - " + e);
				} catch (Exception e) {
					LogUtil.e(TAG, "addBitmapToCache - " + e);
				} finally {
					try {
						if (out != null) {
							out.close();
						}
					} catch (IOException e) {
					}
				}
				// File file = new File(fm.yuyin.android.data.System.IMAGE);
				// File imageFile = new File(file, key);
				// if (imageFile.exists() && imageFile.length() > 0) {
				// return;
				// }
				// FileOutputStream stream;
				// try {
				// imageFile.createNewFile();
				// stream = new FileOutputStream(imageFile);
				//
				// boolean png = param.getType() ==
				// ImageType.TYPE_PORTRAIT_CIRCLE ? true : false;
				//
				// value.getBitmap().compress(png ? CompressFormat.PNG :
				// CompressFormat.JPEG, 100, stream);
				// stream.flush();
				// stream.close();
				// } catch (Exception e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}
		}

		// }
	}

	/**
	 * Get from memory cache.
	 * 
	 * @param data
	 *            Unique identifier for which item to get
	 * @return The bitmap drawable if found in cache, null otherwise
	 */
	public BitmapDrawable getBitmapFromMemCache(ImageParam param) {
		BitmapDrawable memValue = null;

		if (mMemoryCache != null) {
			memValue = mMemoryCache.get(param.getKey());
			if (memValue != null) {
				Bitmap bitmap = memValue.getBitmap();
				if (bitmap == null || bitmap.isRecycled()) {
					memValue = null;
					mMemoryCache.remove(param.getKey());
				}
			}
		}

		if (ImageUtils.DEBUG) {
			LogUtil.i("CountingBitmapDrawable", "Memory cache hit >" + memValue);
		}

		return memValue;
	}

	/**
	 * Get from disk cache.
	 * 
	 * @param data
	 *            Unique identifier for which item to get
	 * @return The bitmap if found in cache, null otherwise
	 */
	public Bitmap getBitmapFromDiskCache(ImageParam param) {

		Bitmap bitmap = null;

		final String path = param.getLocalPath();
		if (Utils.USE_SELF_IMAGE) {

			String filename = path;
			// File file = new File(filename);
			// if (!file.exists()) {
			// final String key = hashKeyForDisk(param.getKey());
			// filename = System.IMAGE + key;
			// file = new File(filename);
			// if (!file.exists()) {
			// return null;
			// }
			// }
			LogUtil.d(TAG, ">>" + filename);
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(filename);
				bitmap = ImageResizer.decodeSampledBitmapFromDescriptor(fis.getFD(), param.getWidth(), param.getHeight(), this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
			} finally {
				if (fis != null)
					try {
						fis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}

			// bitmap = ImageResizer.decodeSampledBitmapFromFile(filename,
			// Integer.MAX_VALUE, Integer.MAX_VALUE, this);
			// if (bitmap == null) {
			// final String key = hashKeyForDisk(param.getKey());
			// filename = System.IMAGE + key;
			// bitmap = ImageResizer.decodeSampledBitmapFromFile(filename,
			// Integer.MAX_VALUE, Integer.MAX_VALUE, this);
			// }

		} else {
			if (mDiskLruCache != null) {
				InputStream inputStream = null;
				try {
					final String key = hashKeyForDisk(param.getKey());
					final DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
					if (snapshot != null) {
						if (ImageUtils.DEBUG) {
							LogUtil.d(TAG, "Disk cache hit");
						}
						inputStream = snapshot.getInputStream(DISK_CACHE_INDEX);
						if (inputStream != null) {
							FileDescriptor fd = ((FileInputStream) inputStream).getFD();

							// Decode bitmap, but we don't want to sample so
							// give
							// MAX_VALUE as the target dimensions
							bitmap = ImageResizer.decodeSampledBitmapFromDescriptor(fd, Integer.MAX_VALUE, Integer.MAX_VALUE, this);
						}
					}
					// if (bitmap == null) {
					//
					// File file = new File(System.IMAGE);
					// File imageFile = new File(file, key);
					// LogUtil.e(TAG, "getBitmapFromDiskCache>>" + imageFile +
					// "," +
					// imageFile.exists());
					// if (imageFile.exists()) {
					// bitmap =
					// ImageResizer.decodeSampledBitmapFromFile(imageFile.getAbsolutePath(),
					// Integer.MAX_VALUE, Integer.MAX_VALUE, this);
					// // return bitmap;
					// }
					//
					// }
				} catch (final IOException e) {
					LogUtil.e(TAG, "getBitmapFromDiskCache - " + e);
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e) {
					}
				}
			}
		}
		// LogUtil.d(TAG, "getBitmapFromDiskCache - " + key);
		if (bitmap == null) {

			// LogUtil.e(TAG, "getBitmapFromDiskCache - " + key);

		}
		return bitmap;
	}

	/**
	 * @param options
	 *            - BitmapFactory.Options with out* options populated
	 * @return Bitmap that case be used for inBitmap
	 */
	protected Bitmap getBitmapFromReusableSet(BitmapFactory.Options options) {
		Bitmap bitmap = null;

		if (mReusableBitmaps != null && !mReusableBitmaps.isEmpty()) {
			final Iterator<SoftReference<Bitmap>> iterator = mReusableBitmaps.iterator();
			Bitmap item;

			while (iterator.hasNext()) {
				item = iterator.next().get();

				if (null != item && item.isMutable()) {
					// Check to see it the item can be used for inBitmap
					if (canUseForInBitmap(item, options)) {
						bitmap = item;

						// Remove from reusable set so it can't be used again
						iterator.remove();
						break;
					}
				} else {
					// Remove from the set if the reference has been cleared.
					iterator.remove();
				}
			}
		}

		return bitmap;
	}

	/**
	 * Clears both the memory and disk cache associated with this ImageCache
	 * object. Note that this includes disk access so this should not be
	 * executed on the main/UI thread.
	 */
	public void clearCache() {
		if (mMemoryCache != null) {
			mMemoryCache.evictAll();
			if (ImageUtils.DEBUG) {
				LogUtil.d(TAG, "Memory cache cleared");
			}
		}

		// synchronized (mDiskCacheLock) {
		// mDiskCacheStarting = true;
		// if (mDiskLruCache != null && !mDiskLruCache.isClosed()) {
		// try {
		// mDiskLruCache.delete();
		// if (ImageUtils.DEBUG) {
		// LogUtil.d(TAG, "Disk cache cleared");
		// }
		// } catch (IOException e) {
		// LogUtil.e(TAG, "clearCache - " + e);
		// }
		// mDiskLruCache = null;
		// initDiskCache();
		// }
		// }
	}

	/**
	 * Flushes the disk cache associated with this ImageCache object. Note that
	 * this includes disk access so this should not be executed on the main/UI
	 * thread.
	 */
	public void flush() {
		synchronized (mDiskCacheLock) {
			if (Utils.USE_SELF_IMAGE) {

			} else {

				if (mDiskLruCache != null) {
					try {
						mDiskLruCache.flush();
						if (ImageUtils.DEBUG) {
							LogUtil.d(TAG, "Disk cache flushed");
						}
					} catch (IOException e) {
						LogUtil.e(TAG, "flush - " + e);
					}
				}
			}
		}
	}

	/**
	 * Closes the disk cache associated with this ImageCache object. Note that
	 * this includes disk access so this should not be executed on the main/UI
	 * thread.
	 */
	public void close() {
		synchronized (mDiskCacheLock) {
			if (Utils.USE_SELF_IMAGE) {

			} else {
				if (mDiskLruCache != null) {
					try {
						if (!mDiskLruCache.isClosed()) {
							mDiskLruCache.close();
							mDiskLruCache = null;
							if (ImageUtils.DEBUG) {
								LogUtil.d(TAG, "Disk cache closed");
							}
						}
					} catch (IOException e) {
						LogUtil.e(TAG, "close - " + e);
					}
				}
			}
		}
	}

	/**
	 * A holder class that contains cache parameters.
	 */
	public static class ImageCacheParams {
		public int memCacheSize = DEFAULT_MEM_CACHE_SIZE;
		public int diskCacheSize = DEFAULT_DISK_CACHE_SIZE;
		public File diskCacheDir;
		public CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
		public int compressQuality = DEFAULT_COMPRESS_QUALITY;
		public boolean memoryCacheEnabled = DEFAULT_MEM_CACHE_ENABLED;
		public boolean diskCacheEnabled = DEFAULT_DISK_CACHE_ENABLED;
		public boolean initDiskCacheOnCreate = DEFAULT_INIT_DISK_CACHE_ON_CREATE;

		/**
		 * Create a set of image cache parameters that can be provided to
		 * {@link ImageCache#getInstance(FragmentManager, ImageCacheParams)} or
		 * {@link ImageWorker#addImageCache(FragmentManager, ImageCacheParams)}.
		 * 
		 * @param context
		 *            A context to use.
		 * @param diskCacheDirectoryName
		 *            A unique subdirectory name that will be appended to the
		 *            application cache directory. Usually "cache" or "images"
		 *            is sufficient.
		 */
		public ImageCacheParams(Context context, String diskCacheDirectoryName) {
			diskCacheDir = getDiskCacheDir(context, diskCacheDirectoryName);
		}

		/**
		 * Sets the memory cache size based on a percentage of the max available
		 * VM memory. Eg. setting percent to 0.2 would set the memory cache to
		 * one fifth of the available memory. Throws
		 * {@link IllegalArgumentException} if percent is < 0.05 or > .8.
		 * memCacheSize is stored in kilobytes instead of bytes as this will
		 * eventually be passed to construct a LruCache which takes an int in
		 * its constructor.
		 * <p/>
		 * This value should be chosen carefully based on a number of factors
		 * Refer to the corresponding Android Training class for more
		 * discussion: http://developer.android.com/training/displaying-bitmaps/
		 * 
		 * @param percent
		 *            Percent of available app memory to use to size memory
		 *            cache
		 */
		public void setMemCacheSizePercent(float percent) {
			if (percent < 0.05f || percent > 0.8f) {
				return;
			}
			percent = 0.06f;
			memCacheSize = Math.min(Math.round(percent * Runtime.getRuntime().maxMemory() / 1024), MAX_MEM_CACEH_SIZE);
		}
	}

	/**
	 * @param candidate
	 *            - Bitmap to check
	 * @param targetOptions
	 *            - Options that have the out* value populated
	 * @return true if <code>candidate</code> can be used for inBitmap re-use
	 *         with <code>targetOptions</code>
	 */
	private static boolean canUseForInBitmap(Bitmap candidate, BitmapFactory.Options targetOptions) {
		int width = targetOptions.outWidth / targetOptions.inSampleSize;
		int height = targetOptions.outHeight / targetOptions.inSampleSize;

		return candidate.getWidth() == width && candidate.getHeight() == height;
	}

	/**
	 * Get a usable cache directory (external if available, internal otherwise).
	 * 
	 * @param context
	 *            The context to use
	 * @param uniqueName
	 *            A unique directory name to append to the cache dir
	 * @return The cache dir
	 */
	public static File getDiskCacheDir(Context context, String uniqueName) {
		// Check if media is mounted or storage is built-in, if so, try and use
		// external cache dir
		// otherwise use internal cache dir

		// File f = getExternalCacheDir(context);
		final String cachePath = Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !isExternalStorageRemovable() ? getExternalCacheDir(context).getPath() : context
				.getCacheDir().getPath();

		return new File(cachePath + File.separator + uniqueName);
	}

	/**
	 * A hashing method that changes a string (like a URL) into a hash suitable
	 * for using as a disk filename.
	 */
	public static String hashKeyForDisk(String key) {
		// String cacheKey;
		// try {
		// final MessageDigest mDigest = MessageDigest.getInstance("MD5");
		// mDigest.update(key.getBytes());
		// cacheKey = bytesToHexString(mDigest.digest());
		// } catch (NoSuchAlgorithmException e) {
		// cacheKey = String.valueOf(key.hashCode());
		// }
		// return cacheKey;
		return key;
	}

	private static String bytesToHexString(byte[] bytes) {
		// http://stackoverflow.com/questions/332079
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	/**
	 * Get the size in bytes of a bitmap in a BitmapDrawable.
	 * 
	 * @param value
	 * @return size in bytes
	 */
	@TargetApi(12)
	public static int getBitmapSize(BitmapDrawable value) {
		if (value == null) {
			return 1024 * 1024;
		}
		Bitmap bitmap = value.getBitmap();
		if (bitmap == null) {
			return 1024 * 1024;
		}
		if (ImageUtils.hasHoneycombMR1()) {
			return bitmap.getByteCount();
		}
		// Pre HC-MR1
		return bitmap.getRowBytes() * bitmap.getHeight();
	}

	/**
	 * Check if external storage is built-in or removable.
	 * 
	 * @return True if external storage is removable (like an SD card), false
	 *         otherwise.
	 */
	@TargetApi(9)
	public static boolean isExternalStorageRemovable() {
		if (ImageUtils.hasGingerbread()) {
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}

	/**
	 * Get the external app cache directory.
	 * 
	 * @param context
	 *            The context to use
	 * @return The external cache dir
	 */
	@TargetApi(8)
	public static File getExternalCacheDir(Context context) {
		// if (Utils.hasFroyo()) {
		// return context.getExternalCacheDir();
		// }

		// Before Froyo we need to construct the external cache dir ourselves
		final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
		return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
	}

	/**
	 * Check how much usable space is available at a given path.
	 * 
	 * @param path
	 *            The path to check
	 * @return The space available in bytes
	 */
	@TargetApi(9)
	public static long getUsableSpace(File path) {
		if (ImageUtils.hasGingerbread()) {
			return path.getUsableSpace();
		}
		final StatFs stats = new StatFs(path.getPath());
		return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
	}

	/**
	 * Locate an existing instance of this Fragment or if not found, create and
	 * add it using FragmentManager.
	 * 
	 * @param fm
	 *            The FragmentManager manager to use.
	 * @return The existing instance of the Fragment or the new instance if just
	 *         created.
	 */
	private static RetainFragment findOrCreateRetainFragment(FragmentManager fm) {
		// Check to see if we have retained the worker fragment.
		RetainFragment mRetainFragment = (RetainFragment) fm.findFragmentByTag(TAG);

		// If not retained (or first time running), we need to create and add
		// it.
		if (mRetainFragment == null) {
			mRetainFragment = new RetainFragment();
			fm.beginTransaction().add(mRetainFragment, TAG).commitAllowingStateLoss();
		}

		return mRetainFragment;
	}

	/**
	 * A simple non-UI Fragment that stores a single Object and is retained over
	 * configuration changes. It will be used to retain the ImageCache object.
	 */
	public static class RetainFragment extends Fragment {
		private Object mObject;

		/**
		 * Empty constructor as per the Fragment documentation
		 */
		public RetainFragment() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Make sure this Fragment is retained over a configuration change
			setRetainInstance(true);
		}

		/**
		 * Store a single object in this Fragment.
		 * 
		 * @param object
		 *            The object to store
		 */
		public void setObject(Object object) {
			mObject = object;
		}

		/**
		 * Get the stored object.
		 * 
		 * @return The stored object
		 */
		public Object getObject() {
			return mObject;
		}
	}

}