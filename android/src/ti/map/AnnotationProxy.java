/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2013-2016 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ti.map;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.graphics.Bitmap;
import android.os.Message;
import android.util.Property;
import android.view.View;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.HashMap;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.AsyncResult;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiMessenger;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiBlob;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiDimension;
import org.appcelerator.titanium.TiPoint;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.view.TiDrawableReference;

@Kroll.proxy(creatableInModule = MapModule.class,
			 propertyAccessors = { TiC.PROPERTY_SUBTITLE, TiC.PROPERTY_SUBTITLEID, TiC.PROPERTY_TITLE,
								   TiC.PROPERTY_TITLEID, TiC.PROPERTY_LATITUDE, TiC.PROPERTY_LONGITUDE,
								   MapModule.PROPERTY_DRAGGABLE, TiC.PROPERTY_IMAGE, TiC.PROPERTY_PINCOLOR,
								   MapModule.PROPERTY_CUSTOM_VIEW, TiC.PROPERTY_LEFT_BUTTON, TiC.PROPERTY_LEFT_VIEW,
								   TiC.PROPERTY_RIGHT_BUTTON, TiC.PROPERTY_RIGHT_VIEW,
								   MapModule.PROPERTY_SHOW_INFO_WINDOW, MapModule.PROPERTY_CENTER_OFFSET,
								   MapModule.PROPERTY_HIDDEN, MapModule.PROPERTY_CLUSTER_IDENTIFIER })
public class AnnotationProxy extends KrollProxy
{
	public interface AnnotationDelegate {
		public void refreshAnnotation(AnnotationProxy annotation);
	}

	private static final String TAG = "AnnotationProxy";

	private MarkerOptions markerOptions;
	private TiMarker marker;
	private TiMapInfoWindow infoWindow = null;
	private static final String defaultIconImageHeight = "40dip"; //The height of the default marker icon
	private static final String defaultIconImageWidth = "36dip";  //The width of the default marker icon
	// The height of the marker icon in the unit of "px". Will use it to analyze the touch event to find out
	// the correct clicksource for the click event.
	private int iconImageHeight = 0;
	private int iconImageWidth = 0;
	private String subTitle;
	private String annoTitle;
	private AnnotationDelegate delegate = null;

	private static final int MSG_FIRST_ID = KrollProxy.MSG_LAST_ID + 1;

	private static final int MSG_SET_LON = MSG_FIRST_ID + 300;
	private static final int MSG_SET_LAT = MSG_FIRST_ID + 301;
	private static final int MSG_SET_DRAGGABLE = MSG_FIRST_ID + 302;
	private static final int MSG_UPDATE_INFO_WINDOW = MSG_FIRST_ID + 303;
	private static final int MSG_SET_HIDDEN = MSG_FIRST_ID + 304;
	private static final int MSG_SET_IMAGE = MSG_FIRST_ID + 305;

	public AnnotationProxy()
	{
		super();
		markerOptions = new MarkerOptions();
		subTitle = "";
		annoTitle = "";
		defaultValues.put(MapModule.PROPERTY_SHOW_INFO_WINDOW, true);
		defaultValues.put(MapModule.PROPERTY_CLUSTER_IDENTIFIER, null);
	}

	@Override
	public void release()
	{
		if (hasProperty(MapModule.PROPERTY_CUSTOM_VIEW)) {
			TiViewProxy customView = (TiViewProxy) getProperty(MapModule.PROPERTY_CUSTOM_VIEW);
			customView.release();
		}
		/*
		if (markerOptions != null) {
			markerOptions = null;
		}
		if (clusterMarker != null) {
			clusterMarker = null;
		}
		if (infoWindow != null) {
			infoWindow = null;
		}
		if (delegate != null) {
			delegate = null;
		}
		*/
		super.release();
	}

	public void releaseMarker()
	{
		if (marker != null) {
			marker = null;
		}
	}

	public void setDelegate(AnnotationDelegate delegate)
	{
		this.delegate = delegate;
	}

	@Override
	protected KrollDict getLangConversionTable()
	{
		KrollDict table = new KrollDict();
		table.put(TiC.PROPERTY_SUBTITLE, TiC.PROPERTY_SUBTITLEID);
		table.put(TiC.PROPERTY_TITLE, TiC.PROPERTY_TITLEID);
		return table;
	}

	public String getTitle()
	{
		return annoTitle;
	}

