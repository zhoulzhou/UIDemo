package com.example.uidemo.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class TestActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Child child = new Child();
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
	
}