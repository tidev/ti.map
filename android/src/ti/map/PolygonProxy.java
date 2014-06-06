package ti.map;

import java.util.ArrayList;
import java.util.HashMap;

import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiContext;
import org.appcelerator.titanium.util.TiConvert;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

@Kroll.proxy(creatableInModule = MapModule.class, propertyAccessors = {
		MapModule.PROPERTY_POINTS, MapModule.PROPERTY_HOLES,
		MapModule.PROPERTY_FILL_COLOR, MapModule.PROPERTY_STROKE_COLOR,
		MapModule.PROPERTY_STROKE_WIDTH, TiC.PROPERTY_ZINDEX,
		TiC.PROPERTY_VISIBLE, MapModule.PROPERTY_GEODESIC_STATUS })
public class PolygonProxy extends KrollProxy {
	private TiPolygon polygon;
	private PolygonOptions options;

	public PolygonProxy() {
		super();
	}

	public PolygonProxy(TiContext tiContext) {
		this();
	}

	public void setTiPolygon(TiPolygon p) {
		polygon = p;
	}

	public TiPolygon getTiPolygon() {
		return polygon;
	}

	public PolygonOptions getOptions() {
		return options;
	}

	public void processOptions() {

		options = new PolygonOptions();

		if (hasProperty(MapModule.PROPERTY_POINTS)) {
			processPoints(getProperty(MapModule.PROPERTY_POINTS));
		}

		if (hasProperty(MapModule.PROPERTY_HOLES)) {
			processHoles(getProperty(MapModule.PROPERTY_HOLES));
		}

		if (hasProperty(MapModule.PROPERTY_FILL_COLOR)) {
			options.fillColor(TiConvert
					.toColor((String) getProperty(MapModule.PROPERTY_FILL_COLOR)));
		}

		if (hasProperty(MapModule.PROPERTY_STROKE_COLOR)) {
			options.strokeColor(TiConvert
					.toColor((String) getProperty(MapModule.PROPERTY_STROKE_COLOR)));
		}

		if (hasProperty(MapModule.PROPERTY_STROKE_WIDTH)) {
			options.strokeWidth(TiConvert
					.toFloat(getProperty(MapModule.PROPERTY_STROKE_WIDTH)));
		}

		if (hasProperty(TiC.PROPERTY_ZINDEX)) {
			options.zIndex(TiConvert.toFloat(getProperty(TiC.PROPERTY_ZINDEX)));
		}

		if (hasProperty(TiC.PROPERTY_VISIBLE)) {
			options.visible(TiConvert
					.toBoolean(getProperty(TiC.PROPERTY_VISIBLE)));
		}

		if (hasProperty(MapModule.PROPERTY_GEODESIC_STATUS)) {
			options.geodesic(TiConvert
					.toBoolean(getProperty(MapModule.PROPERTY_GEODESIC_STATUS)));
		}
	}

	public void processPoints(Object points) {
		if (points instanceof Object[]) {
			Object[] pointsArray = (Object[]) points;
			for (int i = 0; i < pointsArray.length; i++) {
				Object obj = pointsArray[i];
				addLocation(obj);
			}
		}
	}

	public void addLocation(Object loc) {
		LatLng location = parseLocation(loc);
		if (location != null) {
			options.add(location);
		}
	}

	// Expects holes to be a 2-d array of points
	public void processHoles(Object holes) {
		if (holes instanceof Object[]) {
			Object[] holesArray = (Object[]) holes;
			for (int i = 0; i < holesArray.length; i++) {
				ArrayList<LatLng> hole = new ArrayList<LatLng>();

				Object[] holeObj = (Object[]) holesArray[i];
				for (int j = 0; j < holeObj.length; j++) {
					Object obj = holeObj[j];
					LatLng location = parseLocation(obj);
					if (location != null) {
						hole.add(location);
					}
				}

				if (hole.size() > 0) {
					options.addHole(hole);
				}
			}
		}
	}

	// A location can either be a an array of longitude, latitude pairings or
	// an array of longitude, latitude objects.
	// e.g. [123.33, 34.44], OR {longitude: 123.33, latitude, 34.44}
	private LatLng parseLocation(Object loc) {
		LatLng location = null;
		if (loc instanceof HashMap) {
			HashMap<String, String> point = (HashMap<String, String>) loc;
			location = new LatLng(TiConvert.toDouble(point
					.get(TiC.PROPERTY_LATITUDE)), TiConvert.toDouble(point
					.get(TiC.PROPERTY_LONGITUDE)));
		} else if (loc instanceof Object[]) {
			Object[] temp = (Object[]) loc;
			location = new LatLng(TiConvert.toDouble(temp[1]),
					TiConvert.toDouble(temp[0]));
		}
		return location;
	}

}
