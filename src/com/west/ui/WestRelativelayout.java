package com.west.ui;

import com.west.activity.R;
import com.west.adapter.PagerViewAdapter;
import com.west.utils.ScaleUtil;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * WestRelativelayout
 * 
 * @author chendong 2015年1月30日 上午11:05:39
 * @version 1.0.0
 * 
 */
public class WestRelativelayout extends RelativeLayout implements
		OnClickListener {

	private Context context = null;
	private Activity activity = null;
	private LinearLayout barlayout = null;
	private ViewPager viewpager = null;
	private ImageView roller = null;
	private TextView left = null;
	private TextView right = null;

	private static final int BARLAYOUT = 0x000000f0;
	private static final int ROLLER = 0x000000f1;

	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private int screenW = 0;// 屏幕宽度

	public WestRelativelayout(Context context) {
		super(context);
	}

	public WestRelativelayout(Context context, Activity ac,
			PagerViewAdapter adapter) {
		super(context);
		setBackgroundColor(Color.parseColor("#FFFFFF"));
		this.context = context;
		activity = ac;
		createUI(adapter);
		initroller();
	}

	private void createUI(PagerViewAdapter adapter) {
		LayoutParams params = null;
		barlayout = new LinearLayout(context);
		barlayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
		barlayout.setId(BARLAYOUT);
		params = new LayoutParams(LayoutParams.MATCH_PARENT,
				ScaleUtil.scale(70));
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		addView(barlayout, params);

		LinearLayout.LayoutParams lp = null;

		left = new TextView(context);
		left.setGravity(Gravity.CENTER);
		left.setTextColor(Color.GRAY);
		left.setText(R.string.sport_data);
		left.setTextSize(15);
		left.setOnClickListener(this);
		lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		lp.gravity = Gravity.CENTER_VERTICAL;
		barlayout.addView(left, lp);

		right = new TextView(context);
		right.setGravity(Gravity.CENTER);
		right.setText(R.string.sport_road);
		right.setTextSize(15);
		right.setTextColor(Color.GRAY);
		right.setOnClickListener(this);
		lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.weight = 1;
		lp.gravity = Gravity.CENTER_VERTICAL;
		barlayout.addView(right, lp);

		roller = new ImageView(context);
		roller.setScaleType(ScaleType.MATRIX);
		roller.setId(ROLLER);
		roller.setImageResource(R.drawable.roller);
		params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		params.addRule(BELOW, BARLAYOUT);
		addView(roller, params);

		viewpager = new ViewPager(context);
		params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		params.addRule(BELOW, ROLLER);
		addView(viewpager, params);

		viewpager.setOnPageChangeListener(mOnPageChangeListener);
		viewpager.setAdapter(adapter);
		viewpager.setCurrentItem(0);

	}

	private void initroller() {
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.roller)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		roller.setImageMatrix(matrix);// 设置动画初始位置
	}

	private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(screenW / 2, 0, 0,
							0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, screenW / 2, 0, 0);
					//toXDelta为结束点与当前点的差值
				}
				break;

			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			roller.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};

	@Override
	public void onClick(View v) {
		if(v == left){
			viewpager.setCurrentItem(0);
		}else{
			viewpager.setCurrentItem(1);
		}
			
	}

}
