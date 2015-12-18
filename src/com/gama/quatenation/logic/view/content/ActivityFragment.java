package com.gama.quatenation.logic.view.content;

import java.io.File;

import com.gama.quatenation.QuotePreviewActivity;
import com.gama.quatenation.R;
import com.gama.quatenation.logic.Configuration;
import com.gama.quatenation.logic.view.tabs.TabsController;
import com.gama.quatenation.model.book.Quote;
import com.gama.quatenation.utils.CameraUtil;
import com.gama.quatenation.utils.Constants;
import com.gama.quatenation.utils.Util;
import com.gama.quatenation.utils.ui.CropImageView;
import com.googlecode.tesseract.android.TessBaseAPI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ActivityFragment extends Fragment {

	private File pathToPicture;

	private static final int CROPPING_IMAGE_ID = 201;
	private static final int TAKE_PHOTO_TAB_ID = 202;
	private static final int ROTATE_PHOTO_TAB_ID = 203;
	private static final int CROP_PHOTO_TAB_ID = 204;
	private static final int QUOTE_PHOTO_TAB_ID = 205;
	
	private static final int IMAGE_REQUEST_CODE = 1;
	private static final int ADD_QUOTE_ACTIVITY = 0;
	private static final int QUOTE_FEED_ACTIVITY = 1;
	private static final int SEARCH_ACTIVITY = 2;
	private static final int FAVUORITES_ACTIVITY = 3;
	private static final String TAG = "AMIRActivityFragment";

	private static final String CAPTURE_IMAGE_FILE_PROVIDER = "com.gama.quatenation.fileprovider";
	private CropImageView cropImageView;
	private boolean pictureTaken = false;
	private String advId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.main_fragement, container, false);

		if (getArguments() != null) {
			Integer chosenActivity = (Integer) getArguments().get("Activity");
			Log.v(TAG, "entered with activity id " + chosenActivity);
			Log.v(TAG, "current tab " + TabsController.getInstance().getCurrentTab());
			initMainLayout(this.getContext(), rootView, chosenActivity);
		}

		return rootView;
	}

	public void initMainLayout(final Context context, View rootView, int activityIndex) {
		switch (activityIndex) {
		case ADD_QUOTE_ACTIVITY:
			if (!Configuration.getInstance().isInitCroppingActivity()) {
				Log.v(TAG, "init add quote activity");
				initCroppingActivity(context, rootView);
//				Configuration.getInstance().setInitCroppingActivity(true);
			} 
			break;
		case QUOTE_FEED_ACTIVITY:
			final LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.llView);
			for (Quote q : Configuration.getInstance().getUserQuoteList()) {
				ll.addView(new QuoteView(context, q));
			}
			// QuoteRequest request = new QuoteRequest();
			// request.setUserID("fksdl;fkfds");
			//
			// new GetQuotesService(getContext(), request, new RequestListener()
			// {
			//
			// @Override
			// public void onRequestError(int errCode) {
			// // TODO Auto-generated method stub
			//
			// }
			//
			// @Override
			// public <T> void onRequestComplete(T response) {
			// if (response instanceof QuotesResponse) {
			// if (((QuotesResponse) response).getQuotes() != null) {
			// for (QuoteSimpe q : ((QuotesResponse) response).getQuotes()) {
			// Log.v(TAG, "USER adv " +
			// Configuration.getInstance().getUserAdvertisingId());
			// ll.addView(new QuoteView(context, q));
			// }
			// }
			// }
			//
			// }
			// }).execute();
			break;
		}
	}

	private void initCroppingActivity(Context context, View rootView) {
		RelativeLayout croppingLayout = (RelativeLayout) rootView.findViewById(R.id.croppingRelativeLayout);
		croppingLayout.setBackgroundColor(Configuration.getInstance().getSecondaryColor());

		cropImageView = (CropImageView) croppingLayout.findViewById(R.id.cropImageView);
		cropImageView.setId(CROPPING_IMAGE_ID);
		initButtons(context, croppingLayout, CROPPING_IMAGE_ID);
		croppingLayout.setVisibility(View.VISIBLE);

		// Create file path
		File path = new File(this.getContext().getApplicationContext().getFilesDir(), "quatenation/quateImages");
		if (!path.exists()) {
			path.mkdirs();
		}
		pathToPicture = new File(path, "image.jpg");
	}

	private void initButtons(Context context, RelativeLayout croppingLayout, int imageViewID) {
		
		// create take photo button
		RelativeLayout takePhotoTab = Util.createTabView(context, null, " take photo ", "take_photo.png", TAKE_PHOTO_TAB_ID, false);
		takePhotoTab.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onTakePhotoClicked();
				
			}
		});
		
		RelativeLayout rotatePhotoTab = Util.createTabView(context, null, "  rotate  ", "rotate_photo_1.png", ROTATE_PHOTO_TAB_ID, false);
		rotatePhotoTab.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
			}
		});
		
		RelativeLayout cropPhotoTab = Util.createTabView(context, null, "  crop  ", "crop_photo_1.png", CROP_PHOTO_TAB_ID, false);
		cropPhotoTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Get cropped image, and show result.
				cropImageView.setImageBitmap(cropImageView.getCroppedBitmap());
			}
		});
		
		RelativeLayout quotePhotoTab = Util.createTabView(context, null, "  quote!  ", "quote_photo_1.png", QUOTE_PHOTO_TAB_ID, false);
		quotePhotoTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startPreviewActivity(getUTF8TextFromProcessedBitmap(processPhoto()));
			}


		});		
		
		RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlParams.addRule(RelativeLayout.BELOW, imageViewID);
		croppingLayout.addView(takePhotoTab, rlParams);
		RelativeLayout.LayoutParams rlParams2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlParams2.addRule(RelativeLayout.BELOW, imageViewID);
		rlParams2.addRule(RelativeLayout.RIGHT_OF, TAKE_PHOTO_TAB_ID);
		croppingLayout.addView(cropPhotoTab, rlParams2);
		RelativeLayout.LayoutParams rlParams3 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlParams3.addRule(RelativeLayout.BELOW, imageViewID);
		rlParams3.addRule(RelativeLayout.RIGHT_OF, CROP_PHOTO_TAB_ID);
		croppingLayout.addView(rotatePhotoTab, rlParams3);
		RelativeLayout.LayoutParams rlParams4 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rlParams4.addRule(RelativeLayout.BELOW, imageViewID);
		rlParams4.addRule(RelativeLayout.ALIGN_PARENT_END);
		croppingLayout.addView(quotePhotoTab, rlParams4);
	}

	public void onTakePhotoClicked() {
		// Set the captured camera picture to be stored in path
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				FileProvider.getUriForFile(this.getContext(), CAPTURE_IMAGE_FILE_PROVIDER, pathToPicture));
		startActivityForResult(intent, IMAGE_REQUEST_CODE);
	}

	private void startPreviewActivity(String utf8TextFromProcessedBitmap) {
		Intent quotePreview = new Intent(this.getContext(), QuotePreviewActivity.class);
		quotePreview.putExtra(Constants.KEY_QUOTE_CONTENT, utf8TextFromProcessedBitmap);
		startActivity(quotePreview);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
		final TessBaseAPI baseApi = new TessBaseAPI();
		// baseApi.setDebug(true);
		// getLang() returns equ in case of equations detection
		baseApi.init(Constants.TESSBASE_PATH, Constants.DEFAULT_LANGUAGE);
		baseApi.setImage(bmp);
		String tessText = baseApi.getUTF8Text();
		baseApi.end();
		// bmp.recycle();
		return tessText;
	}
}
