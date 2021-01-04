package ti.map.utils;

import org.appcelerator.kroll.common.Log;
import ti.map.utils.Deg2UTM;
import uk.me.jstott.jcoord.LatLng;
import uk.me.jstott.jcoord.UTMRef;

/**
 * 
 * @author Rainer Schleevoigt <rainer@schleevoigt.com>
 */
public class BoundingBox
{
	final static double RADIUS = 6378137.0;
	final static int TILESIZE = 256;
	final static double INITIALRESOLUTION = 2 * Math.PI * RADIUS / TILESIZE;
	final static double ORIGINSHIFT = Math.PI * RADIUS;
	// private static final CoordinateReferenceSystem wgs84 =
	// DefaultGeographicCRS.WGS84;
	// Web Mercator n/w corner of the map.
	private static final double[] TILE_ORIGIN = { -20037508.34789244, 20037508.34789244 };
	// array indexes for that data
	private static final int ORIG_X = 0;
	private static final int ORIG_Y = 1; // "

	// Size of square world map in meters, using WebMerc projection.
	private static final double MAP_SIZE = 20037508.34789244 * 2;

	// array indexes for array to hold bounding boxes.
	protected static final int MINX = 0;
	protected static final int MAXX = 1;
	protected static final int MINY = 2;
	protected static final int MAXY = 3;
	final static String LCAT = "BBOX";
	final static String UTM = "EPSG:258**";
	final static String GOOGLE = "EPSG:3857";
	final static String WGS84 = "EPSG:4326";

	LatLng[] latlngBBox;
	double[] googleBox;
	double xmin;
	double ymin;
	double xmax;
	double ymax;

	public BoundingBox(double tx, double ty, int zoom)
	{
		// https://ariasprado.name/2012/08/13/quick-and-dirty-coordinate-transforming-using-geotools.html
		googleBox = tileBounds(tx, ty, zoom);
		latlngBBox = tileLatLonBounds(tx, ty, zoom);
		xmin = Math.min(latlngBBox[0].getLng(), latlngBBox[1].getLng());
		ymin = Math.min(latlngBBox[0].getLat(), latlngBBox[1].getLat());
		xmax = Math.max(latlngBBox[0].getLng(), latlngBBox[1].getLng());
		ymax = Math.max(latlngBBox[0].getLat(), latlngBBox[1].getLat());
	}

	public String getCRS()
	{
		return "EPSG:258" + new Deg2UTM(ymin, xmin).Zone;
	}

	public int getUTMZone()
	{
		return new Deg2UTM(ymin, xmin).Zone;
	}

	public String getBBox_WGS84()
	{
		return xmin + "," + ymin + "," + xmax + "," + ymax;
	}

	public String getBBox_UTM()
	{
		String BB = String.valueOf(
			new Deg2UTM(ymin, xmin).Easting + "," + String.valueOf(new Deg2UTM(ymin, xmin).Northing) + ","
			+ String.valueOf(new Deg2UTM(ymax, xmax).Easting) + "," + String.valueOf(new Deg2UTM(ymax, xmax).Northing));
		return BB;
	}

	private static UTMRef latlngToUTM(double longitude, double latitude)
	{
		LatLng coordinates = new LatLng(latitude, longitude);
		return coordinates.toUTMRef();
	}

	// Returns bounds of the given tile in EPSG:900913 coordinates
	private static double[] tileBounds(double tx, double ty, int zoom)
	{
		double[] min = pixelsToMeters(tx * TILESIZE, ty * TILESIZE, zoom);
		double[] max = pixelsToMeters((tx + 1) * TILESIZE, (ty + 1) * TILESIZE, zoom);
		double[] ret = new double[4];
		System.arraycopy(min, 0, ret, 0, 2);
		System.arraycopy(max, 0, ret, 2, 2);

		return ret;
	}

	// Return a web Mercator bounding box given tile x/y indexes and a zoom
	// level.
	protected double[] getBoundingBox(double x, double y, int zoom)
	{
		double tileSize = MAP_SIZE / Math.pow(2, zoom);
		double minx = TILE_ORIGIN[ORIG_X] + x * tileSize;
		double maxx = TILE_ORIGIN[ORIG_X] + (x + 1) * tileSize;
		double miny = TILE_ORIGIN[ORIG_Y] - (y + 1) * tileSize;
		double maxy = TILE_ORIGIN[ORIG_Y] - y * tileSize;

		double[] bbox = new double[4];
		bbox[MINX] = minx;
		bbox[MINY] = miny;
		bbox[MAXX] = maxx;
		bbox[MAXY] = maxy;

		return bbox;
	}

	// Converts pixel coordinates in given zoom level of pyramid to
	// EPSG:900913
	private static double[] pixelsToMeters(double px, double py, int zoom)
	{
		double res = resolution(zoom);
		double x = px * res - ORIGINSHIFT;
		double y = py * res - ORIGINSHIFT;
		double[] ret = { Math.abs(x), Math.abs(y) };
		return ret;
	}

	// Resolution (meters/pixel) for given zoom level (measured at Equator)
	private static double resolution(int zoom)
	{
		return INITIALRESOLUTION / Math.pow(2, zoom);
	}

	// Converts XY point from Spherical Mercator EPSG:900913 to lat/lon in WGS84
	// Datum
	static LatLng metersToLatLon(double mx, double my)
	{
		double lon = (mx / ORIGINSHIFT) * 180;
		double lat = (my / ORIGINSHIFT) * 180;
		lat = 180 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180)) - Math.PI / 2);
		return new LatLng(lat, lon);
	}

	// Returns bounds of the given tile in latutude/longitude using WGS84 datum
	private static LatLng[] tileLatLonBounds(double tx, double ty, int zoom)
	{
		double[] bounds = tileBounds(tx, ty, zoom);
		LatLng min = metersToLatLon(bounds[0], bounds[3]);
		LatLng max = metersToLatLon(bounds[2], bounds[1]);
		LatLng[] ret = new LatLng[2];
		// Log.i("MIN = ", min.toString());
		// Log.i("MAX = ", max.toString());

		ret[0] = min;
		ret[1] = max;
		return ret;
	}
}