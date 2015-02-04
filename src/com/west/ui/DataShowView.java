package com.west.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.west.activity.R;
import com.west.interfaces.ClickListenerCallBack;
import com.west.utils.ScaleUtil;

/**
 * 
 * DataShowView
 * 
 * @author chendong 2015年2月2日 下午4:54:43
 * @version 1.0.0
 * 
 */
public class DataShowView extends RelativeLayout implements OnClickListener {

	private static final String TAG = "DataShowView";

	private LinearLayout show_view = null;

	private DisItem distance = null;

	private ItemView time = null;

	private ItemView calorie = null;

	private Button startBtn = null;

	private Button stopBtn = null;

	private Button continueBtn = null;

	private Button overBtn = null;
	
	private ClickListenerCallBack callback = null;

	private static final int SHOWVIEW = 0x000000f2;

	public DataShowView(Context context) {
		super(context);
		Log.d(TAG, "create");
		createUI(context);
	}
	
	public DataShowView(Context context,ClickListenerCallBack back){
		super(context);
		Log.d(TAG, "create");
		callback = back;
		createUI(context);
	}

	private void createUI(Context context) {
		LayoutParams params = null;

		show_view = new LinearLayout(context);
		show_view.setId(SHOWVIEW);
		show_view.setOrientation(LinearLayout.VERTICAL);
		show_view.setBackgroundResource(R.drawable.data_showbg);
		params = new LayoutParams(ScaleUtil.scale(600), ScaleUtil.scale(320));
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.topMargin = ScaleUtil.scale(100);
		addView(show_view, params);

		LinearLayout.LayoutParams lp = null;

		distance = new DisItem(context);
		distance.setGravity(Gravity.CENTER);
		lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		lp.topMargin = ScaleUtil.scale(30);
		show_view.addView(distance, lp);

		LinearLayout hlayout = new LinearLayout(context);

		lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_HORIZONTAL;
		lp.topMargin = ScaleUtil.scale(20);
		show_view.addView(hlayout, lp);

		time = new ItemView(context, R.drawable.goal_ic_time,"00:00:00");
		time.setGravity(Gravity.CENTER);
		lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.weight = 1;
		hlayout.addView(time, lp);

		calorie = new ItemView(context, R.drawable.goal_icon_colora,"0");
		calorie.setGravity(Gravity.CENTER);
		lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER_VERTICAL;
		lp.weight = 1;
		hlayout.addView(calorie, lp);

		startBtn = new Button(context);
		startBtn.setText("开      始");
		startBtn.setBackgroundResource(R.drawable.start_btn_selector);
		startBtn.setGravity(Gravity.CENTER);
		startBtn.setOnClickListener(this);

		params = new LayoutParams(ScaleUtil.scale(400),
				LayoutParams.WRAP_CONTENT);
		params.addRule(BELOW, SHOWVIEW);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.topMargin = ScaleUtil.scale(240);
		addView(startBtn, params);

		stopBtn = new Button(context);
		stopBtn.setText("暂      停");
		stopBtn.setBackgroundResource(R.drawable.stop_btn_selector);
		stopBtn.setGravity(Gravity.CENTER);
		stopBtn.setOnClickListener(this);

		params = new LayoutParams(ScaleUtil.scale(400),
				LayoutParams.WRAP_CONTENT);
		params.addRule(BELOW, SHOWVIEW);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		params.topMargin = ScaleUtil.scale(240);
		addView(stopBtn, params);
		stopBtn.setVisibility(GONE);

		overBtn = new Button(context);
		overBtn.setText("结    束");
		overBtn.setBackgroundResource(R.drawable.stop_btn_selector);
		overBtn.setGravity(Gravity.CENTER);
		overBtn.setOnClickListener(this);

		params = new LayoutParams(ScaleUtil.scale(200),
				LayoutParams.WRAP_CONTENT);
		params.addRule(BELOW, SHOWVIEW);
		params.leftMargin = ScaleUtil.scale(100);
		params.topMargin = ScaleUtil.scale(240);
		addView(overBtn, params);
		overBtn.setVisibility(GONE);

		continueBtn = new Button(context);
		continueBtn.setText("继    续");
		continueBtn.setBackgroundResource(R.drawable.start_btn_selector);
		continueBtn.setGravity(Gravity.CENTER);
		continueBtn.setOnClickListener(this);

		params = new LayoutParams(ScaleUtil.scale(200),
				LayoutParams.WRAP_CONTENT);
		params.addRule(BELOW, SHOWVIEW);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.rightMargin = ScaleUtil.scale(100);
		params.topMargin = ScaleUtil.scale(240);
		addView(continueBtn, params);
		continueBtn.setVisibility(GONE);
	}

	public void refreshTime(String timestr) {
		if (timestr != null)
			time.refreshShow(timestr);
	}

	public void refreshDistance(String dis) {
		if (dis != null)
			distance.refreshDis(dis);
	}

	public void refreshCalorie(String c) {
		if (c != null)
			calorie.refreshShow(c);
	}

	@Override
	public void onClick(View v) {
		if (v == startBtn) {
			startBtn.setVisibility(GONE);
			stopBtn.setVisibility(VISIBLE);
			if(callback != null)
				callback.startCallback();
		} else if (v == stopBtn) {
			continueBtn.setVisibility(VISIBLE);
			overBtn.setVisibility(VISIBLE);
			stopBtn.setVisibility(GONE);
			if(callback != null)
				callback.stopCallback();
		} else if (v == overBtn) {
			startBtn.setVisibility(VISIBLE);
			stopBtn.setVisibility(GONE);
			continueBtn.setVisibility(GONE);
			overBtn.setVisibility(GONE);
			if(callback != null)
				callback.overCallback();
		} else if (v == continueBtn) {
			startBtn.setVisibility(GONE);
			stopBtn.setVisibility(VISIBLE);
			continueBtn.setVisibility(GONE);
			overBtn.setVisibility(GONE);
			if(callback != null)
				callback.continueCallback();
		}
	}

	private class ItemView extends LinearLayout {

		private ImageView icon = null;
		private TextView show = null;

		public ItemView(Context context) {
			super(context);
		}

		public ItemView(Context context, int resbg,String str) {
			super(context);
			setOrientation(LinearLayout.VERTICAL);

			LayoutParams params = null;
			icon = new ImageView(context);
			icon.setImageResource(resbg);

			params = new LayoutParams(ScaleUtil.scale(50), ScaleUtil.scale(50));
			addView(icon, params);

			show = new TextView(context);
			show.setText(str);
			show.setGravity(Gravity.CENTER);
			show.setTextSize(15);
			show.setTextColor(Color.parseColor("#FFFFFF"));
			params = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			params.topMargin = ScaleUtil.scale(5);
			addView(show, params);

		}

		public void refreshShow(String str) {
			if (show != null)
				show.setText(str);
		}
	}

	private class DisItem extends LinearLayout {

		private TextView dis = null;

		private TextView unit = null;

		public DisItem(Context context) {
			super(context);

			dis = new TextView(context);
			dis.setText("0.00");
			dis.setTextColor(Color.parseColor("#FFFFFF"));
			dis.setTextSize(60);
			dis.setGravity(Gravity.CENTER);
			addView(dis);

			LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			p.gravity = Gravity.BOTTOM;
			p.bottomMargin = ScaleUtil.scale(14);
			unit = new TextView(context);
			unit.setText("km");
			unit.setTextSize(16);
			unit.setTextColor(Color.parseColor("#FFFFFF"));
			addView(unit, p);

		}

		public void refreshDis(String str) {
			if (dis != null)
				dis.setText(str);
		}

	}
}
