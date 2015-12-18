package com.gama.quatenation.services;

public interface RequestListener {

	public <T> void onRequestComplete(T response);
	public void onRequestError(int errCode);
}
