package com.gama.quatenation;

import com.gama.quatenation.logic.Configuration;
import com.gama.quatenation.logic.view.content.ActivitiesPagerAdapter;
import com.gama.quatenation.logic.view.tabs.TabsController;
import com.gama.quatenation.logic.view.tabs.TabsFragment;
import com.gama.quatenation.utils.Util;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;

public class MainFragment extends Fragment{
	
	private final static String TAG = "AMIRMainFragment";
	/**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager pager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;
    
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState){
		final View view = inflater.inflate(R.layout.fragment_main, null);
		
		// replaces current framelayout inside the horizontal scroll
		getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.tabs, new TabsFragment()).commit();
		
		// Instantiate a ViewPager and a PagerAdapter.
        pager = (ViewPager) view.findViewById(R.id.pager);
        pagerAdapter = new ActivitiesPagerAdapter(getChildFragmentManager(), Configuration.getInstance().getPlacements().size());
        pager.setAdapter(pagerAdapter);
        pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				Log.v(TAG, "onPageSelected " + arg0);
				TabsController.getInstance().setCurrentTab(arg0, false);				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {		
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
			
		});
        TabsController.getInstance().setViewPager((ViewPager) view.findViewById(R.id.pager));
        TabsController.getInstance().setScrollView((HorizontalScrollView)view.findViewById(R.id.scrlTabs));
        
        view.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Util.removeOnGlobalLayoutListener(view.getViewTreeObserver(), this);
				if (savedInstanceState == null) {
		        	TabsController.getInstance().setCurrentTab(pager.getCurrentItem(), false);
		        } else {
		        	TabsController.getInstance().setCurrentTab(false);
		        }
			}
		});

		return view;
	}
	
	public void setCurrentTab(int tab){
		Log.v(TAG, "setCurrentTab");
		pager.setCurrentItem(tab);
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (pagerAdapter instanceof ActivitiesPagerAdapter) {
			getChildFragmentManager().getFragments().get(0).onActivityResult(requestCode, resultCode, data);
		}
	}

}
