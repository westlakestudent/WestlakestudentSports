package com.west.widget;

import java.io.File;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 
 * TimerTextView
 * 
 * @author chendong 2015年2月2日 上午10:54:56
 * @version 1.0.0
 * 
 */
public class TimerTextView extends TextView {

	private static final String FONTS_FOLDER = "fonts";
	private static final String FONT_DIGITAL_7 = FONTS_FOLDER + File.separator
			+ "digital-7.ttf";

	private void init(Context context) {
		AssetManager assets = context.getAssets();
		final Typeface font = Typeface.createFromAsset(assets, FONT_DIGITAL_7);
		setTypeface(font);
	}

	public TimerTextView(Context context) {
		super(context);
		init(context);
	}

	public TimerTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

}
