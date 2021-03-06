package com.example.uidemo.ui.online.view;

import java.util.ArrayList;

import com.example.uidemo.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class ViewTestActivity extends Activity{
	ItemView mItem;
	ViewGroup mLayout;
    boolean mGo = false;
    LayoutInflater mInflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mInflater = LayoutInflater.from(this);
		
		setRecmdDailyView();
		
//		setContentView(R.layout.view_test); //可以这样使用自定义view
//		setContentView(R.layout.item_view);
//		mItem = (ItemView) findViewById(R.id.item1);
		
	    if(mGo){
	    mLayout = (ViewGroup) findViewById(R.id.layout);
		mItem = new ItemView(this,0);
		mLayout.addView(mItem);
		
		ItemData data = new ItemData();
		data.title = " title title"	;
		data.listnum = " 330 " ;
		
		mItem.updateViews(data, false);
	    }
		
	}
	
	private void setRecmdDailyView(){
		RecmdDailyView v = new RecmdDailyView(this);
		
		setContentView(v);
		
		ArrayList<ItemData> datas = new ArrayList<ItemData>();
		for(int i=0; i<6; i++){
			ItemData data = new ItemData();
			data.title = " title ** " + i;
			data.listnum = " num *** " + i;
			if(i/2 == 0){
				data.type = 2;
			}else{
				data.type = 1;
			}
			datas.add(data);
		}
		
		v.updateViews(datas);
	}
	
}