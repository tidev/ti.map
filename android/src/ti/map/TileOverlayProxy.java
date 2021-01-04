/**
 * Appcelerator Titanium Mobile
 * Copyright (c) 2013-2016 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 */
package ti.map;

import android.content.Context;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.maps.model.UrlTileProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.util.TiConvert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ti.map.utils.BoundingBox;
import ti.map.utils.CanvasTileProvider;
import ti.map.utils.TileProviderUrlTemplate;

@Kroll.proxy(creatableInModule = MapModule.class)
public class TileOverlayProxy extends KrollProxy
{
	private TileOverlay tileOverlay;
	private TileOverlayOptions tileOverlayOptions = new TileOverlayOptions();

	public String LCAT = MapModule.LCAT + "🚧";

	private float opacity = 1.0f;
	private int zIndex = 999;
	private int debuglevel = 0;
	private boolean yInverted = false;

	private Context ctx = TiApplication.getInstance().getApplicationContext();

	private final class UrlTileProviderHandler extends UrlTileProvider
	{
		private final String endpoint;
		private final int type;
		private final List<String> subdomains;

		private UrlTileProviderHandler(final TileProviderUrlTemplate template)
		{
			super(TileProviderUrlTemplate.TILE_WIDTH, TileProviderUrlTemplate.TILE_HEIGHT);
			this.endpoint = template.getUrlTemplate();
			this.type = template.getType();
			this.subdomains = template.getSubdomains();
			if (debuglevel > 0) {
				Log.d(LCAT, "🛠 Constructor of `UrlTileProviderHandler`");
				Log.d(LCAT, "endpoint=" + this.endpoint);
				Log.d(LCAT, "type=" + this.type);
				Log.d(LCAT, "subdomains=" + this.subdomains);
			}
		}

		@Override
		public synchronized URL getTileUrl(final int _x, final int _y, final int _z)
		{
			if (debuglevel > 0) {
				Log.d(LCAT, "getTileUrl(x=" + _x + ", y=" + _y + ", z=" + _z + ")");
			}

			int x = _x;
			int z = _z;
			int y = yInverted ? (int) (Math.pow(2, z) - 1 - _y) : _y;
			String resolvedUrlString = this.endpoint;
			try {
				switch (this.type) {
					case MapModule.TILE_OVERLAY_TYPE_WMS:
						BoundingBox bbox = new BoundingBox(x, y, z);
						resolvedUrlString =
							resolvedUrlString.replace("{bbox}", bbox.getBBox_UTM()).replace("{crs}", bbox.getCRS());
						break;
					case MapModule.TILE_OVERLAY_TYPE_WMTS:
						resolvedUrlString =
							resolvedUrlString.replace("{z}", "" + z).replace("{x}", "" + x).replace("{y}", "" + y);
						break;
					case MapModule.TILE_OVERLAY_TYPE_XYZ:
						// first the right tile depending on xyz
						resolvedUrlString = resolvedUrlString.replace("{r}", "")
												.replace("{z}", "" + z)
												.replace("{x}", "" + x)
												.replace("{y}", "" + y);
						if (subdomains != null && subdomains.size() > 0) {
							Collections.shuffle(subdomains);
							String subdomain = subdomains.get(0);
							resolvedUrlString = resolvedUrlString.replace("{s}", subdomain);
						}
						if (resolvedUrlString.contains("{time}")) {
							Calendar cal = Calendar.getInstance();
							SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
							resolvedUrlString = resolvedUrlString.replace("{time}", yyyyMMdd.format(cal.getTime()));
						}
						break;
					case MapModule.TILE_OVERLAY_TYPE_BING:
						resolvedUrlString = resolvedUrlString.replace("{quadkey}", tileXYToQuadKey(x, y, z));
						if (subdomains != null && subdomains.size() > 0) {
							Collections.shuffle(subdomains);
							String subdomain = subdomains.get(0);
							resolvedUrlString = resolvedUrlString.replace("{subdomain}", subdomain);
						}
						break;
					default:
						Log.w(LCAT, "no service for tile x:" + x + " y:" + y + " z:" + z + " found.");
				}
			} catch (Exception e) {
				Log.e(LCAT, "Failed to populate templated URL: " + resolvedUrlString, e);
			}

			if (debuglevel > 0) {
				Log.i(LCAT, resolvedUrlString);
			}
			try {
				return new URL(resolvedUrlString);
			} catch (Exception e) {
				Log.e(LCAT, "Bad URL: " + resolvedUrlString, e);
			}
			return null;
		}
	}

	public TileOverlayProxy()
	{
		super();
	}

