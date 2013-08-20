/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ti.map;


import java.util.ArrayList;
import java.util.HashMap;

import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.AsyncResult;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiMessenger;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.view.TiUIView;

import android.app.Activity;
import android.os.Message;

@Kroll.proxy(creatableInModule = MapModule.class, propertyAccessors = {
	TiC.PROPERTY_USER_LOCATION,
	MapModule.PROPERTY_USER_LOCATION_BUTTON,
	TiC.PROPERTY_MAP_TYPE,
	TiC.PROPERTY_REGION,
	TiC.PROPERTY_ANNOTATIONS,
	TiC.PROPERTY_ANIMATE,
	MapModule.PROPERTY_TRAFFIC,
	TiC.PROPERTY_ENABLE_ZOOM_CONTROLS
})
public class ViewProxy extends TiViewProxy
{
	private static final String TAG = "MapViewProxy";
		
	private static final int MSG_FIRST_ID = TiViewProxy.MSG_LAST_ID + 1;
	
	private static final int MSG_ADD_ANNOTATION = MSG_FIRST_ID + 500;
	private static final int MSG_ADD_ANNOTATIONS = MSG_FIRST_ID + 501;
	private static final int MSG_REMOVE_ANNOTATION = MSG_FIRST_ID + 502;
	private static final int MSG_REMOVE_ANNOTATIONS = MSG_FIRST_ID + 503;
	private static final int MSG_REMOVE_ALL_ANNOTATIONS = MSG_FIRST_ID + 504;
	private static final int MSG_SELECT_ANNOTATION = MSG_FIRST_ID + 505;
	private static final int MSG_DESELECT_ANNOTATION = MSG_FIRST_ID + 506;
	private static final int MSG_ADD_ROUTE = MSG_FIRST_ID + 507;	
	private static final int MSG_REMOVE_ROUTE = MSG_FIRST_ID + 508;
	private static final int MSG_CHANGE_ZOOM = MSG_FIRST_ID + 509;
	private static final int MSG_SET_LOCATION = MSG_FIRST_ID + 510;
	
	private ArrayList<AnnotationProxy> preloadAnnotations;
	private ArrayList<RouteProxy> preloadRoutes;
	
	public ViewProxy() {
		super();
		preloadAnnotations = new ArrayList<AnnotationProxy>();
		preloadRoutes = new ArrayList<RouteProxy>();
	}
	
	public TiUIView createView(Activity activity) {
		return new TiUIMapView(this, activity);
	}
	
	public void clearPreloadObjects() {
		preloadAnnotations.clear();
		preloadRoutes.clear();
	}

	@Override
	public boolean handleMessage(Message msg) 
	{
		AsyncResult result = null;
		switch (msg.what) {

		case MSG_ADD_ANNOTATION: {
			result = (AsyncResult) msg.obj;
			handleAddAnnotation((AnnotationProxy)result.getArg());
			result.setResult(null);
			return true;
		}
		
		case MSG_ADD_ANNOTATIONS: {
			result = (AsyncResult) msg.obj;
			handleAddAnnotations((Object[])result.getArg());
			result.setResult(null);
			return true;
		}

		case MSG_REMOVE_ANNOTATION: {
			result = (AsyncResult) msg.obj;
			handleRemoveAnnotation(result.getArg());
			result.setResult(null);
			return true;
		}
		
		case MSG_REMOVE_ANNOTATIONS: {
			result = (AsyncResult) msg.obj;
			handleRemoveAnnotations((Object[])result.getArg());
			result.setResult(null);
			return true;
		}
		
		case MSG_REMOVE_ALL_ANNOTATIONS: {
			result = (AsyncResult) msg.obj;
			handleRemoveAllAnnotations();
			result.setResult(null);
			return true;
		}
		
		case MSG_SELECT_ANNOTATION: {
			result = (AsyncResult) msg.obj;
			handleSelectAnnotation(result.getArg());
			result.setResult(null);
			return true;
		}
		
		case MSG_DESELECT_ANNOTATION: {
			result = (AsyncResult) msg.obj;
			handleDeselectAnnotation(result.getArg());
			result.setResult(null);
			return true;
		}
		
		case MSG_ADD_ROUTE: {
			result = (AsyncResult) msg.obj;
			handleAddRoute((RouteProxy)result.getArg());
			result.setResult(null);
			return true;
		}
		
		case MSG_REMOVE_ROUTE: {
			result = (AsyncResult) msg.obj;
			handleRemoveRoute((RouteProxy)result.getArg());
			result.setResult(null);
			return true;
		}

		case MSG_CHANGE_ZOOM: {
			handleZoom(msg.arg1);
			return true;
		}

		case MSG_SET_LOCATION: {
			handleSetLocation((HashMap) msg.obj);
			return true;
		}

		default : {
			return super.handleMessage(msg);
		}
		}
	}
	

