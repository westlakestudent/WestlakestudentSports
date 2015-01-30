package com.west.activity;

import com.west.interfaces.OnTimeChangedListener;
import com.west.utils.WestTimer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

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
	
	private TextView timeText = null;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test1);
		
		timeText = (TextView)findViewById(R.id.time);
		
		mWestTimer = WestTimer.getTimer();
		mWestTimer.registerTimer(this);
		mWestTimer.start();
	}

	@Override
	public void change(String time) {
		timeText.setText(time);
	}

}
