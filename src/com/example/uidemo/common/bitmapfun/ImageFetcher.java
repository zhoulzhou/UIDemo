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
import java.io.OutputStream;
import java.util.HashSet;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;

import com.baidu.music.common.bitmapfun.ImageCache.ImageCacheParams;
import com.baidu.music.common.http.HttpHelper;
import com.baidu.music.common.net.HttpListener;
import com.baidu.music.common.net.HttpProvider;
import com.baidu.music.common.net.HttpResult;
import com.baidu.music.common.utils.LogUtil;
import com.baidu.music.common.utils.StringUtils;

/**
 * A simple subclass of {@link ImageResizer} that fetches and resizes images
 * fetched from a URL.
 */
public class ImageFetcher extends ImageResizer {
	private static final String TAG = "ImageFetcher";
	private static final int HTTP_CACHE_SIZE = 10 * 1024 * 1024; // 10MB
	private static final String HTTP_CACHE_DIR = "http";
	private static final int IO_BUFFER_SIZE = 8 * 1024;

	private DiskLruCache mHttpDiskCache;
	private File mHttpCacheDir;
	private boolean mHttpDiskCacheStarting = true;
	private final Object mHttpDiskCacheLock = new Object();
	private static final int DISK_CACHE_INDEX = 0;

	/**
	 * Initialize providing a target image width and height for the processing
	 * images.
	 * 
	 * @param context
	 * @param imageWidth
	 * @param imageHeight
	 */
//	public ImageFetcher(Context context, int imageWidth, int imageHeight) {
//		super(context, imageWidth, imageHeight);
//		init(context);
//	}
	
	private static ImageFetcher _instance = null;
	
	public static ImageFetcher getInstance(FragmentActivity activity){
		if(activity == null)
			return _instance;
		
		if(_instance == null){
			_instance = new ImageFetcher(activity.getApplicationContext(), -1);
		}
		_instance.refreshImageCache(activity);
		return _instance;
	}
	
	private void refreshImageCache(FragmentActivity activity){
		ImageCacheParams cacheParams = new ImageCacheParams(activity, "");
		cacheParams.setMemCacheSizePercent(0.08f);
		addImageCache(activity.getSupportFragmentManager(), cacheParams);
	}

	/**
	 * Initialize providing a single target image size (used for both width and
	 * height);
	 * 
	 * @param context
	 * @param imageSize
	 */
	private ImageFetcher(Context context, int imageSize) {
		super(context, imageSize);
		init(context);
	}

	private void init(Context context) {
		// checkConnection(context);
		mHttpCacheDir = ImageCache.getDiskCacheDir(context, HTTP_CACHE_DIR);
	}

	@Override
	protected void initDiskCacheInternal() {
		super.initDiskCacheInternal();
		initHttpDiskCache();
	}

	private void initHttpDiskCache() {
		if (!mHttpCacheDir.exists()) {
			mHttpCacheDir.mkdirs();
		}
		synchronized (mHttpDiskCacheLock) {

			if (Utils.USE_SELF_IMAGE) {

			} else {
				if (ImageCache.getUsableSpace(mHttpCacheDir) > HTTP_CACHE_SIZE) {
					try {
						mHttpDiskCache = DiskLruCache.open(mHttpCacheDir, 1, 1, HTTP_CACHE_SIZE);
						if (ImageUtils.DEBUG) {
							LogUtil.d(TAG, "HTTP cache initialized");
						}
					} catch (IOException e) {
						mHttpDiskCache = null;
					}
				}
			}
			mHttpDiskCacheStarting = false;
			mHttpDiskCacheLock.notifyAll();
		}
	}

	@Override
	protected void clearCacheInternal() {
		super.clearCacheInternal();
		if (Utils.USE_SELF_IMAGE) {

		} else {
			synchronized (mHttpDiskCacheLock) {
				if (mHttpDiskCache != null && !mHttpDiskCache.isClosed()) {
					try {
						mHttpDiskCache.delete();
						if (ImageUtils.DEBUG) {
							LogUtil.d(TAG, "HTTP cache cleared");
						}
					} catch (IOException e) {
						LogUtil.e(TAG, "clearCacheInternal - " + e);
					}
					mHttpDiskCache = null;
					mHttpDiskCacheStarting = true;
					initHttpDiskCache();
				}
			}
		}
	}

