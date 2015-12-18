package com.gama.quatenation.logic.view.tabs;

import java.util.ArrayList;

import com.gama.quatenation.logic.view.tabs.TabViewController.TabListener;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

public class TabsController {
	
	private static String TAG = "AMIRTabsController";
	
	// ===== SINGLETON =========
	// Private constructor prevents instantiation from other classes
	private TabsController() {
	}

	/**
	 * SingletonHolder is loaded on the first execution of
	 * Singleton.getInstance() or the first access to SingletonHolder.INSTANCE,
	 * not before.
	 */
	private static class SingletonHolder {
		private static final TabsController INSTANCE = new TabsController();
	}

	public static TabsController getInstance() {
		return SingletonHolder.INSTANCE;
	}

	// ===== END OF SINGLETON =========
	
	private ArrayList<TabViewController> tabs = new ArrayList<TabViewController>();
	private int currentTab = 0;
	private ViewPager viewPager;
	private HorizontalScrollView scrollView;
	
	public ViewGroup addTab(Context context, TabPlacement placement){
		// Check exists
		ViewGroup vg;
		for (TabViewController tab : tabs){
			if (tab.getPlacement().equals(placement)){
				vg = tab.getView();
				try{
					ViewGroup parent  = (ViewGroup)(tab.getView().getParent());
					parent.removeView(vg);
				} catch(Exception e){}
				return vg;
			}
		}
		
		// Create new one
		TabViewController result = TabViewController.getController(context, placement, new TabListener() {
			
			@Override
			public void onTabSelected(TabPlacement placement) {
			}
		});
		tabs.add(result);
		
		return result.getView();
	}
	
	public void inActiveAllTabs(){
		for (TabViewController tab : tabs){
			tab.deactive();
		}
	}
	
	public int getSize(){
		return tabs.size();
	}
	
	public void setCurrentTab(boolean animate){
		setCurrentTab(currentTab, animate);
	}
	
	public synchronized void setCurrentTab(int tab, boolean animate){
		Log.v(TAG, "scrolling to tab: " + tab);
		Log.v(TAG, "currentTab = " + currentTab + " total size = " + getSize());
		HorizontalScrollView scrollView = getScrollView();
		if (scrollView != null){
			int scrollTo = (tab * (scrollView.getChildAt(0).getWidth() - scrollView.getWidth())) / (getSize() - 1);
			if (animate) {
				scrollView.smoothScrollTo(scrollTo, 0);
			} else {
				scrollView.scrollTo(scrollTo, 0);
			}
		}
		
		if (currentTab < getSize()){
			tabs.get(currentTab).deactive();
			
			if (tab < getSize()){
				tabs.get(tab).active();
				currentTab = tab;
			}
		}
	}

	public void setNextTab(){
		int next = (getCurrentTab() + 1) % getSize();
		setCurrentTab(next, true);
	}
	
	public void setPrevTab(){
		int next = (getCurrentTab() - 1) % getSize();
		setCurrentTab(next, true);
	}

	public int getCurrentTab() {
		return currentTab;
	}

	private ViewPager getViewPager() {
		return viewPager;
	}

	public void setViewPager(ViewPager viewPager) {
		this.viewPager = viewPager;
	}
	
	public void setViewPagerPage(TabViewController tab){
		if (getViewPager() != null){
			for (int i = 0; i < tabs.size(); i++){
				if (tabs.get(i) == tab){
					getViewPager().setCurrentItem(i);
				}
			}
		}
	}

	private HorizontalScrollView getScrollView() {
		return scrollView;
	}

	public void setScrollView(HorizontalScrollView scroll) {
		this.scrollView = scroll;
	}

}
