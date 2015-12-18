package com.gama.quatenation.model.book;

import java.util.ArrayList;
import java.util.List;

public class QuoteRequest {
	// quote id list
	private List<String> ID = new ArrayList<String>();
	private String userID = "";
	private String Title = "";
	private String Author = "";
	private String ISBN10 = "";
	private String ISBN13 = "";
	private String OrderBy = "added";
	private String Direction = "DESC";
	
	public QuoteRequest() {
		
	}
	
	public List<String> getID() {
		return ID;
	}
	public void setID(List<String> id) {
		ID = id;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getAuthor() {
		return Author;
	}
	public void setAuthor(String author) {
		Author = author;
	}
	public String getISBN10() {
		return ISBN10;
	}
	public void setISBN10(String iSBN10) {
		ISBN10 = iSBN10;
	}
	public String getISBN13() {
		return ISBN13;
	}
	public void setISBN13(String iSBN13) {
		ISBN13 = iSBN13;
	}
	public String getOrderBy() {
		return OrderBy;
	}
	public void setOrderBy(String orderBy) {
		OrderBy = orderBy;
	}
	public String getDirection() {
		return Direction;
	}
	public void setDirection(String direction) {
		Direction = direction;
	}
}