	@Override
	protected void flushCacheInternal() {
		super.flushCacheInternal();
		if (Utils.USE_SELF_IMAGE) {

		} else {
			synchronized (mHttpDiskCacheLock) {
				if (mHttpDiskCache != null) {
					try {
						mHttpDiskCache.flush();
						if (ImageUtils.DEBUG) {
							LogUtil.d(TAG, "HTTP cache flushed");
						}
					} catch (IOException e) {
						LogUtil.e(TAG, "flush - " + e);
					}
				}
			}
		}
	}

	@Override
	protected void closeCacheInternal() {
		super.closeCacheInternal();
		if (Utils.USE_SELF_IMAGE) {

		} else {
			synchronized (mHttpDiskCacheLock) {
				if (mHttpDiskCache != null) {
					try {
						if (!mHttpDiskCache.isClosed()) {
							mHttpDiskCache.close();
							mHttpDiskCache = null;
							if (ImageUtils.DEBUG) {
								LogUtil.d(TAG, "HTTP cache closed");
							}
						}
					} catch (IOException e) {
						LogUtil.e(TAG, "closeCacheInternal - " + e);
					}
				}
			}
		}
	}

	/**
	 * Simple network connection check.
	 * 
	 * @param context
	 */
	void checkConnection(Context context) {
		final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
			LogUtil.e(TAG, "checkConnection - no connection found");
		}
	}

	static HashSet<String> nullUlrKey = new HashSet<String>();

	/**
	 * The main process method, which will be called by the ImageWorker in the
	 * AsyncTask background thread.
	 * 
	 * @param data
	 *            The data to load the bitmap, in this case, a regular http URL
	 * @return The downloaded and resized bitmap
	 */
	private Bitmap processBitmap(ImageParam data) {
		if (ImageUtils.DEBUG) {
			LogUtil.d(TAG, "processBitmap - " + data);
		}
		Bitmap bitmap = null;
		final ImageParam param = data;
		if (data.getImageLocalQueryObject() != null) {
			if (nullUlrKey.contains(data.getKey())) {
				return null;
			}
			String artist = data.getImageLocalQueryObject().artist;
			String album = data.getImageLocalQueryObject().album;
			long albumId = data.getImageLocalQueryObject().albumId;
			String url = com.baidu.music.common.utils.ImageUtils.getImageUrl2LoaclImage(artist, album, albumId);
			if (StringUtils.isEmpty(url)) {
				nullUlrKey.add(data.getKey());
				return null;
			}
			try {
				downloadUrlToByteArray(url, data.getLocalPath());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
			} catch (Error e1) {
				getImageCache().clearCache();
			}
			{
				bitmap = ImageResizer.decodeSampledBitmapFromFile(data.getLocalPath(), param.getWidth(), param.getHeight(), getImageCache());
			}

			return bitmap;
		}

		final String key = ImageCache.hashKeyForDisk(data.getKey());

		if (Utils.USE_SELF_IMAGE) {
			//
			File file = new File(ImageParam.PATH + key);
			// bitmap = ImageResizer.decodeSampledBitmapFromFile(file.getPath(),
			// mImageWidth, mImageHeight, getImageCache());

			bitmap = getImageCache().getBitmapFromDiskCache(data);
			if (bitmap != null) {
				// try {
				// bitmap = BitmapFactory.decodeStream(new
				// FlushedInputStream(new FileInputStream(file)));
				// } catch (FileNotFoundException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				return bitmap;
			}
			try {
				downloadUrlToByteArray(data.getUrl(), file.getAbsolutePath());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
			} catch (Error e1) {
				getImageCache().clearCache();
			}

			// downloadUrlToFile(data.getUrl());
			// synchronized (mHttpDiskCacheLock)
			{

					// bitmap =
					// ImageResizer.decodeSampledBitmapFromFile(file.getPath(),
					// mImageWidth, mImageHeight, getImageCache());
					// if (bitmap == null) {
				bitmap = ImageResizer.decodeSampledBitmapFromFile(file.getAbsolutePath(), param.getWidth(), param.getHeight(), getImageCache());
					// }
			}

		} else {

			FileDescriptor fileDescriptor = null;
			FileInputStream fileInputStream = null;
			DiskLruCache.Snapshot snapshot;
			synchronized (mHttpDiskCacheLock) {
				// Wait for disk cache to initialize
				while (mHttpDiskCacheStarting) {
					try {
						mHttpDiskCacheLock.wait();
					} catch (InterruptedException e) {
					}
				}

				if (mHttpDiskCache != null) {
					try {
						snapshot = mHttpDiskCache.get(key);
						if (snapshot == null) {
							if (ImageUtils.DEBUG) {
								LogUtil.d(TAG, "processBitmap, not found in http cache, downloading...");
							}
							DiskLruCache.Editor editor = mHttpDiskCache.edit(key);
							if (editor != null) {
								if (downloadUrlToStream(data.getUrl(), editor.newOutputStream(DISK_CACHE_INDEX))) {
									editor.commit();
								} else {
									editor.abort();
								}
							}
							snapshot = mHttpDiskCache.get(key);
						}
						if (snapshot != null) {
							fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_INDEX);
							fileDescriptor = fileInputStream.getFD();
						}
					} catch (IOException e) {
						LogUtil.e(TAG, "processBitmap - " + e);
					} catch (IllegalStateException e) {
						LogUtil.e(TAG, "processBitmap - " + e);
					} finally {
						if (fileDescriptor == null && fileInputStream != null) {
							try {
								fileInputStream.close();
							} catch (IOException e) {
								LogUtil.e(TAG, "processBitmap - " + e);
							}
						}
					}
				}
			}

			if (fileDescriptor != null) {
				bitmap = decodeSampledBitmapFromDescriptor(fileDescriptor, mImageWidth, mImageHeight, getImageCache());
			}
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
				}
			}
		}
		return bitmap;
	}

	@Override
	protected Bitmap processBitmap(Object data) {

		ImageParam param = (ImageParam) data;
		try{
			return processBitmap(param);
		}catch(Throwable e){
			return null;
		}
	}
	
	public static boolean downloadUrlToByteArray(String url, String filePath) {
//		Network network = new Network();
//		network.fetchRemoteAlbum(url, filePath);
		return HttpHelper.storeFile(url, filePath);
	}

	/**
	 * Download a bitmap from a URL and write the content to an output stream.
	 * 
	 * @param urlString
	 *            The URL to fetch
	 * @return true if successful, false otherwise
	 */
	public boolean downloadUrlToStream(String urlString, final OutputStream outputStream) {
//		disableConnectionReuseIfNecessary();
//		HttpURLConnection urlConnection = null;
//		BufferedOutputStream out = null;
//		BufferedInputStream in = null;
//		try {
			HttpProvider httpProvider = new HttpProvider();
			HttpResult result = httpProvider.doGet(urlString, 10, new HttpListener() {
				@Override
				public boolean onCompleted() {
					return true;
				}
				
				@Override
				public void onError(int statusCode) {
				}
				
				@Override
				public boolean onStart(long startPos, long contentLength) {
					if(contentLength <= 0)
						return false;
					return true;
				}
				
				@Override
				public boolean onAdvance(byte[] buffer, int offset, int len) {
					try {
						outputStream.write(buffer, offset, len);
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
					return true;
				}

				@Override
				public boolean onReady(String url) {
					return true;
				}

				@Override
				public boolean onRelease() {
					return true;
				}
			});
			return result != null && result.mContentLength > 0;
			
			/*
//			final URL url = new URL(urlString);
//			urlConnection = (HttpURLConnection) url.openConnection();
			HttpGet httpGet = new HttpGet(urlString);
			DefaultHttpClient client = HttpHelper.createHttpClient(10);
			HttpResponse response = client.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == HttpStatus.SC_OK
					|| statusCode == HttpStatus.SC_PARTIAL_CONTENT){
				in = new BufferedInputStream(response.getEntity().getContent(), IO_BUFFER_SIZE);
				out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

				int b;
				while ((b = in.read()) != -1) {
					out.write(b);
				}
				return true;
			}*/
//		} finally {
//			if (urlConnection != null) {
//				urlConnection.disconnect();
//			}
//			try {
//				if (out != null) {
//					out.close();
//				}
//				if (in != null) {
//					in.close();
//				}
//			} catch (final IOException e) {
//			}
//		}
//		return false;
	}

	/**
	 * Workaround for bug pre-Froyo, see here for more info:
	 * http://android-developers
	 * .bLogUtilspot.com/2011/09/androids-http-clients.html
	 */
//	public static void disableConnectionReuseIfNecessary() {
//		// HTTP connection reuse which was buggy pre-froyo
//		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
//			System.setProperty("http.keepAlive", "false");
//		}
//	}
}
