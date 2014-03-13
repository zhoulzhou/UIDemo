package com.example.uidemo.test.view;

import java.util.ArrayList;

import com.example.uidemo.R;
import com.example.uidemo.test.view.SelfView.SelfAdapter;
import com.example.uidemo.ui.online.view.ItemData;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class SelfComplexActivity extends Activity implements OnClickListener{
	TextView mTitle1;
	TextView mTitle2;
	
	ViewGroup mContainer;
	
	SelfView mView1;
	SelfView mView2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_self_main);
		
		mTitle1 = (TextView) findViewById(R.id.title1);
		mTitle2 = (TextView) findViewById(R.id.title2);
		
		mTitle1.setOnClickListener(this);
		mTitle2.setOnClickListener(this);
		
		mContainer = (ViewGroup) findViewById(R.id.container);
		
		mView1 = (SelfView) LayoutInflater.from(this).inflate(R.layout.view_test_self, null);
		mView2 = (SelfView) LayoutInflater.from(this).inflate(R.layout.view_test_self, null);
		
		mView1.setText("view**1");
		mView2.setText("view**2");
		
		SelfAdapter adapter = new SelfAdapter();
		adapter.setActivity(this);
		adapter.getData().addAll(cData(1));
		mView1.mList.setAdapter(adapter);
		
		SelfAdapter adapter1 = new SelfAdapter();
		adapter1.setActivity(this);
		adapter1.getData().clear();
		adapter1.getData().addAll(cData(2));
		mView2.mList.setAdapter(adapter1);
		
		mContainer.addView(mView1,-1,-1);
		mContainer.addView(mView2,-1,-1);
	}
	
	private ArrayList<ItemData> cData(int j){
		ArrayList<ItemData> data = new ArrayList<ItemData>();
		for (int i=0; i<5; i++){
			ItemData item = new ItemData();
			item.title = ("title*** " + i +"&"+ j);
			data.add(item);
		}
		return data;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch(id){
		case R.id.title1:
			log("click title1");
			mTitle1.setSelected(true);
			mTitle2.setSelected(false);
			
			mView1.setVisibility(View.VISIBLE);
			mView2.setVisibility(View.GONE);
			break;
		case R.id.title2:
			log("click title2");
			mTitle1.setSelected(false);
			mTitle2.setSelected(true);
			
			mView1.setVisibility(View.GONE);
			mView2.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}
	
	private void log(String s){
		Log.d("zhou",s);
	}
}