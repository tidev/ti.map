/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

package ti.map;

import com.google.android.gms.maps.model.Polygon;

public class TiPolygon {
	private Polygon polygon;
	private final PolygonProxy proxy;

	public TiPolygon(Polygon p, PolygonProxy pp) {
		polygon = p;
		proxy = pp;
	}

	public void setPolygon(Polygon p) {
		polygon = p;
	}

	public Polygon getPolygon() {
		return polygon;
	}

	public PolygonProxy getProxy() {
		return proxy;
	}
}
