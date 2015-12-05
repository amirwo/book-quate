package com.gama.quatenation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ArchiveActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_archive_1);
	}

	public void publicClicked(View v) {
		Intent intent = new Intent(this, PublicActivity.class);
		startActivity(intent);

	}

	public void privateClicked(View v) {

		Intent intent = new Intent(this, PrivateActivity.class);
		startActivity(intent);
	}
}
