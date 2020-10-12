package ti.map.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.util.TiConvert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ti.map.LeafletProvidersProxy;
import ti.map.MapModule;

public class TileProviderUrlTemplate
{
	private final String LCAT = MapModule.LCAT;

	public static int TILE_WIDTH = 512;
	public static int TILE_HEIGHT = TILE_WIDTH;

	// key/value pairs to bake in to url
	private KrollDict options;

	// type of service
	private int type = -1;

	// the pre-baked url template
	private String urlTemplate;

	// The list of subdomains to dynamically inject into url
	private List<String> subdomains;

	public TileProviderUrlTemplate(KrollDict opts) throws UnsupportedEncodingException
	{
		String name = null;
		if (opts.containsKeyAndNotNull(TiC.PROPERTY_NAME)) {
			name = opts.getString(TiC.PROPERTY_NAME);
			type = MapModule.TILE_OVERLAY_TYPE_XYZ; // name implies XYZ type
		}
		if (opts.containsKeyAndNotNull("type")) {
			type = opts.getInt("type");
			if (name != null && type != MapModule.TILE_OVERLAY_TYPE_XYZ) {
				Log.w(LCAT, "With property 'name' specified, 'type' should be TILE_OVERLAY_TYPE_XYZ");
				type = MapModule.TILE_OVERLAY_TYPE_XYZ;
			}
		}

		this.options = new KrollDict();
		if (opts.containsKeyAndNotNull("options")) {
			this.options = (KrollDict) opts.getKrollDict("options");
		}

		String rawUrlTemplate = null;
		switch (type) {
			case MapModule.TILE_OVERLAY_TYPE_CARTODB:
				if (!this.options.containsKeyAndNotNull("username")) {
					Log.e(LCAT, "CartoDB needs `options.username` set.");
					return;
				}
				rawUrlTemplate = "https://{username}.carto.com/api/v1/map/{layergroupid}/{z}/{x}/{y}.png";
				break;
			case MapModule.TILE_OVERLAY_TYPE_XYZ:
				LeafletProvidersProxy providerDB = new LeafletProvidersProxy();
				KrollDict tileProviderParams = providerDB.getTileProvider(name);
				rawUrlTemplate = tileProviderParams.getString("url");
				KrollDict bakedOptions = tileProviderParams.getKrollDict("options");
				if (bakedOptions != null) {
					// merge user supplied options over top the ones from baked in json
					bakedOptions.putAll(this.options);
					this.options = bakedOptions;
				}
				break;
			case MapModule.TILE_OVERLAY_TYPE_WMS:
				if (!this.options.containsKeyAndNotNull("version")) {
					Log.e(LCAT, "WMS needs `options.version` set.");
					return;
				}
				if (!this.options.containsKeyAndNotNull("format")) {
					Log.e(LCAT, "WMS needs `options.format` set.");
					return;
				}
				if (!this.options.containsKeyAndNotNull("layer")) {
					Log.e(LCAT, "WMS needs `options.layer` set.");
					return;
				}
				rawUrlTemplate = "?service=WMS&version={version}"
								 + "&request=GetMap"
								 + "&format={format}"
								 + "&layers={layer}"
								 + "&width=" + TILE_WIDTH + "&height=" + TILE_HEIGHT
								 + "&srs={crs}&bbox={bbox}&crs={crs}";
				if (this.options.containsKeyAndNotNull("style")) {
					rawUrlTemplate += "&styles={style}";
				}
				if (this.options.containsKeyAndNotNull("transparent")) {
					rawUrlTemplate += "&transparent={transparent}";
				}
				if (this.options.containsKeyAndNotNull("bgColor")) {
					rawUrlTemplate += "&bgcolor={bgColor}";
				}
				Log.d(LCAT, "WMS = " + rawUrlTemplate + "  built!");
				break;
			case MapModule.TILE_OVERLAY_TYPE_WMTS:
				if (!this.options.containsKeyAndNotNull("version")) {
					this.options.put("version", "1.0.0");
				}
				rawUrlTemplate = "?Service=WMTS&Request=getTile&Version={version}"
								 + "&Layer={layer}"
								 + "&TileMatrix={z}&TileRow={y}&TileCol={x}&Format={format}";
				if (this.options.containsKeyAndNotNull("tilematrixset")) {
					rawUrlTemplate += "&TileMatrixSet={tilematrixset}";
				}
				if (this.options.containsKeyAndNotNull("style")) {
					rawUrlTemplate += "&Style={style}";
				}

				break;
			case MapModule.TILE_OVERLAY_TYPE_BING:
				// FIXME: This is not right!
				rawUrlTemplate = "http://dev.virtualearth.net/REST/V1/Imagery/Metadata/{imagerySet}"
								 + "?output=json&include=ImageryProviders&key={apikey}";
				//  /{centerPoint}?orientation={orientation}&zoomLevel={zoomLevel}&include={ImageryProviders}&
				break;
		}

		Log.d(LCAT, "Raw url template built: " + rawUrlTemplate);

		this.urlTemplate = convertEndpoint(rawUrlTemplate, this.options);
		this.subdomains = new ArrayList<String>();
		String[] subdomains = getSubdomains(this.options.get("subdomains"));
		if (subdomains != null) {
			Collections.addAll(this.subdomains, subdomains);
		}

		Log.d(LCAT, "Baked url template built: " + urlTemplate);
		Log.d(LCAT, "Subdomains: " + this.subdomains);
		Log.d(LCAT, "options: " + this.options);

		// OK, now we need to find all templated variables in the url string, ensure we have values for
		// any that are missing except: x, y, z, s?
	}

	private String[] getSubdomains(Object o)
	{
		if (o instanceof String) {
			String subs = (String) o;
			return Arrays.copyOfRange(subs.split(""), 1, subs.split("").length);
		}

		if (o instanceof Object[]) {
			Object[] arr = (Object[]) o;
			String[] list = new String[arr.length];
			for (int i = 0; i < arr.length; i++) {
				list[i] = (String) arr[i];
			}
			return list;
		}
		return null; // default
	}

	private String convertEndpoint(String urlTemplate, Map<String, Object> options)
	{
		// Go through each of the keys of options, replace relevant value in url string with it?
		String url = urlTemplate;
		for (String keyName : options.keySet()) {
			if (!keyName.equals("subdomains")) { // subdomains are special
				// FIXME: Use helper method to convert values instead of toString?
				url = url.replace("{" + keyName + "}", options.get(keyName).toString());
			}
		}
		// If maxZoom is not set, default to 19?
		url = url.replace("{maxZoom}", "19");
		// If ext is not in options, replace with empty string?
		url = url.replace("{ext}", "");
		return url;
	}

	public String getUrlTemplate()
	{
		return this.urlTemplate;
	}

	public int getType()
	{
		return this.type;
	}

	public List<String> getSubdomains()
	{
		return this.subdomains;
	}

	public KrollDict getOptions()
	{
		return this.options;
	}
}
