package com.west.ui;

import com.west.activity.R;
import com.west.utils.ScaleUtil;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * WestDrawerL
 * 
 * @author chendong 2015年1月30日 上午10:51:03
 * @version 1.0.0
 * 
 */
public class WestDrawerL extends DrawerLayout {

	private ListView navdrawer = null;

	private RelativeLayout relativeLayout = null;

	public WestDrawerL(Context context) {
		super(context);
		createUI(context);
	}

	public WestDrawerL(Context context, RelativeLayout layout) {
		super(context);
		relativeLayout = layout;
		createUI(context);
	}

	private void createUI(Context context) {
		LayoutParams params = null;

		params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		addView(relativeLayout, params);

		navdrawer = new ListView(context);
		navdrawer.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		navdrawer.setBackgroundColor(getResources().getColor(
				R.color.ldrawer_color));
		params = new LayoutParams(ScaleUtil.scale(400),
				LayoutParams.MATCH_PARENT);
		params.gravity = Gravity.START;
		addView(navdrawer, params);
		
		TextView test = new TextView(context);
		test.setText("测试");
		navdrawer.addFooterView(test);
	}
	
	
	public void initNavDrawer(ListAdapter adapter,OnItemClickListener listener){
		navdrawer.setAdapter(adapter);
		navdrawer.setOnItemClickListener(listener);
	}
	
	
	public boolean isOpen(){
		return isDrawerOpen(navdrawer);
	}
	
	
	public void open(){
		openDrawer(navdrawer);
	}
	
	public void close(){
		closeDrawer(navdrawer);
	}
}
