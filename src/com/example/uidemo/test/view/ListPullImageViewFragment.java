package com.example.uidemo.test.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.uidemo.R;
import com.example.uidemo.base.BaseFragment;
import com.example.uidemo.view.widget.BDListView;
import com.example.uidemo.view.widget.BDPullHeaderLayout;
import com.example.uidemo.view.widget.BDPullListView;
import com.example.uidemo.view.widget.BDPullListView.OnTouchUpListener;
import com.example.uidemo.view.widget.BDTitleImageView;

public class ListPullImageViewFragment extends BaseFragment implements OnScrollListener{
	BDPullListView mListView;
	BDPullHeaderLayout mHeaderView;
	BDTitleImageView mTitleImageView;
	
	View mHeadBar;
	View mHeadTitleBar;
	View mImageBar;

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
		
		mImageBar = (View) v.findViewById(R.id.titleimagelayout);
		mTitleImageView = (BDTitleImageView) v.findViewById(R.id.titleimage);
		
		mHeadBar = (View) v.findViewById(R.id.head_bar);
		mHeadTitleBar = (View) v.findViewById(R.id.info_top_layout);
		
		// 设置imageView, 作为弹性下拉
		mListView.setHeadView(mHeaderView);
		mListView.setHeaderImageView(mHeaderView.getPullImage());
		
		TestAdapter ap = new TestAdapter(getActivity());
		TextView tv = new TextView(getActivity());
		tv.setText("head view");
		
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.pull_iamge_view_header, null);
		mListView.addHeaderView(view, null, false);
		mListView.addHeaderView(tv);
		
		
		mListView.setOnScrollListener(this);
		mListView.setOnTouchUpListener(new OnTouchUpListener() {
			@Override
			public void onTouchUp(int downTop) {
				// TODO Auto-generated method stub
				log("touch up");
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				log("item click position= " + arg2);
			}
			
		});
		
		mListView.setAdapter(ap);
		
//		mHeaderView.getPullImage().setImageResource(R.drawable.ic_launcher);
//		mTitleImageView.setImageDrawable(mHeaderView.getPullImage().getDrawable());
//		mHeaderView.onFinishInflate();
		return v;
	}
	
	private void hideBar(){
		mHeadTitleBar.setVisibility(View.VISIBLE);
		mHeadBar.setVisibility(View.GONE);
		mImageBar.setVisibility(View.GONE);
	}
	
	private void showBar(){
		mHeadTitleBar.setVisibility(View.GONE);
		mHeadBar.setVisibility(View.VISIBLE);
		mImageBar.setVisibility(View.VISIBLE);
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
			return 30;
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

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		log("onscroll firstVisibleItem= " + firstVisibleItem + " visibleItemCount= " + visibleItemCount +
		            " totalItemCount= " + totalItemCount);
		if(firstVisibleItem > 0){
			log("show bar");
			showBar();
			
		}else if(firstVisibleItem == 0 && mListView.getChildCount() > 1){
			log("hide bar");
			hideBar();
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if(scrollState != SCROLL_STATE_FLING){
			log("not fling  scrollState= " + scrollState );
		}
	}
	
	private void log(String s){
		Log.d("zhou",s);
	}
	
}