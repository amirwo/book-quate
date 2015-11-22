package com.gama.quatenation;

import java.io.File;
import java.io.IOException;

import com.gama.quatenation.utils.Constants;
import com.googlecode.tesseract.android.TessBaseAPI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

	private final String TAG = "MainActivity";
	protected static final String PHOTO_TAKEN = "photo_taken";

	private static final int IMAGE_REQUEST_CODE = 1;
	// your authority, must be the same as in your manifest file
	private static final String CAPTURE_IMAGE_FILE_PROVIDER = "com.gama.quatenation.fileprovider";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	}

	public void btnCaptureQuateClicked(View v) {

		// Create file path
		File path = new File(this.getFilesDir(), "quatenation/quateImages");
		if (!path.exists()) {
			path.mkdirs();
		}
		File image = new File(path, "image.jpg");
		Uri imageUri = FileProvider.getUriForFile(this, CAPTURE_IMAGE_FILE_PROVIDER, image);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Set the captured camera picture to be stored in imageUri path
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, IMAGE_REQUEST_CODE);

	}

	protected Bitmap processPhotoFromPath(String strTakenPicPath) {

		// This area needs work and optimization
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 4;
		Bitmap bitmap = BitmapFactory.decodeFile(strTakenPicPath, options);
		//TODO: clean the text background - try to use script from: http://www.fmwconcepts.com/imagemagick/textcleaner/index.php 
		//TODO: fix rotation process - add exitinterface
		try {
			ExifInterface exif = new ExifInterface(strTakenPicPath);
			int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
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
				// Setting pre rotate
				Matrix mtx = new Matrix();
				mtx.preRotate(rotate);
				// Rotating Bitmap
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
			}
			// Convert to ARGB_8888, required by tess
			bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		} catch (IOException e) {
			Log.e(TAG, "Couldn't correct orientation: " + e.toString());
		}
		
		return bitmap;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {

		case IMAGE_REQUEST_CODE:
			if (requestCode == IMAGE_REQUEST_CODE) {
				if (resultCode == Activity.RESULT_OK) {
					// Verify path exists
					File path = new File(getFilesDir(), "quatenation/quateImages");
					if (!path.exists())
						path.mkdirs();
					File imageFile = new File(path, "image.jpg");
					Bitmap optimizedBitmap = processPhotoFromPath(imageFile.getAbsolutePath());
					String quoteContent = getUTF8TextFromProcessedBitmap(optimizedBitmap);
					Intent quotePreview = new Intent(this, QuotePreviewActivity.class);
					quotePreview.putExtra(Constants.KEY_QUOTE_CONTENT, quoteContent);
					startActivity(quotePreview);
					
					
//					TextView textView = (TextView) findViewById(R.id.textView1);
//					textView.setText(getUTF8TextFromProcessedBitmap(optimizedBitmap));
				}
			}
		}
	}

	public String getUTF8TextFromProcessedBitmap(Bitmap bmp) {
		
		Log.v(TAG, "Before baseApi");
		final TessBaseAPI baseApi = new TessBaseAPI();
		Log.v(TAG, "Initialize baseApi");
		baseApi.setDebug(true);
		// getLang() returns equ in case of equations detection
		baseApi.init(Constants.TESSBASE_PATH, Constants.DEFAULT_LANGUAGE);
		Log.v(TAG, "init baseApi done");
		baseApi.setImage(bmp);
		String recognizedText = baseApi.getUTF8Text();
		baseApi.end();
		bmp.recycle();
		Log.v(TAG, "Detected TEXT: " + recognizedText);
		return recognizedText;
	}
	
}
