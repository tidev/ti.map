/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2013-2016 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ti.map;

import android.os.Message;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
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

@Kroll.proxy(creatableInModule = MapModule.class,
			 propertyAccessors = { MapModule.PROPERTY_POINTS, TiC.PROPERTY_COLOR, TiC.PROPERTY_WIDTH })
public class RouteProxy extends KrollProxy
{

	private PolylineOptions options;
	private Polyline route;

	private static final int MSG_FIRST_ID = KrollProxy.MSG_LAST_ID + 1;

	private static final int MSG_SET_POINTS = MSG_FIRST_ID + 400;
	private static final int MSG_SET_COLOR = MSG_FIRST_ID + 401;
	private static final int MSG_SET_WIDTH = MSG_FIRST_ID + 402;

	public RouteProxy()
	{
		super();
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		AsyncResult result = null;
		switch (msg.what) {

			case MSG_SET_POINTS: {
				result = (AsyncResult) msg.obj;
				route.setPoints(processPoints(result.getArg(), true));
				result.setResult(null);
				return true;
			}

			case MSG_SET_COLOR: {
				result = (AsyncResult) msg.obj;
				route.setColor((Integer) result.getArg());
				result.setResult(null);
				return true;
			}

			case MSG_SET_WIDTH: {
				result = (AsyncResult) msg.obj;
				route.setWidth((Float) result.getArg());
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

		options = new PolylineOptions();

		if (hasProperty(MapModule.PROPERTY_POINTS)) {
			processPoints(getProperty(MapModule.PROPERTY_POINTS), false);
		}

		if (hasProperty(TiC.PROPERTY_WIDTH)) {
			options.width(TiConvert.toFloat(getProperty(TiC.PROPERTY_WIDTH)));
		}

		if (hasProperty(TiC.PROPERTY_COLOR)) {
			options.color(TiConvert.toColor((String) getProperty(TiC.PROPERTY_COLOR)));
		}
	}

	public void addLocation(Object loc, ArrayList<LatLng> locationArray, boolean list)
	{
		if (loc instanceof HashMap) {
			HashMap<String, String> point = (HashMap<String, String>) loc;
			Object latitude = point.get(TiC.PROPERTY_LATITUDE);
			Object longitude = point.get(TiC.PROPERTY_LONGITUDE);
			if (longitude != null && latitude != null) {
				LatLng location = new LatLng(TiConvert.toDouble(latitude), TiConvert.toDouble(longitude));
				if (list) {
					locationArray.add(location);
				} else {
					options.add(location);
				}
			}
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

	public PolylineOptions getOptions()
	{
		return options;
	}

	public void setRoute(Polyline r)
	{
		route = r;
	}

	public Polyline getRoute()
	{
		return route;
	}

	@Override
	public void onPropertyChanged(String name, Object value)
	{
		super.onPropertyChanged(name, value);
		if (route == null) {
			return;
		}

		else if (name.equals(MapModule.PROPERTY_POINTS)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_POINTS), value);
		}

		else if (name.equals(TiC.PROPERTY_COLOR)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_COLOR),
												TiConvert.toColor((String) value));
		}

		else if (name.equals(TiC.PROPERTY_WIDTH)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_WIDTH),
												TiConvert.toFloat(value));
		}
	}

	@Override
	public boolean hasProperty(String name)
	{
		return (super.getProperty(name) != null);
	}

	public String getApiName()
	{
		return "Ti.Map.Route";
	}
}
