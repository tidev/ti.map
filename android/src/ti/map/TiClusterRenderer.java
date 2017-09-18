package ti.map;

import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.android.gms.maps.GoogleMap;
import android.content.Context;
import com.google.maps.android.clustering.ClusterManager;
import com.google.android.gms.maps.model.Marker;

public class TiClusterRenderer extends DefaultClusterRenderer<TiClusterMarker> {

    public TiClusterRenderer(Context context, GoogleMap map, ClusterManager<TiClusterMarker> clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onClusterItemRendered(TiClusterMarker clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        TiUIMapView.markerItemMap.put(marker.getId(), clusterItem);
    }
}
