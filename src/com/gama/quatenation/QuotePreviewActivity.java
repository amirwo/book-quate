package com.gama.quatenation;

import com.gama.quatenation.model.BookInfoResponse;
import com.gama.quatenation.model.Quote;
import com.gama.quatenation.model.VolumeInfo;
import com.gama.quatenation.service.GetBookInfoService;
import com.gama.quatenation.service.RequestListener;
import com.gama.quatenation.service.SendQuoteService;
import com.gama.quatenation.utils.AdvertisingIdFactory;
import com.gama.quatenation.utils.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

public class QuotePreviewActivity extends Activity {

	private final static String TAG = "QuotePreviewActivity";
	private String userId = "unknown";
	
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor prefEditor;
	
	
	private CheckBox manualEditBox;
	private EditText quoteEditText; 
	private EditText isbnEditText;
	private EditText pageEditText;
	private VolumeInfo volumeInfo;
	
	// book info
	private EditText authorEditText, titleEditText;
	private TableLayout isbnTable;
	private RequestListener isbnRequestListener;
//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//    		quoteEditText.setText(intent.getExtras().getString(Constants.KEY_QUOTE_CONTENT));
//        }
//    };
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quote_preview);
		Bundle extras = getIntent().getExtras();
		String quoteContent = extras.getString(Constants.KEY_QUOTE_CONTENT, "Empty quote");
		isbnEditText = (EditText) findViewById(R.id.isbn_edit_text);
		quoteEditText = (EditText) findViewById(R.id.quote_content_text);
		authorEditText = (EditText) findViewById(R.id.author_auto_complete);
		titleEditText = (EditText) findViewById(R.id.title_auto_complete);
		pageEditText = (EditText) findViewById(R.id.page_edit_text);
		pageEditText.setText("");
		quoteEditText.setText(quoteContent);

		isbnTable = (TableLayout) findViewById(R.id.tableLayout1);
		manualEditBox = (CheckBox) findViewById(R.id.checkbox_edit_manualy);
		manualEditBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean checked) {
				isbnTable.setVisibility(checked ? View.INVISIBLE : View.VISIBLE);
				titleEditText.setEnabled(checked);
				authorEditText.setEnabled(checked);
			}
		});
		
		isbnRequestListener = new RequestListener() {
			
			@Override
			public void onRequestError(int errCode) {
				
			}

			@Override
			public <T> void onRequestComplete(T response) {
				// TODO: not best practice... should refactor later on.
				if (response instanceof BookInfoResponse) {
					updateBookInfoFields(BookInfoResponse.class.cast(response));
				} else {
					Log.e(TAG, "Couldn't handle isbn book response - got " + response.getClass() + " class "
							+ "instead of BookInfoResponse class");
				}
				
			}
		};
		
	}
	
//	@Override
//	protected void onResume() {
//		super.onResume();
//	    IntentFilter filter = new IntentFilter(Constants.ON_COMPLETE_TESS_API_BR_INTENT);
//	    android.support.v4.content.LocalBroadcastManager.getInstance(this).registerReceiver(
//	    		receiver,
//				filter);
//	};

//    @Override
//    protected void onPause() {
//    	android.support.v4.content.LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
//        super.onPause();
//    }
    
	public void updateBookInfoFields(BookInfoResponse bookInfoResponse) {
		volumeInfo = bookInfoResponse.getItems()[0].getVolumeInfo();
		String authorsStr = "";
		String[] authors = volumeInfo.getAuthors();
		for (int i = 0; i < authors.length-1; i++) {
			authorsStr += authors[i] + ",";
		}
		authorsStr += authors[authors.length-1];
		
		((TextView) authorEditText).setText(authorsStr);
		((TextView) titleEditText).setText(volumeInfo.getTitle());
	}
	
	public void onUpdateButtonClicked(View v) {
		Long isbnNum = Long.parseLong(isbnEditText.getText().toString());
		new GetBookInfoService(this, isbnNum, isbnRequestListener).execute();
	}

	@SuppressLint("NewApi")
	public void onSubmitButtonClicked(View v) {

		// volume info has been set manually 
		if (volumeInfo == null) {
			volumeInfo = new VolumeInfo();
		}
		//TODO: support more than 1 author in editable mode
		volumeInfo.setAuthors(authorEditText.getText().toString().split(","));
		volumeInfo.setTitle(titleEditText.getText().toString());
		volumeInfo.setPageCount(pageEditText.getText().toString());
		
		// Create new Quote and update
		Quote quote = new Quote();
		// TODO: move advertisingId to MetaData class for this app 
		// should run not on main thread to get advId
		Runnable getAdvIdRunnable = new Runnable() {

			@Override
			public void run() {
				userId = AdvertisingIdFactory.getAdvertisingId(getApplicationContext()).getId();
			}
			
		};
		
		new Thread(getAdvIdRunnable).start();
		
		quote.setContent(quoteEditText.getText().toString());
		quote.setVolumeInfo(volumeInfo);
		quote.setUser(userId);
		sharedPref = getSharedPreferences("quote_shared_pref",
				MODE_PRIVATE);
		prefEditor = sharedPref.edit();
		prefEditor.putString("content", quote.getContent());
		prefEditor.putString("book_title", volumeInfo.getTitle());
		prefEditor.putString("book_author", volumeInfo.getAuthors()[0]);
		prefEditor.commit();
		
		new SendQuoteService(this, quote).execute();
	}
	
	

}
