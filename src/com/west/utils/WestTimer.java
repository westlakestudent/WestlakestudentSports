package com.west.utils;

import java.util.Locale;

import com.west.constant.Constants;
import com.west.handler.TimerHandler;
import com.west.interfaces.OnTimeChangedListener;

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

	private OnTimeChangedListener mOnTimeChangedListener = null;

	private static final String DATE_FORMAT = "%02d:%02d:%02d";

	private int hour = 0;

	private int min = 0;

	private int sec = 1;

	private TimerHandler handler = null;

	private boolean isStarted = false;

	private WestTimer() {
		Log.i(TAG, "private construct");
	}

	public static WestTimer getTimer() {
		if (instance == null)
			instance = new WestTimer();

		return instance;
	}

	public void registerTimer(OnTimeChangedListener l) {
		mOnTimeChangedListener = l;
		handler = new TimerHandler(mOnTimeChangedListener);
	}

	public void start() {
		if (!isStarted()) {
			handler.post(timeTask);
			isStarted = true;
			Log.d(TAG, "Timer is start...");
		}
	}

	public void pause() {
		handler.removeCallbacks(timeTask);
		isStarted = false;
		Log.d(TAG, "Timer is paused...");
	}

	public void reset() {
		handler.removeCallbacks(timeTask);
		hour = 0;
		min = 0;
		sec = 0;
		isStarted = false;
		if (mOnTimeChangedListener != null) {
			mOnTimeChangedListener.onTimechanged(requireTime());
		}
		Log.d(TAG, "Timer is reset...");
	}

	public void stop() {
		if (isStarted()) {
			handler.removeCallbacks(timeTask);
			isStarted = false;
			Log.d(TAG, "Timer is stop...");
		}

	}

	public boolean isStarted() {
		return isStarted;
	}

	private Runnable timeTask = new Runnable() {

		@Override
		public void run() {
			if (hour == 59 && min == 59 && sec == 60) {
				hour = 0;
				min = 0;
				sec = 0;
			}
			
			if (min == 59 && sec == 60) {
				hour++;
				min = 0;
				sec = 0;
			}
			
			if (sec == 60) {
				min++;
				sec = 0;
			}


			String time = requireTime();

			Message msg = handler.obtainMessage();
			msg.obj = time;
			msg.what = Constants.TIMERSIGNAL;

			handler.sendMessage(msg);
			sec++;
			handler.postDelayed(timeTask, 1000);
		}
	};

	private String requireTime() {
		/*
		 * String shour = ""; String smin = ""; String ssec = "";
		 * 
		 * 
		 * if (sec < 10) { ssec = "0" + sec; } else { ssec =
		 * String.valueOf(sec); }
		 * 
		 * if (min < 10) { smin = "0" + min; } else { smin =
		 * String.valueOf(min); }
		 * 
		 * if (hour < 10) { shour = "0" + hour; } else { shour =
		 * String.valueOf(hour); }
		 */

		return String.format(Locale.CHINA, DATE_FORMAT, hour, min, sec);
	}

	public String Acquire() {
		return requireTime();
	}

	public int getHour() {
		return hour;
	}

	public int getMin() {
		return min;
	}

	public int getSec() {
		return sec;
	}

}
