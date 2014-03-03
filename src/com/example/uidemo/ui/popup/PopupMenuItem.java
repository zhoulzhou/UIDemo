package com.example.uidemo.ui.popup;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * 通用弹出菜单，菜单项
 * 
 */
class PopupMenuItem {
	private Drawable icon;
	private Bitmap thumb;
	private String title;
	private int actionId = -1;
	private boolean selected;
	private boolean sticky;
	private boolean enable=true;

	/**
	 * Constructor
	 * 
	 * @param actionId
	 *            Action id for case statements
	 * @param title
	 *            Title
	 * @param icon
	 *            Icon to use
	 */
	public PopupMenuItem(int actionId, String title, Drawable icon) {
		this.title = title;
		this.icon = icon;
		this.actionId = actionId;
	}

	/**
	 * Constructor
	 */
	public PopupMenuItem() {
		this(-1, null, null);
	}

	/**
	 * Constructor
	 * 
	 * @param actionId
	 *            Action id of the item
	 * @param title
	 *            Text to show for the item
	 */
	public PopupMenuItem(int actionId, String title) {
		this(actionId, title, null);
	}

	/**
	 * Constructor
	 * 
	 * @param icon
	 *            {@link Drawable} action icon
	 */
	public PopupMenuItem(Drawable icon) {
		this(-1, null, icon);
	}

	/**
	 * Constructor
	 * 
	 * @param actionId
	 *            Action ID of item
	 * @param icon
	 *            {@link Drawable} action icon
	 */
	public PopupMenuItem(int actionId, Drawable icon) {
		this(actionId, null, icon);
	}

	/**
	 * Set action title
	 * 
	 * @param title
	 *            action title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get action title
	 * 
	 * @return action title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Set action icon
	 * 
	 * @param icon
	 *            {@link Drawable} action icon
	 */
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	/**
	 * Get action icon
	 * 
	 * @return {@link Drawable} action icon
	 */
	public Drawable getIcon() {
		return this.icon;
	}

	/**
	 * Set action id
	 * 
	 * @param actionId
	 *            Action id for this action
	 */
	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	/**
	 * @return Our action id
	 */
	public int getActionId() {
		return actionId;
	}

	/**
	 * Set sticky status of button
	 * 
	 * @param sticky
	 *            true for sticky, pop up sends event but does not disappear
	 */
	public void setSticky(boolean sticky) {
		this.sticky = sticky;
	}

	/**
	 * @return true if button is sticky, menu stays visible after press
	 */
	public boolean isSticky() {
		return sticky;
	}

	/**
	 * Set selected flag;
	 * 
	 * @param selected
	 *            Flag to indicate the item is selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * Check if item is selected
	 * 
	 * @return true or false
	 */
	public boolean isSelected() {
		return this.selected;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	/**
	 * Set thumb
	 * 
	 * @param thumb
	 *            Thumb image
	 */
	public void setThumb(Bitmap thumb) {
		this.thumb = thumb;
	}

	/**
	 * Get thumb image
	 * 
	 * @return Thumb image
	 */
	public Bitmap getThumb() {
		return this.thumb;
	}
}