	public ArrayList<AnnotationProxy> getPreloadAnnotations() {
		return preloadAnnotations;
	}

	@Kroll.method
	public void addAnnotation(AnnotationProxy annotation) {
		if (TiApplication.isUIThread()) {
			handleAddAnnotation(annotation);
		} else {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_ADD_ANNOTATION), annotation);
		}
	}
	
	private void handleAddAnnotation(AnnotationProxy annotation) {

		TiUIView view = peekView();
		if (view instanceof TiUIMapView) {
			TiUIMapView mapView = (TiUIMapView) view;
			if (mapView.getMap() != null) {
				mapView.addAnnotation(annotation);

			} else {
				addPreloadAnnotation(annotation);
			}
		} else {
			addPreloadAnnotation(annotation);
		}
	}
	
	private void addPreloadAnnotation(AnnotationProxy anno) {
		if (!preloadAnnotations.contains(anno)) {
			preloadAnnotations.add(anno);
		}
	}
	
	@Kroll.method
	public void addAnnotations(AnnotationProxy[] annotations) {
		if (TiApplication.isUIThread()) {
			handleAddAnnotations(annotations);
		} else {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_ADD_ANNOTATIONS), annotations);
		}
	}
	
	private void handleAddAnnotations(Object[] annotations) {
		for (int i = 0; i < annotations.length; i++) {
			Object annotation = annotations[i];
			if (annotation instanceof AnnotationProxy) {
				handleAddAnnotation((AnnotationProxy) annotation);
			}
		}
	}
	
	@Kroll.method
	public void removeAllAnnotations() {
		if (TiApplication.isUIThread()) {
			handleRemoveAllAnnotations();
		} else {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_REMOVE_ALL_ANNOTATIONS));
		}
	}
	
	public void handleRemoveAllAnnotations() {
		TiUIView view = peekView();
		if (view instanceof TiUIMapView) {
			TiUIMapView mapView = (TiUIMapView) view;
			mapView.removeAllAnnotations();
		}
	}
	
	public boolean isAnnotationValid(Object annotation) {
		//Incorrect argument types
		if (!(annotation instanceof AnnotationProxy || annotation instanceof String)) {
			Log.e(TAG, "Unsupported argument type for removeAnnotation");
			return false;
		}
		//Marker isn't on the map
		if (annotation instanceof AnnotationProxy && ((AnnotationProxy)annotation).getTiMarker() == null) {
			return false;
		}
		
		if (annotation instanceof String) {
			TiUIView view = peekView();
			if (view instanceof TiUIMapView) {
				TiUIMapView mapView = (TiUIMapView) view;
				if (mapView.findMarkerByTitle((String)annotation) == null) {
					return false;
				}
			}
		}
		
		return true;
	}
	@Kroll.method
	public void removeAnnotation(Object annotation) {
		if (!isAnnotationValid(annotation)) {
			return;
		}

		if (TiApplication.isUIThread()) {
			handleRemoveAnnotation(annotation);
		} else {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_REMOVE_ANNOTATION), annotation);
		}
		
	}
	
	@Kroll.method
	public void removeAnnotations(Object annotations) {
		if (TiApplication.isUIThread()) {
			handleRemoveAnnotations((Object[])annotations);
		} else {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_REMOVE_ANNOTATIONS), annotations);
		}
	}
	
	public void handleRemoveAnnotations(Object[] annotations) {
		for (int i = 0; i < annotations.length; i++) {
			removeAnnotation(annotations[i]);
		}
	}
	
	public void handleRemoveAnnotation(Object annotation) {
		TiUIView view = peekView();
		if (view instanceof TiUIMapView) {
			TiUIMapView mapView = (TiUIMapView) view;
			if (mapView.getMap() != null) {
				mapView.removeAnnotation(annotation);
			} else {
				removePreloadAnnotation(annotation);
			}
			
		} else {
			removePreloadAnnotation(annotation);
		}
	}
	
	public void removePreloadAnnotation(Object annotation) {
		if (annotation instanceof AnnotationProxy && preloadAnnotations.contains(annotation)) {
			preloadAnnotations.remove(annotation);
		}
	}
	
	@Kroll.method
	public void selectAnnotation(Object annotation) {
		if (!isAnnotationValid(annotation)) {
			return;
		}

		if (TiApplication.isUIThread()) {
			handleSelectAnnotation(annotation);
		} else {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SELECT_ANNOTATION), annotation);
		}
	}
	
	public void handleSelectAnnotation(Object annotation) {
		TiUIView view = peekView();
		if (view instanceof TiUIMapView) {
			((TiUIMapView)view).selectAnnotation(annotation);
		}
	}
	
	@Kroll.method
	public void deselectAnnotation(Object annotation) {
		if (!isAnnotationValid(annotation)) {
			return;
		}

		if (TiApplication.isUIThread()) {
			handleDeselectAnnotation(annotation);
		} else {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_DESELECT_ANNOTATION), annotation);
		}
	}
	
	public void handleDeselectAnnotation(Object annotation) {
		TiUIView view = peekView();
		if (view instanceof TiUIMapView) {
			((TiUIMapView)view).deselectAnnotation(annotation);
		}
	}
	
	@Kroll.method
	public void addRoute(RouteProxy route) {
		
		if (TiApplication.isUIThread()) {
			handleAddRoute(route);
		} else {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_ADD_ROUTE), route);

		}
	}
	
	public void handleAddRoute(Object route) {
		if (route == null) {
			return;
		}
		RouteProxy r = (RouteProxy) route;
		TiUIView view = peekView();
		if (view instanceof TiUIMapView) {
			TiUIMapView mapView = (TiUIMapView) view;
			if (mapView.getMap() != null) {
				mapView.addRoute(r);

			} else {
				addPreloadRoute(r);
			}
		} else {
			addPreloadRoute(r);
		}

	}
	
	public void addPreloadRoute(RouteProxy r) {
		if (!preloadRoutes.contains(r)) {
			preloadRoutes.add(r);
		}
	}
	
	public void removePreloadRoute(RouteProxy r) {
		if (preloadRoutes.contains(r)) {
			preloadRoutes.remove(r);
		}
	}
	
	@Kroll.method
	public void removeRoute(RouteProxy route) {
		if (TiApplication.isUIThread()) {
			handleRemoveRoute(route);
		} else {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_REMOVE_ROUTE), route);

		}
	}
	
	public void handleRemoveRoute(RouteProxy route) {
		TiUIView view = peekView();
		if (view instanceof TiUIMapView) {
			TiUIMapView mapView = (TiUIMapView) view;
			if (mapView.getMap() != null) {
				mapView.removeRoute(route);

			} else {
				removePreloadRoute(route);
			}
		} else {
			removePreloadRoute(route);
		}
	}
	
	public ArrayList<RouteProxy> getPreloadRoutes() {
		return preloadRoutes;
	}

	@Kroll.method
	public void zoom(int delta)
	{
		if (TiApplication.isUIThread()) {
			handleZoom(delta);
		} else {
			getMainHandler().obtainMessage(MSG_CHANGE_ZOOM, delta, 0).sendToTarget();
		}
	}

	public void handleZoom(int delta)
	{
		TiUIView view = peekView();
		if (view instanceof TiUIMapView) {
			((TiUIMapView) view).changeZoomLevel(delta);
		}
	}

	@Kroll.method
	public void setLocation(Object location)
	{
		if (location instanceof HashMap) {
			HashMap dict = (HashMap) location;
			if (!dict.containsKey(TiC.PROPERTY_LATITUDE) || !dict.containsKey(TiC.PROPERTY_LONGITUDE)) {
				Log.e(TAG, "Unable to set location. Missing latitude or longitude.");
				return;
			}
			if (TiApplication.isUIThread()) {
				handleSetLocation(dict);
			} else {
				getMainHandler().obtainMessage(MSG_SET_LOCATION, location).sendToTarget();
			}
		}
	}

	public void handleSetLocation(HashMap<String, Object> location)
	{
		TiUIView view = peekView();
		if (view instanceof TiUIMapView) {
			((TiUIMapView) view).updateCamera(location);
		} else {
			Log.e(TAG, "Unable set location since the map view has not been created yet. Use setRegion() instead.");
		}
	}
}
