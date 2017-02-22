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

import ti.map.Shape.Boundary;
import ti.map.Shape.IShape;
import ti.map.Shape.PolylineBoundary;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.content.res.Resources;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MapStyleOptions;



public class TiUIMapView extends TiUIFragment implements GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener,
	GoogleMap.OnCameraChangeListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.InfoWindowAdapter,
	GoogleMap.OnMapLongClickListener, GoogleMap.OnMapLoadedCallback, OnMapReadyCallback, 
	GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraMoveCanceledListener, GoogleMap.OnCameraIdleListener  
{

	private static final String TAG = "TiUIMapView";
	private GoogleMap map;
	protected boolean animate = false;
	protected boolean preLayout = true;
	protected LatLngBounds preLayoutUpdateBounds;
	protected ArrayList<TiMarker> timarkers;
	protected AnnotationProxy selectedAnnotation;

	private ArrayList<CircleProxy> currentCircles;
	private ArrayList<PolygonProxy> currentPolygons;
	private ArrayList<PolylineProxy> currentPolylines;

	public TiUIMapView(final TiViewProxy proxy, Activity activity) {
		super(proxy, activity);
		timarkers = new ArrayList<TiMarker>();
		currentCircles = new ArrayList<CircleProxy>();
		currentPolygons = new ArrayList<PolygonProxy>();
		currentPolylines = new ArrayList<PolylineProxy>();
	}

	/**
	 * Traverses through the view hierarchy to locate the SurfaceView and set
	 * the background to transparent.
	 *
	 * @param v
	 *            the root view
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
	protected Fragment createFragment() {
		if (proxy == null) {
			Fragment map = SupportMapFragment.newInstance();
			if (map instanceof SupportMapFragment) {
				((SupportMapFragment)map).getMapAsync(this);
			}
			return map;
		} else {
			boolean zOrderOnTop = TiConvert.toBoolean(
				proxy.getProperty(MapModule.PROPERTY_ZORDER_ON_TOP), false);
			GoogleMapOptions gOptions = new GoogleMapOptions();
			gOptions.zOrderOnTop(zOrderOnTop);
			Fragment map = SupportMapFragment.newInstance(gOptions);
			if (map instanceof SupportMapFragment) {
				((SupportMapFragment)map).getMapAsync(this);
			}
			return map;
		}
	}

	protected void processPreloadRoutes() {
		ArrayList<RouteProxy> routes = ((ViewProxy) proxy).getPreloadRoutes();
		for (int i = 0; i < routes.size(); i++) {
			addRoute(routes.get(i));
		}
	}

	protected void processPreloadCircles()

	{
		ArrayList<CircleProxy> circles = ((ViewProxy) proxy)
				.getPreloadCircles();
		for (int i = 0; i < circles.size(); i++) {
			addCircle(circles.get(i));
		}
	}

	protected void processPreloadPolygons() {
		ArrayList<PolygonProxy> polygons = ((ViewProxy) proxy)
				.getPreloadPolygons();
		for (int i = 0; i < polygons.size(); i++) {
			addPolygon(polygons.get(i));
		}
	}

	protected void processPreloadPolylines() {
		ArrayList<PolylineProxy> polylines = ((ViewProxy) proxy)
				.getPreloadPolylines();
		for (int i = 0; i < polylines.size(); i++) {
			addPolyline(polylines.get(i));
		}
	}

	@Override
	public void onMapReady(GoogleMap gMap) {
		map = gMap;

		//A workaround for https://code.google.com/p/android/issues/detail?id=11676 pre Jelly Bean.
		//This problem doesn't exist on 4.1+ since the map base view changes to TextureView from SurfaceView. 
		if (Build.VERSION.SDK_INT < 16) {
			View rootView = proxy.getActivity().findViewById(
					android.R.id.content);
			setBackgroundTransparent(rootView);
		}
		processMapProperties(proxy.getProperties());
		processPreloadRoutes();
		processPreloadPolygons();
		processPreloadCircles();
		processPreloadPolylines();
		map.setOnMarkerClickListener(this);
		map.setOnMapClickListener(this);
		map.setOnCameraChangeListener(this);
		map.setOnCameraIdleListener(this);
		map.setOnCameraMoveStartedListener(this);
		map.setOnCameraMoveListener(this);
		map.setOnCameraMoveCanceledListener(this);
		map.setOnMarkerDragListener(this);
		map.setOnInfoWindowClickListener(this);
		map.setInfoWindowAdapter(this);
		map.setOnMapLongClickListener(this);
		map.setOnMapLoadedCallback(this);

		((ViewProxy) proxy).clearPreloadObjects();
	}

	@Override
	public void processProperties(KrollDict d) {
		super.processProperties(d);

		if (map == null) {
			return;
		}
		processMapProperties(d);
	}

	public void processMapProperties(KrollDict d) {
		if (d.containsKey(TiC.PROPERTY_USER_LOCATION)) {
			setUserLocationEnabled(TiConvert.toBoolean(d,
					TiC.PROPERTY_USER_LOCATION, false));
		}
		if (d.containsKey(MapModule.PROPERTY_USER_LOCATION_BUTTON)) {
			setUserLocationButtonEnabled(TiConvert.toBoolean(d,
					MapModule.PROPERTY_USER_LOCATION_BUTTON, true));
		}
		if (d.containsKey(MapModule.PROPERTY_MAP_TOOLBAR_ENABLED)) {
			setMapToolbarEnabled(TiConvert.toBoolean(d,
					MapModule.PROPERTY_MAP_TOOLBAR_ENABLED, true));
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

		if (d.containsKey(MapModule.PROPERTY_POLYGONS)) {
			Object[] polygons = (Object[]) d.get(MapModule.PROPERTY_POLYGONS);
			addPolygons(polygons);
		}

		if (d.containsKey(MapModule.PROPERTY_POLYLINES)) {
			Object[] polylines = (Object[]) d.get(MapModule.PROPERTY_POLYLINES);
			addPolylines(polylines);
		}

		if (d.containsKey(MapModule.PROPERTY_CIRCLES)) {
			Object[] circles = (Object[]) d.get(MapModule.PROPERTY_CIRCLES);
			addCircles(circles);
		}

		if (d.containsKey(TiC.PROPERTY_ENABLE_ZOOM_CONTROLS)) {
			setZoomControlsEnabled(TiConvert.toBoolean(d,
					TiC.PROPERTY_ENABLE_ZOOM_CONTROLS, true));
		}
		if (d.containsKey(MapModule.PROPERTY_COMPASS_ENABLED)) {
			setCompassEnabled(TiConvert.toBoolean(d,
					MapModule.PROPERTY_COMPASS_ENABLED, true));
		}
		if (d.containsKey(TiC.PROPERTY_STYLE)) {
			setStyle(d.getString(TiC.PROPERTY_STYLE));
		}
	}

	@Override
	public void propertyChanged(String key, Object oldValue, Object newValue,
			KrollProxy proxy) {

		if (newValue == null) {
			return;
		}

		if (key.equals(TiC.PROPERTY_USER_LOCATION)) {
			setUserLocationEnabled(TiConvert.toBoolean(newValue));
		} else if (key.equals(MapModule.PROPERTY_USER_LOCATION_BUTTON)) {
			setUserLocationButtonEnabled(TiConvert.toBoolean(newValue));
		} else if (key.equals(MapModule.PROPERTY_MAP_TOOLBAR_ENABLED)) {
			setMapToolbarEnabled(TiConvert.toBoolean(newValue));
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
		} else if (key.equals(TiC.PROPERTY_STYLE)) {
			setStyle(TiConvert.toString(newValue,""));
		} else {
			super.propertyChanged(key, oldValue, newValue, proxy);
		}
	}

	public GoogleMap getMap() {
		return map;
	}

	protected void setStyle(String style) {
		if (style != null && style != ""){
			try {
				boolean success = map.setMapStyle(new MapStyleOptions(style));
				if (!success) {
					Log.e("MapsActivityRaw", "Style parsing failed.");
				}
			} catch (Resources.NotFoundException e) {
				Log.e("MapsActivityRaw", "Can't find style.", e);
			}
		}
	}

	protected void setUserLocationEnabled(boolean enabled) {
		map.setMyLocationEnabled(enabled);
	}

	protected void setCompassEnabled(boolean enabled) {
		map.getUiSettings().setCompassEnabled(enabled);
	}

	protected void setUserLocationButtonEnabled(boolean enabled) {
		map.getUiSettings().setMyLocationButtonEnabled(enabled);
	}

	protected void setMapToolbarEnabled(boolean enabled) {
		map.getUiSettings().setMapToolbarEnabled(enabled);
	}

	public float getMaxZoomLevel() {
		return map.getMaxZoomLevel();
	}

	public float getMinZoomLevel() {
		return map.getMinZoomLevel();
	}

	protected void setMapType(int type) {
		map.setMapType(type);
	}

	protected void setTrafficEnabled(boolean enabled) {
		map.setTrafficEnabled(enabled);
	}

	protected void setZoomControlsEnabled(boolean enabled) {
		map.getUiSettings().setZoomControlsEnabled(enabled);
	}

	public void updateCamera(HashMap<String, Object> dict) {
		double longitude = 0;
		double longitudeDelta = 0;
		double latitude = 0;
		double latitudeDelta = 0;
		float bearing = 0;
		float tilt = 0;
		float zoom = 0;

		// In the setLocation() method, the old map module allows the user to
		// provide two more properties - "animate" and "regionFit".
		// In this map module, no matter "regionFit" is set to true or false, we
		// will always make sure the specified
		// latitudeDelta / longitudeDelta bounds are centered on screen at the
		// greatest possible zoom level.
		boolean anim = animate;
		if (dict.containsKey(TiC.PROPERTY_ANIMATE)) {
			anim = TiConvert.toBoolean(dict, TiC.PROPERTY_ANIMATE, animate);
		}
		if (dict.containsKey(MapModule.PROPERTY_BEARING)) {
			bearing = TiConvert.toFloat(dict, MapModule.PROPERTY_BEARING, 0);
		}
		if (dict.containsKey(MapModule.PROPERTY_TILT)) {
			tilt = TiConvert.toFloat(dict, MapModule.PROPERTY_TILT, 0);
		}
		if (dict.containsKey(MapModule.PROPERTY_ZOOM)) {
			zoom = TiConvert.toFloat(dict, MapModule.PROPERTY_ZOOM, 0);
		}
		// Workaround for toDouble since there is no method that allows you to set defaults
		if (dict.containsKey(TiC.PROPERTY_LATITUDE) && dict.get(TiC.PROPERTY_LATITUDE) != null) {
			latitude = TiConvert.toDouble(dict, TiC.PROPERTY_LATITUDE);
		}
		if (dict.containsKey(TiC.PROPERTY_LONGITUDE) && dict.get(TiC.PROPERTY_LONGITUDE) != null) {
			longitude = TiConvert.toDouble(dict, TiC.PROPERTY_LONGITUDE);
		}

		CameraPosition.Builder cameraBuilder = new CameraPosition.Builder();
		LatLng location = new LatLng(latitude, longitude);
		cameraBuilder.target(location);
		cameraBuilder.bearing(bearing);
		cameraBuilder.tilt(tilt);
		cameraBuilder.zoom(zoom);

		if (dict.containsKey(TiC.PROPERTY_LATITUDE_DELTA) && dict.get(TiC.PROPERTY_LATITUDE_DELTA) != null) {
			latitudeDelta = TiConvert.toDouble(dict, TiC.PROPERTY_LATITUDE_DELTA);
		}

		if (dict.containsKey(TiC.PROPERTY_LONGITUDE_DELTA) && dict.get(TiC.PROPERTY_LONGITUDE_DELTA) != null) {
			longitudeDelta = TiConvert.toDouble(dict, TiC.PROPERTY_LONGITUDE_DELTA);
		}

		if (latitudeDelta != 0 && longitudeDelta != 0) {
			LatLng northeast = new LatLng(latitude + (latitudeDelta / 2.0),
					longitude + (longitudeDelta / 2.0));
			LatLng southwest = new LatLng(latitude - (latitudeDelta / 2.0),
					longitude - (longitudeDelta / 2.0));

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
		CameraUpdate camUpdate = CameraUpdateFactory
				.newCameraPosition(position);
		moveCamera(camUpdate, anim);
	}

	protected void moveCamera(CameraUpdate camUpdate, boolean anim) {
		if (anim) {
			map.animateCamera(camUpdate);
		} else {
			map.moveCamera(camUpdate);
		}
	}

	protected void addAnnotation(AnnotationProxy annotation) {
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

	protected void addAnnotations(Object[] annotations) {
		for (int i = 0; i < annotations.length; i++) {
			Object obj = annotations[i];
			if (obj instanceof AnnotationProxy) {
				AnnotationProxy annotation = (AnnotationProxy) obj;
				addAnnotation(annotation);
			}
		}
	}

	protected void updateAnnotations(Object[] annotations) {
		// First, remove old annotations from map
		removeAllAnnotations();
		// Then we add new annotations to the map
		addAnnotations(annotations);
	}

	protected void removeAllAnnotations() {
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

	public TiMarker findMarkerByTitle(String title) {
		for (int i = 0; i < timarkers.size(); i++) {
			TiMarker timarker = timarkers.get(i);
			AnnotationProxy annoProxy = timarker.getProxy();
			if (annoProxy != null && annoProxy.getTitle().equals(title)) {
				return timarker;
			}
		}
		return null;
	}

	protected void removeAnnotation(Object annotation) {
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

	protected void selectAnnotation(Object annotation) {
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

	protected void deselectAnnotation(Object annotation) {
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

	private AnnotationProxy getProxyByMarker(Marker m) {
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

	public void addRoute(RouteProxy r) {
		// check if route already added.
		if (r.getRoute() != null) {
			return;
		}

		r.processOptions();
		r.setRoute(map.addPolyline(r.getOptions()));
	}

	public void removeRoute(RouteProxy r) {
		if (r.getRoute() == null) {
			return;
		}

		r.getRoute().remove();
		r.setRoute(null);
	}

	/**
	 * Polygon
	 */
	public void addPolygon(PolygonProxy p) {
		// check if polygon already added.
		if (p.getPolygon() != null) {
			return;
		}

		p.processOptions();
		p.setPolygon(map.addPolygon(p.getOptions()));

		currentPolygons.add(p);
	}

	protected void addPolygons(Object[] polygons) {
		for (int i = 0; i < polygons.length; i++) {
			Object obj = polygons[i];
			if (obj instanceof PolygonProxy) {
				PolygonProxy polygon = (PolygonProxy) obj;
				addPolygon(polygon);
			}
		}
	}

	public void removePolygon(PolygonProxy p) {
		if (p.getPolygon() == null) {
			return;
		}

		if (currentPolygons.contains(p)) {
			p.getPolygon().remove();
			p.setPolygon(null);
			currentPolygons.remove(p);
		}

	}

	public void removeAllPolygons() {
		for (PolygonProxy polygonProxy : currentPolygons) {
			polygonProxy.getPolygon().remove();
			polygonProxy.setPolygon(null);
		}
		currentPolygons.clear();
	}

	/**
	 * Polyline
	 */
	public void addPolyline(PolylineProxy p) {
		// check if polyline already added.
		if (p.getPolyline() != null) {
			return;
		}
		p.processOptions();
		p.setPolyline(map.addPolyline(p.getOptions()));

		currentPolylines.add(p);
	}

	protected void addPolylines(Object[] polylines) {
		for (int i = 0; i < polylines.length; i++) {
			Object obj = polylines[i];
			if (obj instanceof PolylineProxy) {
				PolylineProxy polyline = (PolylineProxy) obj;
				addPolyline(polyline);
			}
		}
	}

	public void removePolyline(PolylineProxy p) {
		if (p.getPolyline() == null) {
			return;
		}

		if(currentPolylines.contains(p)) {
			p.getPolyline().remove();
			p.setPolyline(null);
			currentPolylines.remove(p);
		}
	}

	public void removeAllPolylines() {
		for (PolylineProxy polylineProxy : currentPolylines) {
			polylineProxy.getPolyline().remove();
			polylineProxy.setPolyline(null);
		}
		currentPolylines.clear();
	}


	/**
	 * Circle
	 */
	protected void addCircles(Object[] circles) {
		for (int i = 0; i < circles.length; i++) {
			Object obj = circles[i];
			if (obj instanceof CircleProxy) {
				CircleProxy circle = (CircleProxy) obj;
				addCircle(circle);
			}
		}
	}

	public void addCircle(CircleProxy c)
	{
		if (currentCircles.contains(c)) {
			return;
		}
		c.processOptions();
		c.setCircle(map.addCircle(c.getOptions()));
		currentCircles.add(c);
	}

	public void removeCircle(CircleProxy c)
	{
		if (!currentCircles.contains(c)) {
			return;
		}
		c.getCircle().remove();
		c.setCircle(null);
		currentCircles.remove(c);
	}

	public void removeAllCircles()
	{
		for (CircleProxy circleProxy : currentCircles) {
			circleProxy.getCircle().remove();
			circleProxy.setCircle(null);
		}
		currentCircles.clear();
	}


	public void changeZoomLevel(int delta) {
		CameraUpdate camUpdate = CameraUpdateFactory.zoomBy(delta);
		moveCamera(camUpdate, animate);
	}

	public void fireShapeClickEvent(LatLng clickPosition, IShape shapeProxy, String clickSource) {

		KrollDict d = new KrollDict();

		d.put(TiC.PROPERTY_LATITUDE, clickPosition.latitude);
		d.put(TiC.PROPERTY_LONGITUDE, clickPosition.longitude);

		d.put(MapModule.PROPERTY_MAP, proxy);
		d.put(TiC.PROPERTY_TYPE, TiC.EVENT_CLICK);
		d.put(TiC.PROPERTY_SOURCE, shapeProxy);
		d.put(TiC.EVENT_PROPERTY_CLICKSOURCE, clickSource);
		// In iOS, sometimes the source property is forced to the mapProxy and
		// so we have to send along a more robust message via 'shape' and
		// 'shapeType'.
		d.put(MapModule.PROPERTY_SHAPE, shapeProxy);
		d.put(MapModule.PROPERTY_SHAPE_TYPE, clickSource);

		proxy.fireEvent(TiC.EVENT_CLICK, d);
	}

	public void fireClickEvent(Marker marker, AnnotationProxy annoProxy,
			String clickSource) {
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

	public void fireLongClickEvent(LatLng point) {
		KrollDict d = new KrollDict();
		d.put(TiC.PROPERTY_LATITUDE, point.latitude);
		d.put(TiC.PROPERTY_LONGITUDE, point.longitude);
		d.put(MapModule.PROPERTY_MAP, proxy);
		d.put(TiC.PROPERTY_TYPE, TiC.EVENT_LONGCLICK);
		d.put(TiC.PROPERTY_SOURCE, proxy);
		proxy.fireEvent(TiC.EVENT_LONGCLICK, d);
	}

	public void firePinChangeDragStateEvent(Marker marker,
			AnnotationProxy annoProxy, int dragState) {
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
	public boolean onMarkerClick(Marker marker) {
		AnnotationProxy annoProxy = getProxyByMarker(marker);
		if (annoProxy == null) {
			Log.e(TAG, "Marker can not be found, click event won't fired.",
					Log.DEBUG_MODE);
			return false;
		} else if (selectedAnnotation != null
				&& selectedAnnotation.equals(annoProxy)) {
			selectedAnnotation.hideInfo();
			selectedAnnotation = null;
			fireClickEvent(marker, annoProxy, MapModule.PROPERTY_PIN);
			return true;
		}
		fireClickEvent(marker, annoProxy, MapModule.PROPERTY_PIN);
		selectedAnnotation = annoProxy;
		boolean showInfoWindow = TiConvert.toBoolean(
				annoProxy.getProperty(MapModule.PROPERTY_SHOW_INFO_WINDOW),
				true);
		// Returning false here will enable native behavior, which shows the
		// info window.
		if (showInfoWindow) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void onMapClick(LatLng point) {

		if (selectedAnnotation != null) {
			TiMarker tiMarker = selectedAnnotation.getTiMarker();
			if (tiMarker != null) {
				fireClickEvent(tiMarker.getMarker(), selectedAnnotation, null);
			}
			selectedAnnotation = null;
		}

	}

	@Override
	public void onMapLongClick(LatLng point) {
		fireLongClickEvent(point);
	}

	@Override
	public void onMarkerDrag(Marker marker) {
		Log.d(TAG, "The annotation is dragged.", Log.DEBUG_MODE);
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		AnnotationProxy annoProxy = getProxyByMarker(marker);
		if (annoProxy != null) {
			LatLng position = marker.getPosition();
			annoProxy.setProperty(TiC.PROPERTY_LONGITUDE, position.longitude);
			annoProxy.setProperty(TiC.PROPERTY_LATITUDE, position.latitude);
			firePinChangeDragStateEvent(marker, annoProxy,
					MapModule.ANNOTATION_DRAG_STATE_END);
		}
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		AnnotationProxy annoProxy = getProxyByMarker(marker);
		if (annoProxy != null) {
			firePinChangeDragStateEvent(marker, annoProxy,
					MapModule.ANNOTATION_DRAG_STATE_START);
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		AnnotationProxy annoProxy = getProxyByMarker(marker);
		if (annoProxy != null) {
			String clicksource = annoProxy.getMapInfoWindow().getClicksource();
			// The clicksource is null means the click event is not inside
			// "leftPane", "title", "subtible"
			// or "rightPane". In this case, use "infoWindow" as the
			// clicksource.
			if (clicksource == null) {
				clicksource = MapModule.PROPERTY_INFO_WINDOW;
			}
			fireClickEvent(marker, annoProxy, clicksource);
		}
	}

	@Override
	public View getInfoContents(Marker marker) {
		AnnotationProxy annoProxy = getProxyByMarker(marker);
		if (annoProxy != null) {
			return annoProxy.getMapInfoWindow();
		}
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}

	@Override
	public void release() {
		selectedAnnotation = null;
		if (map != null) {
			map.clear();
		}
		currentCircles = null;
		currentPolygons = null;
		currentPolylines = null;
		map = null;
		timarkers.clear();
		super.release();
	}

	@Override
	public void onCameraChange(CameraPosition position) {
		if (preLayout) {
			if (preLayoutUpdateBounds != null) {
				moveCamera(CameraUpdateFactory.newLatLngBounds(
						preLayoutUpdateBounds, 0), animate);
				preLayoutUpdateBounds = null;
			} else {
				// moveCamera will trigger another callback, so we do this to
				// make sure
				// we don't fire event when region is set initially
				preLayout = false;
			}
		} else if (proxy != null) {
			KrollDict d = new KrollDict();
			d.put(TiC.PROPERTY_LATITUDE, position.target.latitude);
			d.put(TiC.PROPERTY_LONGITUDE, position.target.longitude);
			d.put(TiC.PROPERTY_SOURCE, proxy);
			LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
			d.put(TiC.PROPERTY_LATITUDE_DELTA,
					(bounds.northeast.latitude - bounds.southwest.latitude));
			d.put(TiC.PROPERTY_LONGITUDE_DELTA,
					(bounds.northeast.longitude - bounds.southwest.longitude));

			// In iOS, the region property is updated in the
			// 'regionDidChangeAnimated' method.
			// This allows a user to call getRegion and receive the current map
			// bounds
			proxy.setProperty(TiC.PROPERTY_REGION, d);
			proxy.fireEvent(TiC.EVENT_REGION_CHANGED, d);
		}

	}
	
	@Override
    public void onCameraMoveStarted(int reason) {
		String[] reasons = {"", "REASON_GESTURE", "REASON_API_ANIMATION", "REASON_DEVELOPER_ANIMATION"};
		Log.i(TAG, "onCameraMoveStarted " + reasons[reason]);
		KrollDict d = new KrollDict();
		d.put("reason", reasons[reason]);
		proxy.fireEvent("regionwillchange", d);
    }
	
	@Override
    public void onCameraMove() {
		proxy.fireEvent("cameramove");
    }

    @Override
    public void onCameraMoveCanceled() {
    		proxy.fireEvent("cameramovecancelled");
    }

    @Override
    public void onCameraIdle() {
    		Log.i(TAG, "onCameraIdle");
    		//proxy.fireEvent(TiC.EVENT_REGION_IDLE);
    }


	// Intercept the touch event to find out the correct clicksource if clicking
	// on the info window.
	@Override
	protected boolean interceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_UP
				&& selectedAnnotation != null) {
			TiMapInfoWindow infoWindow = selectedAnnotation.getMapInfoWindow();
			TiMarker timarker = selectedAnnotation.getTiMarker();
			if (infoWindow != null && timarker != null) {
				Marker marker = timarker.getMarker();
				if (marker != null && marker.isInfoWindowShown()) {
					Point markerPoint = map.getProjection().toScreenLocation(
							marker.getPosition());
					infoWindow.analyzeTouchEvent(ev, markerPoint,
							selectedAnnotation.getIconImageHeight());
				}
			}
		}
		return false;
	}

	public void snapshot() {
		map.snapshot(new GoogleMap.SnapshotReadyCallback() {

			@Override
			public void onSnapshotReady(Bitmap snapshot) {
				TiBlob sblob = TiBlob.blobFromImage(snapshot);
				KrollDict data = new KrollDict();
				data.put("snapshot", sblob);
				data.put("source", proxy);
				proxy.fireEvent(MapModule.EVENT_ON_SNAPSHOT_READY, data);
			}
		});
	}

	@Override
	public void onMapLoaded() {
		proxy.fireEvent(TiC.EVENT_COMPLETE, null);
	}
	
	protected void onViewCreated() {
		// keep around for backward compatibility
	}

}