	public String getSubtitle()
	{
		return subTitle;
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		AsyncResult result = null;
		switch (msg.what) {

			case MSG_SET_LON: {
				result = (AsyncResult) msg.obj;
				setPosition(TiConvert.toDouble(getProperty(TiC.PROPERTY_LATITUDE)), (Double) result.getArg());
				result.setResult(null);
				return true;
			}

			case MSG_SET_LAT: {
				result = (AsyncResult) msg.obj;
				setPosition((Double) result.getArg(), TiConvert.toDouble(getProperty(TiC.PROPERTY_LONGITUDE)));
				result.setResult(null);
				return true;
			}

			case MSG_SET_HIDDEN: {
				result = (AsyncResult) msg.obj;
				Marker m = marker.getMarker();
				if (m != null) {
					m.setVisible(!(Boolean) result.getArg());
				}
				result.setResult(null);
				return true;
			}

			case MSG_SET_DRAGGABLE: {
				result = (AsyncResult) msg.obj;
				Marker m = marker.getMarker();
				if (m != null) {
					m.setDraggable((Boolean) result.getArg());
				}
				result.setResult(null);
				return true;
			}

			case MSG_UPDATE_INFO_WINDOW: {
				updateInfoWindow();
				return true;
			}

			case MSG_SET_IMAGE:
				result = (AsyncResult) msg.obj;
				Marker m = marker.getMarker();
				if (m != null) {
					updateImage(m, result.getArg());
				}
				result.setResult(null);
				return true;

			default: {
				return super.handleMessage(msg);
			}
		}
	}

	public void setPosition(double latitude, double longitude)
	{
		Marker m = marker.getMarker();
		if (m != null) {
			animateMarkerToPosition(m, new LatLng(latitude, longitude));
		}
	}

	private void animateMarkerToPosition(Marker marker, LatLng finalPosition)
	{
		TypeEvaluator<LatLng> typeEvaluator = (fraction, startValue, endValue) ->
		{
			double lat = (endValue.latitude - startValue.latitude) * fraction + startValue.latitude;
			double lng = (endValue.longitude - startValue.longitude) * fraction + startValue.longitude;
			return new LatLng(lat, lng);
		};
		Property<Marker, LatLng> property = Property.of(Marker.class, LatLng.class, "position");

		ObjectAnimator animator = ObjectAnimator.ofObject(marker, property, typeEvaluator, finalPosition);
		animator.setDuration(200);
		animator.start();
	}

	public void processOptions()
	{
		double longitude = 0;
		double latitude = 0;

		if (hasProperty(TiC.PROPERTY_LONGITUDE)) {
			longitude = TiConvert.toDouble(getProperty(TiC.PROPERTY_LONGITUDE));
		}
		if (hasProperty(TiC.PROPERTY_LATITUDE)) {
			latitude = TiConvert.toDouble(getProperty(TiC.PROPERTY_LATITUDE));
		}

		markerOptions.position(new LatLng(latitude, longitude));

		if (hasProperty(TiC.PROPERTY_LEFT_BUTTON) || hasProperty(TiC.PROPERTY_LEFT_VIEW)
			|| hasProperty(TiC.PROPERTY_RIGHT_BUTTON) || hasProperty(TiC.PROPERTY_RIGHT_VIEW)
			|| hasProperty(TiC.PROPERTY_TITLE) || hasProperty(TiC.PROPERTY_SUBTITLE)) {
			getOrCreateMapInfoWindow();
			Object leftButton = getProperty(TiC.PROPERTY_LEFT_BUTTON);
			Object leftView = getProperty(TiC.PROPERTY_LEFT_VIEW);
			Object rightButton = getProperty(TiC.PROPERTY_RIGHT_BUTTON);
			Object rightView = getProperty(TiC.PROPERTY_RIGHT_VIEW);
			if (leftButton != null) {
				infoWindow.setLeftOrRightPane(leftButton, TiMapInfoWindow.LEFT_PANE);
			} else {
				infoWindow.setLeftOrRightPane(leftView, TiMapInfoWindow.LEFT_PANE);
			}
			if (rightButton != null) {
				infoWindow.setLeftOrRightPane(rightButton, TiMapInfoWindow.RIGHT_PANE);
			} else {
				infoWindow.setLeftOrRightPane(rightView, TiMapInfoWindow.RIGHT_PANE);
			}
			if (hasProperty(TiC.PROPERTY_TITLE)) {
				String title = TiConvert.toString(getProperty(TiC.PROPERTY_TITLE));
				annoTitle = title;
				infoWindow.setTitle(title);
			} else {
				infoWindow.setTitle(null);
			}
			if (hasProperty(TiC.PROPERTY_SUBTITLE)) {
				infoWindow.setSubtitle(TiConvert.toString(getProperty(TiC.PROPERTY_SUBTITLE)));
				subTitle = TiConvert.toString(getProperty(TiC.PROPERTY_SUBTITLE));
			} else {
				infoWindow.setSubtitle(null);
			}
		}

		if (hasProperty(MapModule.PROPERTY_DRAGGABLE)) {
			markerOptions.draggable(TiConvert.toBoolean(getProperty(MapModule.PROPERTY_DRAGGABLE)));
		}

		if (hasProperty(MapModule.PROPERTY_HIDDEN)) {
			markerOptions.visible(!TiConvert.toBoolean(getProperty(MapModule.PROPERTY_HIDDEN)));
		}

		// customView, image and pincolor must be defined before adding to mapview. Once added, their values are final.
		if (hasProperty(MapModule.PROPERTY_CUSTOM_VIEW)) {
			handleCustomView(getProperty(MapModule.PROPERTY_CUSTOM_VIEW));
		} else if (hasProperty(TiC.PROPERTY_IMAGE)) {
			handleImage(getProperty(TiC.PROPERTY_IMAGE));
		} else if (hasProperty(TiC.PROPERTY_PINCOLOR)) {
			markerOptions.icon(
				BitmapDescriptorFactory.defaultMarker(TiConvert.toFloat(getProperty(TiC.PROPERTY_PINCOLOR))));
			setIconImageDimensions(-1, -1);
		} else {
			setIconImageDimensions(-1, -1);
		}

		if (hasProperty(MapModule.PROPERTY_CENTER_OFFSET)) {
			HashMap centerOffsetProperty = (HashMap) getProperty(MapModule.PROPERTY_CENTER_OFFSET);
			TiPoint centerOffset = new TiPoint(centerOffsetProperty, 0.0, 0.0);

			float offsetX = 0.5f - ((float) centerOffset.getX().getValue() / (float) iconImageWidth);
			float offsetY = 0.5f - ((float) centerOffset.getY().getValue() / (float) iconImageHeight);
			markerOptions.anchor(offsetX, offsetY);
		}
	}

