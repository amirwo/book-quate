
package com.gama.quatenation.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.gama.quatenation.logic.view.tabs.TabPlacement;
import com.gama.quatenation.model.quote.Quote;
import com.gama.quatenation.utils.Util;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

public class Configuration {
	
	private static final String TAG = "Configuration";
	private String packageName = "com.gama.quatenation";
	private String dataPath;
	
    private boolean initialized;
    private List<TabPlacement> placements;
    private String userAdvertisingId;
    private int secondaryColor = Color.rgb(204, 213, 165);
    private int mainColor	   = Color.rgb(242, 185, 101);
    private List<Quote> userQuoteList;
    private HashMap<String, Quote> usersIdToLikedQuote;
    private String[] ocrLanguages = new String[]{"eng", "heb"};
    private String selectedLanguage = "eng";
    private File pathToPicture;

    // ===== SINGLETON =========
    // Private constructor prevents instantiation from other classes
    private Configuration() {
    }

    /**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance() or the first
     * access to SingletonHolder.INSTANCE, not before.
     */
    private static class SingletonHolder {
        private static final Configuration INSTANCE = new Configuration();
    }

    public static Configuration getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static void init() {
    	Configuration.getInstance().initialize();
    }

    private void initialize() {
        if (!initialized) {
            this.initialized = true;
        }
    }
    
    public synchronized List<TabPlacement> getPlacements() {
        if (!isInitialized()) {
            // TODO: log
            return null;
        }

        if (placements == null) {
        	placements = new ArrayList<TabPlacement>();
        	placements.add(new TabPlacement("1", "Add", "tabs_camera_inactive.png", "tabs_camera_active.png", 1));
        	placements.add(new TabPlacement("2", "Quote Feed", "tabs_book_inactive2.png", "tabs_book_active2.png", 1));
        	placements.add(new TabPlacement("3", "Search", "tabs_search_inactive.png", "tabs_search_active.png", 1));
        	placements.add(new TabPlacement("4", "Favorites", "tabs_favorites_inactive.png", "tabs_favorites_active.png", 1));
        }

        return placements;
    }

	public void initTesseractTrainData(Context context, String lang, boolean hasCubeData) {
		String[] paths = new String[] { dataPath, dataPath + "tessdata/" };

		for (String path : paths) {
			File dir = new File(path);
			if (!dir.exists()) {
				if (!dir.mkdirs()) {
					Log.e(TAG, "ERROR: Creation of directory " + path + " on internal storage failed");
					return;
				} else {
					Log.v(TAG, "Created directory " + path + " on internal storage");
				}
			}

		}
		
		Util.createFileFromAssets(context, "tessdata/" + lang + ".traineddata");
		
		if (hasCubeData) {
			String[] cubeFileNames = new String[] { "cube.bigrams", "cube.fold" , "cube.lm", "cube.nn", "cube.params",
					"cube.size", "cube.word-freq", "tesseract_cube.nn"};
			
			for (String path: cubeFileNames) {
				Util.createFileFromAssets(context, "tessdata/" + lang + "." + path);
			}
		}
		
	}

	public void setPictureFile(Context context) {
		// Create file path
		File path = new File(context.getFilesDir(), "photos/");
		if (!path.exists()) {
			path.mkdirs();
		}
		pathToPicture = new File(path, "image.jpg");
	}
	
	public String getUserAdvertisingId() {
		return userAdvertisingId;
	}

	public void setUserAdvertisingId(String userAdvertisingId) {
		this.userAdvertisingId = userAdvertisingId;
	}

	public int getSecondaryColor() {
		return secondaryColor;
	}

	public void setSecondaryColor(int secondaryColor) {
		this.secondaryColor = secondaryColor;
	}

	public int getMainColor() {
		return mainColor;
	}

	public void setMainColor(int mainColor) {
		this.mainColor = mainColor;
	}

	public List<Quote> getUserQuoteList() {
		return userQuoteList;
	}

	public void setUserQuoteList(List<Quote> userQuoteList) {
		this.userQuoteList = userQuoteList;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public String getSelectedLanguage() {
		return selectedLanguage;
	}

	public void setSelectedLanguage(String selectedLanguage) {
		this.selectedLanguage = selectedLanguage;
	}

	public String[] getOcrLanguages() {
		return ocrLanguages;
	}

	public void setOcrLanguages(String[] ocrLanguages) {
		this.ocrLanguages = ocrLanguages;
	}

	public File getPathToPicture() {
		return pathToPicture;
	}

	public void addToUserLikedList(Quote q) {
		getUserLikedList().put(q.getId(), q);
	}
	
	public void removeFromUserLikedList(Quote q) {
		getUserLikedList().remove(q.getId());
	}

	public HashMap<String, Quote> getUserLikedList() {
		return usersIdToLikedQuote;
	}

	public void setUserLikedList(Collection<Quote> collection) {
		HashMap<String, Quote> map = new HashMap<String, Quote>();
		for (Quote q: collection) {
			map.put(q.getId(), q);
		}
		this.usersIdToLikedQuote = map;
	}



}
