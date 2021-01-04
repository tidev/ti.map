/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2013-2016 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ti.map;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.io.TiFileFactory;
import org.json.JSONException;
import org.json.JSONObject;
import ti.map.utils.LeafletProvidersFactory;

@Kroll.proxy(name = "LeafletProviders", creatableInModule = MapModule.class)
public class LeafletProvidersProxy extends KrollProxy
{
	private static final String LCAT = MapModule.LCAT;
	private static Map<String, LeafletProvidersFactory> providerList;

	public LeafletProvidersProxy()
	{
		super();

		// FIXME: Properly only instantiate once
		if (providerList == null) {
			providerList = new HashMap<String, LeafletProvidersFactory>();
			InputStream in = null;
			try {
				ClassLoader classLoader = getClass().getClassLoader();
				in = classLoader.getResourceAsStream("Leaflet.Providers.json");
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buffer = new byte[4096];
				int count = 0;
				while ((count = in.read(buffer)) != -1) {
					out.write(buffer, 0, count);
				}

				String contents = new String(out.toByteArray(), "UTF-8");
				JSONObject Providers = new JSONObject(contents);
				Iterator<String> keys = Providers.keys();
				while (keys.hasNext()) {
					String providerName = keys.next();
					providerList.put(providerName, new LeafletProvidersFactory(Providers.getJSONObject(providerName)));
				}
			} catch (Exception e) {
				Log.e(LCAT, "Failed to load Leaflet.Providers.json", e);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException ioe) {
						// ignore
					}
				}
			}
		}
	}

	@Kroll.method
	public String[] getProviderNames()
	{
		return getAllProviderNames();
	}

	@Kroll.method
	public String[] getAllNames()
	{
		List<String> list = new ArrayList<String>();
		for (String provider : providerList.keySet()) {
			String[] variants = getAllVariantNamesByProvider(provider);
			if (variants.length > 0) {
				for (String variant : variants) {
					list.add(provider + "/" + variant);
				}
			} else {
				list.add(provider);
			}
		}
		return list.toArray(new String[list.size()]);
	}

	@Kroll.method
	public String[] getAllProviderNames()
	{
		return providerList.keySet().toArray(new String[providerList.size()]);
	}

	@Kroll.method
	public String[] getAllVariantNamesByProvider(String pName)
	{
		return providerList.get(pName).getVariantNames();
	}

	@Kroll.method
	public String[] getVariantNamesByProvider(String pName)
	{
		return getAllVariantNamesByProvider(pName);
	}

	@Kroll.method
	public KrollDict getTileProvider(String endpoint)
	{
		Log.d(LCAT, "getTileProvider(" + endpoint + ")");
		if (endpoint.contains("/")) {
			String[] pair = endpoint.split("/");
			String main = pair[0];
			String variant = pair[1];
			if (providerList.containsKey(main) == false) {
				Log.e(LCAT, "TileProvider with name " + main
								+ " not found, here the names of all available providers: " + getAllNames().toString());
				return null;
			}
			return providerList.get(main).getVariant(variant);
		} else {
			if (providerList.containsKey(endpoint) == false) {
				Log.e(LCAT, "TileProvider with name " + endpoint + " not found. Available names are "
								+ getAllNames().toString());
				return null;
			}
			LeafletProvidersFactory provider = providerList.get(endpoint);
			return provider.getVariant(null);
		}
	}

	@Kroll.method
	public String getTileImage(KrollDict position)
	{
		double lat = 0f;
		double lng = 0f;
		int x = 0;
		int y = 0;
		String tileProvider;
		int zoom = 0;
		KrollDict kd = new KrollDict();
		KrollDict variant = new KrollDict();
		if (position.containsKeyAndNotNull("tileProvider")) {
			String endpoint = position.getString("tileProvider");
			if (endpoint.contains("/")) {
				String[] pair = endpoint.split("/");
				variant = providerList.get(pair[0]).getVariant(pair[1]);

			} else {
				variant = providerList.get(endpoint).getVariant(null);
			}
		}
		if (position.containsKeyAndNotNull("lat") && position.containsKeyAndNotNull("lng")) {
			lat = position.getDouble("lat");
			lng = position.getDouble("lat");
			zoom = position.getInt("zoom");
			y = (int) (Math.floor(
				(1 - Math.log(Math.tan(lat * Math.PI / 180) + 1 / Math.cos(lat * Math.PI / 180)) / Math.PI) / 2
				* Math.pow(2, zoom)));
			x = (int) (Math.floor((lng + 180) / 360 * Math.pow(2, zoom)));
		}
		return variant.getString("url").replace("{x}", "" + x).replace("{y}", "" + y).replace("{z}", "" + zoom);
	}

	private String loadJSONFromAsset(String asset)
	{
		String json = null;
		try {
			InputStream inStream = TiFileFactory.createTitaniumFile(new String[] { asset }, false).getInputStream();
			byte[] buffer = new byte[inStream.available()];
			inStream.read(buffer);
			inStream.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;
	}
}
