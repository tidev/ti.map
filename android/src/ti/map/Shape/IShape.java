package ti.map.Shape;

import ti.map.AnnotationProxy;
import ti.map.TiMarker;

import com.google.android.gms.maps.model.Marker;

public interface IShape {

	public void setTiMarker(TiMarker marker);
	
	public TiMarker getTiMarker();
	public Marker getMarker();
	public AnnotationProxy getAnnotation();
	
}
