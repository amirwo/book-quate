package com.gama.quatenation.service;

import com.gama.quatenation.model.BookInfoResponse;
import com.gama.quatenation.utils.Constants;
import com.gama.quatenation.utils.TransportHttp;

import android.content.Context;
import android.util.Log;

public class GetBookInfoService extends BaseService {

	private static final String TAG = "GetBookInfoService";
	
	private long isbnNumber;
	private RequestListener listener;
	private BookInfoResponse response;

	public GetBookInfoService(Context context, long isbnNumber, RequestListener listener) {
			super(context);
			this.isbnNumber = isbnNumber;
			this.listener = listener;
		}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			listener.onRequestComplete(response);
		} else {
			// TODO: add error codes 
			listener.onRequestError(Constants.ERROR_CODE_UNKNOWN);
		}

	}

	@Override
	protected Object sendCommand() {
		try {
			response = TransportHttp.transport(context, Constants.GOOGLE_BOOK_API_GET, "q=ISBN:" + isbnNumber, BookInfoResponse.class);
		} catch (Exception e) {
			Log.e(TAG, "Unable to handle send command!!!!", e);
			errorMessage = e.getMessage();
			return null;
		}

		return response;
	}

	@Override
	protected boolean handleResponse(Object response) {
		// Actions on response / enrichement options...
		return true;
	}
	
	

}
