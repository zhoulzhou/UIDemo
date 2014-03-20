package com.example.uidemo.test.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.ImageView;
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
	
	ViewGroup mHeadInfoLayout;
	ViewGroup mHeadTitleNum;
	
	int mTopTitleHeight;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		//commit test
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
		mTopTitleHeight = mHeadTitleBar.getLayoutParams().height;
		
		mHeadTitleNum = (ViewGroup) v.findViewById(R.id.head_title_num);
		
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
		mTitleImageView.setImageDrawable(new ColorDrawable());
	}
	
	private void showBar(){
		mHeadTitleBar.setVisibility(View.GONE);
		mHeadBar.setVisibility(View.VISIBLE);
		mImageBar.setVisibility(View.VISIBLE);
		mTitleImageView.setImageDrawable(mHeaderView.getPullImage().getDrawable());
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
			
			float a = 1;
			doAlpha(1-a,(ViewGroup) mHeaderView.findViewById(R.id.info_bottom_layout));
			doAlpha(1-a, mHeadTitleNum);
			
		}else if(firstVisibleItem == 0 && mListView.getChildCount() > 1){
			log("hide bar");
			int top = mListView.getChildAt(1).getTop();
			log("top= " + top + " mTopTitleHeight= " + mTopTitleHeight);
			int top1 = mListView.getChildAt(0).getTop();
			log("top1= " + top1);
			int top2 = mListView.getTop();
			log("top2= " + top2);
			if(top < mTopTitleHeight){
				showBar();
			}else{
				hideBar();
			}
			
			int h = mHeaderView.getHeight();
			float a = 0 - mHeaderView.getTop()*1f / h;
			log("h= " + h + " a= " + a + " headview top= " + mHeaderView.getTop());
			doAlpha(1-a,(ViewGroup) mHeaderView.findViewById(R.id.info_bottom_layout));
			doAlpha(1-a, mHeadTitleNum);
		}
	}
	
	private void doAlpha(float a, ViewGroup vg){
		int count = vg.getChildCount();
		log("count= " + count);
		for(int i=0; i<count; i++){
			View v = vg.getChildAt(i);
			if(android.os.Build.VERSION.SDK_INT > 11){
				log("sdk > 11");
				v.setAlpha(a);
			}else{
				if(v instanceof TextView){
					TextView tv = (TextView) v;
					tv.setTextColor( Color.argb( (int) (255*a), 255, 255, 255));
				}else if(v instanceof ImageView){
					ImageView iv = (ImageView) v;
					iv.setAlpha(255*a);
				}
			}
			
			if( a < 0.5f){
				v.setVisibility(View.INVISIBLE);
			}else{
				v.setVisibility(View.VISIBLE);
			}
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