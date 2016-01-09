package com.gama.quatenation.services;

import com.gama.quatenation.logic.Configuration;
import com.gama.quatenation.model.quote.QuotesResponse;
import com.gama.quatenation.utils.Constants;
import com.gama.quatenation.utils.TransportHttp;

import android.content.Context;
import android.os.Process;
import android.util.Log;

public class GetFavoritesService extends BaseService {
	
	private String userId;
	private RequestListener listener;
	private QuotesResponse response;
	private String baseUrl;
	
	private static final String TAG = "GetQuoteFeedService";
	
	public GetFavoritesService(Context context,boolean returnById, RequestListener listener) {
		this(context, Configuration.getInstance().getUserAdvertisingId(), returnById, listener);
	}
		
	public GetFavoritesService(Context context, String userId, boolean returnById, RequestListener listener) {
		super(context,Process.THREAD_PRIORITY_FOREGROUND);
		this.listener = listener;
		this.userId = userId;
		this.baseUrl = returnById ? Constants.SERVICE_GET_FAVORITE_IDS : Constants.SERVICE_GET_FAVORITE_QUOTES;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		listener.onRequestComplete(response);
	}

	@Override
	protected Object sendCommand() {
		try {
			Log.v(TAG, "sending favorites request , user = " + userId);
			response = TransportHttp.transport(context, baseUrl , "userID=" + userId, QuotesResponse.class);
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
