package com.fresko;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class WorkSurface extends SurfaceView implements SurfaceHolder.Callback {

	Drawable[][] chunks;
	Point selected;
	Point destination;
	
	/** Handle to the application context, used to e.g. fetch Drawables. */
	private Context mContext;

	static Paint paint_antialisedSolid = new Paint(Paint.ANTI_ALIAS_FLAG);
	static {
		paint_antialisedSolid.setStyle(Paint.Style.STROKE);
		paint_antialisedSolid.setColor(Color.CYAN);
	}
	static Paint paint_antialisedDarker = new Paint(Paint.ANTI_ALIAS_FLAG);
	static {
		paint_antialisedDarker.setStyle(Paint.Style.FILL);
		paint_antialisedDarker.setColor(Color.BLACK);
		paint_antialisedDarker.setAlpha(1);
	}
	
	Bitmap buffer = null;

	public WorkSurface(Context context) {
		super(context);
		mContext = context;

		// register our interest in hearing about changes to our surface
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// recalculate scaling parameters
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		// create black buffer by size and paint images on it if exist
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		buffer = null;
	}

	public void updateAfterEvent() { 
		SurfaceHolder holder = getHolder();
		Canvas c = holder.lockCanvas(null);
		updateBuffer();
		doDraw(c);
		holder.unlockCanvasAndPost(c);
	}

	/**
	 * Draw to buffer
	 */
	public void	updateBuffer() {
		Canvas canvas = new Canvas(buffer);
		canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint_antialisedDarker);
		float posX = (float) (canvas.getWidth() * Math.random());
		float posY = (float) (canvas.getHeight() * Math.random());
		canvas.drawCircle(posX, posY, (float) (30 * Math.random()),
				paint_antialisedSolid);
	}

	/**
	 * Draw scaled on surface
	 * @param nativeCanvas
	 */
	public void doDraw(Canvas nativeCanvas) {
		if (buffer == null) {
			buffer = Bitmap.createBitmap(nativeCanvas.getWidth(),
					nativeCanvas.getHeight(), Bitmap.Config.ARGB_8888);
			nativeCanvas.drawColor(Color.BLACK);
			updateBuffer();
		}

		nativeCanvas.drawBitmap(buffer, 0, 0, null);
	}

	public Drawable[][] getChunks() {
		return chunks;
	}

	public void setChunks(Drawable[][] chunks) {
		this.chunks = chunks;
	}

	public Point getSelected() {
		return selected;
	}

	public void setSelected(Point selected) {
		this.selected = selected;
	}

	public Point getDestination() {
		return destination;
	}

	public void setDestination(Point destination) {
		this.destination = destination;
	}
}
