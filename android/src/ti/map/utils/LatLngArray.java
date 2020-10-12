package ti.map.utils;

import com.google.android.gms.maps.model.LatLng;
import java.util.HashMap;
import java.util.List;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.util.TiConvert;

public class LatLngArray
{
	private List<LatLng> list;

	public LatLngArray()
	{
	}

	public void addLatLng(Object o)
	{
		LatLng location = parseLatLng(o);
		if (location != null)
			list.add(location);
	}

	// A location can either be a an array of longitude, latitude pairings or
	// an array of longitude, latitude objects.
	// e.g. [123.33, 34.44], OR {longitude: 123.33, latitude, 34.44}
	public LatLng parseLatLng(Object o)
	{
		LatLng location = null;
		if (o instanceof HashMap) {
			HashMap<String, String> point = (HashMap<String, String>) o;
			location = new LatLng(TiConvert.toDouble(point.get(TiC.PROPERTY_LATITUDE)),
								  TiConvert.toDouble(point.get(TiC.PROPERTY_LONGITUDE)));
		} else if (o instanceof Object[]) {
			Object[] temp = (Object[]) o;
			location = new LatLng(TiConvert.toDouble(temp[1]), TiConvert.toDouble(temp[0]));
		}
		return location;
	}
}
