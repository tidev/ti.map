package ti.map.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;
import java.io.ByteArrayOutputStream;

public class CanvasTileProvider implements TileProvider
{

	static final int TILE_SIZE = 512;
	private TileProvider tileProvider;

	public CanvasTileProvider(TileProvider tileProvider)
	{
		this.tileProvider = tileProvider;
	}

	@Override
	public Tile getTile(int x, int y, int zoom)
	{
		byte[] data;
		Bitmap image = getNewBitmap();
		Canvas canvas = new Canvas(image);
		boolean isOk = onDraw(canvas, zoom, x, y);
		data = bitmapToByteArray(image);
		image.recycle();

		if (isOk) {
			Tile tile = new Tile(TILE_SIZE, TILE_SIZE, data);
			return tile;
		} else {
			return tileProvider.getTile(x, y, zoom);
		}
	}

	Paint paint = new Paint();

	private boolean onDraw(Canvas canvas, int zoom, int x, int y)
	{
		x = x * 2;
		y = y * 2;
		Tile leftTop = tileProvider.getTile(x, y, zoom + 1);
		Tile leftBottom = tileProvider.getTile(x, y + 1, zoom + 1);
		Tile rightTop = tileProvider.getTile(x + 1, y, zoom + 1);
		Tile rightBottom = tileProvider.getTile(x + 1, y + 1, zoom + 1);

		if (leftTop == NO_TILE && leftBottom == NO_TILE && rightTop == NO_TILE && rightBottom == NO_TILE) {
			return false;
		}

		Bitmap bitmap;

		if (leftTop != NO_TILE) {
			bitmap = BitmapFactory.decodeByteArray(leftTop.data, 0, leftTop.data.length);
			canvas.drawBitmap(bitmap, 0, 0, paint);
			bitmap.recycle();
		}

		if (leftBottom != NO_TILE) {
			bitmap = BitmapFactory.decodeByteArray(leftBottom.data, 0, leftBottom.data.length);
			canvas.drawBitmap(bitmap, 0, 256, paint);
			bitmap.recycle();
		}
		if (rightTop != NO_TILE) {
			bitmap = BitmapFactory.decodeByteArray(rightTop.data, 0, rightTop.data.length);
			canvas.drawBitmap(bitmap, 256, 0, paint);
			bitmap.recycle();
		}
		if (rightBottom != NO_TILE) {
			bitmap = BitmapFactory.decodeByteArray(rightBottom.data, 0, rightBottom.data.length);
			canvas.drawBitmap(bitmap, 256, 256, paint);
			bitmap.recycle();
		}
		return true;
	}

	private Bitmap getNewBitmap()
	{
		Bitmap image = Bitmap.createBitmap(TILE_SIZE, TILE_SIZE, Bitmap.Config.ARGB_8888);
		image.eraseColor(Color.TRANSPARENT);
		return image;
	}

	private static byte[] bitmapToByteArray(Bitmap bm)
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, bos);

		byte[] data = bos.toByteArray();
		try {
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
}