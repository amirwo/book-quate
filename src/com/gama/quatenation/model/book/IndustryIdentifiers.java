package com.gama.quatenation.model.book;

import java.io.Serializable;

// Represent isbn info class
public class IndustryIdentifiers implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String type;

	private String identifier;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public String toString() {
		return "IndustryIdentifiers [type = " + type + ", identifier = " + identifier + "]";
	}
}
