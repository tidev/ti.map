/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2013-present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package ti.map;

import android.location.Location;
import androidx.annotation.NonNull;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.TiC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Kroll.module(name = "Map", id = "ti.map")
public class MapModule extends KrollModule implements OnMapsSdkInitializedCallback
{
	public static final String EVENT_MAP_CLICK = "mapclick";
	public static final String EVENT_POI_CLICK = "poiclick";
	public static final String EVENT_PIN_CHANGE_DRAG_STATE = "pinchangedragstate";
	public static final String EVENT_ON_SNAPSHOT_READY = "onsnapshotready";
	public static final String EVENT_REGION_WILL_CHANGE = "regionwillchange";
	public static final String EVENT_USER_LOCATION = "userLocation";

	public static final String PROPERTY_DRAGGABLE = "draggable";
	public static final String PROPERTY_POINTS = "points";
	public static final String PROPERTY_TRAFFIC = "traffic";
	public static final String PROPERTY_MAP = "map";
	public static final String PROPERTY_SHAPE = "shape";
	public static final String PROPERTY_SHAPE_TYPE = "shapeType";
	public static final String PROPERTY_NEWSTATE = "newState";
	public static final String PROPERTY_CUSTOM_VIEW = "customView";
	public static final String PROPERTY_PIN = "pin";
	public static final String PROPERTY_INFO_WINDOW = "infoWindow";
	public static final String PROPERTY_LEFT_PANE = "leftPane";
	public static final String PROPERTY_RIGHT_PANE = "rightPane";
	public static final String PROPERTY_SHOW_INFO_WINDOW = "showInfoWindow";
	public static final String PROPERTY_USER_LOCATION_BUTTON = "userLocationButton";
	public static final String PROPERTY_COMPASS_ENABLED = "compassEnabled";
	public static final String PROPERTY_SCROLL_ENABLED = "scrollEnabled";
	public static final String PROPERTY_ZOOM_ENABLED = "zoomEnabled";
	public static final String PROPERTY_MAP_TOOLBAR_ENABLED = "mapToolbarEnabled";
	public static final String PROPERTY_PADDING = "padding";
	public static final String PROPERTY_TILT = "tilt";
	public static final String PROPERTY_BEARING = "bearing";
	public static final String PROPERTY_ZOOM = "zoom";
	public static final String PROPERTY_ZORDER_ON_TOP = "zOrderOnTop";
	public static final String PROPERTY_CENTER_OFFSET = "centerOffset";
	public static final String PROPERTY_PANNING = "panning";
	public static final String PROPERTY_STREET_NAMES = "streetNames";
	public static final String PROPERTY_USER_NAVIGATION = "userNavigation";
	public static final String PROPERTY_HIDDEN = "hidden";
	public static final String PROPERTY_CLUSTER_IDENTIFIER = "clusterIdentifier";
	public static final String PROPERTY_STROKE_COLOR = "strokeColor";
	public static final String PROPERTY_STROKE_WIDTH = "strokeWidth";
	public static final String PROPERTY_FILL_COLOR = "fillColor";
	public static final String PROPERTY_ZINDEX = "zIndex";
	public static final String PROPERTY_POLYGON = "polygon";
	public static final String PROPERTY_POLYGONS = "polygons";
	public static final String PROPERTY_POLYLINE = "polyline";
	public static final String PROPERTY_POLYLINES = "polylines";
	public static final String PROPERTY_CIRCLE = "circle";
	public static final String PROPERTY_CIRCLES = "circles";
	public static final String PROPERTY_CENTER = "center";
	public static final String PROPERTY_RADIUS = "radius";
	public static final String PROPERTY_INDOOR_ENABLED = "indoorEnabled";
	public static final String PROPERTY_PLACE_ID = "placeID";
	public static final String PROPERTY_DESELECTED = "deselected";
	public static final String PROPERTY_LITE_MODE = "liteMode";
	public static final String PROPERTY_MIN_CLUSTER_SIZE = "minClusterSize";
	public static final String PROPERTY_CENTER_COORDINATES = "centerCoordinates";
	public static final String PROPERTY_ALTITIDE = "altitude";
	public static final String PROPERTY_HEADING = "heading";
	public static final String PROPERTY_PITCH = "pitch";

	@Kroll.constant
	public static final int NORMAL_TYPE = GoogleMap.MAP_TYPE_NORMAL;
	@Kroll.constant
	public static final int TERRAIN_TYPE = GoogleMap.MAP_TYPE_TERRAIN;
	@Kroll.constant
	public static final int SATELLITE_TYPE = GoogleMap.MAP_TYPE_SATELLITE;
	@Kroll.constant
	public static final int HYBRID_TYPE = GoogleMap.MAP_TYPE_HYBRID;
	@Kroll.constant
	public static final int MUTED_STANDARD_TYPE = GoogleMap.MAP_TYPE_NORMAL;
	@Kroll.constant
	public static final int ANNOTATION_DRAG_STATE_START = 0;
	@Kroll.constant
	public static final int ANNOTATION_DRAG_STATE_END = 1;

