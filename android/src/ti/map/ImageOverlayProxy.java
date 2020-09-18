/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2018 by Axway, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */

package ti.map;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.view.TiDrawableReference;

@Kroll.proxy(creatableInModule = MapModule.class)
public class ImageOverlayProxy extends KrollProxy
{

	private static final String TAG = "ImageOverlayProxy";

	private static final String PROPERTY_BOUNDS_COORDINATE = "boundsCoordinate";
	private static final String PROPERTY_IMAGE = "image";
	private static final String PROPERTY_TOP_LEFT = "topLeft";
	private static final String PROPERTY_BOTTOM_RIGHT = "bottomRight";
	private static final String PROPERTY_LATITUDE = "latitude";
	private static final String PROPERTY_LONGITUDE = "longitude";

	private GroundOverlay groundOverlay;

	private GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions();
	@Override
	public void handleCreationDict(KrollDict dict)
	{
		super.handleCreationDict(dict);
		if (dict.containsKeyAndNotNull(PROPERTY_BOUNDS_COORDINATE)) {
			handleBoundsCoordinate(dict.getKrollDict(PROPERTY_BOUNDS_COORDINATE));
		}
		if (dict.containsKeyAndNotNull(PROPERTY_IMAGE)) {
			handleImage(dict.get(PROPERTY_IMAGE));
		}
	}

	public GroundOverlay getGroundOverlay()
	{
		return groundOverlay;
	}

	public void setGroundOverlay(GroundOverlay groundOverlay)
	{
		this.groundOverlay = groundOverlay;
	}

	private void handleBoundsCoordinate(KrollDict boundsCoordinateDict)
	{
		KrollDict topLeft = boundsCoordinateDict.getKrollDict(PROPERTY_TOP_LEFT);
		KrollDict botRight = boundsCoordinateDict.getKrollDict(PROPERTY_BOTTOM_RIGHT);
		// Switching some coordinates to transform from topLeft/botRight to southWest/northEast base
		LatLng southWest = new LatLng(botRight.getDouble(PROPERTY_LATITUDE).doubleValue(),
									  topLeft.getDouble(PROPERTY_LONGITUDE).doubleValue());
		LatLng northEast = new LatLng(topLeft.getDouble(PROPERTY_LATITUDE).doubleValue(),
									  botRight.getDouble(PROPERTY_LONGITUDE).doubleValue());
		LatLngBounds latLng = new LatLngBounds(southWest, northEast);
		groundOverlayOptions.positionFromBounds(latLng);
		groundOverlayOptions.visible(true);
	}

	private void handleImage(Object image)
	{
		TiDrawableReference source = TiDrawableReference.fromObject(this, image);
		if (!source.isTypeNull()) {
			BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(source.getBitmap());
			groundOverlayOptions.image(bitmapDescriptor);
		}
	}

	public GroundOverlayOptions getGroundOverlayOptions()
	{
		return groundOverlayOptions;
	}

	@Override
	public String getApiName()
	{
		return "Ti.Map.ImageOverlay";
	}
}