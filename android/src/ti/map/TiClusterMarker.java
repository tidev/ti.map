/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2013 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

package ti.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class TiClusterMarker implements ClusterItem {
 
    private LatLng position;
	private AnnotationProxy proxy;
 
    public TiClusterMarker(AnnotationProxy p) {
        proxy = p;
        position = p.getMarkerOptions().getPosition();
    }
 
    @Override
    public LatLng getPosition() {
        return position;
    }
 
    public void setPosition( LatLng position ) {
        this.position = position;
    }

    public AnnotationProxy getProxy() {
        return proxy;
    }

    @Override
    public String getTitle() {
        return proxy.getTitle();
    }

    @Override
    public String getSnippet() {
        return null;
    }
    
    public void release() {
        if (proxy != null) {
            proxy.release();
            proxy = null;
        }
    }
}
