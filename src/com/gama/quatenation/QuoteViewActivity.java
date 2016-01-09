package com.gama.quatenation;

import com.gama.quatenation.logic.Configuration;
import com.gama.quatenation.model.quote.Quote;
import com.gama.quatenation.services.SendLikeQuoteService;
import com.gama.quatenation.utils.Constants;
import com.gama.quatenation.utils.Util;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class QuoteViewActivity extends Activity {

	private TextView header;
	private TextView content;
	private RelativeLayout quoteLayout;
	private RelativeLayout likeTab;
	private Quote quote;
	private static final String TAG = "QuoteViewActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		quote = (Quote) extras.getSerializable(Constants.KEY_QUOTE);
		setContentView(R.layout.activity_quote_view);
		RelativeLayout tabsLayout = (RelativeLayout) findViewById(R.id.tabsLayout);
		quoteLayout = (RelativeLayout) findViewById(R.id.quoteLayout);
		header = (TextView) findViewById(R.id.QuoteHeader);
		content = (TextView) findViewById(R.id.QuoteContent);
		header.setText(quote.getVolumeInfo().getTitle() + ", " + quote.getVolumeInfo().getAuthors()[0]);
		content.setText(quote.getContent());
		OnClickListener infoListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openExtendedInfo();
			}
		};
		OnClickListener likeListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleLike(quote.isLikedByUser());
				quote.setLikedByUser(!quote.isLikedByUser());
			}
		};
		
		OnClickListener searchListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		};
		
		RelativeLayout.LayoutParams infoParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		infoParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		
		RelativeLayout.LayoutParams searchParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		searchParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		
		RelativeLayout.LayoutParams likeParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		likeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		
		String likeImageUrl =  "tabs_favorites_inactive.png";
		if (quote.isLikedByUser()) {
			likeImageUrl = "tabs_favorites_active.png";
		}
		RelativeLayout infoTab = Util.createTabView(this, infoListener, " info ", "info_photo.png", 500, false);
		likeTab = Util.createTabView(this, likeListener, " like ", likeImageUrl, 501, false);
		RelativeLayout searchTab = Util.createTabView(this, infoListener, " search ", "tabs_search_inactive.png", 502, false);
		tabsLayout.addView(infoTab, infoParams);
		tabsLayout.addView(searchTab, searchParams);
		tabsLayout.addView(likeTab, likeParams);
		
		
	}
	
	private void toggleLike(boolean likedByUser) {
		String imageUrl = "tabs_favorites_active.png";
		if (likedByUser) {
			imageUrl =  "tabs_favorites_inactive.png";
		}
		ImageView like = (ImageView) likeTab.getChildAt(0);
		like.setImageBitmap(Util.getImageBitmap(this, imageUrl));
		
		// add like service and shoot it
		new SendLikeQuoteService(this, quote, Configuration.getInstance().getUserAdvertisingId(), !likedByUser).execute();
	}
	
	private void openExtendedInfo() {
		
	}
	
}
