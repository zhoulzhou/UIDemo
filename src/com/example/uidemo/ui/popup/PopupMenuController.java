/**
 * 
 */
package com.example.uidemo.ui.popup;

import java.util.ArrayList;

import com.example.uidemo.R;
import com.example.uidemo.ui.popup.PopupMenu.OnMenuItemClickListener;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

/**
 *  通用弹出菜单控制类
 * @author zhangxiaoke
 * 
 */
public final class PopupMenuController implements OnMenuItemClickListener {

	public interface Callback {
		public void onPopupMenuItemSelected(
				PopupMenuController popupController, int position, int menuId);
	}

	public static final int MENU_ID_LIST_RENAME = 101;
	public static final int MENU_ID_LIST_CLEAR = 102;
	public static final int MENU_ID_LIST_DELETE = 103;

	private Context mContext;
	private Callback mCallback;
	private int mOrientation;
	private int mAnimStyle;
	private ArrayList<PopupMenuItem> mActionItems;

	private PopupMenu mPopupMenu;

	/**
	 * 弹出菜单控制类
	 */
	public PopupMenuController(Context context,
			PopupMenuController.Callback callback) {
		this(context, callback, PopupMenu.HORIZONTAL,
				PopupMenu.ANIM_GROW_FROM_RIGHT);
	}

	private PopupMenuController(Context context,
			PopupMenuController.Callback callback, int orientation,
			int animStyle) {
		this.mContext = context;
		this.mCallback = callback;
		this.mOrientation = orientation;
		this.mAnimStyle = animStyle;
		this.mActionItems = new ArrayList<PopupMenuItem>();
	}

	public void setCallback(PopupMenuController.Callback callback) {
		this.mCallback = callback;
	}

	public void addMenuItem(int menuId, int titleId, int iconId) {
		if (titleId < 0 || iconId < 0) {
			
			try{
				throw new NullPointerException(
						"title id  or icon id must not be null.");
			}catch(Exception e){
				e.printStackTrace();
				return;
			}

		}
		PopupMenuItem action = createMenuItem(menuId, titleId, iconId);
		if (action != null) {
			this.mActionItems.add(action);
		}
	}
	
	public void addMenuItem(int menuId, int titleId, int iconId, boolean enable) {
		if (titleId < 0 || iconId < 0) {
			
			try{
				throw new NullPointerException(
						"title id  or icon id must not be null.");
			}catch(Exception e){
				e.printStackTrace();
				return;
			}

		}
		PopupMenuItem action = createMenuItem(menuId, titleId, iconId);
		action.setEnable(enable);
		if (action != null) {
			this.mActionItems.add(action);
		}
	}

	public void addMenuItem(int menuId, String title, int iconId) {
		if (title == null || iconId < 0) {
			try{
				throw new NullPointerException("title or icon must not be null.");
			}catch(Exception e){
				e.printStackTrace();
				return;
			}
		}
		PopupMenuItem action = createMenuItem(menuId, title, iconId);
		if (action != null) {
			this.mActionItems.add(action);
		}
	}

	////anchor表示显示在那个view下面
	public PopupMenu showPopup(final View anchor) {
		if (mPopupMenu == null) {
			mPopupMenu = new PopupMenu(mContext, mOrientation);
		}
		setMenuItems();
		mPopupMenu.setAnimStyle(mAnimStyle);
		mPopupMenu.setOnMenuItemClickListener(this);
		mPopupMenu.show(anchor);
		return mPopupMenu;
	}

	private void setMenuItems() {
		if (mPopupMenu != null && mActionItems != null) {
			for (PopupMenuItem action : mActionItems) {
				mPopupMenu.addMenuItem(action);
			}
		}
	}

	private PopupMenuItem createMenuItem(int actionId, int titleId, int iconId) {
		Resources res = mContext.getResources();
		return createMenuItem(actionId, res.getString(titleId),
				res.getDrawable(iconId));
	}

	private PopupMenuItem createMenuItem(int actionId, String title, int iconId) {
		Resources res = mContext.getResources();
		return createMenuItem(actionId, title, res.getDrawable(iconId));
	}

	private PopupMenuItem createMenuItem(int actionId, String title,
			Drawable icon) {
		PopupMenuItem action = new PopupMenuItem(actionId, title, icon);
		return action;
	}

	@Override
	public void onItemClick(PopupMenu source, final int pos, final int menuId) {
		Log.d("PopupMenuController", "onItemClick, menuId=" + menuId);
		if (mCallback != null) {
			// 若是下载, 仅控制在线歌曲的下载, 对于我的下载中LocalPopWindow.MENU_ID_DOWNLOAD_STATUS_RESUME不限制(暂停or继续下载此时无法判断)
			// 在线歌曲的pop下载, 我的收藏pop下载
//			if (FavDownloadSharePopWindow.MENU_ID_LIST_DOWNLOAD == menuId
//					|| FavItemPopupWindow.MENU_ID_DOWNLOAD == menuId) {
//				PreferencesController pc = PreferencesController.getPreferences(mContext);
//				if (NetworkHelpers.isUsingMobileNetwork(mContext)) {
//					OnlyConnectInWifiDialog connectInWifiDialog = new OnlyConnectInWifiDialog(mContext, mContext
//							.getResources().getString(R.string.wifi_mobile_download_desc_flag_off), mContext
//							.getResources().getString(R.string.wifi_mobile_download_yes), null);
//					// 若wifi联网
//					if (pc.getOnlyUseWifi()) {
//						connectInWifiDialog = new OnlyConnectInWifiDialog(mContext, mContext.getResources()
//								.getString(R.string.wifi_mobile_download_desc_flag_on), mContext.getResources()
//								.getString(R.string.wifi_mobile_download_yes), null);
//					}
//					connectInWifiDialog.setContinueListener(new OnlyConnectInWifiDialog.ContinueListener() {
//						@Override
//						public void onContinue() {
//							doCallback(pos, menuId);
//						}
//
//						@Override
//						public void onCancel() {
//							// Empty
//						}
//					});
//					// 提示文字
//					connectInWifiDialog.show();
//					return;
//				}
//			}
			// 否则直接回调, 处理
			doCallback(pos, menuId);
		}
	}
	
	/*
	 * 回调处理
	 */
	private void doCallback(int pos, int menuId){
		mCallback.onPopupMenuItemSelected(this, pos, menuId);
	}

	private static void addLocalListPopupMenuItems(PopupMenuController pmc) {
		//添加menu
		if (pmc != null) {
			pmc.addMenuItem(MENU_ID_LIST_RENAME,
					R.string.popup_item_list_rename,
					R.drawable.ic_list_dropdown_rename_press);
			pmc.addMenuItem(MENU_ID_LIST_CLEAR, R.string.popup_item_list_clear,
					R.drawable.ic_list_dropdown_garbage_press);
			pmc.addMenuItem(MENU_ID_LIST_DELETE,
					R.string.popup_item_list_delete,
					R.drawable.ic_list_dropdown_delete_press);
		}

	}

	public static final void showLocalListPopupMenu(final Context context,
			final PopupMenuController.Callback callback, final View anchor) {
		PopupMenuController pmc = new PopupMenuController(context, callback);
		addLocalListPopupMenuItems(pmc);
		pmc.showPopup(anchor);
	}

}
