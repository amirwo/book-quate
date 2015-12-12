package com.gama.quatenation.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Template implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 844428905396299258L;
	
	private String id;
	private String placement = null;
	private Integer minApi;
	private String name;
	private String imageUrl;
	private String packageInclude = null; // for package include
	private boolean landscape = false;
	private String testVerificatiomPattern = null;
	private String testTemplateName = null;
	private boolean preCache = false;
	
	private transient Bitmap image;
	
	public Template(String id, String name, String imageUrl) {
		super();
		this.id = id;
		this.name = name;
		this.imageUrl = imageUrl;
		image = null;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setPlacement(String placement) {
		this.placement = placement;
	}
	
	public String getPlacement() {
		return placement;
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
	
	public Bitmap getImageBitmap(Context context) {
		if (image == null) {
	        AssetManager assetManager = context.getAssets();
	        InputStream istr;
	        try {
	            istr = assetManager.open(this.imageUrl);
	            image = BitmapFactory.decodeStream(istr);
	        } catch (IOException e) {
	        }
		}

        return image;
	}


	public String getPackageInclude() {
		return packageInclude;
	}
	
	public void setPackageInclude(String packageName) {
		this.packageInclude = packageName;
	}
	
	public boolean isLandscape() {
		return landscape;
	}

	public void setLandscape(boolean landscape) {
		this.landscape = landscape;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((imageUrl == null) ? 0 : imageUrl.hashCode());
		result = prime * result + (landscape ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((packageInclude == null) ? 0 : packageInclude.hashCode());
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
		Template other = (Template) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (imageUrl == null) {
			if (other.imageUrl != null)
				return false;
		} else if (!imageUrl.equals(other.imageUrl))
			return false;
		if (landscape != other.landscape)
			return false;
		if (minApi != other.minApi)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (packageInclude == null) {
			if (other.packageInclude != null)
				return false;
		} else if (!packageInclude.equals(other.packageInclude))
			return false;
		return true;
	}

	public String getTestVerificationPattern() {
	    return testVerificatiomPattern;
	}

	public void setTestVerificationPattern(String pattern) {
	    this.testVerificatiomPattern = pattern;
	}

	public String getTestTemplateName() {
	    return testTemplateName;
	}

	public void setTestTemplateName(String testTemplateName) {
	    this.testTemplateName = testTemplateName;
	}

	public boolean isPreCache() {
		return preCache;
	}

	public void setPreCache(boolean preCache) {
		this.preCache = preCache;
	}

	public Integer getMinApi() {
		return minApi;
	}

	public void setMinApi(Integer minApi) {
		this.minApi = minApi;
	}

}
