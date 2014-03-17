package com.example.uidemo.ui.loadingview;

import com.example.uidemo.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CellListLoading extends RelativeLayout{
	View net;
	View empty;
	View loading;
	
	ImageView netImage;
	TextView netText;
	TextView netSubText;
	Button netButton;
	
	ImageView emptyImage;
	TextView emptyText;
	TextView emptySubText;
	Button emptyButton;
	
	TextView loadingText;
	
	int state;
	public final static int LOADING = 0;
	public final static int NOTHING = 1;
	public final static int NONE_NETWORK = 2;
	public final static int ONLINE_WIFI = 3;

	public CellListLoading(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		net = findViewById(R.id.net);
		empty = findViewById(R.id.empty);
		loading = findViewById(R.id.loading);
		try{
		netImage = (ImageView) findViewById(R.id.netImage);
		netText = (TextView) findViewById(R.id.netTitle);
		netSubText = (TextView) findViewById(R.id.netSubTitle);
		netButton = (Button) findViewById(R.id.netBtn);
		
		emptyImage = (ImageView) findViewById(R.id.emptyImage);
		emptyText = (TextView) findViewById(R.id.emptyTitle);
		emptyText = (TextView) findViewById(R.id.emptySubTitle);
		emptyButton = (Button) findViewById(R.id.netBtn);
		
		loadingText = (TextView) findViewById(R.id.text);
		
		loading.setVisibility(View.VISIBLE);
		net.setVisibility(View.GONE);
		empty.setVisibility(View.GONE);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void showLoading(){
		loading.setVisibility(View.VISIBLE);
		net.setVisibility(View.GONE);
		empty.setVisibility(View.GONE);
		state = LOADING;
	}
	
	public void setLoadingText(String text){
		if(loadingText == null){
			loadingText = (TextView) findViewById(R.id.text);
		}
		loadingText.setText(text);
	}
	

	public void showNothing(int res, String title, String subtitle,String btnText, OnClickListener btnListener) {
		loading.setVisibility(View.GONE);
		net.setVisibility(View.GONE);
		empty.setVisibility(View.VISIBLE);

		emptyImage.setImageResource(res);
		emptyText.setText(title);
		emptySubText.setText(subtitle);
		emptyButton.setText(btnText);
		emptyButton.setOnClickListener(btnListener);
		state = NOTHING;
	}
	
	public void showNoNetwork(int res, String title, String subtitle, String btnText, OnClickListener btnListener) {
		loading.setVisibility(View.GONE);
		net.setVisibility(View.VISIBLE);
		empty.setVisibility(View.GONE);

		netImage.setImageResource(res);
		netText.setText(title);
		netSubText.setText(subtitle);
		netButton.setText(btnText);
		netButton.setOnClickListener(btnListener);
		state = NONE_NETWORK;
	}
	
	public int getCurrentState(){
		return state;
	}
	
}