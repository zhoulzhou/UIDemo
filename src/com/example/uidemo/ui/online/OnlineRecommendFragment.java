package com.example.uidemo.ui.online;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uidemo.base.BaseFragment;
import com.example.uidemo.ui.online.view.ItemData;
import com.example.uidemo.ui.online.view.RecmdDailyView;

public class OnlineRecommendFragment extends BaseFragment{


	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		TextView tv = new TextView(getActivity());
		tv.setText("recommend list");
		return setRecmdDailyView();
	}
	
	private RecmdDailyView setRecmdDailyView(){
		RecmdDailyView v = new RecmdDailyView(getActivity());
		
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
		return v;
	}
	
	private void setRecmdDailyView(View v){
		
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
		
		((RecmdDailyView) v).updateViews(datas);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	}
	

}