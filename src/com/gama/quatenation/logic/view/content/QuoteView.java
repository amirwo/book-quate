package com.gama.quatenation.logic.view.content;

import com.gama.quatenation.R;
import com.gama.quatenation.logic.Configuration;
import com.gama.quatenation.model.book.Quote;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuoteView extends LinearLayout {

	private static final int MAX_CHAR_PER_QUOTE_PREVIEW = 100;
	private ViewState state;
	private String fullQuote, previewQuote;

	public QuoteView(Context context, Quote quote) {
		super(context);
		state = ViewState.PREVIEW;
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
				switch(state) {
				case PREVIEW:
					// expand text
					((TextView) v).setText(fullQuote);
					// add "i" ?
					state = ViewState.EXPANDED;
					break;
				case EXPANDED:
					// close
					((TextView) v).setText(previewQuote);
					state = ViewState.PREVIEW;
					break;
				}
				
			}
		});

	    TextView quoteInfo = (TextView) getChildAt(1);
	    quoteInfo.setText(quote.getVolumeInfo().getTitle() + " ," + quote.getVolumeInfo().getAuthors()[0]);
	    quoteInfo.setTextColor(Configuration.getInstance().getMainColor());
	    
	}
	
	public enum ViewState {
		PREVIEW(1),
		EXPANDED(2);

		
		private int index;
		private ViewState(int index){
			this.index = index;
		}
		
		public int getIndex(){
			return index;
		}
		
		public static ViewState getByIndex(int index){
			ViewState result = ViewState.PREVIEW;
			ViewState types[] = values();
			for (int i = 0; i < types.length; i++){
				if (types[i].getIndex() == index){
					result = types[i];
				}
			}
			return result;
		}
	}
	
}
