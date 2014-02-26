package com.example.uidemo.logic.local;

import android.os.Bundle;

public interface StorageChangeObserver{

	public void onStorageChange(boolean mounted, Bundle data);

	public static final String EXTRA_PREFIX = "com.baidu.music.ui.callback.StorageChangeObserver.";
	public static final String EXTRA_STORAGE_STATE = EXTRA_PREFIX
			+ "EXTRA_STORAGE_STATE";
	public static final String EXTRA_STORAGE_PATH = EXTRA_PREFIX
			+ "EXTRA_STORAGE_PATH";

}