/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2021-present by Axway, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ti.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.util.TiConvert;

public class TiMapUtils
{
	// A location can either be a an array of longitude, latitude pairings or
	// an array of longitude, latitude objects.
	// e.g. [123.33, 34.44], OR {longitude: 123.33, latitude, 34.44}
	public static LatLng parseLocation(Object loc)
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

	public static ArrayList<LatLng> processPoints(Object points)
	{
		ArrayList<LatLng> locationArray = new ArrayList<>();

		// encoded (result from routing API)
		if (points instanceof String) {
			List<LatLng> locationList = PolyUtil.decode((String) points);
			return new ArrayList<>(locationList);
			// multiple points
		} else if (points instanceof Object[]) {
			Object[] pointsArray = (Object[]) points;
			for (int i = 0; i < pointsArray.length; i++) {
				Object obj = pointsArray[i];
				LatLng location = TiMapUtils.parseLocation(obj);
				locationArray.add(location);
			}
			return locationArray;
		}
		return locationArray;
	}

	public static List<LatLng> createOuterBounds() {
		float delta = 0.01f;

		return new ArrayList<LatLng>() {{
			add(new LatLng(90 - delta, -180 + delta));
			add(new LatLng(0, -180 + delta));
			add(new LatLng(-90 + delta, -180 + delta));
			add(new LatLng(-90 + delta, 0));
			add(new LatLng(-90 + delta, 180 - delta));
			add(new LatLng(0, 180 - delta));
			add(new LatLng(90 - delta, 180 - delta));
			add(new LatLng(90 - delta, 0));
			add(new LatLng(90 - delta, -180 + delta));
		}};
	}

	public static Iterable<LatLng> createHole(LatLng center, int radius) {
		int points = 50; // number of corners of inscribed polygon

		double radiusLatitude = Math.toDegrees(radius / 1000 / 6371.0);
		double radiusLongitude = radiusLatitude / Math.cos(Math.toRadians(center.latitude));

		List<LatLng> result = new ArrayList<>(points);

		double anglePerCircleRegion = 2 * Math.PI / points;

		for (int i = 0; i < points; i++) {
			double theta = i * anglePerCircleRegion;
			double latitude = center.latitude + (radiusLatitude * Math.sin(theta));
			double longitude = center.longitude + (radiusLongitude * Math.cos(theta));

			result.add(new LatLng(latitude, longitude));
		}

		return result;
	}
}
