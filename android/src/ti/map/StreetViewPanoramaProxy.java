/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2013-2016 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ti.map;

import android.app.Activity;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.view.TiUIView;

@Kroll.proxy(creatableInModule = MapModule.class,
			 propertyAccessors = { TiC.PROPERTY_POSITION, MapModule.PROPERTY_PANNING, MapModule.PROPERTY_ZOOM,
								   MapModule.PROPERTY_STREET_NAMES, MapModule.PROPERTY_USER_NAVIGATION })
public class StreetViewPanoramaProxy extends ViewProxy
{
	public StreetViewPanoramaProxy()
	{
		super();
	}

	@Override
	public TiUIView createView(Activity activity)
	{
		return new TiStreetViewPanorama(this, activity);
	}

	public String getApiName()
	{
		return "Ti.Map.StreetViewPanorama";
	}
}
