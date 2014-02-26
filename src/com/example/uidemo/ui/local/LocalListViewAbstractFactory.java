package com.example.uidemo.ui.local;

import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.BaseAdapter;

public abstract class LocalListViewAbstractFactory{
	protected View mHeaderView;
	
	protected AbstractLoaderCallbacks mLoaderCallbacks;
	
	public abstract int getLoaderCallbackId();

	public abstract int getTitle();

	public abstract String getFooterText(int count);
	
	public abstract CursorAdapter createAdapter();
	
	public abstract BaseAdapter createBaseAdapter();

	public abstract AbstractLoaderCallbacks createLoaderCallback();

	public AbstractLoaderCallbacks getLoaderCallback() {
		if (mLoaderCallbacks == null) {
			mLoaderCallbacks = createLoaderCallback();
		}

		return mLoaderCallbacks;
	}
}