package com.example.uidemo.common.bitmapfun;

import com.example.uidemo.utils.LogUtil;
import com.example.uidemo.utils.StringUtils;

import android.annotation.SuppressLint;


@SuppressLint("DefaultLocale")
public class ImageParam {

	private final static String TAG = "ImageParam";
	private int width;
	private int height;
	private String url;
	private int type;
	private String key;
	private int resid = -1;

	private int defaultResDrawableId;

	private int cornerDip;

	private String localPath;
	
	private boolean mCacheMemable = true;
	
	public boolean isFading = true;

	public ImageParam(int resid) {
		this.resid = resid;
	}

	public ImageParam(String url, String key, int cornerDip) {
		super();
		this.key = key;
		this.cornerDip = cornerDip;
		this.url = url;
	}
	
	public void setCacheMemable(boolean cacheMemable){
		this.mCacheMemable = cacheMemable;
	}
	
	public boolean isCacheMemable(){
		return mCacheMemable;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public ImageParam(String url, int type) {
		super();

		this.type = type;
		setUrl(url);
	}
	
	public ImageParam(String url,float cornerDip) {
		super();

		this.cornerDip = (int) cornerDip;
		setUrl(url);
	}

	static int S_WIDTH = 0;
	static int S_HEIGHT = 0;

	public int getWidth() {
		if (S_WIDTH == 0) {
			S_WIDTH = TingApplication.getAppContext().getResources().getDisplayMetrics().widthPixels;
		}
		if (width == 0) {
			return S_WIDTH;
		}

		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {

		if (S_HEIGHT == 0) {
			S_HEIGHT = TingApplication.getAppContext().getResources().getDisplayMetrics().heightPixels;
		}
		if (height == 0) {
			return S_HEIGHT;
		}

		return height;
	}
	
	public boolean hasSize(){
		return height != 0 || width != 0;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}

	public String getUrl() {
		if (resid != -1) {
			return String.valueOf(resid);
		}
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		getLocalPath();
	}

	public static final String PATH = "/mnt/sdcard/Baidu_music/image/";

	@SuppressLint("DefaultLocale")
	public String getLocalPath() {
		if (!StringUtils.isEmpty(localPath)) {
			return localPath;
		}
		localPath = PATH + getKey();
		return localPath;
	}

	public static String getOriginUrl(String url) {
		int index = url.indexOf("_");
		// if (index > 0) {
		// url = url.substring(0, index);
		// }
		return url;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getKey() {
		LogUtil.d(TAG, "Key: " + key);
		if(resid!=-1)
		{
			return "default_"+resid;
		}
		if (StringUtils.isEmpty(key)) {
			key = url.hashCode() + "";
		}
		return key;

	}

	public int getCornerDip() {
		return cornerDip;
	}

	public int getResid() {
		return resid;
	}

	public int getDefaultResDrawableId() {
		return defaultResDrawableId;
	}

	public void setDefaultResDrawableId(int defaultResDrawableId) {
		this.defaultResDrawableId = defaultResDrawableId;
	}

	public static interface ImageType {
		// public int TYPE_PORTRAIT = 1;
		// public int TYPE_PORTRAIT_CIRCLE = 2;
		// public int TYPE_TICKER = 3;
		// public int TYPE_RECOMMEND_SMALL = 4;
		// public int TYPE_RECOMMEND_BIG = 5;
		// public int TYPE_ARTICEL = 6;
		//
		// public int TYPE_PLAYER_ARTICEL = 7;
		//
		// public int TYPE_CATEGROY_TAG = 8;
		//
		// public int TYPE_TAKE_PHOTO_BG = 9;
		// public int TYPE_TAKE_PHOTO_PORTRAIT = 10;
		//
		// public int TYPE_WATER = 11;
		//
		// public int TYPE_TOPIT_ME = 12;
	}

	ImageLocalQueryObject imageLocalQueryObject;

	public ImageLocalQueryObject getImageLocalQueryObject() {
		return imageLocalQueryObject;
	}

	public void setImageLocalQueryObject(ImageLocalQueryObject imageLocalQueryObject) {
		this.imageLocalQueryObject = imageLocalQueryObject;
	}

	public static class ImageLocalQueryObject {
		public String artist;
		public String album;
		public long albumId;

	}

}
