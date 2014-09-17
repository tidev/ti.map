package ti.map;

import java.util.HashMap;

import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.AsyncResult;
import org.appcelerator.kroll.common.TiMessenger;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiContext;
import org.appcelerator.titanium.TiDimension;
import org.appcelerator.titanium.util.TiConvert;

import ti.map.MapModule;
import ti.map.Shape.IShape;
import android.graphics.Color;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

@Kroll.proxy(name="Circle", creatableInModule = MapModule.class, propertyAccessors = {
		MapModule.PROPERTY_CENTER, MapModule.PROPERTY_RADIUS,
		TiC.PROPERTY_BORDER_WIDTH, TiC.PROPERTY_BORDER_COLOR,
		TiC.PROPERTY_BACKGROUND_COLOR, TiC.PROPERTY_ZINDEX,
		TiC.PROPERTY_VISIBLE, TiC.PROPERTY_OPACITY })
public class CircleProxy  extends KrollProxy implements IShape 
{
	
	private CircleOptions options;
	private Circle circle;
	
	private static final int MSG_FIRST_ID = KrollProxy.MSG_LAST_ID + 1;

	private static final int MSG_SET_CENTER = MSG_FIRST_ID + 500;
	private static final int MSG_SET_RADIUS = MSG_FIRST_ID + 501;
	private static final int MSG_SET_BORDER_WIDTH = MSG_FIRST_ID + 502;
	private static final int MSG_SET_BORDER_COLOR = MSG_FIRST_ID + 503;
	private static final int MSG_SET_BACKGROUND_COLOR = MSG_FIRST_ID + 504;
	private static final int MSG_SET_ZINDEX = MSG_FIRST_ID + 505;
	private static final int MSG_SET_VISIBLE = MSG_FIRST_ID + 506;
	private static final int MSG_SET_OPACITY = MSG_FIRST_ID + 507;
	

	public CircleProxy() {
		super();
	}

	public CircleProxy(TiContext tiContext) {
		this();
	}
	
//	private LatLng addLocation(Object loc){
//		HashMap<String, String> point = (HashMap<String, String>) loc;
//		return new LatLng(TiConvert.toDouble(point.get(TiC.PROPERTY_LATITUDE)), TiConvert.toDouble(point.get(TiC.PROPERTY_LONGITUDE)));
//		return parseLocation(loc);		
//	}
	
	private int toPx(Object size){
		ViewGroup rootViewGroup = (ViewGroup)TiApplication.getAppCurrentActivity().getWindow().getDecorView().findViewById(android.R.id.content);
		return TiConvert.toTiDimension(size, TiDimension.COMPLEX_UNIT_AUTO).getAsPixels(rootViewGroup);
	}
	
	private int alphaColor(int color){
		if (hasProperty(TiC.PROPERTY_OPACITY)) {
			color = Color.argb(Math.round(TiConvert.toFloat(getProperty(TiC.PROPERTY_OPACITY)) * 255), Color.red(color), Color.green(color), Color.blue(color));
		}
		return color;
	}

	@Override
	public boolean handleMessage(Message msg) {
		AsyncResult result = null;
		switch (msg.what) {
			case MSG_SET_CENTER: {
				result = (AsyncResult) msg.obj;
				circle.setCenter(parseLocation(result.getArg()));
				result.setResult(null);
				return true;
			}
	
			case MSG_SET_RADIUS: {
				result = (AsyncResult) msg.obj;
				circle.setRadius((Double) result.getArg());
				result.setResult(null);
				return true;
			}
			
			case MSG_SET_BORDER_WIDTH: {
				result = (AsyncResult) msg.obj;
				circle.setStrokeWidth((Integer) result.getArg());
				result.setResult(null);
				return true;
			}
	
			case MSG_SET_BORDER_COLOR: {
				result = (AsyncResult) msg.obj;
				circle.setStrokeColor(alphaColor((Integer) result.getArg()));
				result.setResult(null);
				return true;
			}
			
			case MSG_SET_BACKGROUND_COLOR: {
				result = (AsyncResult) msg.obj;
				circle.setFillColor(alphaColor((Integer) result.getArg()));
				result.setResult(null);
				return true;
			}
			
			case MSG_SET_ZINDEX: {
				result = (AsyncResult) msg.obj;
				circle.setZIndex((Float) result.getArg());
				result.setResult(null);
				return true;
			}
			
			case MSG_SET_VISIBLE: {
				result = (AsyncResult) msg.obj;
				circle.setVisible((Boolean) result.getArg());
				result.setResult(null);
				return true;
			}
			
			case MSG_SET_OPACITY: {
				result = (AsyncResult) msg.obj;
				circle.setFillColor(alphaColor(circle.getFillColor()));
				circle.setStrokeColor(alphaColor(circle.getStrokeColor()));
				result.setResult(null);
				return true;
			}			
			
			default: {
				return super.handleMessage(msg);
			}
		}
	}
	
