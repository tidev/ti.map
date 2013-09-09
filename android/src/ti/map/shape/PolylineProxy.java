package ti.map.shape;

import java.util.ArrayList;
import java.util.HashMap;

import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.AsyncResult;
import org.appcelerator.kroll.common.TiMessenger;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiContext;
import org.appcelerator.titanium.util.TiConvert;

import android.os.Message;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import ti.map.MapModule;

@Kroll.proxy(creatableInModule=MapModule.class, propertyAccessors = {
	
	PolygonProxy.PROPERTY_FILL_COLOR,
	PolygonProxy.PROPERTY_STROKE_COLOR,
	PolygonProxy.PROPERTY_STROKE_WIDTH,
	PolygonProxy.PROPERTY_ZINDEX,
	
	MapModule.PROPERTY_POINTS,

})
public class PolylineProxy extends KrollProxy {
	
	private PolylineOptions options;
	private Polyline polyline;
		
	private static final int MSG_FIRST_ID = KrollProxy.MSG_LAST_ID + 1;
	
	private static final int MSG_SET_POINTS 	  = MSG_FIRST_ID + 499;
	private static final int MSG_SET_STROKE_COLOR = MSG_FIRST_ID + 501;
	private static final int MSG_SET_STROKE_WIDTH = MSG_FIRST_ID + 502;
	private static final int MSG_SET_ZINDEX 	  = MSG_FIRST_ID + 503;	
	
	// 			 points (MapView)
	// (int) 	 strokeColor 
	// (float)	 strokeWidth 
	// (int) 	 fillColor 
	// (float)	 zIndex	
	public static final String PROPERTY_STROKE_COLOR = "color";
	public static final String PROPERTY_STROKE_WIDTH = "width";
	public static final String PROPERTY_ZINDEX = "zIndex";	
	
	public PolylineProxy() {
		super();
	}
	
	public PolylineProxy(TiContext tiContext) {
		this();
	}
	
	@Override
	public boolean handleMessage(Message msg) 
	{	
		
		//	MSG_SET_POINTS
		//	MSG_SET_STROKE_WIDTH
		//	MSG_SET_STROKE_COLOR
		//	MSG_SET_FILL_COLOR		
		//	MSG_SET_ZINDEX
		AsyncResult result = null;
		switch (msg.what) {
			case MSG_SET_POINTS: {
				result = (AsyncResult) msg.obj;
				Log.e("PolylineProxy.handleMessage.MSG_SET_POINTS", result.getArg().toString());
				polyline.setPoints(processPoints(result.getArg(), true));
				result.setResult(null);
				return true;
			}
			case MSG_SET_STROKE_WIDTH: {
				result = (AsyncResult) msg.obj;
				options.width((Float)result.getArg());
				result.setResult(null);
				return true;
			}
			case MSG_SET_STROKE_COLOR: {
				result = (AsyncResult) msg.obj;
				options.color((Integer)result.getArg());
				result.setResult(null);
				return true;
			}
			case MSG_SET_ZINDEX: {
				result = (AsyncResult) msg.obj;
				options.zIndex((Float)result.getArg());
				result.setResult(null);
				return true;
			}
			default : {
				return super.handleMessage(msg);
			}
		}
	}
	public void processOptions() {

		options = new PolylineOptions();
		String op;
		// (int) 	 strokeColor 
		// (float)	 strokeWidth 
		// (int) 	 fillColor 
		// (float)	 zIndex
		
		if (hasProperty(MapModule.PROPERTY_POINTS)) {
			 processPoints(getProperty(MapModule.PROPERTY_POINTS), false);
		}
		
		op = PolygonProxy.PROPERTY_STROKE_COLOR;
		if (hasProperty(op)) {
			options.color(TiConvert.toColor((String)getProperty(op)));
		}
		
		op = PolygonProxy.PROPERTY_STROKE_WIDTH;
		if (hasProperty(op)) {
			options.width(TiConvert.toFloat(getProperty(op)));
		}

		op = PolygonProxy.PROPERTY_ZINDEX;
		if (hasProperty(op)) {
			options.zIndex(TiConvert.toFloat(getProperty(op)));
		}

	}
	
	public void addLocation(Object loc, ArrayList<LatLng> locationArray, boolean list) {
		if (loc instanceof HashMap) {
			HashMap<String, String> point = (HashMap<String, String>) loc;
			LatLng location = new LatLng(TiConvert.toDouble(point.get(TiC.PROPERTY_LATITUDE)), TiConvert.toDouble(point.get(TiC.PROPERTY_LONGITUDE)));
			if (list) {
				locationArray.add(location);
			} else {
				options.add(location);
			}
		}
	}

	public ArrayList<LatLng> processPoints(Object points, boolean list) {
		
		ArrayList<LatLng> locationArray = new ArrayList<LatLng>();
		//multiple points
		if (points instanceof Object[]) {
			Object[] pointsArray = (Object[]) points;
			for (int i = 0; i < pointsArray.length; i++) {
				Object obj = pointsArray[i];
				addLocation(obj, locationArray, list);
			}
			return locationArray;
		}

		//single point
		addLocation(points, locationArray, list);
		return locationArray;
	}
	
	public PolylineOptions getOptions() {
		return options;
	}
	
	public void setPolyline(Polyline r) {
		polyline = r;
	}
	
	public Polyline getPolyline() {
		return polyline;
	}
	
	@Override
	public void onPropertyChanged(String name, Object value) {
		
		super.onPropertyChanged(name, value);
		
		if (polyline == null) {
			return;
		}
		
		else if (name.equals(MapModule.PROPERTY_POINTS)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_POINTS), value);
		}

		else if (name.equals(PolylineProxy.PROPERTY_STROKE_WIDTH)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_STROKE_WIDTH), TiConvert.toFloat(value));
		}

		else if (name.equals(PolylineProxy.PROPERTY_STROKE_COLOR)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_STROKE_COLOR), TiConvert.toColor((String)value));
		}

		else if (name.equals(PolylineProxy.PROPERTY_ZINDEX)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_ZINDEX), TiConvert.toFloat(value));
		}

	}	
}
