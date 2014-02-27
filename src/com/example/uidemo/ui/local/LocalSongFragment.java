package com.example.uidemo.ui.local;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uidemo.base.BaseFragment;

public class LocalSongFragment extends TabLocalFragment{
	private Context mContext;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mContext = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mFactory = new SongListViewFactory();
		super.onViewCreated(view, savedInstanceState);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showContainerView();
	}

	@Override
	public void onStartLoadData() {
		// TODO Auto-generated method stub
		super.onStartLoadData();
	}
	
	public class SongListViewFactory extends LocalListViewAbstractFactory{

		@Override
		public int getLoaderCallbackId() {
			// TODO Auto-generated method stub
			return 0;
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
		public AbstractLoaderCallbacks createLoaderCallback() {
			// TODO Auto-generated method stub
			return new SongLoaderCallback();
		}

		@Override
		public BaseAdapter createBaseAdapter() {
			// TODO Auto-generated method stub
			return  new LocalSongAdapter(getActivity());
			
		}
		
	}
	
	public class SongLoaderCallback extends AbstractLoaderCallbacks{

		@Override
		protected CursorLoader createCursorLoader() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected String getEmptyText() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onDataChanged(Cursor data) {
			// TODO Auto-generated method stub
			
			//下载数据后显示view 并gone掉Loadingview
//			showContainerView();
		}

		@Override
		protected void onNoData() {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void onDataReseted() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}