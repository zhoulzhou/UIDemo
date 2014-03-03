package com.example.uidemo.ui.popup;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * 通用弹出菜单框UI框
 * 
 */
abstract class PopupWindows {
	protected Context mContext;
	protected PopupWindow mWindow;
	protected View mRootView;
	protected Drawable mBackground = null;
	protected WindowManager mWindowManager;

	/**
	 * Constructor.
	 * 
	 * @param context
	 *            Context
	 */
	public PopupWindows(Context context) {
		mContext = context;
		mWindow = new PopupWindow(context);

		mWindow.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					mWindow.dismiss();

					return true;
				}

				return false;
			}
		});

		mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
	}

	/**
	 * On dismiss
	 */
	protected void onDismiss() {
	}

	protected abstract void onSetParams(PopupWindow window);

	protected final void prepare() {
		if (mRootView == null)
			return;

		onShow();

		if (mBackground == null)
			mWindow.setBackgroundDrawable(new BitmapDrawable(mContext
					.getResources()));
		else
			mWindow.setBackgroundDrawable(mBackground);

		onSetParams(mWindow);

		mWindow.setTouchable(true);
		mWindow.setFocusable(true);
		mWindow.setOutsideTouchable(true);
		mWindow.setContentView(mRootView);
	}

	/**
	 * On show
	 */
	protected void onShow() {
	}

	/**
	 * Set background drawable.
	 * 
	 * @param background
	 *            Background drawable
	 */
	public final void setBackgroundDrawable(Drawable background) {
		mBackground = background;
	}

	/**
	 * Set content view.
	 * 
	 * @param root
	 *            Root view
	 */
	public final void setContentView(View root) {
		mRootView = root;
		mWindow.setContentView(root);
	}

	/**
	 * Set content view.
	 * 
	 * @param layoutResID
	 *            Resource id
	 */
	public final void setContentView(int layoutResID) {
		LayoutInflater inflator = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		setContentView(inflator.inflate(layoutResID, null));
	}

	/**
	 * Set listener on window dismissed.
	 * 
	 * @param listener
	 */
	public final void setOnDismissListener(
			PopupWindow.OnDismissListener listener) {
		mWindow.setOnDismissListener(listener);
	}

	/**
	 * Dismiss the popup window.
	 */
	public final void dismiss() {
		mWindow.dismiss();
	}
}