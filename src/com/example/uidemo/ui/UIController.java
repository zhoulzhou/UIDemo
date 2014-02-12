package com.example.uidemo.ui;

import com.example.uidemo.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


/**
 *����֮����ת������
 *
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