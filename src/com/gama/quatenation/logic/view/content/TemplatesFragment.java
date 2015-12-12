package com.gama.quatenation.logic.view.content;

import com.gama.quatenation.R;
import com.gama.quatenation.model.Quote;
import com.gama.quatenation.model.VolumeInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

public class TemplatesFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.main_fragement, container,
				false);

		if (getArguments() != null) {
			Integer chosenActivity = (Integer) getArguments().get("Activity");

			if (chosenActivity == 1) {
				Quote q1 = new Quote();
				q1.setContent("this is the first qquote");
				String[] auth1 = new String[]{"author1"};
				VolumeInfo volumeInfo = new VolumeInfo();
				volumeInfo.setAuthors(auth1);
				volumeInfo.setTitle("book title1");
				q1.setVolumeInfo(volumeInfo);
				LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.llView);
//				ListView listView = (ListView) rootView.findViewById(R.id.listView);
				ll.addView(new QuoteView(this.getContext(), q1));
				ll.addView(new QuoteView(this.getContext(), q1));
				ll.addView(new QuoteView(this.getContext(), q1));
				ll.addView(new QuoteView(this.getContext(), q1));
				ll.addView(new QuoteView(this.getContext(), q1));
				ll.addView(new QuoteView(this.getContext(), q1));
				ll.addView(new QuoteView(this.getContext(), q1));
				ll.addView(new QuoteView(this.getContext(), q1));
				ll.addView(new QuoteView(this.getContext(), q1));
				ll.addView(new QuoteView(this.getContext(), q1));
				ll.addView(new QuoteView(this.getContext(), q1));
				ll.addView(new QuoteView(this.getContext(), q1));
				ll.addView(new QuoteView(this.getContext(), q1));
				ll.addView(new QuoteView(this.getContext(), q1));
				ll.addView(new QuoteView(this.getContext(), q1));
				ll.addView(new QuoteView(this.getContext(), q1));
				
			} else if (chosenActivity == 0) {
				
				
				
			}
		}

		return rootView;
	}
}
