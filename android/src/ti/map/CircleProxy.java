package ti.map;

import java.util.HashMap;

import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.AsyncResult;
import org.appcelerator.kroll.common.TiMessenger;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiContext;
import org.appcelerator.titanium.util.TiConvert;

import android.os.Message;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

@Kroll.proxy(creatableInModule=MapModule.class, propertyAccessors = {
	MapModule.PROPERTY_CENTER,
	MapModule.PROPERTY_RADIUS,
	MapModule.PROPERTY_FILL_COLOR,
	MapModule.PROPERTY_STROKE_COLOR,
	MapModule.PROPERTY_STROKE_WIDTH,
	TiC.PROPERTY_VISIBLE,
	TiC.PROPERTY_ZINDEX
})
public class CircleProxy extends KrollProxy{

	private CircleOptions circleOptions;
	private Circle circle;

	private static final int MSG_FIRST_ID = KrollProxy.MSG_LAST_ID + 1;
	
	private static final int MSG_SET_CENTER = MSG_FIRST_ID + 400;
	private static final int MSG_SET_RADIUS = MSG_FIRST_ID + 401;
	private static final int MSG_SET_FILL_COLOR = MSG_FIRST_ID + 402;
	private static final int MSG_SET_STROKE_COLOR = MSG_FIRST_ID + 403;
	private static final int MSG_SET_STROKE_WIDTH = MSG_FIRST_ID + 404;
	private static final int MSG_SET_VISIBLE = MSG_FIRST_ID + 405;
	private static final int MSG_SET_Z_INDEX = MSG_FIRST_ID + 406;


	public CircleProxy() {
		super();
	}
	
	public CircleProxy(TiContext tiContext) {
		this();
	}
	
	@Override
	public boolean handleMessage(Message msg) 
	{
		AsyncResult result = null;
		switch (msg.what) {

		case MSG_SET_CENTER: {
			result = (AsyncResult) msg.obj;
			circle.setCenter(processPoint(result.getArg(), true));
			result.setResult(null);
			return true;
		}
		
		case MSG_SET_RADIUS: {
			result = (AsyncResult) msg.obj;
			circle.setRadius((Double)result.getArg());
			result.setResult(null);
			return true;
		}
		
		case MSG_SET_FILL_COLOR: {
			result = (AsyncResult) msg.obj;
			circle.setFillColor((Integer)result.getArg());
			result.setResult(null);
			return true;
		}
		
		case MSG_SET_STROKE_COLOR: {
			result = (AsyncResult) msg.obj;
			circle.setStrokeColor((Integer)result.getArg());
			result.setResult(null);
			return true;
		}
		
		case MSG_SET_STROKE_WIDTH: {
			result = (AsyncResult) msg.obj;
			circle.setStrokeWidth((Float)result.getArg());
			result.setResult(null);
			return true;
		}
		
		case MSG_SET_VISIBLE: {
			result = (AsyncResult) msg.obj;
			circle.setVisible((Boolean)result.getArg());
			result.setResult(null);
			return true;
		}
		
		case MSG_SET_Z_INDEX: {
			result = (AsyncResult) msg.obj;
			circle.setZIndex((Float)result.getArg());
			result.setResult(null);
			return true;
		}
		
		default : {
			return super.handleMessage(msg);
		}
		}
	}
	public void processOptions() {

		circleOptions = new CircleOptions();
		
		if (hasProperty(MapModule.PROPERTY_CENTER)) {
			 processPoint(getProperty(MapModule.PROPERTY_CENTER), false);
		}
		
		if (hasProperty(MapModule.PROPERTY_RADIUS)) {
			circleOptions.radius(TiConvert.toDouble(getProperty(MapModule.PROPERTY_RADIUS)));
		}
		
		if (hasProperty(MapModule.PROPERTY_FILL_COLOR)) {
			circleOptions.fillColor(TiConvert.toColor((String)getProperty(MapModule.PROPERTY_FILL_COLOR)));
		}
		
		if (hasProperty(MapModule.PROPERTY_STROKE_COLOR)) {
			circleOptions.strokeColor(TiConvert.toColor((String)getProperty(MapModule.PROPERTY_STROKE_COLOR)));
		}

		if (hasProperty(MapModule.PROPERTY_STROKE_WIDTH)) {
			circleOptions.strokeWidth(TiConvert.toFloat(getProperty(MapModule.PROPERTY_STROKE_WIDTH)));
		}
		
		if (hasProperty(TiC.PROPERTY_VISIBLE)) {
			circleOptions.visible(TiConvert.toBoolean(getProperty(TiC.PROPERTY_VISIBLE)));
		}
		
		if (hasProperty(TiC.PROPERTY_ZINDEX)) {
			circleOptions.zIndex(TiConvert.toFloat(getProperty(TiC.PROPERTY_ZINDEX)));
		}
		
	}
	
	public LatLng processPoint(Object loc, boolean getLocation) {
		if (loc instanceof HashMap) {
			HashMap<String, String> point = (HashMap<String, String>) loc;
			Object latitude = point.get(TiC.PROPERTY_LATITUDE);
			Object longitude = point.get(TiC.PROPERTY_LONGITUDE);
			if (longitude != null && latitude != null) {
				LatLng location = new LatLng(TiConvert.toDouble(latitude), TiConvert.toDouble(longitude));
				if (!getLocation) {
					circleOptions.center(location);
				}
				return location;
			}
		}
		return null;
	}

	public CircleOptions getOptions() {
		return circleOptions;
	}
	
	public void setCircle(Circle c) {
		circle = c;
	}
	
	public Circle getCircle() {
		return circle;
	}
	
	@Override
	public void onPropertyChanged(String name, Object value) {
		super.onPropertyChanged(name, value);
		if (circle == null) {
			return;
		}
		
		else if (name.equals(MapModule.PROPERTY_CENTER)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_CENTER), value);
		}
		
		else if (name.equals(MapModule.PROPERTY_RADIUS)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_RADIUS), TiConvert.toDouble(value));
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
