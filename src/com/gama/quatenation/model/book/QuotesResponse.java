package com.gama.quatenation.model.book;

import java.util.List;

public class QuotesResponse {

	private List<Quote> quoteList;
	
	private QuotesResponse() {
		
	}

	public List<Quote> getQuotes() {
		return quoteList;
	}

	public void setQuotes(List<Quote> quotes) {
		this.quoteList = quotes;
	}
}
