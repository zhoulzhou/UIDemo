package com.example.uidemo.ui.online.view;

import com.example.uidemo.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;

public class ViewTestActivity extends Activity{
	ItemView mItem;
	ViewGroup mLayout;
    boolean mGo = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.view_test);
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
	
	
	
}