package com.gama.quatenation;

import java.util.ArrayList;
import java.util.List;

import com.gama.quatenation.logic.Configuration;
import com.gama.quatenation.model.quote.Quote;
import com.gama.quatenation.model.quote.QuoteRequest;
import com.gama.quatenation.model.quote.QuotesResponse;
import com.gama.quatenation.services.GetQuotesService;
import com.gama.quatenation.services.RequestListener;
import com.gama.quatenation.utils.AdvertisingIdFactory;
import com.gama.quatenation.utils.Util;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public class MainActivity extends FragmentActivity {

	private final String TAG = "MainActivity";
	protected static final String PHOTO_TAKEN = "photo_taken";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// initialize configuration only once
		if (!Configuration.getInstance().isInitialized()) {
			Configuration.init();
			Configuration.getInstance().setDataPath(getFilesDir().toString() + "/QuateNation/");
			Configuration.getInstance().initTesseractTrainData(getApplicationContext(), "eng", true);
			Configuration.getInstance().initTesseractTrainData(getApplicationContext(), "heb", false);
			Configuration.getInstance().setPictureFile(this);
			// get user advertising id not on main thread
			setUserAdvertisingId();
			
			// restore quotes from shared prefs 
			List<Quote> savedQuotes = Util.getSavedQuotes("QUOTESLIST", getApplicationContext().getSharedPreferences("QUOTE_SHARED_PREFS", Context.MODE_PRIVATE));
			if (savedQuotes == null) {
				savedQuotes = new ArrayList<Quote>();
			}
			Configuration.getInstance().setUserQuoteList(savedQuotes);
		}

		if (savedInstanceState == null){
			getSupportFragmentManager().beginTransaction().add(R.id.mainContent, new MainFragment()).commit();
		}		
		setContentView(R.layout.activity_main);
		
	}

	private void setUserAdvertisingId() {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				Configuration.getInstance().setUserAdvertisingId(AdvertisingIdFactory.getAdvertisingId(getApplicationContext()).getId());
				
			}
		};
		new Thread(r).start();
	}
	
}
