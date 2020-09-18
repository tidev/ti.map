/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

package ti.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiCompositeLayout;
import org.appcelerator.titanium.view.TiDrawableReference;
import org.appcelerator.titanium.view.TiUIView;

public class TiMapInfoWindow extends RelativeLayout
{
	private static final String TAG = "TiMapInfoWindow";
	public static final int LEFT_PANE = 0;
	public static final int RIGHT_PANE = 1;
	private static final int leftPaneId = 100;
	private static final int textLayoutId = 101;
	private static final int rightPaneId = 102;
	private static final int titleId = 200;
	private static final int snippetId = 201;

	private TiCompositeLayout leftPane;
	private TiCompositeLayout rightPane;
	private TextView title;
	private TextView snippet;
	private View[] clicksourceList;
	private String currentClicksource = null;

	private AnnotationProxy proxy = null;

	public TiMapInfoWindow(Context context, AnnotationProxy proxy)
	{
		super(context);
		setBackgroundColor(Color.WHITE);
		setGravity(Gravity.NO_GRAVITY);

		this.proxy = proxy;
		RelativeLayout.LayoutParams params = null;

		// Left button or left view
		leftPane = new TiCompositeLayout(context);
		leftPane.setId(leftPaneId);
		leftPane.setTag(MapModule.PROPERTY_LEFT_PANE);
		params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.setMargins(0, 0, 5, 0);
		addView(leftPane, params);

		// Title and subtitle
		RelativeLayout textLayout = new RelativeLayout(context);
		textLayout.setGravity(Gravity.NO_GRAVITY);
		textLayout.setId(textLayoutId);

		title = new TextView(context);
		title.setId(titleId);
		title.setTextColor(Color.BLACK);
		title.setTag(TiC.PROPERTY_TITLE);
		title.setTypeface(Typeface.DEFAULT_BOLD);
		params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_TOP);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		textLayout.addView(title, params);

