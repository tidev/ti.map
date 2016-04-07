package ti.map;


import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.view.TiUIView;

import android.app.Activity;

@Kroll.proxy(creatableInModule = MapModule.class, propertyAccessors = {
    TiC.PROPERTY_POSITION,
    MapModule.PROPERTY_PANNING,
    MapModule.PROPERTY_ZOOM,
    MapModule.PROPERTY_STREET_NAMES,
    MapModule.PROPERTY_USER_NAVIGATION
})
public class StreetViewPanoramaProxy extends ViewProxy
{
	public StreetViewPanoramaProxy() {
		super();
	}

	@Override
	public TiUIView createView(Activity activity) {
		return new TiStreetViewPanorama(this, activity);
	}
}
