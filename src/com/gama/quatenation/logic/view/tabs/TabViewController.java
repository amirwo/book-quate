package com.gama.quatenation.logic.view.tabs;

import java.lang.ref.WeakReference;

import com.gama.quatenation.R;
import com.gama.quatenation.logic.Configuration;
import com.gama.quatenation.utils.Util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class TabViewController {	
	private static final int ID_IMAGE = 1001;
	private static final int ID_TEXT = 1002;
	
	public interface TabListener{
		public void onTabSelected(TabPlacement placement);
	}
	
	private ViewGroup view;
	private TabPlacement placement;
	private TabListener listener;
	
	private WeakReference<Context> refContext;

	public ViewGroup getView() {
		return view;
	}

	private void setView(ViewGroup view) {
		this.view = view;
	}
	
	public void deactive(){
		if (view != null){
			TextView text = (TextView)view.findViewById(ID_TEXT);
			text.setTextColor(getContext().getResources().getColor(R.color.inactive_tab));
			
			ImageView image = (ImageView)view.findViewById(ID_IMAGE);
			if (getContext() != null){
				image.setImageBitmap(getPlacement().getImageBitmap(getContext()));
			}
		}
	}		
	
	public void active(){
		TabsController.getInstance().inActiveAllTabs();
		TextView text = (TextView)view.findViewById(ID_TEXT);		
		ImageView image = (ImageView)view.findViewById(ID_IMAGE);
		text.setTextColor(Configuration.getInstance().getMainColor());
		image.setImageBitmap(placement.getImageSelectedBitmap(getContext()));
		
		TabListener listener = getListener();
		if (listener != null){
			listener.onTabSelected(getPlacement());
		}
	}	

	public TabPlacement getPlacement() {
		return placement;
	}

	private void setPlacement(TabPlacement placement) {
		this.placement = placement;
	}

	private TabListener getListener() {
		return listener;
	}

	private void setListener(TabListener listener) {
		this.listener = listener;
	}

	private Context getContext() {
		if (refContext == null){
			return null;
		}
		return refContext.get();
	}

	private void setContext(Context refContext) {
		this.refContext = new WeakReference<Context>(refContext);
	}

	private TabViewController(){}
	
	public static TabViewController getController(Context context, final TabPlacement placement, TabListener listener){
		final TabViewController result = new TabViewController();
		result.setPlacement(placement);
		result.setListener(listener);
		result.setContext(context);

		RelativeLayout view = new RelativeLayout(context);
		ViewGroup.LayoutParams vgParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(vgParams);
		
		// Add Image
		final ImageView image = new ImageView(context);
		RelativeLayout.LayoutParams rlImage = new RelativeLayout.LayoutParams(Util.dpToPx(context, 71), Util.dpToPx(context, 50));
		rlImage.addRule(RelativeLayout.CENTER_HORIZONTAL);
		image.setScaleType(ScaleType.FIT_CENTER);
		
		int dp2 = Util.dpToPx(context, 2);
		int dp12 = Util.dpToPx(context, 12);
		// rlImage.setMargins(dp25 + 6, dp10, dp25 + 6, dp10 / 2);
		rlImage.setMargins(0, dp2, 0, dp12);
		image.setLayoutParams(rlImage);
		image.setId(ID_IMAGE);
		view.addView(image);
		
		// Add Title
		final TextView text = new TextView(context);
		text.setText(result.getPlacement().getName());
		RelativeLayout.LayoutParams rlText = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, 
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		rlText.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rlText.addRule(RelativeLayout.BELOW, ID_IMAGE);
		text.setTextSize(14);
		text.setTypeface(Typeface.createFromAsset(context.getAssets(), "ufonts.com_gotham-bold.ttf"));
		text.setLayoutParams(rlText);
		text.setId(ID_TEXT);
		rlText.setMargins(0, 0, 0, 0);
		view.addView(text);
		
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {				
				TabsController.getInstance().setViewPagerPage(result);
			}
		});
		
		result.setView(view);
		
		result.deactive();
		
		return result;
	}
}