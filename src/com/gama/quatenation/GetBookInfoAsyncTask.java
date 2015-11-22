package com.gama.quatenation;

import com.gama.quatenation.model.BookInfo;
import com.gama.quatenation.model.BookInfoResponse;
import com.gama.quatenation.model.RequestListener;
import com.gama.quatenation.utils.Constants;
import com.gama.quatenation.utils.TransportHttp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class GetBookInfoAsyncTask {

	private final Context context;
	private BookInfoResponse bookInfoResponse = null;
	private long isbnNumber;
	private boolean taskStopped = false;
	private RequestListener listener;
		
		public GetBookInfoAsyncTask(Context context, long isbnNumber, RequestListener listener) {
			this.context = context;
			this.isbnNumber = isbnNumber;
			this.listener = listener;
		}
		
		public void execute() {
			new Thread(new Runnable() {
				@Override
				public void run() {
					android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
					final Boolean result = doInBackground();
					new Handler(Looper.getMainLooper()).post(new Runnable() {
						@Override
						public void run() {
							onPostExecute(result);
						}
					});
				}
			}).start();
		}
		
		public void stop() {
			taskStopped = true;
		}
		
		protected Boolean doInBackground() {
			
			try {
				setBookInfoResponse(TransportHttp.transport(context, Constants.GOOGLE_BOOK_API_GET, "q=ISBN:" + isbnNumber, BookInfoResponse.class));
			} catch (Exception e) {
				return Boolean.FALSE;
			}
			
			return Boolean.TRUE;
		}		

		protected void onPostExecute(Boolean loaded) {
			synchronized (BookInfo.getLock()) {
				if (!taskStopped) {
					if (loaded && getBookInfoResponse() != null && context != null) {
						listener.onRequestComplete();
					} else {
						listener.onRequestError();
					}
				}
			}
		}

		public BookInfoResponse getBookInfoResponse() {
			return bookInfoResponse;
		}

		public void setBookInfoResponse(BookInfoResponse bookInfoResponse) {
			this.bookInfoResponse = bookInfoResponse;
		}
}

