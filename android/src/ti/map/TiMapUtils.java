package ti.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.util.TiConvert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TiMapUtils {

    // A location can either be a an array of longitude, latitude pairings or
    // an array of longitude, latitude objects.
    // e.g. [123.33, 34.44], OR {longitude: 123.33, latitude, 34.44}
    public static LatLng parseLocation(Object loc)
    {
        LatLng location = null;
        if (loc instanceof HashMap) {
            HashMap<String, String> point = (HashMap<String, String>) loc;
            location = new LatLng(TiConvert.toDouble(point.get(TiC.PROPERTY_LATITUDE)),
                    TiConvert.toDouble(point.get(TiC.PROPERTY_LONGITUDE)));
        } else if (loc instanceof Object[]) {
            Object[] temp = (Object[]) loc;
            location = new LatLng(TiConvert.toDouble(temp[1]), TiConvert.toDouble(temp[0]));
        }
        //		Log.w("TiApp MAP", "center lat lng " + location.latitude + ", " + location.longitude);
        return location;
    }

    public static ArrayList<LatLng> processPoints(Object points)
    {
        ArrayList<LatLng> locationArray = new ArrayList<>();

        // encoded (result from routing API)
        if (points instanceof String) {
            List<LatLng> locationList = PolyUtil.decode((String) points);
            return new ArrayList<>(locationList);
        // multiple points
        } else if (points instanceof Object[]) {
            Object[] pointsArray = (Object[]) points;
            for (int i = 0; i < pointsArray.length; i++) {
                Object obj = pointsArray[i];
                LatLng location = TiMapUtils.parseLocation(obj);
                locationArray.add(location);
            }
            return locationArray;
        }

        return locationArray;
    }
}
