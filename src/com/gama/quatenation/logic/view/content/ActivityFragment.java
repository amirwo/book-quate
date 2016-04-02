package com.gama.quatenation.logic.view.content;

import java.util.HashSet;
import java.util.Set;

import com.gama.quatenation.R;
import com.gama.quatenation.logic.Configuration;
import com.gama.quatenation.logic.view.tabs.TabsController;
import com.gama.quatenation.model.book.Book;
import com.gama.quatenation.model.book.BookInfoResponse;
import com.gama.quatenation.model.book.IndustryIdentifiers;
import com.gama.quatenation.model.book.VolumeInfo;
import com.gama.quatenation.model.quote.Quote;
import com.gama.quatenation.model.quote.QuoteRequest;
import com.gama.quatenation.model.quote.QuotesResponse;
import com.gama.quatenation.services.GetBookInfoService;
import com.gama.quatenation.services.GetQuoteFeedService;
import com.gama.quatenation.services.GetQuotesService;
import com.gama.quatenation.services.GetTextFromBitmapService;
import com.gama.quatenation.services.RequestListener;
import com.gama.quatenation.services.SendQuoteService;
import com.gama.quatenation.utils.BitmapUtils;
import com.gama.quatenation.utils.Util;
import com.gama.quatenation.utils.ui.CropImageView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityFragment extends Fragment {

	// View id's
	private static final int CROPPING_IMAGE_ID = 201;
	private static final int TAKE_PHOTO_TAB_ID = 202;
	private static final int ROTATE_PHOTO_TAB_ID = 203;
	private static final int CROP_PHOTO_TAB_ID = 204;
	private static final int QUOTE_PHOTO_TAB_ID = 205;

	// activity intents
	private static final int IMAGE_REQUEST_CODE = 1;
	private static final String CAPTURE_IMAGE_FILE_PROVIDER = "com.gama.quatenation.fileprovider";
	protected static final int PERMISSION_REQUEST_CODE_CAMERA = 66;


	// Activity codes
	private static final int ADD_QUOTE_ACTIVITY = 0;
	private static final int QUOTE_PREV_ACTIVITY = 10;
	private static final int QUOTE_FEED_ACTIVITY = 1;
	private static final int SEARCH_QUOTE_ACTIVITY = 2;
	private static final int FAVORITES_ACTIVITY = 3;
	private static final String TAG = "ActivityFragment";

	// Cropping activity instances
	private CropImageView cropImageView;
	private ProgressDialog dialog;

	// Preview activity instances
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor prefEditor;

	private int choosenVolume = 0;
	private EditText quoteEditText;
	private EditText pageEditText;
	private VolumeInfo volumeInfo;
	private String processedText;
	private AlertDialog.Builder submitCompleteDialog;

	// book info
	private EditText authorEditText, titleEditText;
	private RequestListener bookDetailsRequestListener, searchQuotesRequestListener;
	
	// search activity instances
	private EditText authorSearchEditText, titleSearchEditText;
	private LinearLayout searchResultsLayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		submitCompleteDialog = new AlertDialog.Builder(this.getContext());
		submitCompleteDialog.setTitle("Quote submitted successfuly!");
		
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
//		SetAllActivityLayoutsToGone(rootView);
		switch (activityIndex) {
		case ADD_QUOTE_ACTIVITY:
			RelativeLayout rel = (RelativeLayout) rootView.findViewById(R.id.quotePreviewLayout);
			rel.setVisibility(View.GONE);
			Log.v(TAG, "init add quote activity");
			initCroppingActivity(context, rootView);
			break;
		case QUOTE_FEED_ACTIVITY:
			Log.v(TAG, "init QUOTE_FEED_ACTIVITY");
			final LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.llFeedView);
			ll.setVisibility(View.VISIBLE);

			new GetQuoteFeedService(context, 0, true, new RequestListener() {
				
				@Override
				public void onRequestError(int errCode) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public <T> void onRequestComplete(T response) {
					QuotesResponse quotesResponse = null;
					if (response instanceof QuotesResponse) {
						quotesResponse = (QuotesResponse) response;
					}
					for (Quote q : quotesResponse.getQuotes()) {
						ll.addView(new QuoteView(context, q));
					}
				}
			}).execute();

			break;
		case QUOTE_PREV_ACTIVITY:
			Log.v(TAG, "init QUOTE_PREV_ACTIVITY");
			RelativeLayout croppingLayout = (RelativeLayout) rootView.findViewById(R.id.croppingRelativeLayout);
			croppingLayout.setVisibility(View.GONE);
			RelativeLayout relPrev = (RelativeLayout) rootView.findViewById(R.id.quotePreviewLayout);
			relPrev.setVisibility(View.VISIBLE);

			quoteEditText = (EditText) relPrev.findViewById(R.id.quote_content_text);
			authorEditText = (EditText) relPrev.findViewById(R.id.author_auto_complete);
			titleEditText = (EditText) relPrev.findViewById(R.id.title_auto_complete);
			pageEditText = (EditText) relPrev.findViewById(R.id.page_edit_text);
			pageEditText.setText("");
			quoteEditText.setText(processedText);

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
							Log.i(TAG, "Got some non null response from google API, num of items " + bookInfoResponse.getTotalItems());
							updateBookInfoFields(bookInfoResponse);
						} else {
							Log.i(TAG, "Got no response from google book API - sending quote anyway");
							submitQuote();
							onPreviewActivityFinish();
						}
						
						
					} else {
						Log.e(TAG, "Couldn't handle book response - got " + response.getClass() + " class "
								+ "instead of BookInfoResponse class");
					}

				}

			};

			Button submitButton = (Button) relPrev.findViewById(R.id.submit_button);
			Button cancelButton = (Button) relPrev.findViewById(R.id.cancel_button);
			submitButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					getBookInfoServiceClicked(titleEditText.getText().toString(),authorEditText.getText().toString().split(",")[0], bookDetailsRequestListener);
				}
			});
			cancelButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					onPreviewActivityFinish();

				}
			});
			break;
		case SEARCH_QUOTE_ACTIVITY:
			Log.v(TAG, "init SEARCH_QUOTE_ACTIVITY");
			RelativeLayout searchLayout = (RelativeLayout) rootView.findViewById(R.id.searchQuoteLayout);
			searchLayout.setVisibility(View.VISIBLE);
			authorSearchEditText = (EditText) searchLayout.findViewById(R.id.author_search_complete);
			titleSearchEditText = (EditText) searchLayout.findViewById(R.id.title_search_complete);
			Button submitSearch = (Button) searchLayout.findViewById(R.id.search_button);
			searchResultsLayout = (LinearLayout) rootView.findViewById(R.id.llSearchhView);
			searchQuotesRequestListener = new RequestListener() {
				
				@Override
				public void onRequestError(int errCode) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public <T> void onRequestComplete(T response) {
					if (response instanceof QuotesResponse) {
						QuotesResponse r = (QuotesResponse) response;
						searchResultsLayout.removeAllViews();
						searchResultsLayout.setVisibility(View.VISIBLE);
						for (Quote q : r.getQuotes()) {
							searchResultsLayout.addView(new QuoteView(context, q));
						}
					}
					
				}
			};
			submitSearch.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					QuoteRequest req = new QuoteRequest();
					req.setAuthor(authorSearchEditText.getText().toString());
					req.setTitle(titleSearchEditText.getText().toString());
					submitSearchClicked(req, searchQuotesRequestListener);
				}
			});
			break;
		case FAVORITES_ACTIVITY: 
			Log.v(TAG, "init FAVORITES_ACTIVITY");
			final LinearLayout llFavorites = (LinearLayout) rootView.findViewById(R.id.llFavoritesView);
			llFavorites.setVisibility(View.VISIBLE);
			llFavorites.removeAllViews();
			// First put all the liked quotes
			Set<String> currentIds = new HashSet<String>();
			for (Quote q : Configuration.getInstance().getUserLikedList().values()) {
				llFavorites.addView(new QuoteView(context, q));
				currentIds.add(q.getId());
			}
			
			// add all private quotes 
			for (Quote q : Configuration.getInstance().getUserQuoteList()) {
				// dont add if already exists from favorites
				if (currentIds.contains(q.getId())) {
					continue;
				}
				llFavorites.addView(new QuoteView(context, q));
			}
			
			// Old method - updated list via server response
