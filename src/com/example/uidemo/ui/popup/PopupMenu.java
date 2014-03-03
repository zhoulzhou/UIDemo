package com.example.uidemo.ui.popup;

import java.util.ArrayList;
import java.util.List;

import com.example.uidemo.R;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

/**
 * 通用弹出菜单类
 */
public class PopupMenu extends PopupWindows implements OnDismissListener {
	private static final String TAG = PopupMenu.class.getSimpleName();
	private static final boolean DEBUG = true;

	private static final android.widget.LinearLayout.LayoutParams ACTION_PARAMS = new android.widget.LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);

	private ViewGroup mRootView;
	private LayoutInflater mInflater;
	private OnMenuItemClickListener mItemClickListener;
	private OnDismissListener mDismissListener;

	private List<PopupMenuItem> menuItems = new ArrayList<PopupMenuItem>();

	private boolean mDidAction;

	private int mChildPos;
	private int mInsertPos;
	private int mAnimStyle;
	private int mOrientation;
	private int rootWidth = 0;
	private int mTitleBarHeight = 0;
	private int mLinePadding = 0;

	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	public static final int ANIM_AUTO = 0;
	public static final int ANIM_GROW_FROM_LEFT = 1;
	public static final int ANIM_GROW_FROM_RIGHT = 2;
	public static final int ANIM_GROW_FROM_CENTER = 3;
	public static final int ANIM_REFLECT = 4;
	public static final int ANIM_GROW_FROM_BASELINE = 5;
	public static final int ANIM_MOVE_LEFT_TO_RIGHT = 6;

	/**
	 * Constructor for default vertical layout
	 * 
	 * @param context
	 *            Context
	 */
	public PopupMenu(Context context) {
		this(context, VERTICAL);
	}

	/**
	 * Constructor allowing orientation override
	 * 
	 * @param context
	 *            Context
	 * @param orientation
	 *            Layout orientation, can be vartical or horizontal
	 */
	public PopupMenu(Context context, int orientation) {
		super(context);

		mOrientation = orientation;

		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mTitleBarHeight = context.getResources().getDimensionPixelSize(R.dimen.ui_title_bar_height);
		mLinePadding = context.getResources().getDimensionPixelSize(R.dimen.ui_popup_item_separator_padding);

		if (mOrientation == HORIZONTAL) {
			setRootViewId(R.layout.popup_horizontal);
		} else {
			setRootViewId(R.layout.popup_vertical);
		}

		mAnimStyle = ANIM_AUTO;
		mChildPos = 0;
	}

	/**
	 * Get action item at an index
	 * 
	 * @param index
	 *            Index of item (position from callback)
	 * 
	 * @return Action Item at the position
	 */
	public PopupMenuItem getMenuItem(int index) {
		return menuItems.get(index);
	}

	/**
	 * Set root view.
	 * 
	 * @param id
	 *            Layout resource id
	 */
	public void setRootViewId(int id) {
		mRootView = (ViewGroup) mInflater.inflate(id, null);

		// This was previously defined on show() method, moved here to prevent
		// force close that occured
		// when tapping fastly on a view to show quickaction dialog.
		// Thanx to zammbi (github.com/zammbi)
		if (mOrientation == HORIZONTAL) {
			mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		} else {
			mRootView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}

		setContentView(mRootView);
	}

	/**
	 * Set animation style
	 * 
	 * @param mAnimStyle
	 *            animation style, default is set to ANIM_AUTO
	 */
	public void setAnimStyle(int mAnimStyle) {
		this.mAnimStyle = mAnimStyle;
	}

	/**
	 * Set listener for action item clicked.
	 * 
	 * @param listener
	 *            Listener
	 */
	public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
		mItemClickListener = listener;
	}

	/**
	 * Add action item
	 * 
	 * @param action
	 *            {@link PopupMenuItem}
	 */
	public void addMenuItem(PopupMenuItem action) {
		menuItems.add(action);

		String title = action.getTitle();
		Drawable icon = action.getIcon();

		View container;

		if (mOrientation == HORIZONTAL) {
			container = mInflater.inflate(R.layout.popup_action_item_horizontal, null);
		} else {
			container = mInflater.inflate(R.layout.popup_action_item_vertical, null);
		}

		ImageView img = (ImageView) container.findViewById(R.id.iv_icon);
		TextView text = (TextView) container.findViewById(R.id.tv_title);
		// setFakeBold(text);

		if (icon != null) {
			img.setImageDrawable(icon);
		} else {
			img.setVisibility(View.GONE);
		}

		if (title != null) {
			text.setText(title);
		} else {
			text.setVisibility(View.GONE);
		}
		img.setEnabled(action.isEnable());
		text.setEnabled(action.isEnable());
		container.setEnabled(action.isEnable());

		final int pos = mChildPos;
		final int actionId = action.getActionId();

		container.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mItemClickListener != null) {
					mItemClickListener.onItemClick(PopupMenu.this, pos, actionId);
				}

				if (!getMenuItem(pos).isSticky()) {
					mDidAction = true;

					dismiss();
				}
			}
		});

		container.setFocusable(true);
		container.setClickable(true);

		if (mOrientation == HORIZONTAL && mChildPos != 0) {
			View separator = mInflater.inflate(R.layout.popup_horizontal_separator, null);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1, LayoutParams.MATCH_PARENT);

			separator.setLayoutParams(params);
			separator.setPadding(0, mLinePadding, 0, mLinePadding);

			mRootView.addView(separator, mInsertPos);

			mInsertPos++;
		}

		if (mOrientation == HORIZONTAL) {
			mRootView.addView(container, mInsertPos, ACTION_PARAMS);
		} else {
			mRootView.addView(container, mInsertPos);
		}

		mChildPos++;
		mInsertPos++;
	}

	private void setFakeBold(final TextView tv) {
		TextPaint tp = tv.getPaint();
		tp.setFakeBoldText(true);
	}

	@Override
	protected void onSetParams(PopupWindow window) {
		if (mOrientation == HORIZONTAL) {
			window.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
			window.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		} else {
			window.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
			window.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		}
	}

	/**
	 * Show quickaction popup. Popup is automatically positioned, on top or
	 * bottom of anchor view.
	 * 
	 */
	public void show(View anchor) {
		prepare();

		// setBackgroundDrawable(new ColorDrawable(Color.BLUE));

		int xPos, yPos;

		mDidAction = false;

		int[] location = new int[2];

		anchor.getLocationOnScreen(location);

		Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] + anchor.getHeight());

		if (mOrientation == HORIZONTAL) {
			mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		} else {
			mRootView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}

		int rootWidth = mRootView.getMeasuredWidth();
		int rootHeight = mRootView.getMeasuredHeight();

		if (rootWidth == 0) {
			rootWidth = mRootView.getMeasuredWidth();
		}

		Display display = mWindowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();

		DisplayMetrics dm = new DisplayMetrics();
		dm = mContext.getResources().getDisplayMetrics();//notice.
		float density = dm.density;

		// automatically get X coord of popup (top left)
		// if ((anchorRect.left + rootWidth) > screenWidth) {
		// xPos = anchorRect.left - (rootWidth - anchor.getWidth());
		// xPos = (xPos < 0) ? 0 : xPos;
		// } else {
		// if (anchor.getWidth() > rootWidth) {
		// xPos = anchorRect.centerX() - (rootWidth / 2);
		// } else {
		// xPos = anchorRect.left;
		// }
		// }

		xPos = anchorRect.right - 60; // top right

		int dyTop = anchorRect.top;
		int dyBottom = screenHeight - anchorRect.bottom;

		boolean showOnTop = (dyTop > mTitleBarHeight + anchor.getHeight()) ? true : false;
		// showOnTop = (rootHeight + mTitleBarHeight > dyBottom);
		// showOnTop=(anchorRect.bottom+rootHeight)>screenHeight;

		if (showOnTop) {
			yPos = anchorRect.top - rootHeight / 2 - 20;
			mRootView.setBackgroundResource(R.drawable.popup_downarrow_bg);
		} else {
			yPos = anchorRect.bottom - 8;
			mRootView.setBackgroundResource(R.drawable.popup_uparrow_bg);
		}

		if (DEBUG) {
			Log.i(TAG, " anchorRect.top=" + anchorRect.top + " anchorRect.left=" + anchorRect.left + " anchorRect.bottom=" + anchorRect.bottom + " anchorRect.right=" + anchorRect.right);
			Log.i(TAG, " showOnTop=" + showOnTop + " dyTop=" + dyTop + " dyBottom=" + dyBottom + " screenHeight=" + screenHeight + " screenWidth=" + screenWidth + " rootHeight=" + rootHeight);
			Log.i(TAG, " xPos=" + xPos + " yPos=" + yPos);
		}

		setAnimationStyle(screenWidth, anchorRect.centerX(), showOnTop);
		mWindow.showAtLocation(anchor, Gravity.RIGHT | Gravity.TOP, (int) (14 * density), yPos);
	}

	/**
	 * Set animation style
	 * 
	 * @param screenWidth
	 *            screen width
	 * @param requestedX
	 *            distance from left edge
	 * @param onTop
	 *            flag to indicate where the popup should be displayed. Set TRUE
	 *            if displayed on top of anchor view and vice versa
	 */
	private void setAnimationStyle(int screenWidth, int requestedX, boolean showOnTop) {
		int arrowPos = requestedX;

		if (DEBUG) {
			Log.v(TAG, "setAnimationStyle() screenWidth=" + screenWidth + " requestedX=" + requestedX + " onTop=" + showOnTop);
		}

		switch (mAnimStyle) {
		case ANIM_GROW_FROM_LEFT:
			mWindow.setAnimationStyle((showOnTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
			break;

		case ANIM_GROW_FROM_RIGHT:
			mWindow.setAnimationStyle((showOnTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
			break;

		case ANIM_GROW_FROM_CENTER:
			mWindow.setAnimationStyle((showOnTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
			break;

		case ANIM_REFLECT:
			mWindow.setAnimationStyle((showOnTop) ? R.style.Animations_PopUpMenu_Reflect : R.style.Animations_PopDownMenu_Reflect);
			break;
		case ANIM_GROW_FROM_BASELINE:
			mWindow.setAnimationStyle((showOnTop) ? R.style.Animations_PopUpMenu_GROW : R.style.Animations_PopDownMenu_GROW);
			break;
		case ANIM_MOVE_LEFT_TO_RIGHT:
			mWindow.setAnimationStyle((showOnTop) ? R.style.Animations_PopUpMenu_MoveLR : R.style.Animations_PopDownMenu_MoveLR);
			break;
		case ANIM_AUTO:
			if (arrowPos <= screenWidth / 4) {
				mWindow.setAnimationStyle((showOnTop) ? R.style.Animations_PopUpMenu_Left : R.style.Animations_PopDownMenu_Left);
			} else if (arrowPos > screenWidth / 4 && arrowPos < 3 * (screenWidth / 4)) {
				mWindow.setAnimationStyle((showOnTop) ? R.style.Animations_PopUpMenu_Center : R.style.Animations_PopDownMenu_Center);
			} else {
				mWindow.setAnimationStyle((showOnTop) ? R.style.Animations_PopUpMenu_Right : R.style.Animations_PopDownMenu_Right);
			}

			break;
		}
	}

	/**
	 * Set listener for window dismissed. This listener will only be fired if
	 * the quicakction dialog is dismissed by clicking outside the dialog or
	 * clicking on sticky item.
	 */
	public void setOnDismissListener(PopupMenu.OnDismissListener listener) {
		setOnDismissListener(this);

		mDismissListener = listener;
	}

	@Override
	public void onDismiss() {
		if (!mDidAction && mDismissListener != null) {
			mDismissListener.onDismiss();
		}
	}

	/**
	 * Listener for item click
	 * 
	 */
	public interface OnMenuItemClickListener {
		public abstract void onItemClick(PopupMenu source, int pos, int menuId);
	}

	/**
	 * Listener for window dismiss
	 * 
	 */
	public interface OnDismissListener {
		public abstract void onDismiss();
	}
}