/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2013-2016 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ti.map;

import android.os.Message;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.AsyncResult;
import org.appcelerator.kroll.common.TiMessenger;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.util.TiConvert;
import ti.map.Shape.IShape;

@Kroll.proxy(name = "Polygon", creatableInModule = MapModule.class,
			 propertyAccessors = { MapModule.PROPERTY_FILL_COLOR, MapModule.PROPERTY_STROKE_COLOR,
								   MapModule.PROPERTY_STROKE_WIDTH, MapModule.PROPERTY_ZINDEX,
								   MapModule.PROPERTY_POINTS, TiC.PROPERTY_TOUCH_ENABLED })
public class PolygonProxy extends KrollProxy implements IShape
{

	private PolygonOptions options;
	private Polygon polygon;
	private boolean clickable;

	private static final int MSG_FIRST_ID = KrollProxy.MSG_LAST_ID + 1;

	private static final int MSG_SET_POINTS = MSG_FIRST_ID + 699;
	private static final int MSG_SET_FILL_COLOR = MSG_FIRST_ID + 700;
	private static final int MSG_SET_STROKE_COLOR = MSG_FIRST_ID + 701;
	private static final int MSG_SET_STROKE_WIDTH = MSG_FIRST_ID + 702;
	private static final int MSG_SET_ZINDEX = MSG_FIRST_ID + 703;
	private static final int MSG_SET_HOLES = MSG_FIRST_ID + 704;
	private static final int MSG_SET_TOUCH_ENABLED = MSG_FIRST_ID + 705;

	public static final String PROPERTY_HOLES = "holes";

	public PolygonProxy()
	{
		super();
		clickable = true;
	}

	@Override
	public boolean handleMessage(Message msg)
	{

		AsyncResult result = null;
		switch (msg.what) {
			case MSG_SET_POINTS: {
				result = (AsyncResult) msg.obj;
				polygon.setPoints(processPoints(result.getArg(), true));
				result.setResult(null);
				return true;
			}
			case MSG_SET_HOLES: {
				result = (AsyncResult) msg.obj;
				polygon.setHoles(processHoles(result.getArg(), true));
				result.setResult(null);
				return true;
			}
			case MSG_SET_FILL_COLOR: {
				result = (AsyncResult) msg.obj;
				polygon.setFillColor((Integer) result.getArg());
				result.setResult(null);
				return true;
			}
			case MSG_SET_STROKE_WIDTH: {
				result = (AsyncResult) msg.obj;
				polygon.setStrokeWidth((float) result.getArg());
				result.setResult(null);
				return true;
			}
			case MSG_SET_STROKE_COLOR: {
				result = (AsyncResult) msg.obj;
				polygon.setStrokeColor((Integer) result.getArg());
				result.setResult(null);
				return true;
			}
			case MSG_SET_ZINDEX: {
				result = (AsyncResult) msg.obj;
				polygon.setZIndex((float) result.getArg());
				result.setResult(null);
				return true;
			}
			case MSG_SET_TOUCH_ENABLED: {
				result = (AsyncResult) msg.obj;
				clickable = TiConvert.toBoolean(result.getArg(), true);
				result.setResult(null);
				return true;
			}
			default: {
				return super.handleMessage(msg);
			}
		}
	}

	public void processOptions()
	{

		options = new PolygonOptions();

		if (hasProperty(MapModule.PROPERTY_POINTS)) {
			processPoints(getProperty(MapModule.PROPERTY_POINTS), false);
		}

		if (hasProperty(PolygonProxy.PROPERTY_HOLES)) {
			processHoles(getProperty(PolygonProxy.PROPERTY_HOLES), false);
		}

		if (hasProperty(MapModule.PROPERTY_STROKE_COLOR)) {
			options.strokeColor(TiConvert.toColor((String) getProperty(MapModule.PROPERTY_STROKE_COLOR)));
		}

		if (hasProperty(MapModule.PROPERTY_STROKE_WIDTH)) {
			options.strokeWidth(TiConvert.toFloat(getProperty(MapModule.PROPERTY_STROKE_WIDTH)));
		}

		if (hasProperty(MapModule.PROPERTY_FILL_COLOR)) {
			options.fillColor(TiConvert.toColor((String) getProperty(MapModule.PROPERTY_FILL_COLOR)));
		}

		if (hasProperty(MapModule.PROPERTY_ZINDEX)) {
			options.zIndex(TiConvert.toFloat(getProperty(MapModule.PROPERTY_ZINDEX)));
		}
		if (hasProperty(TiC.PROPERTY_TOUCH_ENABLED)) {
			clickable = TiConvert.toBoolean(getProperty(TiC.PROPERTY_TOUCH_ENABLED));
		}
	}

	public void addLocation(Object loc, ArrayList<LatLng> locationArray, boolean list)
	{
		LatLng location = parseLocation(loc);
		if (list) {
			locationArray.add(location);
		} else {
			options.add(location);
		}
	}

