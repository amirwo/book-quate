package com.gama.quatenation.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Placement implements Serializable {
	
	public static final int DISPLAY_MODE_GRID = 1;
	public static final int DISPLAY_MODE_LIST = 2;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1313014689524447309L;
	
	private String id;
	private String name;
	private String imageUrl;
	private String imageSelectedUrl;
	private int displayMode;
	
	private transient Bitmap selected = null;
	private transient Bitmap image = null;
	
	public Placement(String id, String name, String imageUrl,
			String imageSelectedUrl, int displayMode) {
		super();
		this.id = id;
		this.name = name;
		this.imageUrl = imageUrl;
		this.imageSelectedUrl = imageSelectedUrl;
		this.displayMode = displayMode;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getImageSelectedUrl() {
		return imageSelectedUrl;
	}
	
	public void setImageSelectedUrl(String imageSelectedUrl) {
		this.imageSelectedUrl = imageSelectedUrl;
	}

	public int getDisplayMode() {
		return displayMode;
	}

	public void setDisplayMode(int displayMode) {
		this.displayMode = displayMode;
	}
	
	public Bitmap getImageBitmap(Context context) {
        if (image != null){
        	return image;
        }
        
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        try {
            istr = assetManager.open(this.imageUrl);
            image = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            return null;
        }

        return image;
	}
	
	public Bitmap getImageSelectedBitmap(Context context) {
        if (selected != null){
        	return selected;
        }
        
        AssetManager assetManager = context.getAssets();

        InputStream istr;
        try {
            istr = assetManager.open(this.imageSelectedUrl);
            selected = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            return null;
        }

        return selected;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + displayMode;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((imageSelectedUrl == null) ? 0 : imageSelectedUrl.hashCode());
		result = prime * result
				+ ((imageUrl == null) ? 0 : imageUrl.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Placement other = (Placement) obj;
		if (displayMode != other.displayMode)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imageSelectedUrl == null) {
			if (other.imageSelectedUrl != null)
				return false;
		} else if (!imageSelectedUrl.equals(other.imageSelectedUrl))
			return false;
		if (imageUrl == null) {
			if (other.imageUrl != null)
				return false;
		} else if (!imageUrl.equals(other.imageUrl))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}

