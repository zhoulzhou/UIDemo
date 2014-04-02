package com.example.uidemo.logic.render;

import java.util.ArrayList;
import java.util.List;

/**
 * 鍙繘琛屽姩浣滅殑鑺傜偣
 * @author yangzc
 *
 */
public abstract class CActionNode extends CNode {

	public abstract void rotate(float degrees);
	
	public abstract void setSkew(float skewX, float skewY);
	
	public abstract void setAnchor(int x, int y);
	
	public abstract void setScale(float sx, float sy);
	
	public abstract void setAlpha(int alpha);
	

	//鍔ㄤ綔鍒楄〃
	private List<CAction> mActions;
	
	/**
	 * 鎵ц鍔ㄤ綔
	 * @param action
	 */
	public synchronized void runAction(CAction action){
		if(action == null)
			return;
		if(mActions == null)
			mActions = new ArrayList<CAction>();
		
		mActions.add(action);
		action.start(this);
	}
	
	@Override
	public synchronized void update(float dt) {
		super.update(dt);
		
		if(mActions != null){
			List<CAction> rmAction = new ArrayList<CAction>();
			for(int i =0; i< mActions.size(); i++){
				CAction action = mActions.get(i);
				if(action == null)
					continue;
				action.update(dt);
				if(action.isDone())
					rmAction.add(action);
			}
			mActions.removeAll(rmAction);
		}
	}
	
}
