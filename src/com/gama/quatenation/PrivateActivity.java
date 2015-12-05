package com.gama.quatenation;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class PrivateActivity extends Activity {

	private SharedPreferences sharedPref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_private_1);
		TextView content = (TextView) findViewById(R.id.QuoteContent);
		TextView bookInfo = (TextView) findViewById(R.id.quote_book_info);
		sharedPref = getSharedPreferences("quote_shared_pref",
				MODE_PRIVATE);
		;
		String contentStr = sharedPref.getString("content","no content");
		contentStr = contentStr.substring(0, 30);
		content.setText("\"" + contentStr + "..." + "\"");
		bookInfo.setText(" - " + sharedPref.getString("book_title","no content") 
						+ " , " + sharedPref.getString("book_author","no content"));
	}

}
