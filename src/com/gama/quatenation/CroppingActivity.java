package com.gama.quatenation;

import java.io.File;

import com.gama.quatenation.utils.Constants;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.isseiaoki.simplecropview.CropImageView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CroppingActivity extends Activity {

	private final String TAG = "CroppiingActivity";
	private static final String CAPTURE_IMAGE_FILE_PROVIDER = "com.gama.quatenation.fileprovider";
	private CropImageView cropImageView;
	private TextView imageText;
	private static final int IMAGE_REQUEST_CODE = 1;
	private File pathToPicture;
	private boolean pictureTaken = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cropping);
		// Create file path
		File path = new File(this.getFilesDir(), "quatenation/quateImages");
		if (!path.exists()) {
			path.mkdirs();
		}
		pathToPicture = new File(path, "image.jpg");
		cropImageView = (CropImageView) findViewById(R.id.cropImageView);
		cropImageView.setInitialFrameScale(1.0f);
		imageText = (TextView) findViewById(R.id.imageText);
		Button cropButton = (Button) findViewById(R.id.crop_button);
		
		cropButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Get cropped image, and show result.
				cropImageView.setImageBitmap(cropImageView.getCroppedBitmap());
			}
		});

		Button okButton = (Button) findViewById(R.id.ok_button);
		okButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent quotePreview = new Intent(getApplicationContext(), QuotePreviewActivity.class);
				quotePreview.putExtra(Constants.KEY_QUOTE_CONTENT, getUTF8TextFromProcessedBitmap(processPhoto()));
				startActivity(quotePreview);
				
//				imageText.setText(getUTF8TextFromProcessedBitmap(processPhoto()));
			}
		});

		Button rotateButton = (Button) findViewById(R.id.rotate_button);
		rotateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
				setOrientation();
			}
		});
		

	}

	private void setOrientation() {
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!pictureTaken) {
			// Set the captured camera picture to be stored in path
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			intent.putExtra(MediaStore.EXTRA_OUTPUT,
					FileProvider.getUriForFile(this, CAPTURE_IMAGE_FILE_PROVIDER, pathToPicture));
			startActivityForResult(intent, IMAGE_REQUEST_CODE);
		}
		
	};
		
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case IMAGE_REQUEST_CODE:
			if (requestCode == IMAGE_REQUEST_CODE) {
				if (resultCode == Activity.RESULT_OK) {
					// This area needs work and optimization
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 1;
					Bitmap bitmap = BitmapFactory.decodeFile(pathToPicture.getAbsolutePath(), options);
					cropImageView.setImageBitmap(bitmap);
					pictureTaken = true;
				}
			}
		}
	}

	protected Bitmap processPhoto() {
		Bitmap bitmap = cropImageView.getCroppedBitmap();
		bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		return bitmap;
	}
	

	public String getUTF8TextFromProcessedBitmap(Bitmap bmp) {
		Log.v(TAG, "Before baseApi");
		final TessBaseAPI baseApi = new TessBaseAPI();
		Log.v(TAG, "Initialize baseApi");
		// baseApi.setDebug(true);
		// getLang() returns equ in case of equations detection
		baseApi.init(Constants.TESSBASE_PATH, Constants.DEFAULT_LANGUAGE);
		Log.v(TAG, "init baseApi done");
		baseApi.setImage(bmp);
		String tessText = baseApi.getUTF8Text();
		baseApi.end();
//		bmp.recycle();
		 Log.v(TAG, "Detected TEXT: " + tessText);
		return tessText;
	}
	

	
//	radioCropMode = (RadioGroup) findViewById(R.id.radioCropMode);
//	radioCropMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//		
//		@Override
//		public void onCheckedChanged(RadioGroup group, int checkedId) {
//			CropMode cropMode = CropMode.RATIO_FIT_IMAGE;
//			switch(checkedId) {
//				case R.id.radio_RATIO_1_1:
//					cropMode = CropMode.RATIO_1_1;
//					break;
//				case R.id.radio_RATIO_16_9:
//					cropMode = CropMode.RATIO_16_9;
//					break;
//				case R.id.radio_RATIO_3_4:
//					cropMode = CropMode.RATIO_3_4;
//					break;
//				case R.id.radio_RATIO_4_3:
//					cropMode = CropMode.RATIO_4_3;
//					break;
//				case R.id.radio_RATIO_9_16:
//					cropMode = CropMode.RATIO_9_16;
//					break;
//				case R.id.radio_RATIO_FREE:
//					cropMode = CropMode.RATIO_FREE;
//					break;
//			}
//			
//			cropImageView.setCropMode(cropMode);
//			cropImageView.setInitialFrameScale(1.0f);
//		}
//	});
	
}
