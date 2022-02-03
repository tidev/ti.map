package ti.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import java.util.HashMap;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.TiBlob;
import org.appcelerator.titanium.TiC;
import org.appcelerator.titanium.TiDimension;
import org.appcelerator.titanium.TiPoint;
import org.appcelerator.titanium.view.TiDrawableReference;

public class TiClusterRenderer extends DefaultClusterRenderer<TiMarker>
{
	private static final String TAG = "ClusterRender";
	private static final String defaultIconImageHeight = "40dip"; //The height of the default marker icon
	private static final String defaultIconImageWidth = "36dip";  //The width of the default marker icon
	private int iconImageHeight = 0;
	private int iconImageWidth = 0;

	public TiClusterRenderer(Context context, GoogleMap map, ClusterManager<TiMarker> clusterManager)
	{
		super(context, map, clusterManager);
	}

	@Override
	protected void onBeforeClusterItemRendered(TiMarker clusterItem, MarkerOptions markerOptions)
	{

		AnnotationProxy anno = clusterItem.getProxy();
		if (anno != null) {
			if (anno.hasProperty(TiC.PROPERTY_IMAGE)) {
				handleImage(anno, markerOptions, anno.getProperty(TiC.PROPERTY_IMAGE));
			}

			if (anno.hasProperty(MapModule.PROPERTY_CENTER_OFFSET)) {
				HashMap centerOffsetProperty = (HashMap) anno.getProperty(MapModule.PROPERTY_CENTER_OFFSET);
				TiPoint centerOffset = new TiPoint(centerOffsetProperty, 0.0, 0.0);
				float offsetX = 0.5f - ((float) centerOffset.getX().getValue() / (float) iconImageWidth);
				float offsetY = 0.5f - ((float) centerOffset.getY().getValue() / (float) iconImageHeight);
				markerOptions.anchor(offsetX, offsetY);
			}
		}
	}

	private void handleImage(AnnotationProxy anno, MarkerOptions markerOptions, Object image)
	{
		// Image path
		if (image instanceof String) {
			TiDrawableReference imageref =
				TiDrawableReference.fromUrl(anno, (String) anno.getProperty(TiC.PROPERTY_IMAGE));
			Bitmap bitmap = imageref.getBitmap();
			if (bitmap != null) {
				try {
					markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
					setIconImageDimensions(bitmap.getWidth(), bitmap.getHeight());
				} catch (Exception e) {
				}
				return;
			}
		}

		// Image blob
		if (image instanceof TiBlob) {
			Bitmap bitmap = ((TiBlob) image).getImage();
			if (bitmap != null) {
				markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
				setIconImageDimensions(bitmap.getWidth(), bitmap.getHeight());
				return;
			}
		}

		Log.w(TAG, "Unable to get the image from the path: " + image);
		setIconImageDimensions(-1, -1);
	}

	@Override
	protected void onClusterItemRendered(TiMarker clusterItem, Marker marker)
	{
		super.onClusterItemRendered(clusterItem, marker);
		clusterItem.setMarker(marker);
	}

	protected void onClusterItemUpdated(TiMarker item, Marker marker)
	{
		boolean changed = false;
		// Update marker text if the item text changed - same logic as adding marker in CreateMarkerTask.perform()
		if (item.getTitle() != null && item.getSnippet() != null) {
			if (!item.getTitle().equals(marker.getTitle())) {
				marker.setTitle(item.getTitle());
				changed = true;
			}
			if (!item.getSnippet().equals(marker.getSnippet())) {
				marker.setSnippet(item.getSnippet());
				changed = true;
			}
		} else if (item.getSnippet() != null && !item.getSnippet().equals(marker.getTitle())) {
			marker.setTitle(item.getSnippet());
			changed = true;
		} else if (item.getTitle() != null && !item.getTitle().equals(marker.getTitle())) {
			marker.setTitle(item.getTitle());
			changed = true;
		}
		// Update marker position if the item changed position
		if (!marker.getPosition().equals(item.getPosition())) {
			if (item.getPosition() != null) {
				marker.setPosition(item.getPosition());
			}
			changed = true;
		}
		if (changed && marker.isInfoWindowShown()) {
			// Force a refresh of marker info window contents
			marker.showInfoWindow();
		}
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
