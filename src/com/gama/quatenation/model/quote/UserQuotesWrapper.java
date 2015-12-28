package com.gama.quatenation.model.quote;

import java.io.Serializable;
import java.util.List;

public class UserQuotesWrapper implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Quote> userQuotes;

	public UserQuotesWrapper() {
	}
	
	public List<Quote> getUserQuotes() {
		return userQuotes;
	}

	public void setUserQuotes(List<Quote> userQuotes) {
		this.userQuotes = userQuotes;
	}
	

}
