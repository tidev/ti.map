package ti.map.Shape;

import org.appcelerator.kroll.KrollProxy;

import ti.map.AnnotationProxy;
import ti.map.TiMarker;

import com.google.android.gms.maps.model.Marker;

abstract public class Shape extends KrollProxy implements IShape
{

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
	
}