	public ArrayList<LatLng> processPoints(Object points, boolean list)
	{

		ArrayList<LatLng> locationArray = new ArrayList<LatLng>();
		// encoded (result from routing API)
		if (points instanceof String) {
			List<LatLng> locationList = PolyUtil.decode((String) points);
			return new ArrayList<LatLng>(locationList);
		}
		// multiple points
		if (points instanceof Object[]) {
			Object[] pointsArray = (Object[]) points;
			for (int i = 0; i < pointsArray.length; i++) {
				Object obj = pointsArray[i];
				addLocation(obj, locationArray, list);
			}
			return locationArray;
		}

		// single point
		addLocation(points, locationArray, list);
		return locationArray;
	}

	/**
	 * Add holes as a list of list
	 *
	 * holes: [ [ { latitude: .., longitude: .. } ] ]
	 *
	 */
	public ArrayList<ArrayList<LatLng>> processHoles(Object holesList, boolean list)
	{

		ArrayList<ArrayList<LatLng>> holesArray = new ArrayList<ArrayList<LatLng>>();

		// multiple points
		if (holesList instanceof Object[]) {

			Object[] singleHoleArray = (Object[]) holesList;
			for (int h = 0; h < singleHoleArray.length; h++) {

				ArrayList<LatLng> holeContainerArray = new ArrayList<LatLng>();

				Object[] pointsArray = (Object[]) singleHoleArray[h];
				if (pointsArray instanceof Object[]) {
					for (int i = 0; i < pointsArray.length; i++) {
						Object obj = pointsArray[i];
						holeContainerArray.add(parseLocation(obj));
					}
				}

				if (holeContainerArray.size() > 0) {
					if (!list) {
						if (polygon == null) {
							// Log.e("TiApplicationMapDBG",
							// "add holes to options");
							options.addHole(holeContainerArray);
						}
					} else {
						holesArray.add(holeContainerArray);
					}
				}
			}
		}

		if (!list) {
			// Log.e("TiApplicationMapDBG", "polygon exists?");
			if (polygon != null) {
				// Log.e("TiApplicationMapDBG", "Yes, add holes to polygon");
				polygon.setHoles(holesArray);
			}

			return null;
		} else
			return holesArray;
	}

	public PolygonOptions getOptions()
	{
		return options;
	}

	@Kroll.setProperty
	public void setHoles(Object[] holesList)
	{
		TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_HOLES), holesList);
	}

	@Kroll.getProperty
	public Object[] getHoles()
	{
		return (Object[]) getProperty(PolygonProxy.PROPERTY_HOLES);
	}

	public void setPolygon(Polygon r)
	{
		polygon = r;
	}

	public Polygon getPolygon()
	{
		return polygon;
	}

	public boolean getClickable()
	{
		return clickable;
	}

	/*
	 * public List<? extends List<LatLng>> getHoles() { return polygon.getHoles(); }
	 */

	@Override
	public void onPropertyChanged(String name, Object value)
	{

		super.onPropertyChanged(name, value);

		if (polygon == null) {
			return;
		}

		else if (name.equals(MapModule.PROPERTY_POINTS)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_POINTS), value);
		} else if (name.equals(PolygonProxy.PROPERTY_HOLES)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_HOLES), value);
		} else if (name.equals(MapModule.PROPERTY_STROKE_WIDTH)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_STROKE_WIDTH),
												TiConvert.toFloat(value));
		}

		else if (name.equals(MapModule.PROPERTY_STROKE_COLOR)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_STROKE_COLOR),
												TiConvert.toColor((String) value));
		}

		else if (name.equals(MapModule.PROPERTY_FILL_COLOR)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_FILL_COLOR),
												TiConvert.toColor((String) value));
		}

		else if (name.equals(MapModule.PROPERTY_ZINDEX)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_ZINDEX),
												TiConvert.toFloat(value));
		}

		else if (name.equals(TiC.PROPERTY_TOUCH_ENABLED)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_TOUCH_ENABLED),
												TiConvert.toBoolean(value));
		}
	}

	// A location can either be a an array of longitude, latitude pairings or
	// an array of longitude, latitude objects.
	// e.g. [123.33, 34.44], OR {longitude: 123.33, latitude, 34.44}
	private LatLng parseLocation(Object loc)
	{
		LatLng location = null;
		if (loc instanceof HashMap) {
			HashMap<String, String> point = (HashMap<String, String>) loc;
			location = new LatLng(TiConvert.toDouble(point.get(TiC.PROPERTY_LATITUDE)),
								  TiConvert.toDouble(point.get(TiC.PROPERTY_LONGITUDE)));
		} else if (loc instanceof Object[]) {
			Object[] temp = (Object[]) loc;
			location = new LatLng(TiConvert.toDouble(temp[1]), TiConvert.toDouble(temp[0]));
		}
		return location;
	}

	public String getApiName()
	{
		return "Ti.Map.Polygon";
	}
}
