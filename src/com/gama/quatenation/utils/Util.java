package com.gama.quatenation.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class Util {

	private static ThreadPoolExecutor trackingExecutor = new ThreadPoolExecutor(1, 4, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
	
	@SuppressLint("NewApi")
	public static void removeOnGlobalLayoutListener(ViewTreeObserver observer, OnGlobalLayoutListener listener) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
			observer.removeOnGlobalLayoutListener(listener);
		} else {
			observer.removeGlobalOnLayoutListener(listener);
		}
	}
	
	public static int pxToDp(Context context, int px) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) ((px - 0.5f) / scale);
	}

	public static int dpToPx(Context context, int dp) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
	
	public static float spToPx(Context context, Float sp) {
	    float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
	    return sp * scaledDensity;
	}
	
}
