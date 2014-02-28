package com.example.uidemo.ui.local;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.BaseAdapter;

import com.example.uidemo.logic.local.LoaderCallBack;

public class LocalArtistFragment extends TabLocalFragment{
	private Context mContext;
	private String[] PROJECTION = {MediaStore.Audio.Artists._ID,MediaStore.Audio.Artists.ARTIST,MediaStore.Audio.Artists.NUMBER_OF_TRACKS};
    private CursorAdapter mAdapter;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mContext = activity;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mFactory = new ArtistListViewFactory();
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onStartLoadData() {
		// TODO Auto-generated method stub
		super.onStartLoadData();
	}
	
	public class ArtistListViewFactory extends LocalListViewAbstractFactory{

		@Override
		public int getLoaderCallbackId() {
			// TODO Auto-generated method stub
			return LoaderCallBack.LOADER_ID_LOCAL_ARTIST;
		}

		@Override
		public int getTitle() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public String getFooterText(int count) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public CursorAdapter createAdapter() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public BaseAdapter createBaseAdapter() {
			// TODO Auto-generated method stub
			if(mAdapter == null){
				mAdapter = new LocalArtistAdapter(mContext, null, true);
			}
			return mAdapter;
		}

		@Override
		public AbstractLoaderCallbacks createLoaderCallback() {
			// TODO Auto-generated method stub
			return new ArtistLoadedCallback();
		}
		
	}
	
	public class ArtistLoadedCallback extends AbstractLoaderCallbacks{

		@Override
		protected CursorLoader createCursorLoader() {
			// TODO Auto-generated method stub
			return new CursorLoader(getActivity().getApplicationContext(), MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, PROJECTION, null, null, null);
		}

		@Override
		protected String getEmptyText() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onDataChanged(Cursor data) {
			// TODO Auto-generated method stub
			if(!mInit){
				return ;
			}
			mAdapter.swapCursor(data);
//			showContainerView();
		}

		@Override
		protected void onNoData() {
			// TODO Auto-generated method stub
			if(!mInited){
				return;
			}
			getEmptyText();
		}

		@Override
		protected void onDataReseted() {
			// TODO Auto-generated method stub
			if(!mInit){
				return ;
			}
			mAdapter.swapCursor(null);
		}
		
	}

}