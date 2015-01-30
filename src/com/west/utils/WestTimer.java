package com.west.utils;

import com.west.interfaces.OnTimeChangedListener;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 
 * WestTimer
 * 
 * @author chendong 2015年1月30日 下午4:57:39
 * @version 1.0.0
 * 
 */
public class WestTimer {

	private static final String TAG = "WestTimer";

	private static WestTimer instance = null;

	private static final int SIGNAL = 0;

	private OnTimeChangedListener mOnTimeChangedListener = null;

	private int hour = 0;

	private int min = 0;

	private int sec = 0;

	private WestTimer() {
	}

	public static WestTimer getTimer() {
		if (instance == null)
			instance = new WestTimer();

		return instance;
	}

	public void registerTimer(OnTimeChangedListener l) {
		mOnTimeChangedListener = l;
	}

	public void start() {
		if (mOnTimeChangedListener != null) {
			mOnTimeChangedListener.change(requireTime());
		}
		handler.post(timeTask);
	}

	public void pause() {
		handler.removeCallbacks(timeTask);
	}

	public void reset() {
		handler.removeCallbacks(timeTask);
		hour = 0;
		min = 0;
		sec = 0;
	}

	public void stop() {
		handler.removeCallbacks(timeTask);
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == SIGNAL) {
				String time = (String)msg.obj;
				Log.i(TAG, time);
				if (mOnTimeChangedListener != null) {
					mOnTimeChangedListener.change(time);
				}
			}
		}

	};

	private Runnable timeTask = new Runnable() {

		@Override
		public void run() {
			if (sec >= 59) {
				min++;
				sec = 0;
			}

			if (min == 59 && sec == 59) {
				hour++;
				min = 0;
				sec = 0;
			}

			String time = requireTime();
			
			Message msg = handler.obtainMessage();
			msg.obj = time;
			msg.what = SIGNAL;
			
			handler.sendMessage(msg);
			sec++;
			handler.postDelayed(timeTask, 1000);
		}
	};

	private String requireTime() {
		String shour = "";
		String smin = "";
		String ssec = "";

		if (sec < 10) {
			ssec = "0" + sec;
		} else {
			ssec = String.valueOf(sec);
		}

		if (min < 10) {
			smin = "0" + min;
		} else {
			String.valueOf(min);
		}

		if (hour < 10) {
			shour = "0" + hour;
		} else {
			String.valueOf(hour);
		}

		return shour + ":" + smin + ":" + ssec;
	}

	public String Acquire() {
		return requireTime();
	}
}
