
package com.gama.quatenation.logic;

import java.util.ArrayList;
import java.util.List;

import com.gama.quatenation.model.Placement;

public class PlacementsConfig {
    private boolean initialized;
    private List<Placement> placements;

    // ===== SINGLETON =========
    // Private constructor prevents instantiation from other classes
    private PlacementsConfig() {
    }

    /**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance() or the first
     * access to SingletonHolder.INSTANCE, not before.
     */
    private static class SingletonHolder {
        private static final PlacementsConfig INSTANCE = new PlacementsConfig();
    }

    public static PlacementsConfig getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public static void init() {
        PlacementsConfig.getInstance().initialize();
    }

    private void initialize() {
        if (!initialized) {
            this.initialized = true;
        }
    }

    public synchronized List<Placement> getPlacements() {
        if (!initialized) {
            // TODO: log
            return null;
        }

        if (placements == null) {
        	placements = new ArrayList<Placement>();
        	placements.add(new Placement("1", "Add", "tabs_camera_inactive.png", "tabs_camera_active.png", 1));
        	placements.add(new Placement("2", "Quote Feed", "tabs_book_inactive2.png", "tabs_book_active2.png", 1));
        	placements.add(new Placement("3", "Search", "tabs_search_inactive.png", "tabs_search_active.png", 1));
        	placements.add(new Placement("4", "Favorites", "tabs_favorites_inactive.png", "tabs_favorites_active.png", 1));
        }

        return placements;
    }
}
