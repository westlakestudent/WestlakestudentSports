package com.west.widget;

import android.content.Context;
import android.widget.Toast;

public class WestInfoToast {

	public static void show(Context context,String msg){
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
}
