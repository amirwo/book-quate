package com.gama.quatenation.services;

import java.nio.charset.Charset;

import com.gama.quatenation.model.quote.QuoteRequest;
import com.gama.quatenation.model.quote.QuotesResponse;
import com.gama.quatenation.utils.Constants;
import com.gama.quatenation.utils.TransportHttp;
import com.google.gson.Gson;

import android.content.Context;
import android.util.Log;

public class GetQuotesService extends BaseService {

	private static final String TAG = "SendQuoteService";

	private QuoteRequest quoteRequest;
	private RequestListener listener;
	private QuotesResponse quotesResponse;

	public GetQuotesService(Context context, QuoteRequest quoteRequest, RequestListener listener) {
		super(context);
		this.quoteRequest = quoteRequest;
		this.listener = listener;
	}

	@Override
	protected Object sendCommand() {

		String response;
		try {
			response = TransportHttp.sendPost(context, Constants.SERVICE_QUOTES_POST_URL,
					new Gson().toJson(quoteRequest).getBytes(Charset.defaultCharset()));
			
		} catch (Exception e) {
			Log.e(TAG, "Unable to handle SendQuoteService command!!!!", e);
			errorMessage = e.getMessage();
			return null;
		}

		return response;
	}

	@Override
	protected boolean handleResponse(Object response) {
		if (response == null) {
			errorMessage = "Empty Response";
			Log.e(TAG, "Error Empty Response");
			return false;
		} else {
			quotesResponse = new Gson().fromJson(response.toString(), QuotesResponse.class);
		}

		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			listener.onRequestComplete(quotesResponse);
		}
	}

}
