package com.gama.quatenation.logic.view.tabs;

import java.util.List;

import com.gama.quatenation.logic.Configuration;
import com.gama.quatenation.utils.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class TabsFragment extends Fragment {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {    	
    	final RelativeLayout result = new RelativeLayout(getActivity());
    	ViewGroup.LayoutParams prms = new ViewGroup.LayoutParams(
    			ViewGroup.LayoutParams.MATCH_PARENT,
    			ViewGroup.LayoutParams.WRAP_CONTENT);
    	result.setLayoutParams(prms);
    	result.setBackgroundColor(Configuration.getInstance().getSecondaryColor());
		initTabs(result, Configuration.getInstance().getPlacements());	
        return result;
    }
    
    @SuppressLint("NewApi")
	private void initTabs(final RelativeLayout result, List<TabPlacement> placements) {
		ViewGroup view;
    	RelativeLayout.LayoutParams viewParams;
    	int id = 100;
    	for (TabPlacement placement : placements){
        	view = TabsController.getInstance().addTab(result.getContext(), placement);
        	viewParams = new RelativeLayout.LayoutParams(
        			RelativeLayout.LayoutParams.WRAP_CONTENT,
        			RelativeLayout.LayoutParams.WRAP_CONTENT);
        	int dp18 = Util.dpToPx(result.getContext(), 18);
        	int dp10 = Util.dpToPx(result.getContext(), 10);
        	Point size = new Point();
        	((WindowManager)result.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(size);
        	if (size.x > (placements.size() * Util.dpToPx(result.getContext(), 78) + dp10 * 2)) {
        		dp10 = (size.x -  placements.size() * Util.dpToPx(result.getContext(), 78)) / (placements.size() * 2);
        	}
        	view.setPadding(dp10, dp18, dp10, dp18);
        	view.setId(id);
        	if (id > 100){
        		viewParams.addRule(RelativeLayout.RIGHT_OF, id - 1);
        	}
        	id++;
        	view.setLayoutParams(viewParams);
        	result.addView(view);		    
    	}
    	int dp10 = Util.dpToPx(result.getContext(), 10);
    	result.setPadding(dp10, 0, dp10, 0);
		TabsController.getInstance().setCurrentTab(true);
	}
    
}