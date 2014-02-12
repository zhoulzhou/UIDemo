package com.example.uidemo.ui;

import com.example.uidemo.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


/**
 *界面之间跳转及控制
 */
public class UIController {
	public static final void showNewUIMain(Context context, Bundle extras) {
		Intent intent = new Intent(context, MainActivity.class);
		if (extras != null) {
			intent.putExtras(extras);
		}
		context.startActivity(intent);
	}
}