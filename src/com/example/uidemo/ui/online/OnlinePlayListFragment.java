package com.example.uidemo.ui.online;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.uidemo.R;
import com.example.uidemo.base.BaseFragment;
import com.example.uidemo.widget.waterfall.StaggeredGridView;
import com.example.uidemo.widget.waterfall.test.MainActivity;
import com.example.uidemo.widget.waterfall.test.StaggeredAdapter;
import com.example.uidemo.widget.waterfall.test.Utils;

public class OnlinePlayListFragment extends BaseFragment{


	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.waterfall_main, null);
		return v;
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
		
        StaggeredGridView gridView = (StaggeredGridView) view.findViewById(R.id.staggeredGridView1);
		
		int margin = getResources().getDimensionPixelSize(R.dimen.margin);
		
		gridView.setItemMargin(margin); // set the GridView margin
		
		gridView.setPadding(margin, 0, margin, 0); // have the margin on the sides as well 
		
		StaggeredAdapter adapter = new StaggeredAdapter(getActivity(), R.id.imageView1, Utils.urls);
		
		gridView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	

}