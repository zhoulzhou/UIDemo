package com.example.uidemo.test.view;

import com.example.uidemo.R;
import com.example.uidemo.view.widget.BDListView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewActivity extends Activity{
	BDListView mList;
    Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.list);
		
		mContext = this;
		
		mList = (BDListView) findViewById(R.id.listview);
		
		TestAdapter adapter = new TestAdapter();
		mList.setAdapter(adapter);
	}
	
	public class TestAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 100;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.list_cell, null);
			}
			return convertView;
		}
		
	}
	
}