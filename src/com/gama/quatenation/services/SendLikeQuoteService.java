package com.gama.quatenation.services;

import com.gama.quatenation.logic.Configuration;
import com.gama.quatenation.model.quote.Quote;
import com.gama.quatenation.model.quote.UserQuotesWrapper;
import com.gama.quatenation.utils.Constants;
import com.gama.quatenation.utils.TransportHttp;
import com.gama.quatenation.utils.Util;

import android.content.Context;
import android.util.Log;

public class SendLikeQuoteService extends BaseService {

	private final static String TAG = "SendLikeQuoteService";
	private String userId;
	private Quote quote;
	private boolean isForLike = true;
	
	public SendLikeQuoteService(Context context, Quote quote, String userId, boolean isForLike) {
		super(context);
		this.userId = userId;
		this.quote = quote;
		this.isForLike = isForLike;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		UserQuotesWrapper quoteWarpper = new UserQuotesWrapper();
		quote.setLikedByUser(isForLike);
		if (isForLike) {
			Configuration.getInstance().addToUserLikedList(quote);
		} else {
			Configuration.getInstance().removeFromUserLikedList(quote);
		}
		quoteWarpper.setUserQuotes(Configuration.getInstance().getUserLikedList().values());
		Util.saveQuotesToSharedPrefs(quoteWarpper, "LIKED_QUOTESLIST", context.getApplicationContext().getSharedPreferences("QUOTE_SHARED_PREFS", Context.MODE_PRIVATE));
		
	}

	@Override
	protected String sendCommand() {
		String response;
		try {
			Log.v(TAG, "sending like command");
			response = TransportHttp.transport(context, Constants.SERVICE_SEND_LIKE_URL , "&like=" + isForLike + "&userID=" + userId + "&quoteID=" + quote.getId());
		} catch (Exception e) {
			Log.e(TAG, "Unable to handle SendQuoteService command!!!!", e);
			errorMessage = e.getMessage();
			return null;
		}
		Log.v(TAG, "response: " + response);
		return response;
	}

	@Override
	protected boolean handleResponse(Object response) {
		String responseStr = null;
		if (response instanceof String) {
			responseStr = (String) response;
		}
		if (!responseStr.startsWith("Error")) {
			Log.v(TAG, "server accepted like send, like id:" + response.toString());
		} else {
			Log.v(TAG, "server failed to accepted like, " + response.toString());
			return false;
		}
		return true;
	}
	

}
