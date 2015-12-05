package com.gama.quatenation.service;

import java.nio.charset.StandardCharsets;

import com.gama.quatenation.model.Quote;
import com.gama.quatenation.utils.Constants;
import com.gama.quatenation.utils.TransportHttp;
import com.google.gson.Gson;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


public class SendQuoteService extends BaseService{
	private static final String TAG = "SendQuoteService";
	
	private Quote quote;
	
	public SendQuoteService(Context context, Quote quote) {
		super(context);
		this.quote = quote;
	}

	@Override
	protected Object sendCommand() {		
		
		String response;
		try {
			response = TransportHttp.sendPost(context, Constants.SERVICE_QUOTE_SEND_URL, new Gson().toJson(quote).getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			Log.e(TAG, "Unable to handle SendQuoteService command!!!!", e);
			errorMessage = e.getMessage();
			return null;
		}

		return response;
	}

	@Override
	protected boolean handleResponse(Object response) {
		// here will be server object response
		if (response == null) {
			errorMessage = "Empty Response";
			Log.e(TAG, "Error Empty Response");
			return false;
		}

		return true;
	}
	
	
	@Override
	protected void onPostExecute(Boolean result) {
		// TODO: register Activity to intent boadcast and do whatever needed in activity
		Intent intent = new Intent(Constants.ON_RECEIVE_RESPONSE_LISTENER_BR_INTENT);
		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}	

}
