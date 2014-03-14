package com.example.uidemo.test;

import com.example.uidemo.R;
import com.example.uidemo.test.view.AutoScrollViewpagerFragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class TestActivity extends FragmentActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		Child child = new Child();
		
		setContentView(R.layout.test_main);
		
		AutoScrollViewpagerFragment f = new AutoScrollViewpagerFragment();
		setFragment(f,true);
	}
	
	private  void setFragment(Fragment fragment, boolean save){
		log("set fragment");
		// Add the fragment to the activity, pushing this transaction
	    // on to the back stack.
	    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	    ft.replace(R.id.mainview, fragment);
	    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
	    ft.addToBackStack(fragment.getClass().getName());
	    ft.commit();
	}
	
	public class Parent{
		private int i = 0;
		public Parent(){
			i = 3;
			Log.d("zhou", "parent i= " + i);
		}
	}
	
	public class Child extends Parent{
		int j = 1;
		public Child(){
			j = 5;
			Log.d("zhou","child j= " + j);
		}
	}
	
	private void log(String s){
		Log.d("zhou",s);
	}
}