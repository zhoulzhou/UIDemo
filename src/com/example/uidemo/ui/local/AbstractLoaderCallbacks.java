package com.example.uidemo.ui.local;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

public abstract class AbstractLoaderCallbacks implements LoaderCallbacks<Cursor>{

	protected CursorLoader mCursorLoader;
	
	protected abstract CursorLoader createCursorLoader();

	protected abstract String getEmptyText();
	
	protected abstract void onDataChanged(Cursor data );
	protected abstract void onNoData();
	
	protected abstract void onDataReseted();
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		if(mCursorLoader == null){
			mCursorLoader = createCursorLoader();
		}
		return mCursorLoader;
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

		if(data != null && data.getCount() != 0){

			onDataChanged(data);
		} else {
			onNoData();
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

		onDataReseted();
		
		mCursorLoader = null;

	}
}	