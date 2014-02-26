package com.example.uidemo.logic.local;

import android.os.Bundle;

public interface PlayStateChangeObserver{
	public void onPlayStateChange(String what, Bundle data);
}