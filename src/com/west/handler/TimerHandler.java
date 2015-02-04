package com.west.handler;

import com.west.constant.Constants;
import com.west.interfaces.OnTimeChangedListener;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 *
 * TimerHandler
 * @author chendong
 * 2015年2月2日 上午9:36:48
 * @version 1.0.0
 *
 */
public class TimerHandler extends Handler {

	private OnTimeChangedListener mOnTimeChangedListener = null;
	
	private static final String TAG = "TimerHandler";
	
	public TimerHandler(OnTimeChangedListener listener){
		mOnTimeChangedListener = listener;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if (msg.what == Constants.TIMERSIGNAL) {
			String time = (String)msg.obj;
			Log.i(TAG, time);
			if (mOnTimeChangedListener != null) {
				mOnTimeChangedListener.onTimechanged(time);
			}
		}
	}
	
	
	
}
