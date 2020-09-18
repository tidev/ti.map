/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2013-2016 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ti.map;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;
import java.util.HashMap;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.proxy.TiViewProxy;
import org.appcelerator.titanium.util.TiConvert;
import org.appcelerator.titanium.view.TiUIFragment;

public class TiStreetViewPanorama extends TiUIFragment implements OnStreetViewPanoramaReadyCallback
{
	private StreetViewPanorama panorama;
	public TiStreetViewPanorama(TiViewProxy proxy, Activity activity)
	{
		super(proxy, activity);
	}

	@Override
	protected Fragment createFragment()
	{
		SupportStreetViewPanoramaFragment streetView = SupportStreetViewPanoramaFragment.newInstance();
		streetView.getStreetViewPanoramaAsync(this);
		return streetView;
	}

	private void setPosition(HashMap<String, Object> position)
	{
		double longitude = 0;
		double latitude = 0;
		if (position.containsKey(TiC.PROPERTY_LONGITUDE) && position.get(TiC.PROPERTY_LONGITUDE) != null) {
			longitude = TiConvert.toDouble(position.get(TiC.PROPERTY_LONGITUDE));
		}
		if (position.containsKey(TiC.PROPERTY_LATITUDE) && position.get(TiC.PROPERTY_LATITUDE) != null) {
			latitude = TiConvert.toDouble(position.get(TiC.PROPERTY_LATITUDE));
		}

		LatLng location = new LatLng(latitude, longitude);
		panorama.setPosition(location);
	}

	public void processStreetProperties(KrollDict options)
	{
		if (options.containsKey(TiC.PROPERTY_POSITION)) {
			setPosition(options.getKrollDict(TiC.PROPERTY_POSITION));
		}
		if (options.containsKey(MapModule.PROPERTY_PANNING)) {
			panorama.setPanningGesturesEnabled(TiConvert.toBoolean(options, MapModule.PROPERTY_PANNING, true));
		}
		if (options.containsKey(MapModule.PROPERTY_ZOOM)) {
			panorama.setZoomGesturesEnabled(TiConvert.toBoolean(options, MapModule.PROPERTY_ZOOM, true));
		}
		if (options.containsKey(MapModule.PROPERTY_STREET_NAMES)) {
			panorama.setStreetNamesEnabled(TiConvert.toBoolean(options, MapModule.PROPERTY_STREET_NAMES, true));
		}
		if (options.containsKey(MapModule.PROPERTY_USER_NAVIGATION)) {
			panorama.setUserNavigationEnabled(TiConvert.toBoolean(options, MapModule.PROPERTY_USER_NAVIGATION, true));
		}
	}

	@Override
	public void propertyChanged(String key, Object oldValue, Object newValue, KrollProxy proxy)
	{
		if (panorama == null || newValue == null) {
			return;
		}

		if (key.equals(TiC.PROPERTY_POSITION)) {
			setPosition((HashMap) newValue);
		} else if (key.equals(MapModule.PROPERTY_PANNING)) {
			panorama.setPanningGesturesEnabled(TiConvert.toBoolean(newValue, true));
		} else if (key.equals(MapModule.PROPERTY_ZOOM)) {
			panorama.setZoomGesturesEnabled(TiConvert.toBoolean(newValue, true));
		} else if (key.equals(MapModule.PROPERTY_STREET_NAMES)) {
			panorama.setStreetNamesEnabled(TiConvert.toBoolean(newValue, true));
		} else if (key.equals(MapModule.PROPERTY_USER_NAVIGATION)) {
			panorama.setUserNavigationEnabled(TiConvert.toBoolean(newValue, true));
		} else {
			super.propertyChanged(key, oldValue, newValue, proxy);
		}
	}

	@Override
	public void onStreetViewPanoramaReady(StreetViewPanorama panorama)
	{
		this.panorama = panorama;
		processStreetProperties(proxy.getProperties());
	}

	//TODO: This method does not override anything from the super class and cannot be kept here for backward compatibility.
	//@Override
	protected void onViewCreated()
	{
		// Keep this method for backward compat.
	}
}
