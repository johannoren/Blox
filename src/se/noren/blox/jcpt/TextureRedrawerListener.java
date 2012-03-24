package se.noren.blox.jcpt;

import android.graphics.Canvas;

public interface TextureRedrawerListener {

	/**
	 * Get a callback so you may paint on this canvas.
	 * @param canvas
	 * @param height
	 * @param width
	 */
	public void drawCallback(Canvas canvas, int height, int width);
}
