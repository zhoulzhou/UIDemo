package com.example.uidemo.utils;

/**
 * 日志控制
 * @author yangzc
 * 
 */
public class LogUtil {
	/**
	 * 默认的文库日志Tag标签
	 */
	public final static String DEFAULT_TAG = "BaiduMusic";

	private static Logger mLogger = Logger.getLogger("Baidu_music.log");
	
	public static final String KEYWORD_lOG_2_FILE = "baidumusic_1";
	public static final String KEYWORD_lOGCAT_2_SDCARD = "baidumusic_2";
	
	/**
	 * 设置是否开启日志
	 * @param debugable
	 */
	public static void setDebug(boolean debugable){
		if(debugable)
			mLogger.traceOn();
		else
			mLogger.traceOff();
	}
	
	/**
	 * 设置日志级别
	 * @param level
	 */
	public static void setLevel(int level){
		mLogger.setLevel(level);;
	}
	
	/**
	 * 打印debug级别的log
	 * 
	 * @param tag tag标签
	 * @param str 内容
	 */
	public static void d(String tag, String msg) {
		if (mLogger != null) {
			mLogger.d(tag, msg);
		}
	}

	/**
	 * 打印warning级别的log
	 * @param tag tag标签
	 * @param str 内容
	 */
	public static void w(String tag, String msg) {
		if (mLogger != null) {
			mLogger.w(tag, msg);
		}
	}

	/**
	 * 打印error级别的log
	 * @param tag tag标签
	 */
	public static void e(String tag, Throwable tr) {
		if (mLogger != null) {
			mLogger.e(tag, tr);
		}
	}
	
	/**
	 * 打印error级别的log
	 * @param tag tag标签
	 */
	public static void e(String tag, String msg){
		if (mLogger != null) {
			mLogger.e(tag, msg);
		}
	}
	
	/**
	 * 打印error级别的log
	 * @param tag tag标签
	 */
	public static void e(String tag, String msg, Throwable tr){
		if (mLogger != null) {
			mLogger.e(tag, msg, tr);
		}
	}

	/**
	 * 打印info级别的log
	 * 
	 * @param tag
	 *            tag标签
	 * @param str
	 *            内容
	 */
	public static void i(String tag, String str) {
		if (mLogger != null) {
			mLogger.i(tag, str);
		}
	}
	
	/**
	 * 打印verbose级别的log
	 * @param tag tag标签
	 * @param str 内容
	 */
	public static void v(String tag, String str) {
		if (mLogger != null) {
			mLogger.v(tag, str);
		}
	}
	
	//********************************************************************************//

	/**
	 * 打印info级别的log
	 * @param str 内容
	 */
	public static void i(String str) {
		i(DEFAULT_TAG, str);
	}

	/**
	 * 打印verbose级别的log
	 * @param str 内容
	 */
	public static void v(String str) {
		v(DEFAULT_TAG, str);
	}
	
	/**
	 * 打印warning级别的log
	 * @param str 内容
	 */
	public static void w(String str) {
		w(DEFAULT_TAG, str);
	}
	
	/**
	 * 打印debug级别的log
	 * @param str 内容
	 */
	public static void d(String str) {
		d(DEFAULT_TAG, str);
	}
	
	/**
	 * 打印Error级别log
	 */
	public static void e(String str){
		e(DEFAULT_TAG, str);
	}
}
