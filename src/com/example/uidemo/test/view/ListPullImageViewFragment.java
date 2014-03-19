package com.example.uidemo.test.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uidemo.R;
import com.example.uidemo.base.BaseFragment;
import com.example.uidemo.view.widget.BDListView;
import com.example.uidemo.view.widget.BDPullHeaderLayout;
import com.example.uidemo.view.widget.BDPullListView;
import com.example.uidemo.view.widget.BDTitleImageView;

public class ListPullImageViewFragment extends BaseFragment{
	BDPullListView mListView;
	BDPullHeaderLayout mHeaderView;
	BDTitleImageView mTitleImageView;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.pull_image_view, null);
		mHeaderView = (BDPullHeaderLayout) LayoutInflater.from(getActivity()).inflate(R.layout.bdpull_header_layout, null);
		mListView = (BDPullListView) v.findViewById(R.id.listview);
		mListView.requestFocus();
		
		mTitleImageView = (BDTitleImageView) v.findViewById(R.id.titleimage);
		
		// 设置imageView, 作为弹性下拉
		mListView.setHeadView(mHeaderView);
		mListView.setHeaderImageView(mHeaderView.getPullImage());
		
		TestAdapter ap = new TestAdapter(getActivity());
		TextView tv = new TextView(getActivity());
		tv.setText("head view");
		mListView.addHeaderView(tv);
		
		mListView.setAdapter(ap);
		
//		mHeaderView.getPullImage().setImageResource(R.drawable.ic_launcher);
//		mTitleImageView.setImageDrawable(mHeaderView.getPullImage().getDrawable());
//		mHeaderView.onFinishInflate();
		return v;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		mHeaderView.getPullImage().setBackgroundResource(R.drawable.ic_launcher);
	}
	
	public class TestAdapter extends BaseAdapter{
		Context mContext;

		public TestAdapter(Context context){
			mContext = context;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 10;
		}

		@Override
		public Object getItem(int position) {
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.test_list_item, null);
			}
			return convertView;
		}
		
	}
	
}