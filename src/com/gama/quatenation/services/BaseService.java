package com.gama.quatenation.services;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;


public abstract class BaseService{

	final protected Context context;
	
	protected String errorMessage = null;
	
	public BaseService(Context context) {
		this.context = context;
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

	protected Boolean doInBackground() {
		Object response = null;

		response = sendCommand();
		return handleResponse(response);
	}
	
	abstract protected void onPostExecute(Boolean result);
	
	abstract protected Object sendCommand();

	abstract protected boolean handleResponse(Object response);

}
