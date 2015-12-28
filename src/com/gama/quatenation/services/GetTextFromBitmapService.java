package com.gama.quatenation.services;

import com.gama.quatenation.logic.Configuration;
import com.googlecode.tesseract.android.TessBaseAPI;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Process;

// High priority service
public class GetTextFromBitmapService extends BaseService {
	
	private final TessBaseAPI baseApi = new TessBaseAPI();
	private RequestListener listener;
	private String processedText;
	private Bitmap bmp;

	public GetTextFromBitmapService(Context context, Bitmap bmp, RequestListener listener) {
		super(context, Process.THREAD_PRIORITY_URGENT_AUDIO);
		this.listener = listener;
		this.bmp = bmp;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		bmp.recycle();
		listener.onRequestComplete(processedText);
		
	}
	
	public void stopService() {
		baseApi.stop();
	}

	@Override
	protected Object sendCommand() {
		// baseApi.setDebug(true);
		// getLang() returns equ in case of equations detection
		baseApi.init(Configuration.getInstance().getDataPath(), Configuration.getInstance().getSelectedLanguage());
		baseApi.setImage(bmp);
		processedText = baseApi.getUTF8Text();
		baseApi.end();
		// bmp.recycle();
		return processedText;
	}

	@Override
	protected boolean handleResponse(Object response) {
		// another processes on processedText ?
		return true;
	}
	

}
