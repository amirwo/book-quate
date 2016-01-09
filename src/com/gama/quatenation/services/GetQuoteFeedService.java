package com.gama.quatenation.services;

import com.gama.quatenation.model.quote.QuotesResponse;
import com.gama.quatenation.utils.Constants;
import com.gama.quatenation.utils.TransportHttp;

import android.content.Context;
import android.os.Process;
import android.util.Log;

public class GetQuoteFeedService extends BaseService {

	private int page = 0;
	private String baseUrl;
	private RequestListener listener;
	private QuotesResponse response;
	
	private static final String TAG = "GetQuoteFeedService";
	
	public GetQuoteFeedService(Context context, int page, boolean topRated, RequestListener listener) {
		super(context,Process.THREAD_PRIORITY_FOREGROUND);
		this.listener = listener;
		this.page = page;
		this.baseUrl = topRated ? Constants.SERVICE_GET_TOP_FEED_URL : Constants.SERVICE_GET_FEED_URL;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		listener.onRequestComplete(response);
	}

	@Override
	protected Object sendCommand() {
		try {
			Log.v(TAG, "sending quote feed request , page = " + page);
			response = TransportHttp.transport(context, baseUrl , "page=" + page, QuotesResponse.class);
		} catch (Exception e) {
			Log.e(TAG, "Unable to handle SendQuoteService command!!!!", e);
			errorMessage = e.getMessage();
			return null;
		}
		Log.v(TAG, "response with " + response.getQuotes().size() + " quotes");
		return response;
	}

	@Override
	protected boolean handleResponse(Object response) {
		if (response == null) {
			Log.e(TAG, "Unable to handle GetQuoteFeed command!!!!");
			return false;
		} 
		// TODO Auto-generated method stub
		return true;
	}

}
