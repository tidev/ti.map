package ti.map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
// from: https://github.com/googlemaps/android-maps-utils
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.util.TiConvert;

@Kroll.proxy(creatableInModule = MapModule.class)
public class HeatmapOverlayProxy extends KrollProxy
{
	/**
	 * Colors for default gradient. Array of colors, represented by ints.
	 */
	private static final int[] DEFAULT_GRADIENT_COLORS = { Color.rgb(102, 225, 0), Color.rgb(255, 0, 0) };
	private static final float[] DEFAULT_GRADIENT_START_POINTS = { 0.2f, 1f };
	private static final int DEFAULT_RADIUS = 20;
	private float[] gradientStartPoints = { DEFAULT_GRADIENT_START_POINTS[0], DEFAULT_GRADIENT_START_POINTS[1] };
	private int[] gradientColors = { DEFAULT_GRADIENT_COLORS[0], DEFAULT_GRADIENT_COLORS[1] };
	private int radius = DEFAULT_RADIUS;

	public static final Gradient DEFAULT_GRADIENT =
		new Gradient(DEFAULT_GRADIENT_COLORS, DEFAULT_GRADIENT_START_POINTS);
	private Gradient gradient = new Gradient(gradientColors, gradientStartPoints);

	private float opacity = 1.0f;
	private int zIndex = 0;
	private TileOverlay tileOverlay;
	public String LCAT = MapModule.LCAT + "Heatmap";
	HeatmapTileProvider heatmapTileProvider;

	private List<LatLng> pointList = null;

	public HeatmapOverlayProxy()
	{
		super();
	}

	private void addLocation(Object loc, List<LatLng> locationArray)
	{

		if (loc instanceof HashMap) {
			Log.d(LCAT, "type HashMap");
			HashMap<String, String> point = (HashMap<String, String>) loc;
			Object latitude = point.get(TiC.PROPERTY_LATITUDE);
			Object longitude = point.get(TiC.PROPERTY_LONGITUDE);
			if (longitude != null && latitude != null) {
				LatLng location = new LatLng(TiConvert.toDouble(latitude), TiConvert.toDouble(longitude));
				locationArray.add(location);
			}
		} else if (loc instanceof Object[]) {

			Object[] temp = (Object[]) loc;
			LatLng location = new LatLng(TiConvert.toDouble(temp[1]), TiConvert.toDouble(temp[0]));
			locationArray.add(location);
		}
	}

	private List<LatLng> processPoints(Object points)
	{
		List<LatLng> locationArray = new ArrayList<LatLng>();
		if (points instanceof Object[]) {
			Object[] pointsArray = (Object[]) points;
			for (int i = 0; i < pointsArray.length; i++) {
				Object obj = pointsArray[i];
				addLocation(obj, locationArray);
			}
			return locationArray;
		}
		addLocation(points, locationArray);
		return locationArray;
	}

	// http://stackoverflow.com/questions/23806348/blurred-custom-tiles-on-android-maps-v2
	@Override
	public void handleCreationDict(KrollDict o)
	{
		super.handleCreationDict(o);
		String sql = null;
		String dbname = null;

		if (o.containsKeyAndNotNull(TiC.PROPERTY_OPACITY)) {
			opacity = TiConvert.toFloat(o.getDouble(TiC.PROPERTY_OPACITY));
		}
		if (o.containsKeyAndNotNull(TiC.PROPERTY_ZINDEX)) {
			zIndex = o.getInt(TiC.PROPERTY_ZINDEX);
		}
		if (o.containsKeyAndNotNull(MapModule.PROPERTY_POINTS)) {
			pointList = processPoints(getProperty(MapModule.PROPERTY_POINTS));
		}
		if (o.containsKeyAndNotNull("dbname")) {
			dbname = o.getString("dbname");
		}
		if (o.containsKeyAndNotNull("sql")) {
			sql = o.getString("sql");
		}
		if (dbname != null && sql != null) {
			importFromDB(dbname, sql);
		}
	}

	private void importFromDB(String dbname, String sql)
	{
		Context ctx = TiApplication.getInstance().getApplicationContext();
		List<LatLng> locationArray = new ArrayList<LatLng>();
		SQLiteDatabase db = ctx.openOrCreateDatabase(dbname, Context.MODE_PRIVATE, null);
		Cursor c = db.rawQuery(sql, null);
		if (c.moveToFirst()) {
			do {
				locationArray.add(new LatLng(c.getDouble(0), c.getDouble(1)));
			} while (c.moveToNext());
		}
		c.close();
		db.close();
	}

	public void setHeatmapOverlay(TileOverlay tileOverlay)
	{
		this.tileOverlay = tileOverlay;
	}

	public TileOverlayOptions getTileOverlayOptions()
	{
		Log.d(LCAT, "TileOverlayOptions getOptions");
		if (pointList != null)
			Log.d(LCAT, pointList.toString());
		else
			Log.d(LCAT, "pointList ist null");
		// https://developers.google.com/maps/documentation/android-sdk/utility/heatmap
		heatmapTileProvider = new HeatmapTileProvider.Builder()
								  .data(pointList)
								  .opacity(opacity)
								  .radius(radius)
								  .gradient(gradient)
								  .build();
		TileOverlayOptions tileOverlayOptions = new TileOverlayOptions();
		tileOverlayOptions.tileProvider(heatmapTileProvider);
		return tileOverlayOptions;
	}
}