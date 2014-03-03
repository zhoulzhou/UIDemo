package com.example.uidemo.ui.popup;

import com.example.uidemo.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PopupTestActivity extends Activity{
	
	public final static int ID_USER = 11;
	public final static int ID_GROUNP = 12;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		final Context mContext;
		
		super.onCreate(savedInstanceState);
		
		final Button btn = new Button(this);
		btn.setText("popup button");
		
		setContentView(btn);
		
		mContext = this;
		
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PopupMenuItem userItem = new PopupMenuItem(ID_USER,"用户",getResources().getDrawable(R.drawable.ic_launcher));
				PopupMenuItem grounpItem = new PopupMenuItem(ID_GROUNP,"qun",getResources().getDrawable(R.drawable.ic_launcher));
				//use setSticky(true) to disable PopuJar dialog being dismissed after an item is clicked
				PopupMenuController pmc = new PopupMenuController(mContext,new callBack());
                pmc.addMenuItem(ID_USER,"用户",R.drawable.ic_launcher);
                pmc.addMenuItem(ID_GROUNP,"qun",R.drawable.ic_launcher);
                pmc.showPopup(btn);
			}
		});
	}
	
	public class callBack implements PopupMenuController.Callback{

		@Override
		public void onPopupMenuItemSelected(
				PopupMenuController popupController, int position, int menuId) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}