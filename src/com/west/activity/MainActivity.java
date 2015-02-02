package com.west.activity;

import java.util.ArrayList;
import java.util.List;

import com.west.adapter.PagerViewAdapter;
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
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

	private WestDrawerL mDrawerLayout = null;
	private WestRelativelayout layout = null;
	private ActionBarDrawerToggle mDrawerToggle = null;
	private DrawerArrowDrawable drawerArrow = null;
	private LocalActivityManager LocalMgr = null;
	private NiftyDialogBuilder dialogBuilder = null;
	private WestTimer mWestTimer = null;
	private List<View> views = new ArrayList<View>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ScaleUtil.scaleInit(this, 1280, 720, 320);
		ActionBar ab = getActionBar();
		ab.setDisplayHomeAsUpEnabled(true);
		ab.setHomeButtonEnabled(true);

		LocalMgr = new LocalActivityManager(this, true);
		LocalMgr.dispatchCreate(savedInstanceState);

		dialogBuilder = NiftyDialogBuilder.getInstance(this);
		dialogBuilder.withTitle("提示").withMessage("真的要退出吗?");
		
		mWestTimer = WestTimer.getTimer();

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
		Intent intent = new Intent(this, DataActivity.class);
		views.add(LocalMgr.startActivity("DataActivity", intent).getDecorView());
		intent = new Intent(this, MapActivity.class);
		views.add(LocalMgr.startActivity("MapActivity", intent).getDecorView());
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
	protected void onPause() {
		super.onPause();
		LocalMgr.dispatchPause(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		LocalMgr.dispatchResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mWestTimer.isStarted())
			mWestTimer.stop();
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

}
