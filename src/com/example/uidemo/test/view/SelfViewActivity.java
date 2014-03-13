package com.example.uidemo.test.view;

import java.util.ArrayList;

import com.example.uidemo.R;
import com.example.uidemo.test.view.SelfView.SelfAdapter;
import com.example.uidemo.ui.online.view.ItemData;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;

public class SelfViewActivity extends Activity{
	LayoutInflater mInflater;
	SelfView mSelfView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		
		mSelfView = (SelfView) mInflater.inflate(R.layout.view_test_self, null);
		
		setContentView(mSelfView);
		
		mSelfView.setText("title***t");
		
		SelfAdapter adapter = new SelfAdapter();
		adapter.setActivity(this);
		adapter.getData().addAll(cData());
		
		mSelfView.mList.setAdapter(adapter);
	}
	
	private ArrayList<ItemData> cData(){
		ArrayList<ItemData> data = new ArrayList<ItemData>();
		for (int i=0; i<5; i++){
			ItemData item = new ItemData();
			item.title = ("title*** " + i);
			data.add(item);
		}
		return data;
	}
	
}