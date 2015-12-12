package com.gama.quatenation.logic.view.content;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TemplatesPagerAdapter extends FragmentPagerAdapter {
	
	private int count; 
	
	public TemplatesPagerAdapter(FragmentManager fm, int count) {
		super(fm);
		this.count = count;
	}

	@Override
	public Fragment getItem(int position) {
		// Create here a fragement to each "acrivity" according to position
		Fragment newFragment = new TemplatesFragment();
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
