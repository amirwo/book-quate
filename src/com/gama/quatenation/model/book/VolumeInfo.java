package com.gama.quatenation.model.book;

import java.io.Serializable;

public class VolumeInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String pageCount;

	private String[] authors;

	private String title;

	private String language;

	private String publishedDate;

	private IndustryIdentifiers[] industryIdentifiers;

	public String getPageCount() {
		return pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	public String[] getAuthors() {
		return authors;
	}

	public void setAuthors(String[] authors) {
		this.authors = authors;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(String publishedDate) {
		this.publishedDate = publishedDate;
	}

	public IndustryIdentifiers[] getIndustryIdentifiers() {
		return industryIdentifiers;
	}

	public void setIndustryIdentifiers(IndustryIdentifiers[] industryIdentifiers) {
		this.industryIdentifiers = industryIdentifiers;
	}

	@Override
	public String toString() {
		return "ClassPojo [pageCount = " + pageCount + ", authors = " + authors + ", title = " + title
				+ ", language = " + language + ", publishedDate = " + publishedDate
				+ ", industryIdentifiers = " + industryIdentifiers + "]";
	}
}
