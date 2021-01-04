package ti.map.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.common.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ti.map.MapModule;

public class LeafletProvidersFactory
{
	public String LCAT = MapModule.LCAT + "🚡";

	private String url;
	private JSONObject options;
	private JSONObject variants;

	public String[] getVariantNames()
	{
		try {
			if (this.variants == null) {
				if (this.options != null) {
					if (this.options.has("variant")) {
						return new String[] { this.options.getString("variant") };
					}
				}
				return new String[0];
			}

			List<String> list = new ArrayList<String>();
			Iterator<String> vkeys = this.variants.keys();
			while (vkeys.hasNext()) {
				list.add(vkeys.next());
			}
			return list.toArray(new String[list.size()]);
		} catch (JSONException e) {
			Log.e(LCAT, "Error parsing variant names", e);
		}
		return new String[0];
	}

	/**
	 * @param urlTemplate base url template
	 * @param options options to inject into url template
	 * @return String
	 */
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

	private Map<String, Object> getOptions(JSONObject variant) throws JSONException
	{
		if (!variant.has("options")) {
			return new HashMap<>();
		}
		return convertToMap(variant.getJSONObject("options"));
	}

	private Map<String, Object> convertToMap(JSONObject options) throws JSONException
	{
		Map<String, Object> map = new HashMap<>();
		Iterator<String> keys = options.keys();
		while (keys.hasNext()) {
			String keyName = keys.next();
			map.put(keyName, options.get(keyName));
		}
		return map;
	}

	// This should return a KrollDict with three properties:
	// "url" - the url template string to use
	// "options" - a Map of key/value pairs used to inject into the string
	// "options.subdomains" - list of subdomains to use
	// "options.variant" - name of varient to inject into url template
	public KrollDict getVariant(String vName)
	{
		Log.d(LCAT, "getVariant(" + vName + ")");

		KrollDict result = new KrollDict();
		result.put("url", this.url);

		try {
			// build up options/ usee top-level as base
			KrollDict options = new KrollDict(this.options);
			// if we're trying to get sub-variant, find it and get it's options/url overrides
			if (vName != null && this.variants != null && this.variants.has(vName)) {
				Object o = this.variants.get(vName);
				if (o instanceof JSONObject) {
					JSONObject variantObj = (JSONObject) o;
					// merge variant optiosn over top base/top-level options
					if (variantObj.has("options")) {
						options.putAll(new KrollDict(variantObj.getJSONObject("options")));
					}
					// override url with variant url
					if (variantObj.has("url")) {
						result.put("url", variantObj.getString("url"));
					}
				} else {
					// override result.options.variant
					options.put("variant", o.toString());
				}
			}
			result.put("options", options);
		} catch (JSONException e) {
			result.put("options", new KrollDict());
			Log.e(LCAT, "Error handling JSON for provider: " + this.url, e);
		}
		return result;
	}

	public LeafletProvidersFactory(JSONObject provider)
	{
		JSONObject options;
		if (provider == null) {
			throw new IllegalStateException("JSONObject for provider is null!");
		}

		try {
			this.url = provider.getString("url");
			if (provider.has("options")) {
				this.options = provider.getJSONObject("options");
			}
			if (provider.has("variants")) {
				this.variants = provider.getJSONObject("variants");
			}
		} catch (JSONException e) {
			// ignore?
		}
	}
}