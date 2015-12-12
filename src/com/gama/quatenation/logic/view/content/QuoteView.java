package com.gama.quatenation.logic.view.content;

import com.gama.quatenation.R;
import com.gama.quatenation.model.Quote;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuoteView extends LinearLayout {

	public QuoteView(Context context, Quote quote) {
		super(context);

		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER_HORIZONTAL);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.quote_view_placeholders, this, true);

	    TextView content = (TextView) getChildAt(0);
	    content.setText(quote.getContent());

	    TextView quoteInfo = (TextView) getChildAt(1);
	    quoteInfo.setText(quote.getVolumeInfo().getTitle() + " ," + quote.getVolumeInfo().getAuthors()[0] );
	    
	}

}
