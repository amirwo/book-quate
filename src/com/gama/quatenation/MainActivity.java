package com.gama.quatenation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.gama.quatenation.logic.Configuration;
import com.gama.quatenation.model.book.Quote;
import com.gama.quatenation.utils.AdvertisingIdFactory;
import com.gama.quatenation.utils.Constants;
import com.gama.quatenation.utils.Util;
import com.googlecode.tesseract.android.TessBaseAPI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class MainActivity extends FragmentActivity {

	private final String TAG = "MainActivity";
	protected static final String PHOTO_TAKEN = "photo_taken";

	
	private static final int IMAGE_REQUEST_CODE = 1;
	private static final int IMAGE_CROPPING_CODE = 2;
	private String recognizedText = "";
	private ProgressDialog progress;
	private Bitmap bmp;
	// your authority, must be the same as in your manifest file
	private static final String CAPTURE_IMAGE_FILE_PROVIDER = "com.gama.quatenation.fileprovider";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Configuration.init();
		setUserAdvertisingId();
		List<Quote> savedQuotes = Util.getSavedQuotes("QUOTESLIST", getApplicationContext().getSharedPreferences("QUOTE_SHARED_PREFS", Context.MODE_PRIVATE));
		if (savedQuotes == null) {
			savedQuotes = new ArrayList<Quote>();
		}
		Configuration.getInstance().setUserQuoteList(savedQuotes);
		Log.v(TAG, "advId: " + Configuration.getInstance().getUserAdvertisingId());
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null){
			getSupportFragmentManager().beginTransaction().add(R.id.mainContent, new MainFragment()).commit();
		}		
		setContentView(R.layout.activity_main);
		progress = new ProgressDialog(this);
		
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
	
	public void btnCaptureQuateClicked(View v) {
		Intent intent = new Intent(this,CroppingActivity.class);
		startActivity(intent);
		// Create file path
//		File path = new File(this.getFilesDir(), "quatenation/quateImages");
//		if (!path.exists()) {
//			path.mkdirs();
//		}
//		File image = new File(path, "image.jpg");
//		Uri imageUri = FileProvider.getUriForFile(this, CAPTURE_IMAGE_FILE_PROVIDER, image);
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		// Set the captured camera picture to be stored in imageUri path
//		intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//		startActivityForResult(intent, IMAGE_REQUEST_CODE);
		

	}

	protected Bitmap processPhotoFromPath(String strTakenPicPath) {

		// This area needs work and optimization
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap bitmap = BitmapFactory.decodeFile(strTakenPicPath, options);
		//TODO: clean the text background - try to use script from: http://www.fmwconcepts.com/imagemagick/textcleaner/index.php 
		try {
			ExifInterface exif = new ExifInterface(strTakenPicPath);
			int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_ROTATE_90);
			Log.v(TAG, "Orient: " + exifOrientation);
			int rotate = 0;
			switch (exifOrientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate = 270;
				break;
			}
			Log.v(TAG, "Rotation: " + rotate);
			if (rotate != 0) {
				// Getting width & height of the given image.
				int w = bitmap.getWidth();
				int h = bitmap.getHeight();
				Log.v(TAG, "getWidth: " + w);
				Log.v(TAG, "getHeight: " + h);
				// Setting pre rotate
				Matrix mtx = new Matrix();
				mtx.preRotate(rotate);
				// Rotating Bitmap
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
			}
			// Convert to ARGB_8888, required by tess
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
	        Log.v(TAG, "Before baseApi");
			
		} catch (IOException e) {
			Log.e(TAG, "Couldn't correct orientation: " + e.toString());
		}
		
		return bitmap;
	}

	
	public String getUTF8TextFromProcessedBitmap(Bitmap bmp) {
		Log.v(TAG, "Before baseApi");
		final TessBaseAPI baseApi = new TessBaseAPI();
		Log.v(TAG, "Initialize baseApi");
//				baseApi.setDebug(true);
		// getLang() returns equ in case of equations detection
		baseApi.init(Constants.TESSBASE_PATH, Constants.DEFAULT_LANGUAGE);
		Log.v(TAG, "init baseApi done");
		baseApi.setImage(bmp);
		recognizedText = baseApi.getUTF8Text();
		baseApi.end();
		bmp.recycle();
//				Log.v(TAG, "Detected TEXT: " + recognizedText);
		return recognizedText;
	}
	
}
