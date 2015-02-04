package com.west.activity;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.opengles.GL10;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapDrawFrameCallback;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.west.adapter.PagerViewAdapter;
import com.west.interfaces.ClickListenerCallBack;
import com.west.interfaces.OnTimeChangedListener;
import com.west.ui.DataShowView;
import com.west.ui.MapLoadView;
import com.west.ui.WestDrawerL;
import com.west.ui.WestRelativelayout;
import com.west.utils.ScaleUtil;
import com.west.utils.WestTimer;
import com.west.widget.ActionBarDrawerToggle;
import com.west.widget.DrawerArrowDrawable;
import com.west.widget.dialog.Effectstype;
import com.west.widget.dialog.NiftyDialogBuilder;

import android.os.Bundle;
import android.os.Process;
import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

public class MainActivity extends Activity implements OnTimeChangedListener,
		ClickListenerCallBack, OnMapDrawFrameCallback {

	private static final String TAG = "MainActivity";
	private WestDrawerL mDrawerLayout = null;
	private WestRelativelayout layout = null;
	private ActionBarDrawerToggle mDrawerToggle = null;
	private DrawerArrowDrawable drawerArrow = null;
	private NiftyDialogBuilder dialogBuilder = null;
	private WestTimer mWestTimer = null;
	private List<View> views = new ArrayList<View>();
	private DataShowView showView = null;
	private MapLoadView mapView = null;
	private BaiduMap mBaiduMap = null;
	private LocationClient mLocClient = null;
	private LocationMode mCurrentMode = null;
	boolean isFirstLoc = true;// 是否首次定位
	private LatLng lastLatLng = null;
	private List<LatLng> latLngPolygon = new ArrayList<LatLng>();
	private float[] vertexs = null;
	private FloatBuffer vertexBuffer = null;
	private boolean drawopen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScaleUtil.scaleInit(this, 1280, 720, 320);
		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);

		dialogBuilder = NiftyDialogBuilder.getInstance(this);
		dialogBuilder.withTitle("提示").withMessage("真的要退出吗?");

		mWestTimer = WestTimer.getTimer();
		mWestTimer.registerTimer(this);

		mLocClient = new LocationClient(getApplicationContext());
		mLocClient.registerLocationListener(mBDLocationListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);

		drawerArrow = new DrawerArrowDrawable(this) {
			@Override
			public boolean isLayoutRtl() {
				return false;
			}
		};
		initpagers();
		initView();

		setContentView(mDrawerLayout);
	}

	private void initpagers() {
		showView = new DataShowView(this, this);
		views.add(showView);
		mapView = new MapLoadView(this, this);
		views.add(mapView);
		mBaiduMap = mapView.getMap();
	}

	private void initView() {
		PagerViewAdapter pageradapter = new PagerViewAdapter(views);
		layout = new WestRelativelayout(this, this, pageradapter);

		mDrawerLayout = new WestDrawerL(this, layout);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				drawerArrow, R.string.drawer_open, R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.syncState();

		String[] values = new String[] { "Stop Animation (Back icon)",
				"Stop Animation (Home icon)", "Start Animation",
				"Change Color", "GitHub Page", "Share", "Rate" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		mDrawerLayout.initNavDrawer(adapter,
				new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

					}
				});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (mDrawerLayout.isOpen()) {
				mDrawerLayout.close();
			} else {
				mDrawerLayout.open();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onResume() {
		if (mLocClient != null) {
			mLocClient.start();
			Log.d(TAG, mLocClient.isStarted() + "----------->onResume");
			if (mLocClient.isStarted())
				mLocClient.requestLocation();
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mWestTimer.isStarted())
			mWestTimer.stop();
		if (mLocClient != null) {
			mLocClient.stop();
			mLocClient = null;
		}
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		dialogBuilder.withEffect(Effectstype.Slidetop)
				.withTitleColor("#FFFFFF").withDividerColor("#11000000")
				.withMessageColor("#FFFFFFFF").withDialogColor("#66B3FF")
				.withIcon(getResources().getDrawable(R.drawable.ic_launcher))
				.withDuration(200).isCancelableOnTouchOutside(true)
				.setButton1Click(confirm).setButton2Click(cancel)
				.withButton1Text("确定").withButton2Text("取消");
		dialogBuilder.show();
	}

	private OnClickListener cancel = new OnClickListener() {

		@Override
		public void onClick(View v) {
			dialogBuilder.dismiss();
		}
	};

	private OnClickListener confirm = new OnClickListener() {

		@Override
		public void onClick(View v) {
			dialogBuilder.dismiss();
			MainActivity.this.finish();
			Process.killProcess(Process.myPid());
			System.exit(0);
		}
	};

	@Override
	public void onTimechanged(String time) {
		if (showView != null)
			showView.refreshTime(time);
	}

	private BDLocationListener mBDLocationListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null && mBaiduMap != null)
				return;

			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius()).direction(100)
					.latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);

			LatLng ll = new LatLng(location.getLatitude(),
					location.getLongitude());

			Log.d(TAG, ll.latitude + "#####" + ll.longitude);
			if (isFirstLoc) {
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
				isFirstLoc = false;
			}

			if (lastLatLng != null && drawopen) {
				latLngPolygon.clear();
				latLngPolygon.add(lastLatLng);
				latLngPolygon.add(ll);
			}
			lastLatLng = ll;

		}

		@Override
		public void onReceivePoi(BDLocation pou) {

		}

	};

	@Override
	public void startCallback() {
		mWestTimer.start();
		mBaiduMap.clear();
		BitmapDescriptor marker = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_st);
		OverlayOptions option = new MarkerOptions().position(lastLatLng).icon(
				marker).zIndex(9);
		mBaiduMap.addOverlay(option);
		drawopen = true;
	}

	@Override
	public void stopCallback() {
		mWestTimer.pause();
		drawopen = false;
	}

	@Override
	public void continueCallback() {
		mWestTimer.start();
		drawopen = true;
	}

	@Override
	public void overCallback() {
		mWestTimer.reset();
		BitmapDescriptor marker = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_en);
		OverlayOptions option = new MarkerOptions().position(lastLatLng).icon(
				marker).zIndex(9);
		mBaiduMap.addOverlay(option);
		drawopen = false;
	}

	@Override
	public void onMapDrawFrame(GL10 gl, MapStatus sta) {
		if (mBaiduMap.getProjection() != null && drawopen) {
			Log.d(TAG, "onMapDrawFrame------>" + sta.target.toString());
			calPolylinePoint(sta);
			drawPolyline(gl, Color.argb(255, 255, 0, 0), vertexBuffer, 10, 2,
					sta);
		}

	}

	public void calPolylinePoint(MapStatus mspStatus) {
		PointF[] polyPoints = new PointF[latLngPolygon.size()];
		vertexs = new float[3 * latLngPolygon.size()];
		int i = 0;
		for (LatLng xy : latLngPolygon) {
			polyPoints[i] = mBaiduMap.getProjection().toOpenGLLocation(xy,
					mspStatus);
			vertexs[i * 3] = polyPoints[i].x;
			vertexs[i * 3 + 1] = polyPoints[i].y;
			vertexs[i * 3 + 2] = 0.0f;
			i++;
		}
		for (int j = 0; j < vertexs.length; j++) {
			Log.d(TAG, "vertexs[" + j + "]: " + vertexs[j]);
		}
		vertexBuffer = makeFloatBuffer(vertexs);
	}

	private FloatBuffer makeFloatBuffer(float[] fs) {
		ByteBuffer bb = ByteBuffer.allocateDirect(fs.length * 4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(fs);
		fb.position(0);
		return fb;
	}

	private void drawPolyline(GL10 gl, int color, FloatBuffer lineVertexBuffer,
			float lineWidth, int pointSize, MapStatus drawingMapStatus) {
		Log.d(TAG, "drawline");
		gl.glEnable(GL10.GL_BLEND);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		float colorA = Color.alpha(color) / 255f;
		float colorR = Color.red(color) / 255f;
		float colorG = Color.green(color) / 255f;
		float colorB = Color.blue(color) / 255f;

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, lineVertexBuffer);
		gl.glColor4f(colorR, colorG, colorB, colorA);
		gl.glLineWidth(lineWidth);
		gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, pointSize);

		gl.glDisable(GL10.GL_BLEND);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

}
