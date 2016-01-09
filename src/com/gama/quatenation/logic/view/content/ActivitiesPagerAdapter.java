package com.gama.quatenation.logic.view.content;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class ActivitiesPagerAdapter extends FragmentPagerAdapter {
	
	private int count; 
	private static String TAG = "ActivitiesPagerAdapter";
	
	public ActivitiesPagerAdapter(FragmentManager fm, int count) {
		super(fm);
		this.count = count;
	}

	@Override
	public Fragment getItem(int position) {
		// Create here a fragement to each "acrivity" according to position
		Log.v(TAG, "getItem in position " + position);
		Fragment newFragment = new ActivityFragment();
		Bundle args = new Bundle();
		args.putSerializable("Activity", position);
		newFragment.setArguments(args);
		
		return newFragment;
	}

	@Override
	public int getCount() {
		return count;
	}
	

}
