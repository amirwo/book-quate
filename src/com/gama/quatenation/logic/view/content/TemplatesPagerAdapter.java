package com.gama.quatenation.logic.view.content;

import java.util.List;

import com.gama.quatenation.model.Placement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TemplatesPagerAdapter extends FragmentPagerAdapter {
	
	List<Placement> placements;

	public TemplatesPagerAdapter(FragmentManager fm, List<Placement> placements) {
		super(fm);
		this.placements = placements;
	}

	@Override
	public Fragment getItem(int position) {
		// Create here a fragement to each "acrivity" according to position
		Fragment newFragment = new TemplatesFragment();
		Bundle args = new Bundle();
		args.putSerializable("Placement", placements.get(position));
		newFragment.setArguments(args);
		
		return newFragment;
	}

	@Override
	public int getCount() {
		return placements.size();
	}

}
