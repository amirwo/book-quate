package com.gama.quatenation;

import com.gama.quatenation.logic.Configuration;
import com.gama.quatenation.model.book.Book;
import com.gama.quatenation.model.book.BookInfoResponse;
import com.gama.quatenation.model.book.IndustryIdentifiers;
import com.gama.quatenation.model.book.VolumeInfo;
import com.gama.quatenation.model.quote.Quote;
import com.gama.quatenation.model.quote.QuoteRequest;
import com.gama.quatenation.model.quote.QuotesResponse;
import com.gama.quatenation.services.GetBookInfoService;
import com.gama.quatenation.services.GetQuotesService;
import com.gama.quatenation.services.RequestListener;
import com.gama.quatenation.services.SendQuoteService;
import com.gama.quatenation.utils.Constants;
import com.gama.quatenation.utils.ui.QustomDialogBuilder;

import android.app.Activity;
import android.content.DialogInterface;
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

	private SharedPreferences sharedPref;
	private SharedPreferences.Editor prefEditor;

	private int choosenVolume = 0;
	private CheckBox manualEditBox;
	private EditText quoteEditText;
	private EditText isbnEditText;
	private EditText pageEditText;
	private VolumeInfo volumeInfo;

	// book info
	private EditText authorEditText, titleEditText;
	private TableLayout isbnTable;
	private RequestListener bookDetailsRequestListener;

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

		bookDetailsRequestListener = new RequestListener() {

			@Override
			public void onRequestError(int errCode) {

			}

			@Override
			public <T> void onRequestComplete(T response) {
				if (response instanceof BookInfoResponse) {
					// update book details if needed
					BookInfoResponse bookInfoResponse = BookInfoResponse.class.cast(response);
					if (bookInfoResponse.getItems() != null) {
						updateBookInfoFields(bookInfoResponse);
					}
					submitQuote();
				} else {
					Log.e(TAG, "Couldn't handle book response - got " + response.getClass() + " class "
							+ "instead of BookInfoResponse class");
				}

			}

		};

	}

	public void updateBookInfoFields(final BookInfoResponse bookInfoResponse) {
		boolean foundMatch = false;
		CharSequence books[] = new CharSequence[bookInfoResponse.getItems().length];
		int j = 0;
		for (Book book : bookInfoResponse.getItems()) {
			String title = book.getVolumeInfo().getTitle();
			String[] authors = book.getVolumeInfo().getAuthors();
			String authorsStr = "";
			for (int i = 0; i < authors.length - 1; i++) {
				authorsStr += authors[i] + ",";
			}
			authorsStr += authors[authors.length - 1];

			if (title.equals(titleEditText.getText().toString())
					&& authorsStr.equals(authorEditText.getText().toString())) {
				// found matching info - no need for user intervention
				foundMatch = true;
				choosenVolume = j;
			}
			books[j++] = title + " ," + authorsStr;
		}

		// ask for user to match
		if (!foundMatch) {
			QustomDialogBuilder qustomDialogBuilder = new QustomDialogBuilder(this)
					.setTitle("Pick the currect book details").setTitleColor(Configuration.getInstance().getMainColor())
					.setDividerColor(Configuration.getInstance().getSecondaryColor())
					.setMessageBGColor(Configuration.getInstance().getMainColor());

			qustomDialogBuilder.setItems(books, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					choosenVolume = which;
					updateVolume(bookInfoResponse);
				}
			});
			qustomDialogBuilder.show();
			// AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// builder.setTitle("Pick the currect book details");
			// builder.setItems(books, new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// choosenVolume = which;
			// }
			// });
			// builder.show();
		}

	}

	private void updateVolume(BookInfoResponse bookInfoResponse) {
		volumeInfo = bookInfoResponse.getItems()[choosenVolume].getVolumeInfo();
		String authorsStr = "";
		String[] authors = volumeInfo.getAuthors();
		for (int i = 0; i < authors.length - 1; i++) {
			authorsStr += authors[i] + ",";
		}
		authorsStr += authors[authors.length - 1];
		IndustryIdentifiers[] industryIdentifiers = volumeInfo.getIndustryIdentifiers();
		if (industryIdentifiers != null) {
			for (IndustryIdentifiers identifier : industryIdentifiers) {
				identifier.setIdentifier("1111111111");
			}
		}
		volumeInfo.setIndustryIdentifiers(industryIdentifiers);
		((TextView) authorEditText).setText(authorsStr);
		((TextView) titleEditText).setText(volumeInfo.getTitle());
	}

	public void onUpdateButtonClicked(View v) {
		// Long isbnNum = Long.parseLong(isbnEditText.getText().toString());
		// new GetBookInfoService(this, -1, titleEditText.getText().toString(),
		// authorEditText.getText().toString().split(",")[0],
		// bookDetailsRequestListener).execute();
	}

	public void onSubmitButtonClicked(View v) {
		new GetBookInfoService(this, titleEditText.getText().toString(),
				authorEditText.getText().toString().split(",")[0], bookDetailsRequestListener).execute();
	}

	private void submitQuote() {
		// volume info has been set manually
		if (volumeInfo == null) {
			volumeInfo = new VolumeInfo();
		}
		// TODO: support more than 1 author in editable mode
		volumeInfo.setAuthors(authorEditText.getText().toString().split(","));
		volumeInfo.setTitle(titleEditText.getText().toString());
		// TODO fix page in server
		// pageEditText.getText().toString()
		volumeInfo.setPageCount("0");

		// Create new Quote and update
		Quote quote = new Quote();
		// TODO: move advertisingId to MetaData class for this app
		// should run not on main thread to get advId

		quote.setContent(quoteEditText.getText().toString());
		quote.setVolumeInfo(volumeInfo);
		quote.setUser(Configuration.getInstance().getUserAdvertisingId());
		sharedPref = getSharedPreferences("quote_shared_pref", MODE_PRIVATE);
		prefEditor = sharedPref.edit();
		prefEditor.putString("content", quote.getContent());
		prefEditor.putString("book_title", volumeInfo.getTitle());
		prefEditor.putString("book_author", volumeInfo.getAuthors()[0]);
		prefEditor.commit();

		new SendQuoteService(this, quote).execute();
	}
}
