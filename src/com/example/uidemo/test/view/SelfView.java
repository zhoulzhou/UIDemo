package com.example.uidemo.test.view;

import java.util.ArrayList;

import com.example.uidemo.R;
import com.example.uidemo.ui.online.view.ItemData;
import com.example.uidemo.view.widget.BDListView;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class SelfView extends RelativeLayout implements OnClickListener{
	TextView mText;
	ImageView mImage;
	BDListView mList;

	public SelfView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	public SelfView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	public SelfView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	private void init(Context context){
		log("init");
	}

	@Override
	protected void onFinishInflate() {
		log("onFinishInflate");
		super.onFinishInflate();
		
		mImage = (ImageView) findViewById(R.id.image);
		mText = (TextView) findViewById(R.id.text);
		mList = (BDListView) findViewById(R.id.list);
		
		mImage.setOnClickListener(this);
		
	}
	
	public void setText(String text){
		mText.setText(text);
	}
	
	public void setImage(int resid){
		mImage.setBackgroundResource(resid);
	}
	
	public void updateViews(){
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch(id){
		case R.id.image:
			log("click image");
			break;
		default :
			break;
		}
	}
	
	public static class SelfAdapter extends BaseAdapter{
		ArrayList<ItemData> data = new ArrayList<ItemData>();
		Context mContext;

		public void setActivity(Context context){
			mContext = context;
		}
		
		public void setActivity(Fragment fragment){
			mContext = fragment.getActivity();
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public ItemData getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.view_test_self_item, null);
			}
			
			SelfViewItem item = (SelfViewItem) convertView;
			item.setText(getItem(position).title);
			
			return convertView;
		}
		
		public ArrayList<ItemData> getData(){
			return data;
		}
		
		public void clear(){
			data.clear();
			mContext = null;
		}
		
	}
	
	private void log(String s){
		Log.d("zhou",s);
	}
}