	private void handleCustomView(Object obj)
	{
		if (obj instanceof TiViewProxy) {
			TiBlob imageBlob = ((TiViewProxy) obj).toImage();
			Bitmap image = imageBlob.getImage();
			if (image != null) {
				markerOptions.icon(BitmapDescriptorFactory.fromBitmap(image));
				setIconImageDimensions(image.getWidth(), image.getHeight());
			}
			return;
		}
		Log.w(TAG, "Unable to get the image from the custom view: " + obj);
		setIconImageDimensions(-1, -1);
	}

	private void handleImage(Object image)
	{
		// Image path
		if (image instanceof String) {
			TiDrawableReference imageref = TiDrawableReference.fromUrl(this, (String) image);
			Bitmap bitmap = imageref.getBitmap();
			if (bitmap != null) {
				try {
					markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
					setIconImageDimensions(bitmap.getWidth(), bitmap.getHeight());
				} catch (Exception e) {
				}
				return;
			}
		}

		// Image blob
		if (image instanceof TiBlob) {
			Bitmap bitmap = ((TiBlob) image).getImage();
			if (bitmap != null) {
				markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
				setIconImageDimensions(bitmap.getWidth(), bitmap.getHeight());
				return;
			}
		}

		Log.w(TAG, "Unable to get the image from the path: " + image);
		setIconImageDimensions(-1, -1);
	}

	public MarkerOptions getMarkerOptions()
	{
		return markerOptions;
	}

	public void updateImage(Marker m, Object value)
	{
		if (hasProperty(MapModule.PROPERTY_CUSTOM_VIEW))
		{
			// Custom view used. Update image not allowed
			return;
		}

		if (value == null)
		{
			m.setIcon(
				BitmapDescriptorFactory.defaultMarker(TiConvert.toFloat(getProperty(TiC.PROPERTY_PINCOLOR))));
			setIconImageDimensions(-1, -1);
			return;
		}
		
		// image not null has only effect if customView is null. Any other case, customView has more priority
		if (value != null)
		{
			// Image path
			if (value instanceof String) {
				TiDrawableReference imageref = TiDrawableReference.fromUrl(this, (String) value);
				Bitmap bitmap = imageref.getBitmap();
				if (bitmap != null) {
					try {
						if (m != null)
						{
							m.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
							setIconImageDimensions(bitmap.getWidth(), bitmap.getHeight());
						}
					} catch (Exception e) {
					}
					return;
				}
			}

			// Image blob
			if (value instanceof TiBlob) {
				Bitmap bitmap = ((TiBlob) value).getImage();
				if (bitmap != null) {
					if (m != null)
					{
						m.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
						setIconImageDimensions(bitmap.getWidth(), bitmap.getHeight());
					}
					return;
				}
			}
		}
	}

	public void setTiMarker(TiMarker m)
	{
		marker = m;
	}

	public TiMarker getTiMarker()
	{
		return marker;
	}

