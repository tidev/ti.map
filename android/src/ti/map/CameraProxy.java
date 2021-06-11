/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2013-present by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ti.map;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.util.TiConvert;

import java.util.HashMap;

@Kroll.
proxy(name = "Camera", creatableInModule = MapModule.class)
public class CameraProxy extends KrollProxy
{

	private CameraPosition cameraPosition = null;
	private LatLng position;
	private Float zoom = 1.0f;
	private Float pitch = 0.0f;
	private Float heading = 0.0f;

	public CameraProxy()
	{
		super();
	}

	public void handleCreationDict(KrollDict dict)
	{
		super.handleCreationDict(dict);
		if (dict.containsKeyAndNotNull(MapModule.PROPERTY_CENTER_COORDINATES)) {
			Object loc = dict.getKrollDict(MapModule.PROPERTY_CENTER_COORDINATES);

			if (loc instanceof HashMap) {
				HashMap<String, String> point = (HashMap<String, String>) loc;
				position = new LatLng(TiConvert.toDouble(point.get(TiC.PROPERTY_LATITUDE)),
						TiConvert.toDouble(point.get(TiC.PROPERTY_LONGITUDE)));
			}
		}
		if (dict.containsKeyAndNotNull(MapModule.PROPERTY_ALTITIDE)) {
			zoom = TiConvert.toFloat(dict.get(MapModule.PROPERTY_ALTITIDE));
		}
		if (dict.containsKeyAndNotNull(MapModule.PROPERTY_HEADING)) {
			heading = TiConvert.toFloat(dict.get(MapModule.PROPERTY_HEADING));
		}
		if (dict.containsKeyAndNotNull(MapModule.PROPERTY_PITCH)) {
			pitch = TiConvert.toFloat(dict.get(MapModule.PROPERTY_PITCH));
		}

		updateCamera();
	}

	@Override
	public void onPropertyChanged(String name, Object value)
	{
		super.onPropertyChanged(name, value);

		if (name.equals(MapModule.PROPERTY_CENTER_COORDINATES)) {
			Object loc = value;

			if (loc instanceof HashMap) {
				HashMap<String, String> point = (HashMap<String, String>) loc;
				position = new LatLng(TiConvert.toDouble(point.get(TiC.PROPERTY_LATITUDE)),
						TiConvert.toDouble(point.get(TiC.PROPERTY_LONGITUDE)));
			}
		}
		if (name.equals(MapModule.PROPERTY_ALTITIDE)) {
			zoom = TiConvert.toFloat(value);
		}
		if (name.equals(MapModule.PROPERTY_HEADING)) {
			heading = TiConvert.toFloat(value);
		}
		if (name.equals(MapModule.PROPERTY_PITCH)) {
			pitch = TiConvert.toFloat(value);
		}

		updateCamera();
	}

	private void updateCamera(){
		cameraPosition = new CameraPosition.Builder().target(position)
				.zoom(zoom)
				.bearing(heading)
				.tilt(pitch)
				.build();
	}

	public CameraUpdate getCamera(){
		if (cameraPosition != null) {
			return CameraUpdateFactory.newCameraPosition(cameraPosition);
		} else {
			return null;
		}
	}

}
