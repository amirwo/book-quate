package com.gama.quatenation.model;

public class Quote {
	
	private String[] tags;

	private String content;

	private String id;

	private VolumeInfo volumeInfo;

	private String userId;

	public Quote() {
		
	}
	
	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public VolumeInfo getVolumeInfo() {
		return volumeInfo;
	}

	public void setVolumeInfo(VolumeInfo volumeInfo) {
		this.volumeInfo = volumeInfo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "ClassPojo [tags = " + tags + ", content = " + content + ", id = " + id + ", volumeInfo = " + volumeInfo
				+ ", userId = " + userId + "]";
	}
}