//			new GetFavoritesService(context, false, new RequestListener() {
//				
//				@Override
//				public void onRequestError(int errCode) {
//					// TODO Auto-generated method stub
//					
//				}
//				
//				@Override
//				public <T> void onRequestComplete(T response) {
//					QuotesResponse quotesResponse = null;
//					if (response instanceof QuotesResponse) {
//						quotesResponse = (QuotesResponse) response;
//					}
//					for (Quote q : quotesResponse.getQuotes()) {
//						llFavorites.addView(new QuoteView(context, q));
//					}
//				}
//			}).execute();
		}
	}

	private void initCroppingActivity(Context context, View rootView) {
		RelativeLayout croppingLayout = (RelativeLayout) rootView.findViewById(R.id.croppingRelativeLayout);
		croppingLayout.setVisibility(View.VISIBLE);
		croppingLayout.setBackgroundColor(Configuration.getInstance().getSecondaryColor());
		cropImageView = (CropImageView) croppingLayout.getChildAt(0);
		cropImageView.setId(CROPPING_IMAGE_ID);
		cropImageView.setBackgroundColor(Configuration.getInstance().getSecondaryColor());
		cropImageView.setImageBitmap(null);;
		initButtons(context, croppingLayout, CROPPING_IMAGE_ID);
	}

	private void initButtons(final Context context, RelativeLayout croppingLayout, int imageViewID) {
		OnItemSelectedListener listener = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String selectedLang = Configuration.getInstance().getOcrLanguages()[position];
				Configuration.getInstance().setSelectedLanguage(selectedLang);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		};
		RelativeLayout spinnerLangTab = Util.createSpinnerTabView(context, listener, "language.png", 209);
		// create take photo button
		RelativeLayout takePhotoTab = Util.createTabView(context, null, " take photo ", "take_photo.png",
				TAKE_PHOTO_TAB_ID, false);
		takePhotoTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			       if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
		                // if sdk is marshmallow
		                if (ContextCompat.checkSelfPermission(getActivity(),
		                        Manifest.permission.CAMERA)
		                        != PackageManager.PERMISSION_GRANTED) {
		                    // permission is not granted
		                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
		                            Manifest.permission.CAMERA)) {
		                        // if permissions explanation is needed
		                        Toast.makeText(getActivity().getApplicationContext(),
		                                "Camera permission allows us to access the camera." +
		                                " Please allow in App Settings for additional " +
		                                "functionality.",Toast.LENGTH_SHORT).show();
		                    } else {
		                        ActivityCompat.requestPermissions(getActivity(),
		                                new String[]{Manifest.permission.CAMERA},
		                                PERMISSION_REQUEST_CODE_CAMERA);
		                    }
		                    return; // permission is not yet granted
		                } else {
		                	onTakePhotoClicked();
		                }
		            } else {
		            	onTakePhotoClicked();
		            }
			}
		});

		RelativeLayout rotatePhotoTab = Util.createTabView(context, null, "  rotate  ", "rotate_photo_2.png",
				ROTATE_PHOTO_TAB_ID, false);
		rotatePhotoTab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cropImageView.getImageBitmap() != null) {
					cropImageView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
				}
			}
		});

		RelativeLayout cropPhotoTab = Util.createTabView(context, null, "  crop  ", "crop_photo_1.png",
				CROP_PHOTO_TAB_ID, false);
		cropPhotoTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Get cropped image, and show result.
				if (cropImageView.getImageBitmap() != null) {
					cropImageView.setImageBitmap(cropImageView.getCroppedBitmap());
				}
			}
		});

		RelativeLayout quotePhotoTab = Util.createTabView(context, null, "  quote!  ", "quote_photo_1.png",
				QUOTE_PHOTO_TAB_ID, false);

		quotePhotoTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final GetTextFromBitmapService getTextService = new GetTextFromBitmapService(getContext(),
						processPhoto(), new RequestListener() {

					@Override
					public void onRequestError(int errCode) {
						// TODO Auto-generated method stub
					}

					@Override
					public <T> void onRequestComplete(T response) {
						processedText = "Error while processing photo to text";
						if (dialog.isShowing()) {
							dialog.dismiss();
						}
						if (response instanceof String) {
							processedText = (String) response;
						}
						startPreviewActivity();
					}
				});
				getTextService.execute();
				dialog = new ProgressDialog(context);
				dialog.setTitle("Processing photo, please wait...");
				dialog.setMessage("this will take up to a minute");
				dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						getTextService.stopService();
						dialog.dismiss();
					}

				});
				dialog.show();
			}
		});

		RelativeLayout.LayoutParams rlParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		rlParams.addRule(RelativeLayout.BELOW, imageViewID);
		croppingLayout.addView(takePhotoTab, rlParams);
		RelativeLayout.LayoutParams rlParams2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		rlParams2.addRule(RelativeLayout.BELOW, imageViewID);
		rlParams2.addRule(RelativeLayout.RIGHT_OF, TAKE_PHOTO_TAB_ID);
		croppingLayout.addView(cropPhotoTab, rlParams2);
		RelativeLayout.LayoutParams rlParams3 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		rlParams3.addRule(RelativeLayout.BELOW, imageViewID);
		rlParams3.addRule(RelativeLayout.RIGHT_OF, CROP_PHOTO_TAB_ID);
		croppingLayout.addView(rotatePhotoTab, rlParams3);
		RelativeLayout.LayoutParams rlParamsSpinner = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		rlParamsSpinner.addRule(RelativeLayout.BELOW, imageViewID);
		rlParamsSpinner.addRule(RelativeLayout.RIGHT_OF, ROTATE_PHOTO_TAB_ID);
		croppingLayout.addView(spinnerLangTab, rlParamsSpinner);
		RelativeLayout.LayoutParams rlParams4 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		rlParams4.addRule(RelativeLayout.BELOW, imageViewID);
		rlParams4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		croppingLayout.addView(quotePhotoTab, rlParams4);
	}

	public void onTakePhotoClicked() {
		// Set the captured camera picture to be stored in path
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this.getContext().getApplicationContext(),
				CAPTURE_IMAGE_FILE_PROVIDER, Configuration.getInstance().getPathToPicture()));
		startActivityForResult(intent, IMAGE_REQUEST_CODE);
	}

	private void startPreviewActivity() {
		Log.v(TAG, "starting preview layout");
		View rootView = this.getActivity().findViewById(R.id.mainRelativeLayout);
		initMainLayout(this.getContext(), rootView, QUOTE_PREV_ACTIVITY);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case IMAGE_REQUEST_CODE:
			if (requestCode == IMAGE_REQUEST_CODE) {
				if (resultCode == Activity.RESULT_OK) {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 1;
					Bitmap bitmap = BitmapFactory
							.decodeFile(Configuration.getInstance().getPathToPicture().getAbsolutePath(), options);
					cropImageView.setImageBitmap(bitmap);
					// make photo process buttons available
					
				}
			}
		}
	}

	protected Bitmap processPhoto() {
		Bitmap bitmap = cropImageView.getCroppedBitmap();
		if (bitmap == null) {
			bitmap = BitmapUtils.getTextImage("no photo taken", 480, 640);
		}
		bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
		return bitmap;
	}

	////////////////////////////
	// Quote Preview methods //
	////////////////////////////

	public void updateBookInfoFields(final BookInfoResponse bookInfoResponse) {
		boolean foundMatch = false;
		CharSequence books[] = new CharSequence[bookInfoResponse.getItems().length];
		int j = 0;
		for (Book book : bookInfoResponse.getItems()) {
			String title = book.getVolumeInfo().getTitle();
			String[] authors = book.getVolumeInfo().getAuthors();
			String authorsStr = "";
			if (authors != null) {
				for (int i = 0; i < authors.length - 1; i++) {
					authorsStr += authors[i] + ",";
				}
				authorsStr += authors[authors.length - 1];
			}

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
			AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
			builder.setTitle("Pick the currect book details");
			builder.setCancelable(true);
			builder.setItems(books, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					choosenVolume = which;
					updateVolume(bookInfoResponse);
					submitQuote();
					onPreviewActivityFinish();
				}
			});
			builder.show();
		} else {
			submitQuote();
			onPreviewActivityFinish();
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
		volumeInfo.setIndustryIdentifiers(getEmptyIdentifiers(industryIdentifiers));
		((TextView) authorEditText).setText(authorsStr);
		((TextView) titleEditText).setText(volumeInfo.getTitle());
	}

	private IndustryIdentifiers[] getEmptyIdentifiers(IndustryIdentifiers[] industryIdentifiers) {
		if (industryIdentifiers != null) {
			for (IndustryIdentifiers identifier : industryIdentifiers) {
				identifier.setIdentifier("1111111111");
			}
		} else {
			IndustryIdentifiers isbn10 = new IndustryIdentifiers();
			isbn10.setType("ISBN_10");
			isbn10.setIdentifier("0000000000");
			IndustryIdentifiers isbn13 = new IndustryIdentifiers();
			isbn13.setType("ISBN_13");
			isbn13.setIdentifier("0000000000000");
			IndustryIdentifiers[] identifiers = {isbn10, isbn13};
			industryIdentifiers = identifiers;
		}
		return industryIdentifiers;
		
	}

	private void submitQuote() {
		Log.i(TAG, "trying to submit quote with author:" + authorEditText.getText().toString() + ". title:" + titleEditText.getText().toString());
		// volume info has been set manually
		if (volumeInfo == null) {
			volumeInfo = new VolumeInfo();
		}
		IndustryIdentifiers[] industryIdentifiers = volumeInfo.getIndustryIdentifiers();
		volumeInfo.setIndustryIdentifiers(getEmptyIdentifiers(industryIdentifiers));
		// TODO: support more than 1 author in editable mode
		volumeInfo.setAuthors(authorEditText.getText().toString().split(","));
		volumeInfo.setTitle(titleEditText.getText().toString());
		volumeInfo.setPageCount("0");

		// Create new Quote and update
		Quote quote = new Quote();

		quote.setContent(quoteEditText.getText().toString());
		quote.setVolumeInfo(volumeInfo);
		quote.setUser(Configuration.getInstance().getUserAdvertisingId());
		sharedPref = this.getActivity().getSharedPreferences("quote_shared_pref", 0);
		prefEditor = sharedPref.edit();
		prefEditor.putString("content", quote.getContent());
		prefEditor.putString("book_title", volumeInfo.getTitle());
		prefEditor.putString("book_author", volumeInfo.getAuthors()[0]);
		prefEditor.commit();

		new SendQuoteService(this.getContext(), quote).execute();
	}

	public void getBookInfoServiceClicked(String title, String author, RequestListener listener) {
		new GetBookInfoService(this.getContext(), title, author, listener).execute();
	}

	public void submitSearchClicked(QuoteRequest request, RequestListener listener) {
		new GetQuotesService(this.getContext(), request, listener).execute();
	}
	public void onPreviewActivityFinish() {
		View rootView = this.getActivity().findViewById(R.id.mainRelativeLayout);
		rootView.findViewById(R.id.croppingRelativeLayout).setVisibility(View.VISIBLE);
		initMainLayout(this.getContext(), rootView, ADD_QUOTE_ACTIVITY);
	}

	 @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                	
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),"Permission Denied," +
                            " You cannot access the camera.",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
	 
}
