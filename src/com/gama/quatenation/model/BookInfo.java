package com.gama.quatenation.model;

import com.gama.quatenation.GetBookInfoAsyncTask;

import android.content.Context;

public class BookInfo {

	private static transient Object lock = new Object();
	private static transient BookInfo instance = new BookInfo();
	private static transient GetBookInfoAsyncTask task;
	
	private long isbn = 0;
	
	public BookInfo() {
	}

	public static Object getLock() {
		return lock;
	}
	
	
	public static void update(Context context, BookInfo bookInfo) {
		synchronized (lock) {

			BookInfo.setInstance(bookInfo);
		}
	}

	public static void failedLoading() {
		// TODO Auto-generated method stub
		
	}
	
	public void loadFromServer(Context context,RequestListener listener) {

		synchronized (lock) {
			task = new GetBookInfoAsyncTask(context, isbn, listener);
			task.execute();
		}

			// TODO: handle error for get task
		return;
	}

	public long getIsbn() {
		return isbn;
	}

	public void setIsbn(long isbn) {
		this.isbn = isbn;
	}

	public static BookInfo getInstance() {
		return instance;
	}

	public static void setInstance(BookInfo instance) {
		BookInfo.instance = instance;
	}

	public static BookInfoResponse getBookInfoResponse() {
		return task.getBookInfoResponse();
	}

	
}