	@Kroll.constant
	public static final int SUCCESS = 0;
	@Kroll.constant
	public static final int SERVICE_MISSING = 1;
	@Kroll.constant
	public static final int SERVICE_VERSION_UPDATE_REQUIRED = 2;
	@Kroll.constant
	public static final int SERVICE_DISABLED = 3;
	@Kroll.constant
	public static final int SERVICE_INVALID = 9;

	@Kroll.constant
	public static final float ANNOTATION_AZURE = BitmapDescriptorFactory.HUE_AZURE;
	@Kroll.constant
	public static final float ANNOTATION_BLUE = BitmapDescriptorFactory.HUE_BLUE;
	@Kroll.constant
	public static final float ANNOTATION_CYAN = BitmapDescriptorFactory.HUE_CYAN;
	@Kroll.constant
	public static final float ANNOTATION_GREEN = BitmapDescriptorFactory.HUE_GREEN;
	@Kroll.constant
	public static final float ANNOTATION_MAGENTA = BitmapDescriptorFactory.HUE_MAGENTA;
	@Kroll.constant
	public static final float ANNOTATION_ORANGE = BitmapDescriptorFactory.HUE_ORANGE;
	@Kroll.constant
	public static final float ANNOTATION_RED = BitmapDescriptorFactory.HUE_RED;
	@Kroll.constant
	public static final float ANNOTATION_ROSE = BitmapDescriptorFactory.HUE_ROSE;
	@Kroll.constant
	public static final float ANNOTATION_VIOLET = BitmapDescriptorFactory.HUE_VIOLET;
	@Kroll.constant
	public static final float ANNOTATION_YELLOW = BitmapDescriptorFactory.HUE_YELLOW;
	@Kroll.constant
	public static final float ANNOTATION_PURPLE = 276.92f; // Based on the HUI color scheme

	@Kroll.constant
	public static final int REASON_API_ANIMATION = GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION;
	@Kroll.constant
	public static final int REASON_DEVELOPER_ANIMATION =
		GoogleMap.OnCameraMoveStartedListener.REASON_DEVELOPER_ANIMATION;
	@Kroll.constant
	public static final int REASON_GESTURE = GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE;

	@Kroll.constant
	public static final int POLYLINE_PATTERN_DASHED = 0;
	@Kroll.constant
	public static final int POLYLINE_PATTERN_DOTTED = 1;
	@Kroll.constant
	public static final int POLYLINE_JOINT_DEFAULT = 0;
	@Kroll.constant
	public static final int POLYLINE_JOINT_BEVEL = 1;
	@Kroll.constant
	public static final int POLYLINE_JOINT_ROUND = 2;

	public MapModule()
	{
		super();
		MapsInitializer.initialize(TiApplication.getInstance().getApplicationContext(), MapsInitializer.Renderer.LATEST,
								   this);
	}

	@Kroll.method
	public int isGooglePlayServicesAvailable()
	{
		return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(TiApplication.getInstance());
	}

	@Kroll.method
	public boolean geometryContainsLocation(KrollDict dict)
	{
		HashMap<String, String> point = (HashMap<String, String>) dict.get("location");
		LatLng location = new LatLng(TiConvert.toDouble(point.get(TiC.PROPERTY_LATITUDE)),TiConvert.toDouble(point.get(TiC.PROPERTY_LONGITUDE)));
		List<LatLng> polygon = new ArrayList<>();
		Object[] dictPoints =(Object[]) dict.get("points");
		for (Object _point : dictPoints) {
			HashMap<String, String> _location = (HashMap<String, String>)_point;
			polygon.add(new LatLng(TiConvert.toDouble(_location.get(TiC.PROPERTY_LATITUDE)), TiConvert.toDouble(_location.get(TiC.PROPERTY_LONGITUDE))));
		}
		return PolyUtil.containsLocation(location, polygon, true);
	}
	
	@Kroll.method
	public double geometryDistanceBetweenPoints(Object jsLocation1, Object jsLocation2)
	{
		HashMap<String, String> location1Dict = (HashMap<String, String>) jsLocation1;
		HashMap<String, String> location2Dict = (HashMap<String, String>) jsLocation2;

		LatLng location1 = new LatLng(TiConvert.toDouble(location1Dict.get(TiC.PROPERTY_LATITUDE)), TiConvert.toDouble(location1Dict.get(TiC.PROPERTY_LONGITUDE)));
		LatLng location2 = new LatLng(TiConvert.toDouble(location2Dict.get(TiC.PROPERTY_LATITUDE)), TiConvert.toDouble(location2Dict.get(TiC.PROPERTY_LONGITUDE)));

		float[] results = new float[1];
		Location.distanceBetween(location1.latitude, location1.longitude, location2.latitude, location2.longitude, results);
				
		return results[0];
	}

	@Override
	public String getApiName()
	{
		return "Ti.Map";
	}

	@Override
	public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer)
	{
		switch (renderer) {
			case LEGACY:
				Log.d("Ti.Map", "The legacy version of the renderer is used.");
				break;
		}
	}
}