		snippet = new TextView(context);
		snippet.setId(snippetId);
		snippet.setTextColor(Color.GRAY);
		snippet.setTag(TiC.PROPERTY_SUBTITLE);
		snippet.setTypeface(Typeface.DEFAULT);
		params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.BELOW, titleId);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		textLayout.addView(snippet, params);

		params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.RIGHT_OF, leftPaneId);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		addView(textLayout, params);

		// Right button or right view
		rightPane = new TiCompositeLayout(context);
		rightPane.setId(rightPaneId);
		rightPane.setTag(MapModule.PROPERTY_RIGHT_PANE);
		params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.RIGHT_OF, textLayoutId);
		params.addRule(RelativeLayout.CENTER_VERTICAL);
		params.setMargins(5, 0, 0, 0);
		addView(rightPane, params);

		clicksourceList = new View[] { leftPane, title, snippet, rightPane };
	}

	public void setTitle(String title)
	{
		if (title != null) {
			this.title.setVisibility(VISIBLE);
			this.title.setText(title);
		} else {
			this.title.setVisibility(GONE);
		}
	}

	public String getTitle()
	{
		if (this.title.getVisibility() == VISIBLE) {
			return (String) this.title.getText();
		}
		return null;
	}

	public void setSubtitle(String subtitle)
	{
		if (subtitle != null) {
			this.snippet.setVisibility(VISIBLE);
			this.snippet.setText(subtitle);
		} else {
			snippet.setVisibility(GONE);
		}
	}

	public String getSubtitle()
	{
		if (this.snippet.getVisibility() == VISIBLE) {
			return (String) this.snippet.getText();
		}
		return null;
	}

	public void setLeftOrRightPane(Object obj, int flag)
	{
		TiCompositeLayout pane = null;
		if (flag == LEFT_PANE) {
			pane = leftPane;
		} else if (flag == RIGHT_PANE) {
			pane = rightPane;
		} else {
			Log.e(TAG, "Invalid valud for flag in setLeftOrRightPane", Log.DEBUG_MODE);
			return;
		}

		pane.removeAllViews();

		if (obj == null) {
			pane.setVisibility(GONE);
			return;
		}

		pane.setVisibility(VISIBLE);
		if (obj instanceof String) {
			ImageView imageview = new ImageView(getContext());
			TiDrawableReference imageref = TiDrawableReference.fromUrl(TiApplication.getAppCurrentActivity(),
																	   proxy.resolveUrl(null, (String) obj));
			Bitmap bitmap = imageref.getBitmap();
			if (bitmap != null) {
				imageview.setImageBitmap(bitmap);
				pane.addView(imageview);
			} else {
				Log.w(TAG, "Unable to get the image from the left / right button: " + obj, Log.DEBUG_MODE);
			}
		} else if (obj instanceof TiViewProxy) {
			TiUIView view = ((TiViewProxy) obj).getOrCreateView();
			if (view != null) {
				pane.addView(view.getNativeView());
			} else {
				Log.w(TAG, "Unable to get the view from the left / right view: " + obj, Log.DEBUG_MODE);
			}
		}
	}

	/**
	 * Analyze the touch event to find out:
	 * 1. whether it is inside the info window and
	 * 2. if it is, what the corresponding clicksource is.
	 * The clicksource can be one of "leftPane", "title", "subtible", "rightPane" or null. Null means the event
	 * is not inside "leftPane", "title", "subtible" or "rightPane".
	 * @param ev The MotionEvent detected. Its coordinates are relative to the map view.
	 * @param markerPoint The screen coordinate for the marker which displays the info window.
	 * @param iconImageHeight The height of the marker icon.
	 */
	public void analyzeTouchEvent(MotionEvent ev, Point markerPoint, int iconImageHeight)
	{
		int evX = (int) ev.getX();
		int evY = (int) ev.getY();
		ViewParent p = this.getParent();
		int infoWindowHalfWidth;
		int infoWindowHeight;
		if (p instanceof View) {
			View infoWindowParent = (View) p;
			infoWindowHalfWidth = infoWindowParent.getWidth() / 2;
			infoWindowHeight = infoWindowParent.getHeight();
		} else {
			infoWindowHalfWidth = this.getWidth() / 2;
			infoWindowHeight = this.getHeight();
		}
		if (evX > markerPoint.x - infoWindowHalfWidth && evX < markerPoint.x + infoWindowHalfWidth
			&& evY > markerPoint.y - infoWindowHeight - iconImageHeight && evY < markerPoint.y - iconImageHeight) {
			MotionEvent evCopy = MotionEvent.obtain(ev);
			evCopy.offsetLocation(-markerPoint.x + infoWindowHalfWidth,
								  -markerPoint.y + infoWindowHeight + iconImageHeight);

			int x = (int) evCopy.getX();
			int y = (int) evCopy.getY();

			Rect hitRect = new Rect();

			int count = clicksourceList.length;
			for (int i = 0; i < count; i++) {
				View v = clicksourceList[i];
				String tag = (String) v.getTag();
				if (v.getVisibility() == View.VISIBLE && tag != null) {
					v.getHitRect(hitRect);

					// The title and subtitle are the children of a relative layout which is the child of this.
					if (tag.equals(TiC.PROPERTY_TITLE) || tag.equals(TiC.PROPERTY_SUBTITLE)) {
						Rect textLayoutRect = new Rect();
						((ViewGroup) (v.getParent())).getHitRect(textLayoutRect);
						hitRect.offset(textLayoutRect.left, textLayoutRect.top);
					}

					if (hitRect.contains(x, y)) {
						setClickSource(tag);
						return;
					}
				}
			}
			setClickSource(null);
		}
	}

	public String getClicksource()
	{
		return currentClicksource;
	}

	private void setClickSource(String source)
	{
		currentClicksource = source;
	}
}
