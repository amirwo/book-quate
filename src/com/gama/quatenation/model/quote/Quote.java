package com.gama.quatenation.model.quote;

import java.io.Serializable;

import com.gama.quatenation.model.book.VolumeInfo;

public class Quote implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String[] tags;

	private String content;

	private String id;
	
	private VolumeInfo volumeInfo;

	private String user;

	private boolean sentToServer = false;

	// added features
	
	private boolean likedByUser = false;
	
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

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "ClassPojo [tags = " + tags + ", content = " + content + ", id = " + id + ", volumeInfo = " + volumeInfo
				+ ", userId = " + user + "]";
	}

	public boolean isSentToServer() {
		return sentToServer;
	}

	public void setSentToServer(boolean sentToServer) {
		this.sentToServer = sentToServer;
	}

	public boolean isLikedByUser() {
		return likedByUser;
	}

	public void setLikedByUser(boolean likedByUser) {
		this.likedByUser = likedByUser;
	}
}