	// http://stackoverflow.com/questions/23806348/blurred-custom-tiles-on-android-maps-v2
	@Override
	public void handleCreationDict(KrollDict o)
	{
		super.handleCreationDict(o);

		if (o.containsKeyAndNotNull(TiC.PROPERTY_OPACITY)) {
			this.opacity = TiConvert.toFloat(o.get(TiC.PROPERTY_OPACITY));
		}
		if (o.containsKeyAndNotNull(TiC.PROPERTY_ZINDEX)) {
			this.zIndex = o.getInt(TiC.PROPERTY_ZINDEX);
		}
		if (o.containsKeyAndNotNull("debuglevel")) {
			this.debuglevel = o.getInt("debuglevel");
		}

		try {
			// building urlSchema depending service:
			TileProviderUrlTemplate urlTemplate = new TileProviderUrlTemplate(o);
			String providerUrl = urlTemplate.getUrlTemplate();
			int type = urlTemplate.getType();
			TileProvider tileProvider = null;
			switch (type) {
				case MapModule.TILE_OVERLAY_TYPE_CARTODB:
					// UrlTileProviderHandler will instantiated async after http response:
					handleCartoDBUrlTileProviderHandler(urlTemplate);
					break;
				case MapModule.TILE_OVERLAY_TYPE_BING:
					handleBingUrlTileProviderHandler(urlTemplate);
					break;
				case MapModule.TILE_OVERLAY_TYPE_WMS:
					Log.d(LCAT, "handleCreationDict TILE_OVERLAY_TYPE_WMS");
					tileProvider = new UrlTileProviderHandler(urlTemplate);
					break;
				case MapModule.TILE_OVERLAY_TYPE_XYZ:
					Log.d(LCAT, "handleCreationDict TILE_OVERLAY_TYPE_XYZ");
					tileProvider = new CanvasTileProvider(new UrlTileProviderHandler(urlTemplate));
					break;
				case MapModule.TILE_OVERLAY_TYPE_WMTS:
					Log.d(LCAT, "handleCreationDict TILE_OVERLAY_TYPE_WMTS");
					tileProvider = new CanvasTileProvider(new UrlTileProviderHandler(urlTemplate));
					break;
			}
			if (tileProvider != null) {
				tileOverlayOptions.tileProvider(tileProvider).transparency(1.0f - opacity).zIndex(zIndex);
			} else {
				Log.w(LCAT, "tileProvider was null");
			}
		} catch (UnsupportedEncodingException e) {
			Log.e(LCAT, "Unsupported encoding", e);
		}
	}

	void add(GoogleMap map)
	{
		this.tileOverlay = map.addTileOverlay(tileOverlayOptions);
	}

	void remove()
	{
		if (this.tileOverlay != null) {
			this.tileOverlay.remove();
			this.tileOverlay = null;
		}
	}

	@Kroll.method
	public void destroy()
	{
		this.remove();
	}

	private void handleCartoDBUrlTileProviderHandler(TileProviderUrlTemplate urlSchema)
	{
		KrollDict options = urlSchema.getOptions();
		final String username = options.getString("username");
		final String posturl = "https://" + username + ".carto.com/api/v1/map";
		final String mapconfig = options.getString("mapconfig");
		AsyncHttpClient client = new AsyncHttpClient();
		client.post(
			ctx, posturl, new StringEntity(mapconfig, "UTF-8"), "application/json", new AsyncHttpResponseHandler() {
				@Override
				public void onStart()
				{
					if (debuglevel > 0)
						Log.d(LCAT, posturl);
				}

				@Override
				public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
				{
					// called when response HTTP status is "200 OK"
					JSONObject json = parseObject(responseBody);
					if (debuglevel > 0)
						Log.d(LCAT, json.toString());
					// try {
					// Starting event listener:
					new CanvasTileProvider(new UrlTileProviderHandler(urlSchema));
					// } catch (JSONException e) {
					// 	Log.e(LCAT, "answer from cartodb is wrong, missing 'layergroupid', mayby wrong username");
					// 	e.printStackTrace();
					// }
				}

				@Override
				public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e)
				{
					// called when response HTTP status is "4XX" (eg. 401, 403, 404)
				}

				@Override
				public void onRetry(int retryNo)
				{
				}

				private JSONObject parseObject(byte[] response)
				{
					JSONObject jsonObj = null;
					try {
						String json = new String(response, "UTF-8");
						jsonObj = new JSONObject(json);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return jsonObj;
				}
			});
	}

	private void handleBingUrlTileProviderHandler(TileProviderUrlTemplate urlSchema)
	{
		final KrollDict options = urlSchema.getOptions();
		final String apikey = options.getString("apikey");
		final String geturl = urlSchema.getUrlTemplate();

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(ctx, geturl, new AsyncHttpResponseHandler() {
			@Override
			public void onStart()
			{
				if (debuglevel > 0)
					Log.d(LCAT, geturl);
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
			{
				// called when response HTTP status is "200 OK"
				JSONObject json = parseObject(responseBody);
				if (debuglevel > 0)
					Log.d(LCAT, json.toString());
				try {
					if (json.getInt("statusCode") != 200)
						return;
					if (json.has("resourceSets") == false)
						return;
					JSONObject res =
						json.getJSONArray("resourceSets").getJSONObject(0).getJSONArray("resources").getJSONObject(0);
					// FIXME: We need to update the url template object with new subdomain list and url template?
					String providerUrl = res.getString("imageUrl");
					List<String> subdomainlist = new ArrayList<String>();
					for (int i = 0; i < res.getJSONArray("imageUrlSubdomains").length(); i++) {
						subdomainlist.add(res.getJSONArray("imageUrlSubdomains").getString(i));
					}
					// Starting event listener:
					new CanvasTileProvider(new UrlTileProviderHandler(urlSchema));

				} catch (JSONException e) {
					Log.e(LCAT, "answer from Bing is wrong, maybe wrong apikey");
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e)
			{
				// called when response HTTP status is "4XX" (eg. 401, 403, 404)
			}

			@Override
			public void onRetry(int retryNo)
			{
			}

			private JSONObject parseObject(byte[] response)
			{
				JSONObject jsonObj = null;
				try {
					String json = new String(response, "UTF-8");
					jsonObj = new JSONObject(json);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return jsonObj;
			}
		});
	}

	private static String tileXYToQuadKey(int tileX, int tileY, int zoomLevel)
	{
		StringBuilder quadKey = new StringBuilder();
		for (int i = zoomLevel; i > 0; i--) {
			char digit = '0';
			int mask = 1 << (i - 1);
			if ((tileX & mask) != 0) {
				digit++;
			}
			if ((tileY & mask) != 0) {
				digit++;
				digit++;
			}
			quadKey.append(digit);
		}

		return quadKey.toString();
	}

	public String getApiName()
	{
		return "Ti.Map.TileOverlay";
	}
}