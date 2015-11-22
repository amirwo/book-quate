package com.gama.quatenation;

import java.nio.charset.StandardCharsets;

import com.gama.quatenation.model.BookInfo;
import com.gama.quatenation.model.BookInfoResponse;
import com.gama.quatenation.model.Quote;
import com.gama.quatenation.model.RequestListener;
import com.gama.quatenation.model.VolumeInfo;
import com.gama.quatenation.utils.Constants;
import com.gama.quatenation.utils.TransportHttp;
import com.google.gson.Gson;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

public class QuotePreviewActivity extends Activity {

	private CheckBox manualEditBox;
	private EditText quoteEditText; 
	private EditText isbnEditText;
	private EditText pageEditText;
	private VolumeInfo volumeInfo;
	
	// book info
	private EditText authorEditText, titleEditText;
	private TableLayout isbnTable;
	private RequestListener listener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quote_preview);
		Bundle extras = getIntent().getExtras();
		String quoteStr = extras.getString(Constants.KEY_QUOTE_CONTENT);
		isbnEditText = (EditText) findViewById(R.id.isbn_edit_text);
		quoteEditText = (EditText) findViewById(R.id.quote_content_text);
		authorEditText = (EditText) findViewById(R.id.author_auto_complete);
		titleEditText = (EditText) findViewById(R.id.title_auto_complete);
		pageEditText = (EditText) findViewById(R.id.page_edit_text);
		quoteEditText.setText(quoteStr);

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
		
		listener = new RequestListener() {
			
			@Override
			public void onRequestError() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onRequestComplete() {
				BookInfoResponse bookInfoResponse = BookInfo.getInstance().getBookInfoResponse();
				updateBookInfoFields(bookInfoResponse);
				
			}
		};
		
	}
	
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
		BookInfo.getInstance().setIsbn(isbnNum);
		BookInfo.getInstance().loadFromServer(this, listener);
		
	}

	public void onSubmitbuttonClicked(View v) {

		// volume info has been set manually 
		if (volumeInfo == null) {
			volumeInfo = new VolumeInfo();
		}
		//TODO: support more than 1 author in editable mode
		volumeInfo.setAuthors(authorEditText.getText().toString().split(","));
		volumeInfo.setTitle(titleEditText.getText().toString());
		
		// Create new Quote and update
		Quote quote = new Quote();
		quote.setContent(quoteEditText.getText().toString());
		quote.setVolumeInfo(volumeInfo);
		
		try {
			TransportHttp.sendPost(this, "TODO - THIS IS SERVER POST ADDRESS", new Gson().toJson(quote).getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