	public void processOptions() {
		options = new CircleOptions();

		if (hasProperty(MapModule.PROPERTY_CENTER)) {
			options.center(parseLocation(getProperty(MapModule.PROPERTY_CENTER)));
		}
		
		if (hasProperty(MapModule.PROPERTY_RADIUS)) {
			options.radius(TiConvert.toDouble(getProperty(MapModule.PROPERTY_RADIUS)));
		}
		
		if (hasProperty(TiC.PROPERTY_BORDER_WIDTH)) {
			options.strokeWidth(toPx(getProperty(TiC.PROPERTY_BORDER_WIDTH)));
		}
		
		if (hasProperty(TiC.PROPERTY_BORDER_COLOR)) {
			options.strokeColor(alphaColor(TiConvert.toColor((String)getProperty(TiC.PROPERTY_BORDER_COLOR))));
		}
		
		if (hasProperty(TiC.PROPERTY_BACKGROUND_COLOR)) {
			options.fillColor(alphaColor(TiConvert.toColor((String)getProperty(TiC.PROPERTY_BACKGROUND_COLOR))));
		}
		
		if (hasProperty(TiC.PROPERTY_ZINDEX)) {
			options.zIndex(TiConvert.toFloat(getProperty(TiC.PROPERTY_ZINDEX)));
		}
		
		if (hasProperty(TiC.PROPERTY_VISIBLE)) {
			options.visible(TiConvert.toBoolean(getProperty(TiC.PROPERTY_VISIBLE)));
		}	
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
		
		else if (name.equals(TiC.PROPERTY_BORDER_WIDTH)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_BORDER_WIDTH), toPx(value));
		}

		else if (name.equals(TiC.PROPERTY_BORDER_COLOR)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_BORDER_COLOR), TiConvert.toColor((String)value));
		}
		
		else if (name.equals(TiC.PROPERTY_BACKGROUND_COLOR)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_BACKGROUND_COLOR), TiConvert.toColor((String)value));
		}
		
		else if (name.equals(TiC.PROPERTY_ZINDEX)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_ZINDEX), TiConvert.toFloat(value));
		}
		
		else if (name.equals(TiC.PROPERTY_VISIBLE)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_VISIBLE), TiConvert.toBoolean(value));
		}
		
		else if (name.equals(TiC.PROPERTY_OPACITY)) {
			TiMessenger.sendBlockingMainMessage(getMainHandler().obtainMessage(MSG_SET_OPACITY));			
		}
	}
	
	public CircleOptions getOptions() {
		return options;
	}
	
	public void setCircle(Circle c) {
		circle = c;
	}
	
	public Circle getCircle() {
		return circle;
	}
	
	protected TiMarker marker;
	
	/**
	 * Handle timarker reference
	 * */
	public void setTiMarker(TiMarker marker) {
		this.marker = marker;
	}
	
	public TiMarker getTiMarker() {
		return this.marker;
	}
	
	public Marker getMarker() {
		return this.marker != null ? this.marker.getMarker() : null;
	}

	public AnnotationProxy getAnnotation() {
		return this.marker != null ? this.marker.getProxy() : null;
	}	
	
	// A location can either be a an array of longitude, latitude pairings or
	// an array of longitude, latitude objects.
	// e.g. [123.33, 34.44], OR {longitude: 123.33, latitude, 34.44}
	private LatLng parseLocation(Object loc) {
		LatLng location = null;
		if (loc instanceof HashMap) {
			HashMap<String, String> point = (HashMap<String, String>) loc;
			location = new LatLng(TiConvert.toDouble(point.get(TiC.PROPERTY_LATITUDE)), TiConvert.toDouble(point.get(TiC.PROPERTY_LONGITUDE)));
		} else if (loc instanceof Object[]) {
			Object[] temp = (Object[]) loc;
			location = new LatLng(TiConvert.toDouble(temp[1]),
					TiConvert.toDouble(temp[0]));
		}
//		Log.w("TiApp MAP", "center lat lng " + location.latitude + ", " + location.longitude);
		return location;
	}
	
}
