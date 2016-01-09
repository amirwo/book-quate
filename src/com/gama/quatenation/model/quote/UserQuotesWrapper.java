package com.gama.quatenation.model.quote;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class UserQuotesWrapper implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Collection<Quote> userQuotes;

	public UserQuotesWrapper() {
	}
	
	public Collection<Quote> getUserQuotes() {
		return userQuotes;
	}

	public void setUserQuotes(List<Quote> userQuotes) {
		this.userQuotes = userQuotes;
	}
	
	public void setUserQuotes(Collection<Quote> userQuotes) {
		this.userQuotes = userQuotes;
	}
	

}
