package com.gama.quatenation;

import java.io.File;
import java.io.IOException;

import com.gama.quatenation.utils.Constants;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.isseiaoki.simplecropview.CropImageView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

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
		setContentView(R.layout.activity_main);
		progress = new ProgressDialog(this);

	}

	public void btnViewArchiveClicked(View v) {
		Intent intent = new Intent(this,ArchiveActivity.class);
		startActivity(intent);
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

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case IMAGE_CROPPING_CODE:
			Intent intent = new Intent(this,CroppingActivity.class);
			startActivityForResult(intent, IMAGE_REQUEST_CODE);
		case IMAGE_REQUEST_CODE:
			if (requestCode == IMAGE_REQUEST_CODE) {
				if (resultCode == Activity.RESULT_OK) {
					
					// Verify path exists
					File path = new File(getFilesDir(), "quatenation/quateImages");
					if (!path.exists())
						path.mkdirs();
					File imageFile = new File(path, "image.jpg");
					Bitmap optimizedBitmap = processPhotoFromPath(imageFile.getAbsolutePath());
//					new BitmapToStringService(this, new TessBaseAPI(), optimizedBitmap).execute();
					bmp = optimizedBitmap;
//					getUTF8TextFromProcessedBitmap(optimizedBitmap);
					// TODO: clear files + dir
					
					Log.v(TAG, "Before baseApi");
					final TessBaseAPI baseApi = new TessBaseAPI();
					Log.v(TAG, "Initialize baseApi");
//					baseApi.setDebug(true);
					// getLang() returns equ in case of equations detection
					baseApi.init(Constants.TESSBASE_PATH, Constants.DEFAULT_LANGUAGE);
					Log.v(TAG, "init baseApi done");
					baseApi.setImage(bmp);
					recognizedText = baseApi.getUTF8Text();
					baseApi.end();
//					bmp.recycle();
					Log.v(TAG, recognizedText);
					
//		            return recognizedText;
//					ConvertImageToTextAsync async = new ConvertImageToTextAsync();
//					async.execute("");

					
				}
			}
		}
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
	
	
	 private class ConvertImageToTextAsync extends AsyncTask<String, Void, String> {

	        @Override
	        protected String doInBackground(String... params) {
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

	        @Override
	        protected void onPostExecute(String result) {
	        	progress.dismiss();
				Intent quotePreview = new Intent(getApplicationContext(), QuotePreviewActivity.class);
				quotePreview.putExtra(Constants.KEY_QUOTE_CONTENT, recognizedText);
				startActivity(quotePreview);
	        	
	        }

	        @Override
	        protected void onPreExecute() {
	    		progress.setTitle("Processing");
	    		progress.setMessage("Converting image to text...");
	    		progress.show();
	        }

	        @Override
	        protected void onProgressUpdate(Void... values) {}
	    }

	 
//	 public static Image Rescale(Image image, int dpiX, int dpiY)
//	 {
//	     Bitmap bm = new Bitmap((int)(image.Width * dpiX / image.HorizontalResolution), (int)(image.Height * dpiY / image.VerticalResolution));
//	     bm.SetResolution(dpiX, dpiY);
//	     Graphics g = Graphics.FromImage(bm);
//	     g.InterpolationMode = InterpolationMode.Bicubic;
//	     g.PixelOffsetMode = PixelOffsetMode.HighQuality;
//	     g.DrawImage(image, 0, 0);
//	     g.Dispose();
//
//	     return bm;
//	 }
	 
//	 protected void ocr() {
//		 
//	        BitmapFactory.Options options = new BitmapFactory.Options();
//	        options.inSampleSize = 2;
//	        Bitmap bitmap = BitmapFactory.decodeFile(IMAGE_PATH, options);
//	 
//	        try {
//	            ExifInterface exif = new ExifInterface(IMAGE_PATH);
//	            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//	 
//	            Log.v(LOG_TAG, "Orient: " + exifOrientation);
//	 
//	            int rotate = 0;
//	            switch (exifOrientation) {
//	                case ExifInterface.ORIENTATION_ROTATE_90:
//	                    rotate = 90;
//	                    break;
//	                case ExifInterface.ORIENTATION_ROTATE_180:
//	                    rotate = 180;
//	                    break;
//	                case ExifInterface.ORIENTATION_ROTATE_270:
//	                    rotate = 270;
//	                    break;
//	            }
//	 
//	            Log.v(LOG_TAG, "Rotation: " + rotate);
//	 
//	            if (rotate != 0) {
//	 
//	                // Getting width & height of the given image.
//	                int w = bitmap.getWidth();
//	                int h = bitmap.getHeight();
//	 
//	                // Setting pre rotate
//	                Matrix mtx = new Matrix();
//	                mtx.preRotate(rotate);
//	 
//	                // Rotating Bitmap
//	                bitmap = Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
//	                // tesseract req. ARGB_8888
//	                bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
//	            }
//	 
//	        } catch (IOException e) {
//	            Log.e(LOG_TAG, "Rotate or coversion failed: " + e.toString());
//	        }
//	 
//	        ImageView iv = (ImageView) findViewById(Page on r.id.image);
//	        iv.setImageBitmap(bitmap);
//	        iv.setVisibility(View.VISIBLE);
//	 
//	        Log.v(LOG_TAG, "Before baseApi");
//	 
//	        TessBaseAPI baseApi = new TessBaseAPI();
//	        baseApi.setDebug(true);
//	        baseApi.init(DATA_PATH, LANG);
//	        baseApi.setImage(bitmap);
//	        String recognizedText = baseApi.getUTF8Text();
//	        baseApi.end();
//	 
//	        Log.v(LOG_TAG, "OCR Result: " + recognizedText);
//	 
//	        // clean up and show
//	        if (LANG.equalsIgnoreCase("eng")) {
//	            recognizedText = recognizedText.replaceAll("[^a-zA-Z0-9]+", " ");
//	        }
//	        if (recognizedText.length() != 0) {
//	            ((TextView) findViewById(Page on r.id.field)).setText(recognizedText.trim());
//	        }
//	    }
}
