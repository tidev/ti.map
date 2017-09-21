package ti.map;

import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.android.gms.maps.GoogleMap;
import android.content.Context;
import com.google.maps.android.clustering.ClusterManager;
import com.google.android.gms.maps.model.Marker;
import org.appcelerator.titanium.view.TiDrawableReference;
import android.graphics.Bitmap;
import com.google.android.gms.maps.model.MarkerOptions;
import org.appcelerator.titanium.TiApplication;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import android.view.View;
import org.appcelerator.titanium.TiDimension;
import org.appcelerator.titanium.TiPoint;
import java.util.HashMap;
import org.appcelerator.titanium.TiC;

public class TiClusterRenderer extends DefaultClusterRenderer<TiClusterMarker> {

    private static final String defaultIconImageHeight = "40dip"; //The height of the default marker icon
    private static final String defaultIconImageWidth = "36dip"; //The width of the default marker icon
    private int iconImageHeight = 0;
    private int iconImageWidth = 0;
    private ClusterManager<TiClusterMarker> clusterManager;

    public TiClusterRenderer(Context context, GoogleMap map, ClusterManager<TiClusterMarker> clusterManager) {
        super(context, map, clusterManager);
        this.clusterManager = clusterManager;
    }

    @Override
    protected void onBeforeClusterItemRendered(TiClusterMarker clusterItem, MarkerOptions markerOptions) {
        AnnotationProxy anno = clusterItem.getProxy();

        if (anno.hasProperty(TiC.PROPERTY_IMAGE)) {
            TiDrawableReference imageref = TiDrawableReference.fromUrl(anno, (String) anno.getProperty(TiC.PROPERTY_IMAGE));
            Bitmap bitmap = imageref.getBitmap(true, false);
            if (bitmap != null) {
                try {
                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                    setIconImageDimensions(bitmap.getWidth(), bitmap.getHeight());
                } catch (Exception e){}
            }
        }

        if (anno.hasProperty(MapModule.PROPERTY_CENTER_OFFSET)) {
            HashMap centerOffsetProperty = (HashMap) anno.getProperty(MapModule.PROPERTY_CENTER_OFFSET);
            TiPoint centerOffset = new TiPoint(centerOffsetProperty, 0.0, 0.0);
            float offsetX = 0.5f - ((float) centerOffset.getX().getValue() / (float) iconImageWidth);
            float offsetY = 0.5f - ((float) centerOffset.getY().getValue() / (float) iconImageHeight);
            markerOptions.anchor(offsetX, offsetY);
        }
    }

    @Override
    protected void onClusterItemRendered(TiClusterMarker clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        TiUIMapView.markerItemMap.put(marker.getId(), clusterItem);
    }

    public void setIconImageDimensions(int w, int h)
    {
        if (w >= 0 && h >= 0) {
            iconImageWidth = w;
            iconImageHeight = h;
        } else { // default maker icon
            TiDimension widthDimension = new TiDimension(defaultIconImageWidth, TiDimension.TYPE_UNDEFINED);
            TiDimension heightDimension = new TiDimension(defaultIconImageHeight, TiDimension.TYPE_UNDEFINED);
            View view = TiApplication.getAppCurrentActivity().getWindow().getDecorView();
            iconImageWidth = widthDimension.getAsPixels(view);
            iconImageHeight = heightDimension.getAsPixels(view);
        }
    }
}
