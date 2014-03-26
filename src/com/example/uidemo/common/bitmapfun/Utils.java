package com.example.uidemo.common.bitmapfun;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class Utils {
	public final static boolean USE_SELF_IMAGE = true;

	public static long SDCardSpace() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());

			long blockSize = stat.getBlockSize();
			long availableBlocks = stat.getAvailableBlocks();

			return availableBlocks * blockSize; // byte
		}
		return -1;
	}

	public static int getSDKVersionInt() {
		int sdkVersion = 4;
		try {
			sdkVersion = Integer.parseInt(android.os.Build.VERSION.SDK.trim());
		} catch (Exception e) {
			sdkVersion = 4;
		}
		return sdkVersion;
	}

	public static void showSoftKeybroad(Activity activity, EditText editText) {
		InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		manager.showSoftInput(editText, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	public static void hideSoftKeyBroad(Activity activity, EditText editText) {
		if (activity == null) {
			return;
		}
		InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		manager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static byte[] readFile(File filePath) {
		FileInputStream ins = null;
		ByteArrayOutputStream baos = null;
		if (filePath.exists()) {
			try {
				ins = new FileInputStream(filePath);
				byte[] buffer = new byte[4096];
				int len = 0;
				baos = new ByteArrayOutputStream();
				while ((len = ins.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
					baos.flush();
				}
				return baos.toByteArray();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			} finally {
				if (ins != null) {
					try {
						ins.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (baos != null) {
					try {
						baos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	public static synchronized boolean writeFile(File filePath, byte[] data) throws IOException {
		return writeFile(filePath, data, false);
	}

	/**
	 * Write file.
	 * 
	 * @param filePath
	 *            the file path
	 * @param data
	 *            the data
	 * @return true, if successful
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static synchronized boolean writeFile(File filePath, byte[] data, boolean append) throws IOException {
		if (data == null)
			return false;
		if (!filePath.exists()) {
			filePath.createNewFile();
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath, append);
			out.write(data);
			out.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	public static List<File> findDirs(String path) {
		File files = new File(path);
		ArrayList<File> list = new ArrayList<File>();
		if (files.isDirectory()) {
			File[] subs = files.listFiles();
			if (subs != null && subs.length > 0) {
				for (int i = 0; i < subs.length; i++) {
					if (subs[i].isDirectory() && !subs[i].getName().startsWith(".")) {
						if (subs[i].canWrite())
							list.add(subs[i]);
					}
				}
			}
		}
		Collections.sort(list, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				if (o1 != null && o2 != null) {
					Collator col = Collator.getInstance(Locale.CHINESE);
					return col.compare(o1.getName(), o2.getName());
				}
				return 0;
			}

		});
		return list;
	}

	public static boolean isNetworkConnected() {
		Context context = TingApplication.getAppContext();
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;

	}

	public static boolean isWifi() {
		Context context = TingApplication.getAppContext();
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo.State mobile = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
			NetworkInfo.State wifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
			if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING)
				return true;

		}
		return false;

	}

	public static void setTextViewHtml(String content, TextView tv) {
		tv.setText(Html.fromHtml(content, new ImageGetter() {

			@Override
			public Drawable getDrawable(String source) {
				// TODO Auto-generated method stub
				return new ColorDrawable(0x00000000);
			}
		}, null));
	}

	private final static long ONE_SECONED = 1000;
	private final static long ONE_MINUTE = ONE_SECONED * 60;
	private final static long ONE_HOUR = ONE_MINUTE * 60;
	private final static long ONE_DAY = ONE_HOUR * 24;

	public static String convertDateToTimeLeft(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date d = sdf.parse(date);
			long time = d.getTime();
			long now = System.currentTimeMillis();
			if (now < time || now - time < ONE_SECONED) {
				return "刚刚";
			} else if (now - time < ONE_DAY) {

				long b = now - time;
				if (b < ONE_HOUR) {

					int a = (int) (b / ONE_MINUTE);
					if (a <= 0) {
						a = 1;
					}
					return a + "分钟前";

				} else {
					int a = (int) (b / ONE_HOUR);
					if (a <= 0) {
						a = 1;
					}
					return a + "小时前";

				}

			}
			sdf = new SimpleDateFormat("MM-dd");
			date = sdf.format(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}

	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}

	// public static String decode(String arg1) {
	// int loc9 = 0;
	// int loc1 = Integer.parseInt(String.valueOf(arg1.charAt(0)));
	// String loc2 = arg1.substring(1);
	// int loc3 = (int) Math.floor(loc2.length() / loc1);
	// int loc4 = loc2.length() % loc1;
	// String[] loc5 = new String[loc1];
	// int loc6 = 0;
	// while (loc6 < loc4) {
	// if (loc5[loc6] == null) {
	// loc5[loc6] = "";
	// }
	// loc5[loc6] = loc2.substring((loc3 + 1) * loc6, (loc3 + 1) * loc6 + loc3 +
	// 1);
	// ++loc6;
	// }
	// loc6 = loc4;
	// while (loc6 < loc1) {
	// loc5[loc6] = loc2.substring(loc3 * (loc6 - loc4) + (loc3 + 1) * loc4,
	// loc3 * (loc6 - loc4) + (loc3 + 1) * loc4 + loc3);
	// ++loc6;
	// }
	// String loc7 = "";
	// loc6 = 0;
	// while (loc6 < loc5[0].length()) {
	// loc9 = 0;
	// while (loc9 < loc5.length && loc6 < loc5[loc9].length()) {
	// loc7 = loc7 + loc5[loc9].charAt(loc6);
	// ++loc9;
	// }
	// ++loc6;
	// }
	// loc7 = unescape(loc7);
	// String loc8 = "";
	// loc6 = 0;
	// while (loc6 < loc7.length()) {
	// if (loc7.charAt(loc6) != '^') {
	// loc8 = loc8 + loc7.charAt(loc6);
	// } else {
	// loc8 = loc8 + "0";
	// }
	// ++loc6;
	// }
	// return loc8 = loc8.replace('+', ' ');
	//
	// }
	public static String decode(String arg1) {
		int loc1 = Integer.parseInt(String.valueOf(arg1.charAt(0)));
		String loc2 = arg1.substring(1);
		int loc3 = (int) Math.floor(loc2.length() / loc1);
		int loc4 = loc2.length() % loc1;
		String[] loc5 = new String[loc1];
		int i = 0;
		for (i = 0; i < loc4; i++) {
			if (loc5[i] == null) {
				loc5[i] = "";
			}
			loc5[i] = loc2.substring((loc3 + 1) * i, (loc3 + 1) * i + loc3 + 1);
		}

		for (i = loc4; i < loc1; i++) {
			loc5[i] = loc2.substring(loc3 * (i - loc4) + (loc3 + 1) * loc4, loc3 * (i - loc4) + (loc3 + 1) * loc4 + loc3);
		}
		String loc7 = "";

		for (i = 0; i < loc5[0].length(); i++) {
			for (int j = 0; j < loc5.length; j++) {
				if (i < loc5[j].length()) {
					loc7 += loc5[j].charAt(i);
				}
			}
		}
		loc7 = unescape(loc7);
		String loc8 = "";

		for (i = 0; i < loc7.length(); i++) {
			if (loc7.charAt(i) != '^') {
				loc8 += loc7.charAt(i);
			} else {
				loc8 += "0";
			}
		}
		return loc8 = loc8.replace('+', ' ');
	}

}
