package com.west.activity;

import com.west.interfaces.OnTimeChangedListener;
import com.west.ui.DataShowView;
import com.west.utils.WestTimer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 *
 * DataActivity
 * @author chendong
 * 2015年1月30日 下午2:16:28
 * @version 1.0.0
 *
 */
public class DataActivity extends Activity implements OnTimeChangedListener{

	private WestTimer mWestTimer = null;
	
	private static final String TAG = "DataActivity";
	
	private DataShowView showView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mWestTimer = WestTimer.getTimer();
		mWestTimer.registerTimer(this);
//		mWestTimer.start();
		
		showView = new DataShowView(this,mWestTimer);
		setContentView(showView);
	}

	@Override
	public void change(String time) {
		if(showView != null)
			showView.refreshTime(time);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
		if(mWestTimer != null)
			mWestTimer.stop();
	}
	
}
