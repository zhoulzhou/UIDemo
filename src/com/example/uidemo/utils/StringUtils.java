package com.example.uidemo.utils;

import android.text.TextUtils;

public class StringUtils{
	 /*
     * 字符串是否为空。
     */
    public static boolean isEmpty(String str) {
    	String string = str;
    	if(string==null){
    		return true;
    	}
    	string = string.trim();
        return TextUtils.isEmpty(string);
    }
}