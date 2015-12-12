package com.gama.quatenation.logic.view.content;

import com.gama.quatenation.model.Placement;
import com.gama.quatenation.model.Template;
import com.gama.quatenation.utils.Util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class TemplateAdapter extends BaseAdapter {
    
	private Context context;
    private Placement placement;
    

    public TemplateAdapter(Context c, Placement placement) {
        context = c;
        this.placement = placement;
    }

    public int getCount() {
    	return 0;
//    	return placement.getTemplates().size();
    }

    public Object getItem(int position) {
    	return new Template("this", "is", "");
//        return placement.getTemplates().get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
    	RelativeLayout layout = new RelativeLayout(context);
    	
        ImageView imageView;
        TextView textView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            int padding = Util.dpToPx(context, 5);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setAdjustViewBounds(true);
            imageView.setId(1234);
            
            int width, height;
            if (placement.getDisplayMode() == Placement.DISPLAY_MODE_GRID) {
            	width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            	height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            } else {
            	width = RelativeLayout.LayoutParams.MATCH_PARENT;
            	height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            }
            
            RelativeLayout.LayoutParams imageViewParams = new RelativeLayout.LayoutParams(width, height);
            imageViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            imageViewParams.addRule(RelativeLayout.ALIGN_TOP);
            imageView.setLayoutParams(imageViewParams);
            layout.addView(imageView);
            
            textView = new TextView(context);
            textView.setTypeface(Typeface.createFromAsset(context.getAssets(), "ufonts.com_gotham-book.ttf"));
            textView.setTextSize(15);
            textView.setTextColor(Color.rgb(88,94,103));
            textView.setId(2345);
            
            RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            textViewParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
            textViewParams.addRule(RelativeLayout.BELOW, 1234);
            textView.setLayoutParams(textViewParams);
            layout.addView(textView);
            
        } else {
            layout = (RelativeLayout) convertView;
            imageView = (ImageView)layout.findViewById(1234);
            textView = (TextView)layout.findViewById(2345);
        }
        Template templte = new Template("this", "is", "");
//        Template template = placement.getTemplates().get(position);
//        imageView.setImageBitmap(template.getImageBitmap(context));
//        textView.setText(template.getName());
        return layout;
    }
    
}