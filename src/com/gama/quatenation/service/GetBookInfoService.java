package com.gama.quatenation.service;

import com.gama.quatenation.model.BookInfoResponse;
import com.gama.quatenation.utils.Constants;
import com.gama.quatenation.utils.TransportHttp;

import android.content.Context;
import android.util.Log;

public class GetBookInfoService extends BaseService {

	private static final String TAG = "GetBookInfoService";
	
	private long isbnNumber;
	private String inTitle,inAuthor;
	private RequestListener listener;
	private BookInfoResponse response;

	public GetBookInfoService(Context context, long isbnNumber, String inTitle, String inAuthor, RequestListener listener) {
			super(context);
			this.isbnNumber = isbnNumber;
			this.inTitle = inTitle;
			this.inAuthor = inAuthor;
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
		String queryStr = buildQueryStr();
		Log.i(TAG, "queryStr!  " + queryStr);
		try {
			response = TransportHttp.transport(context, Constants.GOOGLE_BOOK_API_GET, queryStr, BookInfoResponse.class);
		} catch (Exception e) {
			Log.e(TAG, "Unable to handle send command!!!!", e);
			errorMessage = e.getMessage();
			return null;
		}

		return response;
	}

	private String buildQueryStr() {
		String ret = "q=ISBN:";
		if (isbnNumber > 0) {
			ret += isbnNumber;
		} else {
			ret += inAuthor.isEmpty() ? "" : "+inauthor:" + inAuthor;
			ret += inTitle.isEmpty() ? "" : "+intitle:" + inTitle;
		}
		return ret.replaceAll(" ", "%20");
	}

	@Override
	protected boolean handleResponse(Object response) {
		// Actions on response / enrichement options...
		return true;
	}
	
	

}
