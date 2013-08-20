/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

package ti.map;

import com.google.android.gms.maps.model.Marker;

public class TiMarker {
	private Marker marker;
	private AnnotationProxy proxy;
	
	public TiMarker(Marker m, AnnotationProxy p) {
		marker = m;
		proxy = p;
	}
	
	public void setMarker(Marker m) {
		marker = m;
	}
	public Marker getMarker() {
		return marker;
	}
	
	public AnnotationProxy getProxy() {
		return proxy;
	}
}
