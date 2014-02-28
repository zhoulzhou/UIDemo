package com.example.uidemo.ui.local;

import com.example.uidemo.R;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LocalArtistAdapter extends CursorAdapter{
	private Context mContext;
	private LayoutInflater mInflater;

	public LocalArtistAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		// TODO Auto-generated constructor stub
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	
	public LocalArtistAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		// TODO Auto-generated constructor stub
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		// TODO Auto-generated method stub
		final ViewHolder vh = (ViewHolder) v.getTag();
		String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
		vh.line1.setText(name);
		long n = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
		name = n + "首";
		vh.line2.setText(name);
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View v = mInflater.inflate(R.layout.ui_local_artist_item, null);
		ViewHolder vh = new ViewHolder();
		vh.icon = (ImageView) v.findViewById(R.id.local_list_item_2_icon);
		vh.icon.setPadding(0, 0, 1, 0);
		vh.line1 = (TextView) v.findViewById(R.id.local_list_item_2_line1);
		vh.line2 = (TextView) v.findViewById(R.id.local_list_item_2_line2);
		vh.hint = (ImageView) v.findViewById(R.id.local_list_item_2_arrow);
		
		vh.playContainer = (LinearLayout) v.findViewById(R.id.local_list_play_container);
		vh.addContainer = (LinearLayout) v.findViewById(R.id.local_list_addto_container);
		vh.deleteContainer = (LinearLayout) v.findViewById(R.id.local_list_filter_container);
		
		vh.play = (ImageView) v.findViewById(R.id.local_list_play);
		vh.add = (ImageView) v.findViewById(R.id.local_list_addto);
		vh.delete = (ImageView) v.findViewById(R.id.local_list_filter);
		
		vh.playText = (TextView) v.findViewById(R.id.local_list_play_name);
		vh.addText = (TextView) v.findViewById(R.id.local_list_addto_name);
		vh.deleteText = (TextView) v.findViewById(R.id.local_list_filter_name);
		v.setTag(vh);
		
		return v;
	}
	
	public class ViewHolder {
		TextView line1;
		TextView line2;
		ImageView icon;
		ImageView hint;
		
		LinearLayout playContainer;
		LinearLayout addContainer;
		LinearLayout deleteContainer;
		
		ImageView play;
		ImageView add;
		ImageView delete;
		
		TextView playText;
		TextView addText;
		TextView deleteText;
	}
	
}