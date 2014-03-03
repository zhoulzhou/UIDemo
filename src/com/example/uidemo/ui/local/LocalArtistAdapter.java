package com.example.uidemo.ui.local;

import com.example.uidemo.R;
import com.example.uidemo.ui.popup.PopupMenuController;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LocalArtistAdapter extends CursorAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	
	private OnMenuClickListener mOnMenuClickListener;
	
	public interface OnMenuClickListener {
		/**
		 * 设置铃声回调
		 */
		public void onPlayClicked(String artist);

		/**
		 * 添加到列表回调
		 * 
		 * @param id
		 */
		public void onAddtoClicked(String artist);

		/**
		 * 删除回调
		 */
		public void onDeleteClicked(String artist);
	}

	public LocalArtistAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		// TODO Auto-generated constructor stub
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}
	
	public LocalArtistAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		// TODO Auto-generated constructor stub
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		// TODO Auto-generated method stub
		final ViewHolder vh = (ViewHolder) v.getTag();
		String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Artists.ARTIST));
		vh.line1.setText(name);
		long n = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
		name = n + "首";
		vh.line2.setText(name);
		
		final PopupWindowCallBack callback = new PopupWindowCallBack(c.getPosition());
		vh.itemContainer.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				showPopupWindow(vh.itemContainer, callback);
				return true;
			}
		});
	}
	
	private void showPopupWindow(View view, PopupWindowCallBack callback) {
		ArtistListPopupWindow.showPlayAddToDeletePopupMenu(mContext, callback, view);
	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View v = mInflater.inflate(R.layout.ui_local_artist_item, null);
		ViewHolder vh = new ViewHolder();
		vh.icon = (ImageView) v.findViewById(R.id.local_list_item_2_icon);
		vh.icon.setPadding(0, 0, 1, 0);
		vh.line1 = (TextView) v.findViewById(R.id.local_list_item_2_line1);
		vh.line2 = (TextView) v.findViewById(R.id.local_list_item_2_line2);
		vh.hint = (ImageView) v.findViewById(R.id.local_list_item_2_arrow);
		
		vh.itemContainer = (RelativeLayout) v.findViewById(R.id.local_list_item_name_container);
		
		vh.playContainer = (LinearLayout) v.findViewById(R.id.local_list_play_container);
		vh.addContainer = (LinearLayout) v.findViewById(R.id.local_list_addto_container);
		vh.deleteContainer = (LinearLayout) v.findViewById(R.id.local_list_filter_container);
		
		vh.play = (ImageView) v.findViewById(R.id.local_list_play);
		vh.add = (ImageView) v.findViewById(R.id.local_list_addto);
		vh.delete = (ImageView) v.findViewById(R.id.local_list_filter);
		
		vh.playText = (TextView) v.findViewById(R.id.local_list_play_name);
		vh.addText = (TextView) v.findViewById(R.id.local_list_addto_name);
		vh.deleteText = (TextView) v.findViewById(R.id.local_list_filter_name);
		v.setTag(vh);
		
		return v;
	}
	
	public class ViewHolder {
		TextView line1;
		TextView line2;
		ImageView icon;
		ImageView hint;
		
		RelativeLayout itemContainer;
		
		LinearLayout playContainer;
		LinearLayout addContainer;
		LinearLayout deleteContainer;
		
		ImageView play;
		ImageView add;
		ImageView delete;
		
		TextView playText;
		TextView addText;
		TextView deleteText;
	}
	
	class PopupWindowCallBack implements PopupMenuController.Callback {
		private int mPosition;

		public PopupWindowCallBack(int position) {
			mPosition = position;
		}

		@Override
		public void onPopupMenuItemSelected(PopupMenuController popupController, int position, int menuId) {
			Cursor cursor = getCursor();
			cursor.moveToPosition(mPosition);
			String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
			switch (menuId) {
			case ArtistListPopupWindow.MENU_ID_LIST_ADDTO:
				if (mOnMenuClickListener != null) {
					mOnMenuClickListener.onAddtoClicked(name);
				}
				break;
			case ArtistListPopupWindow.MENU_ID_LIST_PLAY:
				if (mOnMenuClickListener != null) {
					mOnMenuClickListener.onPlayClicked(name);
				}
				break;
			case ArtistListPopupWindow.MENU_ID_LIST_DELETE:
				if (mOnMenuClickListener != null) {
					mOnMenuClickListener.onDeleteClicked(name);
				}
				break;
			default:
				break;
			}
		}
	};
	
	static class ArtistListPopupWindow {
		public static final int MENU_ID_LIST_ADDTO = 113;
		public static final int MENU_ID_LIST_PLAY = 114;
		public static final int MENU_ID_LIST_DELETE = 115;

		public static void showPlayAddToDeletePopupMenu(final Context context, final PopupMenuController.Callback callback, final View anchor) {
			PopupMenuController pmc = new PopupMenuController(context, callback);
			addPlayAddToDeletePopupMenuItems(pmc);
			pmc.showPopup(anchor);
		}

		private static void addPlayAddToDeletePopupMenuItems(PopupMenuController pmc) {
			if (pmc != null) {
				pmc.addMenuItem(MENU_ID_LIST_PLAY, R.string.popup_item_play, R.drawable.ic_list_dropdown_play_press);
				pmc.addMenuItem(MENU_ID_LIST_ADDTO, R.string.popup_item_add_to, R.drawable.ic_list_dropdown_plus_press);
				pmc.addMenuItem(MENU_ID_LIST_DELETE, R.string.popup_item_delete, R.drawable.ic_list_dropdown_delete_press);
			}
		}
	}
	
}