	public void showInfo()
	{
		if (marker == null) {
			return;
		}
		Marker m = marker.getMarker();
		if (m != null) {
			m.showInfoWindow();
		}
	}

	public void hideInfo()
	{
		if (marker == null) {
			return;
		}
		Marker m = marker.getMarker();
		if (m != null) {
			m.hideInfoWindow();
		}
	}

	public TiMapInfoWindow getMapInfoWindow()
	{
		return infoWindow;
	}

	private void setIconImageDimensions(int w, int h)
	{
		if (w >= 0 && h >= 0) {
			iconImageWidth = w;
			iconImageHeight = h;
		} else { // default maker icon
			TiDimension widthDimension = new TiDimension(defaultIconImageWidth, TiDimension.TYPE_UNDEFINED);
			TiDimension heightDimension = new TiDimension(defaultIconImageHeight, TiDimension.TYPE_UNDEFINED);
			// TiDimension needs a view to grab the window manager, so we'll just use the decorview of the current window
			View view = TiApplication.getAppCurrentActivity().getWindow().getDecorView();
			iconImageWidth = widthDimension.getAsPixels(view);
			iconImageHeight = heightDimension.getAsPixels(view);
		}
	}

	public int getIconImageHeight()
	{
		return iconImageHeight;
	}

	@Override
	public boolean hasProperty(String name)
	{
		return (super.getProperty(name) != null);
	}

	@Override
	public void onPropertyChanged(String name, Object value)
	{
		super.onPropertyChanged(name, value);

		if (marker == null || value == null) {
			return;
		}

		if (name.equals(TiC.PROPERTY_LONGITUDE)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_LON), TiConvert.toDouble(value));
		} else if (name.equals(TiC.PROPERTY_LATITUDE)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_LAT), TiConvert.toDouble(value));
		} else if (name.equals(TiC.PROPERTY_TITLE)) {
			String title = TiConvert.toString(value);
			annoTitle = title;
			getOrCreateMapInfoWindow().setTitle(title);
			updateInfoWindow();
		} else if (name.equals(TiC.PROPERTY_SUBTITLE)) {
			getOrCreateMapInfoWindow().setSubtitle(TiConvert.toString(value));
			subTitle = TiConvert.toString(value);
			updateInfoWindow();
		} else if (name.equals(TiC.PROPERTY_LEFT_BUTTON)) {
			getOrCreateMapInfoWindow().setLeftOrRightPane(value, TiMapInfoWindow.LEFT_PANE);
			updateInfoWindow();
		} else if (name.equals(TiC.PROPERTY_LEFT_VIEW) && getProperty(TiC.PROPERTY_LEFT_BUTTON) == null) {
			getOrCreateMapInfoWindow().setLeftOrRightPane(value, TiMapInfoWindow.LEFT_PANE);
			updateInfoWindow();
		} else if (name.equals(TiC.PROPERTY_RIGHT_BUTTON)) {
			getOrCreateMapInfoWindow().setLeftOrRightPane(value, TiMapInfoWindow.RIGHT_PANE);
			updateInfoWindow();
		} else if (name.equals(TiC.PROPERTY_RIGHT_VIEW) && getProperty(TiC.PROPERTY_RIGHT_BUTTON) == null) {
			getOrCreateMapInfoWindow().setLeftOrRightPane(value, TiMapInfoWindow.RIGHT_PANE);
			updateInfoWindow();
		} else if (name.equals(MapModule.PROPERTY_DRAGGABLE)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_DRAGGABLE),
												TiConvert.toBoolean(value));
		} else if (name.equals(TiC.PROPERTY_PINCOLOR)) {
			requestRefresh();
		} else if (name.equals(MapModule.PROPERTY_HIDDEN)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_HIDDEN),
												TiConvert.toBoolean(value));
		} else if (name.equals(TiC.PROPERTY_IMAGE)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_IMAGE),
												value);
		}
	}

	private TiMapInfoWindow getOrCreateMapInfoWindow()
	{
		if (infoWindow == null) {
			infoWindow = new TiMapInfoWindow(TiApplication.getInstance().getApplicationContext(), this);
		}
		return infoWindow;
	}

	private void updateInfoWindow()
	{
		if (marker == null) {
			return;
		}
		if (TiApplication.isUIThread()) {
			Marker m = marker.getMarker();
			if (m != null && m.isInfoWindowShown()) {
				m.showInfoWindow();
			}
		} else {
			getMainHandler().sendEmptyMessage(MSG_UPDATE_INFO_WINDOW);
		}
	}

	private void requestRefresh()
	{
		if (this.delegate != null) {
			this.delegate.refreshAnnotation(this);
		}
	}

	public String getApiName()
	{
		return "Ti.Map.Annotation";
	}
}
