package com.gama.quatenation.logic.view.content;

import com.gama.quatenation.QuoteViewActivity;
import com.gama.quatenation.R;
import com.gama.quatenation.logic.Configuration;
import com.gama.quatenation.model.quote.Quote;
import com.gama.quatenation.utils.Constants;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuoteView extends LinearLayout {

	private static final int MAX_CHAR_PER_QUOTE_PREVIEW = 100;
	private String fullQuote, previewQuote;

	public QuoteView(final Context context,final Quote quote) {
		super(context);
		fullQuote = quote.getContent();
		
		previewQuote = fullQuote.substring(0, 
				MAX_CHAR_PER_QUOTE_PREVIEW > fullQuote.length() ? fullQuote.length() : MAX_CHAR_PER_QUOTE_PREVIEW);
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER_HORIZONTAL);

		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.quote_view_placeholders, this, true);

	    TextView content = (TextView) getChildAt(0);
	    content.setBackgroundColor(Configuration.getInstance().getSecondaryColor());
	    content.setText(previewQuote);
	    content.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, QuoteViewActivity.class);
				intent.putExtra(Constants.KEY_QUOTE, quote);
				context.startActivity(intent);		
			}
		});

	    TextView quoteInfo = (TextView) getChildAt(1);
	    quoteInfo.setText(quote.getVolumeInfo().getTitle() + " ," + quote.getVolumeInfo().getAuthors()[0]);
	    quoteInfo.setTextColor(getResources().getColor(R.color.book_details_color));
	    
	}
	
	
}
