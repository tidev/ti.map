package ti.map.Shape;

import android.graphics.Point;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import ti.map.PolygonProxy;

public class Boundary
{

	final static int precision = 10000000;
	final static double radius = 0.0005;

	private Point point;
	private Point[] points;

	public ArrayList<PolygonProxy> contains(ArrayList<PolygonProxy> p, LatLng ll)
	{
		ArrayList<PolygonProxy> list = new ArrayList<PolygonProxy>();
		cast(ll);
		for (int i = 0; i < p.size(); i++) {
			if (contains(p.get(i))) {
				list.add(p.get(i));
			}
		}
		return list;
	}

	public boolean contains(PolygonProxy p, LatLng ll)
	{
		return cast(p).cast(ll).contains();
	}

	public boolean contains(PolygonProxy p)
	{
		return cast(p).contains();
	}

	protected Boundary cast(LatLng ll)
	{
		this.point = new Point((int) (ll.longitude * precision), (int) (ll.latitude * precision));
		return this;
	}

	protected Boundary cast(PolygonProxy p)
	{

		int npoints = p.getPolygon().getPoints().size();
		this.points = new Point[npoints];

		for (int i = 0; i < npoints; i++) {

			LatLng ll = p.getPolygon().getPoints().get(i);

			int x = (int) (ll.longitude * precision);
			int y = (int) (ll.latitude * precision);

			this.points[i] = new Point(x, y);
		}

		return this;
	}

	public boolean contains()
	{
		return contains(this.points, this.point);
	}

	/**
	 * Return true if the given point is contained inside the boundary. See:
	 * http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
	 * 
	 * @param test
	 *            The point to check
	 * @return true if the point is inside the boundary, false otherwise
	 */
	public boolean contains(Point[] points, Point test)
	{
		int i;
		int j;
		boolean result = false;
		for (i = 0, j = points.length - 1; i < points.length; j = i++) {
			if ((points[i].y > test.y) != (points[j].y > test.y)
				&& (test.x < (points[j].x - points[i].x) * (test.y - points[i].y) / (points[j].y - points[i].y)
								 + points[i].x)) {
				result = !result;
			}
		}
		return result;
	}

	public boolean containsCircle(Point[] points, Point test)
	{
		for (int i = 0; i < points.length; i++) {
			if (isPointInCircle((double) test.x, (double) test.y, Boundary.radius, (double) points[i].x,
								(double) points[i].y)) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(Point[] points, Point test, double radius)
	{
		for (int i = 0; i < points.length; i++) {
			if (isPointInCircle((double) test.x, (double) test.y, radius, (double) points[i].x, (double) points[i].y)) {
				return true;
			}
		}
		return false;
	}

	// test if coordinate (x, y) is within a radius from coordinate (centerX, centerY)
	public boolean isPointInCircle(double centerX, double centerY, double radius, double x, double y)
	{
		if (isInRectangle(centerX, centerY, radius, x, y)) {
			double dx = centerX - x;
			double dy = centerY - y;
			dx *= dx;
			dy *= dy;
			double distanceSquared = dx + dy;
			double radiusSquared = radius * radius;
			return distanceSquared <= radiusSquared;
		}
		return false;
	}

	boolean isInRectangle(double centerX, double centerY, double radius, double x, double y)
	{
		return x >= centerX - radius && x <= centerX + radius && y >= centerY - radius && y <= centerY + radius;
	}
}
