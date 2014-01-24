/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

package ti.map;

import java.util.ArrayList;
import java.util.HashMap;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiBlob;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.view.TiUIFragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;

public class TiUIMapView extends TiUIFragment implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener,
	GoogleMap.OnCameraChangeListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.InfoWindowAdapter,
	GoogleMap.OnMapLongClickListener, GoogleMap.OnMapLoadedCallback
{
	private static final String TAG = "TiUIMapView";
	private GoogleMap map;
	protected boolean animate = false;
	protected boolean preLayout = true;
	protected LatLngBounds preLayoutUpdateBounds;
	protected ArrayList<TiMarker> timarkers;
	protected AnnotationProxy selectedAnnotation;

	public TiUIMapView(final TiViewProxy proxy, Activity activity)
	{
		super(proxy, activity);
		timarkers = new ArrayList<TiMarker>();
	}

	/**
	 * Traverses through the view hierarchy to locate the SurfaceView and set the background to transparent.
	 * @param v the root view
	 */
	private void setBackgroundTransparent(View v) {
	    if (v instanceof SurfaceView) {
	        SurfaceView sv = (SurfaceView) v;
	        sv.setBackgroundColor(Color.TRANSPARENT);
	    }

	    if (v instanceof ViewGroup) {
	        ViewGroup viewGroup = (ViewGroup) v;
	        for (int i = 0; i < viewGroup.getChildCount(); i++) {
	            setBackgroundTransparent(viewGroup.getChildAt(i));
	        }
	    }
	}

	@Override
	protected Fragment createFragment()
	{
		if (proxy == null) {
			return SupportMapFragment.newInstance();
		} else {
			boolean zOrderOnTop = TiConvert.toBoolean(proxy.getProperty(MapModule.PROPERTY_ZORDER_ON_TOP), false);
			GoogleMapOptions gOptions = new GoogleMapOptions();
			gOptions.zOrderOnTop(zOrderOnTop);
			return SupportMapFragment.newInstance(gOptions);
		}
	}

	protected void processPreloadRoutes()
	{
		ArrayList<RouteProxy> routes = ((ViewProxy) proxy).getPreloadRoutes();
		for (int i = 0; i < routes.size(); i++) {
			addRoute(routes.get(i));
		}
	}

	protected void onViewCreated()
	{
		map = acquireMap();
		//A workaround for https://code.google.com/p/android/issues/detail?id=11676 pre Jelly Bean.
		//This problem doesn't exist on 4.1+ since the map base view changes to TextureView from SurfaceView. 
		if (Build.VERSION.SDK_INT < 16) {
			View rootView = proxy.getActivity().findViewById(android.R.id.content);
			setBackgroundTransparent(rootView);
		}
		processMapProperties(proxy.getProperties());
		processPreloadRoutes();
		map.setOnMarkerClickListener(this);
		map.setOnMapClickListener(this);
		map.setOnCameraChangeListener(this);
		map.setOnMarkerDragListener(this);
		map.setOnInfoWindowClickListener(this);
		map.setInfoWindowAdapter(this);
		map.setOnMapLongClickListener(this);
		map.setOnMapLoadedCallback(this);
		((ViewProxy) proxy).clearPreloadObjects();
	}

	@Override
	public void processProperties(KrollDict d)
	{
		super.processProperties(d);

		if (acquireMap() == null) {
			return;
		}
		processMapProperties(d);
	}

	public void processMapProperties(KrollDict d)
	{
		if (d.containsKey(TiC.PROPERTY_USER_LOCATION)) {
			setUserLocationEnabled(TiConvert.toBoolean(d, TiC.PROPERTY_USER_LOCATION, false));
		}
		if (d.containsKey(MapModule.PROPERTY_USER_LOCATION_BUTTON)) {
			setUserLocationButtonEnabled(TiConvert.toBoolean(d, MapModule.PROPERTY_USER_LOCATION_BUTTON, true));
		}
		if (d.containsKey(TiC.PROPERTY_MAP_TYPE)) {
			setMapType(d.getInt(TiC.PROPERTY_MAP_TYPE));
		}
		if (d.containsKey(MapModule.PROPERTY_TRAFFIC)) {
			setTrafficEnabled(d.getBoolean(MapModule.PROPERTY_TRAFFIC));
		}
		if (d.containsKey(TiC.PROPERTY_ANIMATE)) {
			animate = d.getBoolean(TiC.PROPERTY_ANIMATE);
		}
		if (d.containsKey(TiC.PROPERTY_REGION)) {
			updateCamera(d.getKrollDict(TiC.PROPERTY_REGION));
		}
		if (d.containsKey(TiC.PROPERTY_ANNOTATIONS)) {
			Object[] annotations = (Object[]) d.get(TiC.PROPERTY_ANNOTATIONS);
			addAnnotations(annotations);
		}
		if (d.containsKey(TiC.PROPERTY_ENABLE_ZOOM_CONTROLS)) {
			setZoomControlsEnabled(TiConvert.toBoolean(d, TiC.PROPERTY_ENABLE_ZOOM_CONTROLS, true));
		}
		if (d.containsKey(MapModule.PROPERTY_COMPASS_ENABLED)) {
			setCompassEnabled(TiConvert.toBoolean(d, MapModule.PROPERTY_COMPASS_ENABLED, true));
		}
	}

	@Override
	public void propertyChanged(String key, Object oldValue, Object newValue, KrollProxy proxy)
	{

		if (key.equals(TiC.PROPERTY_USER_LOCATION)) {
			setUserLocationEnabled(TiConvert.toBoolean(newValue));
		} else if (key.equals(MapModule.PROPERTY_USER_LOCATION_BUTTON)) {
			setUserLocationButtonEnabled(TiConvert.toBoolean(newValue));
		} else if (key.equals(TiC.PROPERTY_MAP_TYPE)) {
			setMapType(TiConvert.toInt(newValue));
		} else if (key.equals(TiC.PROPERTY_REGION)) {
			updateCamera((HashMap) newValue);
		} else if (key.equals(MapModule.PROPERTY_TRAFFIC)) {
			setTrafficEnabled(TiConvert.toBoolean(newValue));
		} else if (key.equals(TiC.PROPERTY_ANIMATE)) {
			animate = TiConvert.toBoolean(newValue);
		} else if (key.equals(TiC.PROPERTY_ANNOTATIONS)) {
			updateAnnotations((Object[]) newValue);
		} else if (key.equals(MapModule.PROPERTY_COMPASS_ENABLED)) {
			setCompassEnabled(TiConvert.toBoolean(newValue, true));
		} else if (key.equals(TiC.PROPERTY_ENABLE_ZOOM_CONTROLS)) {
			setZoomControlsEnabled(TiConvert.toBoolean(newValue, true));
		} else {
			super.propertyChanged(key, oldValue, newValue, proxy);
		}
	}

	public GoogleMap acquireMap()
	{
		return ((SupportMapFragment) getFragment()).getMap();
	}

	public GoogleMap getMap()
	{
		return map;
	}

	protected void setUserLocationEnabled(boolean enabled)
	{
		map.setMyLocationEnabled(enabled);
	}
	
	protected void setCompassEnabled(boolean enabled) 
	{
		map.getUiSettings().setCompassEnabled(enabled);
	}

	protected void setUserLocationButtonEnabled(boolean enabled)
	{
		map.getUiSettings().setMyLocationButtonEnabled(enabled);
	}

	public float getMaxZoomLevel() 
	{
		return map.getMaxZoomLevel();
	}
		
	public float getMinZoomLevel() 
	{
		return map.getMinZoomLevel();
	}

	protected void setMapType(int type)
	{
		map.setMapType(type);
	}

	protected void setTrafficEnabled(boolean enabled)
	{
		map.setTrafficEnabled(enabled);
	}

	protected void setZoomControlsEnabled(boolean enabled)
	{
		map.getUiSettings().setZoomControlsEnabled(enabled);
	}

	public void updateCamera(HashMap<String, Object> dict)
	{
		double longitude = 0;
		double longitudeDelta = 0;
		double latitude = 0;
		double latitudeDelta = 0;
		float bearing = 0;
		float tilt = 0;
		float zoom = 0;

		// In the setLocation() method, the old map module allows the user to provide two more properties - "animate" and "regionFit".
		// In this map module, no matter "regionFit" is set to true or false, we will always make sure the specified 
		// latitudeDelta / longitudeDelta bounds are centered on screen at the greatest possible zoom level.
		boolean anim = animate;
		if (dict.containsKey(TiC.PROPERTY_ANIMATE)) {
			anim = TiConvert.toBoolean(dict, TiC.PROPERTY_ANIMATE);
		}
		if (dict.containsKey(MapModule.PROPERTY_BEARING)) {
			bearing = TiConvert.toFloat(dict, MapModule.PROPERTY_BEARING);
		}
		if (dict.containsKey(MapModule.PROPERTY_TILT)) {
			tilt = TiConvert.toFloat(dict, MapModule.PROPERTY_TILT);
		}
		if (dict.containsKey(MapModule.PROPERTY_ZOOM)) {
			zoom = TiConvert.toFloat(dict, MapModule.PROPERTY_ZOOM);
		}
		if (dict.containsKey(TiC.PROPERTY_LATITUDE)) {
			latitude = TiConvert.toDouble(dict, TiC.PROPERTY_LATITUDE);
		}
		if (dict.containsKey(TiC.PROPERTY_LONGITUDE)) {
			longitude = TiConvert.toDouble(dict, TiC.PROPERTY_LONGITUDE);
		}

		CameraPosition.Builder cameraBuilder = new CameraPosition.Builder();
		LatLng location = new LatLng(latitude, longitude);
		cameraBuilder.target(location);
		cameraBuilder.bearing(bearing);
		cameraBuilder.tilt(tilt);
		cameraBuilder.zoom(zoom);

		if (dict.containsKey(TiC.PROPERTY_LATITUDE_DELTA)) {
			latitudeDelta = TiConvert.toDouble(dict, TiC.PROPERTY_LATITUDE_DELTA);
		}

		if (dict.containsKey(TiC.PROPERTY_LONGITUDE_DELTA)) {
			longitudeDelta = TiConvert.toDouble(dict, TiC.PROPERTY_LONGITUDE_DELTA);
		}

		if (latitudeDelta != 0 && longitudeDelta != 0) {
			LatLng northeast = new LatLng(latitude + (latitudeDelta / 2.0), longitude + (longitudeDelta / 2.0));
			LatLng southwest = new LatLng(latitude - (latitudeDelta / 2.0), longitude - (longitudeDelta / 2.0));

			final LatLngBounds bounds = new LatLngBounds(southwest, northeast);
			if (preLayout) {
				preLayoutUpdateBounds = bounds;
				return;
			} else {
				moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0), anim);
				return;
			}
		}

		CameraPosition position = cameraBuilder.build();
		CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(position);
		moveCamera(camUpdate, anim);
	}

	protected void moveCamera(CameraUpdate camUpdate, boolean anim)
	{
		if (anim) {
			map.animateCamera(camUpdate);
		} else {
			map.moveCamera(camUpdate);
		}
	}

	protected void addAnnotation(AnnotationProxy annotation)
	{
		// if annotation already on map, remove it first then re-add it
		TiMarker tiMarker = annotation.getTiMarker();
		if (tiMarker != null) {
			removeAnnotation(tiMarker);
		}
		annotation.processOptions();
		// add annotation to map view
		Marker marker = map.addMarker(annotation.getMarkerOptions());
		tiMarker = new TiMarker(marker, annotation);
		annotation.setTiMarker(tiMarker);
		timarkers.add(tiMarker);
	}

	protected void addAnnotations(Object[] annotations)
	{
		for (int i = 0; i < annotations.length; i++) {
			Object obj = annotations[i];
			if (obj instanceof AnnotationProxy) {
				AnnotationProxy annotation = (AnnotationProxy) obj;
				addAnnotation(annotation);
			}
		}
	}

	protected void updateAnnotations(Object[] annotations)
	{
		// First, remove old annotations from map
		removeAllAnnotations();
		// Then we add new annotations to the map
		addAnnotations(annotations);
	}

	protected void removeAllAnnotations()
	{
		for (int i = 0; i < timarkers.size(); i++) {
			TiMarker timarker = timarkers.get(i);
			timarker.getMarker().remove();
			AnnotationProxy proxy = timarker.getProxy();
			if (proxy != null) {
				proxy.setTiMarker(null);
			}
		}
		timarkers.clear();
	}

	public TiMarker findMarkerByTitle(String title)
	{
		for (int i = 0; i < timarkers.size(); i++) {
			TiMarker timarker = timarkers.get(i);
			AnnotationProxy annoProxy = timarker.getProxy();
			if (annoProxy != null && annoProxy.getTitle().equals(title)) {
				return timarker;
			}
		}
		return null;
	}

	protected void removeAnnotation(Object annotation)
	{
		TiMarker timarker = null;
		if (annotation instanceof TiMarker) {
			timarker = (TiMarker) annotation;
		} else if (annotation instanceof AnnotationProxy) {
			timarker = ((AnnotationProxy) annotation).getTiMarker();
		} else if (annotation instanceof String) {
			timarker = findMarkerByTitle((String) annotation);
		}

		if (timarker != null && timarkers.remove(timarker)) {
			timarker.getMarker().remove();
			AnnotationProxy proxy = timarker.getProxy();
			if (proxy != null) {
				proxy.setTiMarker(null);
			}
		}
	}

	protected void selectAnnotation(Object annotation)
	{
		if (annotation instanceof AnnotationProxy) {
			AnnotationProxy proxy = (AnnotationProxy) annotation;
			if (proxy.getTiMarker() != null) {
				proxy.showInfo();
				selectedAnnotation = proxy;
			}
		} else if (annotation instanceof String) {
			String title = (String) annotation;
			TiMarker marker = findMarkerByTitle(title);
			if (marker != null) {
				marker.getMarker().showInfoWindow();
				selectedAnnotation = marker.getProxy();
			}
		}
	}

	protected void deselectAnnotation(Object annotation)
	{
		if (annotation instanceof AnnotationProxy) {
			AnnotationProxy proxy = (AnnotationProxy) annotation;
			if (proxy.getTiMarker() != null) {
				((AnnotationProxy) annotation).hideInfo();
			}
		} else if (annotation instanceof String) {
			String title = (String) annotation;
			TiMarker marker = findMarkerByTitle(title);
			if (marker != null) {
				marker.getMarker().hideInfoWindow();
			}
		}
		selectedAnnotation = null;
	}

	private AnnotationProxy getProxyByMarker(Marker m)
	{
		if (m != null) {
			for (int i = 0; i < timarkers.size(); i++) {
				TiMarker timarker = timarkers.get(i);
				if (m.equals(timarker.getMarker())) {
					return timarker.getProxy();
				}
			}
		}
		return null;
	}

	public void addRoute(RouteProxy r)
	{
		// check if route already added.
		if (r.getRoute() != null) {
			return;
		}

		r.processOptions();
		r.setRoute(map.addPolyline(r.getOptions()));
	}

	public void removeRoute(RouteProxy r)
	{
		if (r.getRoute() == null) {
			return;
		}

		r.getRoute().remove();
		r.setRoute(null);
	}

	public void changeZoomLevel(int delta)
	{
		CameraUpdate camUpdate = CameraUpdateFactory.zoomBy(delta);
		moveCamera(camUpdate, animate);
	}

	public void fireClickEvent(Marker marker, AnnotationProxy annoProxy, String clickSource)
	{
		KrollDict d = new KrollDict();
		String title = null;
		String subtitle = null;
		TiMapInfoWindow infoWindow = annoProxy.getMapInfoWindow();
		if (infoWindow != null) {
			title = infoWindow.getTitle();
			subtitle = infoWindow.getSubtitle();
		}
		d.put(TiC.PROPERTY_TITLE, title);
		d.put(TiC.PROPERTY_SUBTITLE, subtitle);
		d.put(TiC.PROPERTY_LATITUDE, marker.getPosition().latitude);
		d.put(TiC.PROPERTY_LONGITUDE, marker.getPosition().longitude);
		d.put(TiC.PROPERTY_ANNOTATION, annoProxy);
		d.put(MapModule.PROPERTY_MAP, proxy);
		d.put(TiC.PROPERTY_TYPE, TiC.EVENT_CLICK);
		d.put(TiC.PROPERTY_SOURCE, proxy);
		d.put(TiC.EVENT_PROPERTY_CLICKSOURCE, clickSource);
		proxy.fireEvent(TiC.EVENT_CLICK, d);
	}
	
	public void fireLongClickEvent(LatLng point)
	{
		KrollDict d = new KrollDict();
		d.put(TiC.PROPERTY_LATITUDE, point.latitude);
		d.put(TiC.PROPERTY_LONGITUDE, point.longitude);
		d.put(MapModule.PROPERTY_MAP, proxy);
		d.put(TiC.PROPERTY_TYPE, TiC.EVENT_LONGCLICK);
		d.put(TiC.PROPERTY_SOURCE, proxy);
		proxy.fireEvent(TiC.EVENT_LONGCLICK, d);
	}

	public void firePinChangeDragStateEvent(Marker marker, AnnotationProxy annoProxy, int dragState)
	{
		KrollDict d = new KrollDict();
		String title = null;
		TiMapInfoWindow infoWindow = annoProxy.getMapInfoWindow();
		if (infoWindow != null) {
			title = infoWindow.getTitle();
		}
		d.put(TiC.PROPERTY_TITLE, title);
		d.put(TiC.PROPERTY_ANNOTATION, annoProxy);
		d.put(MapModule.PROPERTY_MAP, proxy);
		d.put(TiC.PROPERTY_SOURCE, proxy);
		d.put(MapModule.PROPERTY_NEWSTATE, dragState);
		d.put(TiC.PROPERTY_TYPE, MapModule.EVENT_PIN_CHANGE_DRAG_STATE);
		proxy.fireEvent(MapModule.EVENT_PIN_CHANGE_DRAG_STATE, d);
	}

	@Override
	public boolean onMarkerClick(Marker marker)
	{
		AnnotationProxy annoProxy = getProxyByMarker(marker);
		if (annoProxy == null) {
			Log.e(TAG, "Marker can not be found, click event won't fired.", Log.DEBUG_MODE);
			return false;
		} else if (selectedAnnotation != null && selectedAnnotation.equals(annoProxy)) {
			selectedAnnotation.hideInfo();
			selectedAnnotation = null;
			fireClickEvent(marker, annoProxy, MapModule.PROPERTY_PIN);
			return true;
		}
		fireClickEvent(marker, annoProxy, MapModule.PROPERTY_PIN);
		selectedAnnotation = annoProxy;
		boolean showInfoWindow = TiConvert.toBoolean(annoProxy.getProperty(MapModule.PROPERTY_SHOW_INFO_WINDOW), true);
		//Returning false here will enable native behavior, which shows the info window.
		if (showInfoWindow) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void onMapClick(LatLng point)
	{
		if (selectedAnnotation != null) {
			TiMarker tiMarker = selectedAnnotation.getTiMarker();
			if (tiMarker != null) {
				fireClickEvent(tiMarker.getMarker(), selectedAnnotation, null);
			}
			selectedAnnotation = null;
		}

	}
	
	@Override
	public void onMapLongClick(LatLng point)
	{
		fireLongClickEvent(point);
	}

	@Override
	public void onMarkerDrag(Marker marker)
	{
		Log.d(TAG, "The annotation is dragged.", Log.DEBUG_MODE);
	}

	@Override
	public void onMarkerDragEnd(Marker marker)
	{
		AnnotationProxy annoProxy = getProxyByMarker(marker);
		if (annoProxy != null) {
			LatLng position = marker.getPosition();
			annoProxy.setProperty(TiC.PROPERTY_LONGITUDE, position.longitude);
			annoProxy.setProperty(TiC.PROPERTY_LATITUDE, position.latitude);
			firePinChangeDragStateEvent(marker, annoProxy, MapModule.ANNOTATION_DRAG_STATE_END);
		}
	}

	@Override
	public void onMarkerDragStart(Marker marker)
	{
		AnnotationProxy annoProxy = getProxyByMarker(marker);
		if (annoProxy != null) {
			firePinChangeDragStateEvent(marker, annoProxy, MapModule.ANNOTATION_DRAG_STATE_START);
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker)
	{
		AnnotationProxy annoProxy = getProxyByMarker(marker);
		if (annoProxy != null) {
			String clicksource = annoProxy.getMapInfoWindow().getClicksource();
			// The clicksource is null means the click event is not inside "leftPane", "title", "subtible"
			// or "rightPane". In this case, use "infoWindow" as the clicksource.
			if (clicksource == null) {
				clicksource = MapModule.PROPERTY_INFO_WINDOW;
			}
			fireClickEvent(marker, annoProxy, clicksource);
		}
	}

	@Override
	public View getInfoContents(Marker marker)
	{
		AnnotationProxy annoProxy = getProxyByMarker(marker);
		if (annoProxy != null) {
			return annoProxy.getMapInfoWindow();
		}
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker)
	{
		return null;
	}

	@Override
	public void release()
	{
		selectedAnnotation = null;
		map.clear();
		map = null;
		timarkers.clear();
		super.release();
	}

	@Override
	public void onCameraChange(CameraPosition position)
	{
		if (preLayout) {
			if (preLayoutUpdateBounds != null) {
				moveCamera(CameraUpdateFactory.newLatLngBounds(preLayoutUpdateBounds, 0), animate);
				preLayoutUpdateBounds = null;
			} else {
				// moveCamera will trigger another callback, so we do this to make sure
				// we don't fire event when region is set initially
				preLayout = false;
			}
		} else if (proxy != null) {
			KrollDict d = new KrollDict();
			d.put(TiC.PROPERTY_LATITUDE, position.target.latitude);
			d.put(TiC.PROPERTY_LONGITUDE, position.target.longitude);
			d.put(TiC.PROPERTY_SOURCE, proxy);
			LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
			d.put(TiC.PROPERTY_LATITUDE_DELTA, (bounds.northeast.latitude - bounds.southwest.latitude));
			d.put(TiC.PROPERTY_LONGITUDE_DELTA, (bounds.northeast.longitude - bounds.southwest.longitude));
			proxy.fireEvent(TiC.EVENT_REGION_CHANGED, d);
		}

	}

	// Intercept the touch event to find out the correct clicksource if clicking on the info window.
	@Override
	protected boolean interceptTouchEvent(MotionEvent ev)
	{
		if (ev.getAction() == MotionEvent.ACTION_UP && selectedAnnotation != null) {
			TiMapInfoWindow infoWindow = selectedAnnotation.getMapInfoWindow();
			TiMarker timarker = selectedAnnotation.getTiMarker();
			if (infoWindow != null && timarker != null) {
				Marker marker = timarker.getMarker();
				if (marker != null && marker.isInfoWindowShown()) {
					Point markerPoint = map.getProjection().toScreenLocation(marker.getPosition());
					infoWindow.analyzeTouchEvent( ev, markerPoint, selectedAnnotation.getIconImageHeight());
				}
			}
		}
		return false;
	}
	
	public void snapshot() 
	{
		map.snapshot(new GoogleMap.SnapshotReadyCallback()
		{
			
			@Override
			public void onSnapshotReady(Bitmap snapshot)
			{
				TiBlob sblob = TiBlob.blobFromImage(snapshot);
				KrollDict data = new KrollDict();
				data.put("snapshot", sblob);
				data.put("source", proxy);
				proxy.fireEvent(MapModule.EVENT_ON_SNAPSHOT_READY, data);
			}
		});
	}

	@Override
	public void onMapLoaded()
	{
		proxy.fireEvent(TiC.EVENT_COMPLETE, null);
	}
}
