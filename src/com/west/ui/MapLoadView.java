package com.west.ui;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.BaiduMap.OnMapDrawFrameCallback;

import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * 
 * MapLoadView
 * 
 * @author chendong 2015年2月4日 下午2:00:50
 * @version 1.0.0
 * 
 */
public class MapLoadView extends RelativeLayout{

	private static final String TAG = "MapLoadView";

	private MapView mMapView = null;;
	private BaiduMap mBaiduMap = null;

	private OnMapDrawFrameCallback callback = null;

	public MapLoadView(Context context) {
		super(context);
		init(context);
	}

	public MapLoadView(Context context, OnMapDrawFrameCallback back) {
		super(context);
		callback = back;
		init(context);
	}

	private void init(Context context) {
		Log.d(TAG, "init");
		mMapView = new MapView(context);
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(17.0f);
		mBaiduMap.setMapStatus(msu);
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setOnMapDrawFrameCallback(callback);
		addView(mMapView);
	}

	public BaiduMap getMap(){
		return mBaiduMap;
	}

}
