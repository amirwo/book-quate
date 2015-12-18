
package com.gama.quatenation.logic;

import java.util.ArrayList;
import java.util.List;

import com.gama.quatenation.logic.view.tabs.TabPlacement;
import com.gama.quatenation.model.book.Quote;

import android.graphics.Color;

public class Configuration {
    private boolean initialized;
    private List<TabPlacement> placements;
    private String userAdvertisingId;
    private int secondaryColor = Color.rgb(204, 213, 165);
    private int mainColor	   = Color.rgb(242, 185, 101);
    private List<Quote> userQuoteList;
    private boolean initCroppingActivity = false;

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
        if (!initialized) {
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

	public boolean isInitCroppingActivity() {
		return initCroppingActivity;
	}

	public void setInitCroppingActivity(boolean initCroppingActivity) {
		this.initCroppingActivity = initCroppingActivity;
	}
    


}
