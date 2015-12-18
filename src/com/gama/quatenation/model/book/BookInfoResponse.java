package com.gama.quatenation.model.book;

public class BookInfoResponse {

	private Book[] items;

	private int totalItems;

	private String kind;

	public Book[] getItems() {
		return items;
	}

	public void setItems(Book[] items) {
		this.items = items;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(int totalItems) {
		this.totalItems = totalItems;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	@Override
	public String toString() {
		return "ClassPojo [items = " + items + ", totalItems = " + totalItems + ", kind = " + kind + "]";
	}

}
