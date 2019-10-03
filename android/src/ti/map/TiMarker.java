/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

package ti.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterItem;

public class TiMarker implements ClusterItem
{
	private Marker marker;
	protected AnnotationProxy proxy;

	public TiMarker(Marker m, AnnotationProxy p)
	{
		marker = m;
		proxy = p;
	}

	public void setMarker(Marker m)
	{
		marker = m;
	}
	public Marker getMarker()
	{
		return marker;
	}

	public AnnotationProxy getProxy()
	{
		return proxy;
	}

	public void release()
	{
		if (marker != null) {
			marker.remove();
			marker = null;
		}
		if (proxy != null) {
			proxy.releaseMarker();
			proxy = null;
		}
	}

	@Override
	public LatLng getPosition()
	{
		return proxy.getMarkerOptions().getPosition();
	}

	@Override
	public String getTitle()
	{
		return proxy.getTitle();
	}

	@Override
	public String getSnippet()
	{
		return proxy.getSubtitle();
	}
}
