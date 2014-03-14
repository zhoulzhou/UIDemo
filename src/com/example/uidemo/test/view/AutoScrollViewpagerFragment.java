package com.example.uidemo.test.view;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uidemo.R;
import com.example.uidemo.base.BaseFragment;
import com.example.uidemo.view.widget.AutoScrollViewPager;

public class AutoScrollViewpagerFragment extends BaseFragment{
	AutoScrollViewPager mPager;
	TextView mText;
	ArrayList<View> mListViews = new ArrayList<View>();
	ArrayList<Integer> mImageIdList = new ArrayList<Integer>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.auto_scroll_viewpager_test, null);
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
		
		mPager = (AutoScrollViewPager) view.findViewById(R.id.scrollviewpager);
		mText = (TextView) view.findViewById(R.id.text);
		
		mImageIdList.add(R.drawable.booting);
		mImageIdList.add(R.drawable.houhou);
		mImageIdList.add(R.drawable.ku);
		mImageIdList.add(R.drawable.shuai);
		
		mPager.setAdapter(new ScrollViewPagerAdapter(getActivity(),mImageIdList));
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
		mPager.setInterval(2000);
		mPager.startAutoScroll();
	}
	
	
	
	public class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
        	mText.setText(new StringBuilder().append(position + 1).append("/").append(mImageIdList.size()));
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
	
	
	public class ScrollViewPagerAdapter extends PagerAdapter{
		private ArrayList<Integer> mData = new ArrayList<Integer>();
		private Context mContext;
		
		public ScrollViewPagerAdapter(Context context, ArrayList<Integer> data){
			mData = data;
			mContext = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

      

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			log("inistantiate item");
			final int pos = position;
			ImageView imageView = new ImageView(mContext);
			imageView.setBackgroundResource(mData.get(position));
			((ViewPager)container).addView(imageView, 0);
			
			imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					log("click iamgeview " + pos);
				}
			});
			
			return imageView;
		}
		
		

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager)container).removeView((ImageView)object);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}
        
	}
	
	private void log(String s){
		Log.d("zhou",s);
	}
	
}