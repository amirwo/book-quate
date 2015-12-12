package com.gama.quatenation.logic.view.content;

import com.gama.quatenation.CroppingActivity;
import com.gama.quatenation.R;
import com.gama.quatenation.model.Placement;
import com.gama.quatenation.model.Template;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class TemplatesFragment extends Fragment {
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.main_fragement, container,
				false);

		if (getArguments() != null) {
			final Placement placement = (Placement)getArguments().get("Placement");

			GridView gridview = (GridView) rootView.findViewById(R.id.gridview);
			if (placement.getDisplayMode() == Placement.DISPLAY_MODE_GRID) {
				gridview.setNumColumns(GridView.AUTO_FIT);
			} else {
				gridview.setNumColumns(1);
			}
			gridview.setAdapter(new TemplateAdapter(rootView.getContext(), placement));

			gridview.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					Template template = (Template)parent.getItemAtPosition(position);
					
					Intent intent = new Intent(getActivity(), CroppingActivity.class);
					intent.putExtra("Template", template);
					if (template.getPlacement() != null) {
						intent.putExtra("Placement", template.getPlacement());
					} else {
						intent.putExtra("Placement", placement.getId());
					}
					getActivity().startActivity(intent);
					
				}
			});
		}

		return rootView;
	}
}
