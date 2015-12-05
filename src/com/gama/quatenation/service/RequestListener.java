package com.gama.quatenation.service;

public interface RequestListener {

	public <T> void onRequestComplete(T response);
	public void onRequestError(int errCode);
}
