package com.gama.quatenation.services;

import java.nio.charset.Charset;

import com.gama.quatenation.logic.Configuration;
import com.gama.quatenation.model.quote.Quote;
import com.gama.quatenation.model.quote.UserQuotesWrapper;
import com.gama.quatenation.utils.Constants;
import com.gama.quatenation.utils.TransportHttp;
import com.gama.quatenation.utils.Util;
import com.google.gson.Gson;

import android.content.Context;
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
			String json = new Gson().toJson(quote);
			Log.v(TAG, "SendQuoteService jsonObject: " + json);
			response = TransportHttp.sendPost(context, Constants.SERVICE_QUOTE_SEND_URL, json.getBytes(Charset.forName("UTF-8")));
		} catch (Exception e) {
			Log.e(TAG, "Unable to handle SendQuoteService command!!!!", e);
			errorMessage = e.getMessage();
			return null;
		}
		Log.v(TAG, "SendQuoteService response: " + response);
		return response;
	}

	@Override
	protected boolean handleResponse(Object response) {
		// here will be server object response
		if (response == null) {
			errorMessage = "Empty Response";
			Log.e(TAG, "Error Empty Response");
			return false;
		} else {
			// add quote to user quote list and save to shared prefs (device)
			quote.setId((String) response);
			quote.setSentToServer(true);
			UserQuotesWrapper quoteWarpper = new UserQuotesWrapper();
			Configuration.getInstance().getUserQuoteList().add(quote);
			quoteWarpper.setUserQuotes(Configuration.getInstance().getUserQuoteList());
			Util.saveQuotesToSharedPrefs(quoteWarpper, "QUOTESLIST", context.getApplicationContext().getSharedPreferences("QUOTE_SHARED_PREFS", Context.MODE_PRIVATE));
		}

		return true;
	}
	
	
	@Override
	protected void onPostExecute(Boolean result) {
//		// TODO: register Activity to intent boadcast and do whatever needed in activity
//		Intent intent = new Intent(Constants.ON_RECEIVE_RESPONSE_LISTENER_BR_INTENT);
//		LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
	}	

}
