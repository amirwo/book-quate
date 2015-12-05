package com.gama.quatenation.service;

import com.gama.quatenation.utils.Constants;
import com.googlecode.tesseract.android.TessBaseAPI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class BitmapToStringService extends BaseService {
	
	private final String TAG = "BitmapToStringService";
	private TessBaseAPI baseApi;
	private Bitmap bmp;

	public BitmapToStringService(Context context,final TessBaseAPI baseApi, Bitmap bmp ) {
		super(context);
		this.baseApi = baseApi;
		this.bmp = bmp;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			baseApi.end();
			bmp.recycle();
		}
		
	}

	@Override
	protected Object sendCommand() {
		Log.v(TAG, "Before baseApi");
		Log.v(TAG, "Initialize baseApi");
//		baseApi.setDebug(true);
		// getLang() returns equ in case of equations detection
		baseApi.init(Constants.TESSBASE_PATH, Constants.DEFAULT_LANGUAGE);
		Log.v(TAG, "init baseApi done");
		baseApi.setImage(bmp);
//		Log.v(TAG, "Detected TEXT: " + recognizedText);
		return baseApi.getUTF8Text();
	}

	@Override
	protected boolean handleResponse(Object response) {
		Intent intent = new Intent(Constants.ON_COMPLETE_TESS_API_BR_INTENT);
		String str = (String) response;
		intent.putExtra(Constants.KEY_QUOTE_CONTENT, str);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
		return true;
	}

}
