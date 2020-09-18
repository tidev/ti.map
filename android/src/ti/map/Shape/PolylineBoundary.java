package ti.map.Shape;

import android.graphics.PointF;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import ti.map.PolylineProxy;

public class PolylineBoundary
{

	public static double defaultDistance = 10;

	private double distance;
	private PointF point;
	private PointF[] points;

	public ArrayList<PolylineProxy> contains(ArrayList<PolylineProxy> p, LatLng ll, double distance)
	{
		ArrayList<PolylineProxy> list = new ArrayList<PolylineProxy>();
		cast(ll);
		this.distance = distance;
		for (int i = 0; i < p.size(); i++) {
			if (cast(p.get(i)).contains()) {
				list.add(p.get(i));
			}
		}
		return list;
	}

	protected PolylineBoundary cast(LatLng ll)
	{
		this.point = new PointF((float) ll.longitude, (float) ll.latitude);
		return this;
	}

	protected PolylineBoundary cast(PolylineProxy p)
	{

		int npoints = p.getPolyline().getPoints().size();
		this.points = new PointF[npoints];

		for (int i = 0; i < npoints; i++) {

			LatLng ll = p.getPolyline().getPoints().get(i);

			float x = (float) ll.longitude;
			float y = (float) ll.latitude;
			this.points[i] = new PointF(x, y);
		}

		return this;
	}

	public boolean contains()
	{
		return contains(this.points, this.point);
	}

	public boolean contains(PointF[] points, PointF test)
	{
		for (int i = 0; i < points.length; i++) {
			if (i + 1 == points.length) {
				return false;
			}
			double distance = getDistance(test, points[i], points[i + 1]);
			//			Log.e("TiApplicationDBG", "Distance is " + distance + ", requested " + this.distance);
			if (distance <= this.distance) {
				return true;
			}
		}
		return false;
	}

	double getDistance(PointF test, PointF point1, PointF point2)
	{

		double diffX = point2.x - point1.x;
		double diffY = point2.y - point.y;
		if ((diffX == 0) && (diffY == 0)) {
			diffX = test.x - point1.x;
			diffY = test.y - point1.y;
			return Math.sqrt(diffX * diffX + diffY * diffY);
		}

		double t = ((test.x - point1.x) * diffX + (test.y - point1.y) * diffY) / (diffX * diffX + diffY * diffY);

		if (t < 0) {
			//point is nearest to the first point i.e x1 and y1
			diffX = test.x - point1.x;
			diffY = test.y - point1.y;
		} else if (t > 1) {
			//point is nearest to the end point i.e x2 and y2
			diffX = test.x - point2.x;
			diffY = test.y - point2.y;
		} else {
			//if perpendicular line intersect the line segment.
			diffX = test.x - (point1.x + t * diffX);
			diffY = test.y - (point1.y + t * diffY);
		}

		//returning shortest distance
		return Math.sqrt(diffX * diffX + diffY * diffY);
	}
}
