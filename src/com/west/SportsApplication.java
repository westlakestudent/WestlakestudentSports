package com.west;

import com.baidu.mapapi.SDKInitializer;

import android.app.Application;

/**
 *
 * SportsApplication
 * @author chendong
 * 2015年1月30日 上午10:38:42
 * @version 1.0.0
 *
 */
public class SportsApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		SDKInitializer.initialize(this);
	}

	
}
