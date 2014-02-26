package com.example.uidemo.ui.local;

import com.example.uidemo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class LocalSongAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	
	public LocalSongAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 6;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return mInflater.inflate(R.layout.ui_local_song_item, null);
	}
	
}