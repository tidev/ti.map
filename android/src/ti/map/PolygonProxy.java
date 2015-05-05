package ti.map;

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

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

@Kroll.proxy(creatableInModule=MapModule.class, propertyAccessors = {
	MapModule.PROPERTY_POINTS,
	MapModule.PROPERTY_FILL_COLOR,
	MapModule.PROPERTY_STROKE_COLOR,
	MapModule.PROPERTY_STROKE_WIDTH,
	TiC.PROPERTY_VISIBLE,
	TiC.PROPERTY_ZINDEX
})
public class PolygonProxy extends KrollProxy{
	
	private PolygonOptions polygonOptions;
	private Polygon polygon;
		
	private static final int MSG_FIRST_ID = KrollProxy.MSG_LAST_ID + 1;
	
	private static final int MSG_SET_POINTS = MSG_FIRST_ID + 400;
	private static final int MSG_SET_FILL_COLOR = MSG_FIRST_ID + 401;
	private static final int MSG_SET_STROKE_COLOR = MSG_FIRST_ID + 402;
	private static final int MSG_SET_STROKE_WIDTH = MSG_FIRST_ID + 403;
	private static final int MSG_SET_VISIBLE = MSG_FIRST_ID + 404;
	private static final int MSG_SET_Z_INDEX = MSG_FIRST_ID + 405;

	public PolygonProxy() {
		super();
	}
	
	public PolygonProxy(TiContext tiContext) {
		this();
	}
	
	@Override
	public boolean handleMessage(Message msg) 
	{
		AsyncResult result = null;
		switch (msg.what) {

		case MSG_SET_POINTS: {
			result = (AsyncResult) msg.obj;
			polygon.setPoints(processPoints(result.getArg(), true));
			result.setResult(null);
			return true;
		}
		
		case MSG_SET_FILL_COLOR: {
			result = (AsyncResult) msg.obj;
			polygon.setFillColor((Integer)result.getArg());
			result.setResult(null);
			return true;
		}
		
		case MSG_SET_STROKE_COLOR: {
			result = (AsyncResult) msg.obj;
			polygon.setStrokeColor((Integer)result.getArg());
			result.setResult(null);
			return true;
		}
		
		case MSG_SET_STROKE_WIDTH: {
			result = (AsyncResult) msg.obj;
			polygon.setStrokeWidth((Float)result.getArg());
			result.setResult(null);
			return true;
		}
		
		case MSG_SET_VISIBLE: {
			result = (AsyncResult) msg.obj;
			polygon.setVisible((Boolean)result.getArg());
			result.setResult(null);
			return true;
		}
		
		case MSG_SET_Z_INDEX: {
			result = (AsyncResult) msg.obj;
			polygon.setZIndex((Float)result.getArg());
			result.setResult(null);
			return true;
		}
		
		default : {
			return super.handleMessage(msg);
		}
		}
	}
	public void processOptions() {

		polygonOptions = new PolygonOptions();

		if (hasProperty(MapModule.PROPERTY_POINTS)) {
			 processPoints(getProperty(MapModule.PROPERTY_POINTS), false);
		}
		
		if (hasProperty(MapModule.PROPERTY_FILL_COLOR)) {
			polygonOptions.fillColor(TiConvert.toColor((String)getProperty(MapModule.PROPERTY_FILL_COLOR)));
		}
		
		if (hasProperty(MapModule.PROPERTY_STROKE_COLOR)) {
			polygonOptions.strokeColor(TiConvert.toColor((String)getProperty(MapModule.PROPERTY_STROKE_COLOR)));
		}
		
		if (hasProperty(MapModule.PROPERTY_STROKE_WIDTH)) {
			polygonOptions.strokeWidth(TiConvert.toFloat(getProperty(MapModule.PROPERTY_STROKE_WIDTH)));
		}
		
		if (hasProperty(TiC.PROPERTY_VISIBLE)) {
			polygonOptions.visible(TiConvert.toBoolean(getProperty(TiC.PROPERTY_VISIBLE)));
		}
		
		if (hasProperty(TiC.PROPERTY_ZINDEX)) {
			polygonOptions.zIndex(TiConvert.toFloat(getProperty(TiC.PROPERTY_ZINDEX)));
		}		
		
	}
	
	public void addLocation(Object loc, ArrayList<LatLng> locationArray, boolean list) {
		if (loc instanceof HashMap) {
			HashMap<String, String> point = (HashMap<String, String>) loc;
			Object latitude = point.get(TiC.PROPERTY_LATITUDE);
			Object longitude = point.get(TiC.PROPERTY_LONGITUDE);
			if (longitude != null && latitude != null) {
				LatLng location = new LatLng(TiConvert.toDouble(latitude), TiConvert.toDouble(longitude));
				if (list) {
					locationArray.add(location);
				} else {
					polygonOptions.add(location);
				}
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
	
	public PolygonOptions getOptions() {
		return polygonOptions;
	}
	
	public void setPolygon(Polygon p) {
		polygon = p;
	}
	
	public Polygon getPolygon() {
		return polygon;
	}
	
	@Override
	public void onPropertyChanged(String name, Object value) {
		super.onPropertyChanged(name, value);
		if (polygon == null) {
			return;
		}
		
		else if (name.equals(MapModule.PROPERTY_POINTS)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_POINTS), value);
		}

		else if (name.equals(MapModule.PROPERTY_FILL_COLOR)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_FILL_COLOR), TiConvert.toColor((String)value));
		}
		
		else if (name.equals(MapModule.PROPERTY_STROKE_COLOR)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_STROKE_COLOR), TiConvert.toColor((String)value));
		}
		
		else if (name.equals(MapModule.PROPERTY_STROKE_WIDTH)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_STROKE_WIDTH), TiConvert.toFloat(value));
		}
		
		else if (name.equals(TiC.PROPERTY_VISIBLE)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_VISIBLE), TiConvert.toBoolean(value));
		}
		
		else if (name.equals(TiC.PROPERTY_ZINDEX)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_Z_INDEX), TiConvert.toFloat(value));
		}
		
	}

	@Override
	public boolean hasProperty(String name)
	{
		return (super.getProperty(name) != null);
	}
	
}
