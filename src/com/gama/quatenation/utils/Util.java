package com.gama.quatenation.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import com.gama.quatenation.logic.Configuration;
import com.gama.quatenation.model.quote.Quote;
import com.gama.quatenation.model.quote.UserQuotesWrapper;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class Util {

	private static final String TAG = "Util";
	
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
	
	public static void saveQuotesToSharedPrefs(UserQuotesWrapper object, String key, SharedPreferences prefs) {
	    SharedPreferences.Editor editor = prefs.edit();

	    editor.putString(key, new Gson().toJson(object));
	    editor.commit();
	}
	
	public static List<Quote> getSavedQuotes(String key, SharedPreferences prefs) {
    	UserQuotesWrapper wrapper = new Gson().fromJson(prefs.getString(key, null),UserQuotesWrapper.class );
    	if (wrapper!=null) {
    		return wrapper.getUserQuotes();
    	} else {
    		return null;
    	}
	}
	
	public static RelativeLayout createTabView(Context context, OnClickListener listener, String tabText, String imageUrl, int viewId, boolean isSmallTab) {
		
		RelativeLayout view = new RelativeLayout(context);
		ViewGroup.LayoutParams vgParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(vgParams);
		view.setId(viewId);
		
		// Add Image
		final ImageView image = new ImageView(context);
		int dpHeight = 60;
		int dpWidth = 42;
		if (isSmallTab) {
			dpHeight = 71;
			dpWidth = 52;
		}
		RelativeLayout.LayoutParams rlImage = new RelativeLayout.LayoutParams(Util.dpToPx(context, dpHeight), Util.dpToPx(context, dpWidth));
		rlImage.addRule(RelativeLayout.CENTER_HORIZONTAL);
		image.setScaleType(ScaleType.FIT_CENTER);
		image.setImageBitmap(Util.getImageBitmap(context, imageUrl));
		int dp2 = Util.dpToPx(context, 2);
		int dp12 = Util.dpToPx(context, 12);
		// rlImage.setMargins(dp25 + 6, dp10, dp25 + 6, dp10 / 2);
		rlImage.setMargins(0, dp2, 0, dp12);
		image.setLayoutParams(rlImage);
		image.setId(viewId+100);
		view.addView(image);
		
		// Add Title
		final TextView text = new TextView(context);
		text.setText(tabText);
		RelativeLayout.LayoutParams rlText = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		rlText.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rlText.addRule(RelativeLayout.BELOW, viewId + 100);
		int textSize = 11;
		text.setTextSize(textSize);
		text.setTypeface(Typeface.createFromAsset(context.getAssets(), "ufonts.com_gotham-bold.ttf"));
		text.setLayoutParams(rlText);
		rlText.setMargins(0, 0, 0, 0);
		view.addView(text);
		if (listener != null) {
			view.setOnClickListener(listener);
		}
		
		return view;
	}
	
	public static RelativeLayout createSpinnerTabView(Context context,OnItemSelectedListener listener, String imageUrl, int viewId) {
		
		RelativeLayout view = new RelativeLayout(context);
		ViewGroup.LayoutParams vgParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(vgParams);
		view.setId(viewId);
		
		// Add Image
		final ImageView image = new ImageView(context);
		int dpHeight = 60;
		int dpWidth = 42;

		RelativeLayout.LayoutParams rlImage = new RelativeLayout.LayoutParams(Util.dpToPx(context, dpHeight), Util.dpToPx(context, dpWidth));
		rlImage.addRule(RelativeLayout.CENTER_HORIZONTAL);
		image.setScaleType(ScaleType.FIT_CENTER);
		image.setImageBitmap(Util.getImageBitmap(context, imageUrl));
		int dp2 = Util.dpToPx(context, 2);
		rlImage.setMargins(0, dp2, 0, 0);
		image.setLayoutParams(rlImage);
		image.setId(viewId+100);
		view.addView(image);
		
	    Spinner spinner = new Spinner(context);
	    ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, Arrays.asList(Configuration.getInstance().getOcrLanguages()));
	    spinner.setAdapter(spinnerArrayAdapter);
	    spinner.setOnItemSelectedListener(listener);
		RelativeLayout.LayoutParams rlSpinner = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		rlSpinner.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rlSpinner.addRule(RelativeLayout.BELOW, viewId + 100);
		spinner.setLayoutParams(rlSpinner);
		rlSpinner.setMargins(0, 0, 0, 0);
		view.addView(spinner);
		
		return view;
	}
	
	public static Bitmap getImageBitmap(Context context, String imageUrl) {
		Bitmap image;
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        try {
            istr = assetManager.open(imageUrl);
            image = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
        	Log.e("Utils", "couldnt decode bitmap for " + imageUrl);
            return null;
        }

        return image;
	}
	
	public static void createFileFromAssets(Context context, String fullPath) {
		if (!(new File(fullPath)).exists()) {
			try {

				AssetManager assetManager = context.getAssets();
				InputStream in = assetManager.open(fullPath);
				//GZIPInputStream gin = new GZIPInputStream(in);
				OutputStream out = new FileOutputStream(Configuration.getInstance().getDataPath() + fullPath);

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				//while ((lenf = gin.read(buff)) > 0) {
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				//gin.close();
				out.close();
				
				Log.v(TAG, "Copied file " + fullPath);
			} catch (IOException e) {
				Log.e(TAG, "Was unable to copy " + fullPath + " error:" + e.toString());
			}
		}
	}
	
	
}
