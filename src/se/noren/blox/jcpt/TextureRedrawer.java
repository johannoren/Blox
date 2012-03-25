package se.noren.blox.jcpt;

import se.noren.blox.gameengine.GameContext;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.threed.jpct.Object3D;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.util.BitmapHelper;

/**
 * Redraw a texture in real time with possibility to write text on 
 * is as well.
 * 
 * @author Johan Norén - 25 mar 2012
 */
public class TextureRedrawer {

	Bitmap bitmap = null;
	Canvas canvas = null;
	Paint paint = null;
	
	GameContext context = null;
    String textureName = null;
    String objectName = null;
    int resourceId = 0; 
    int width = 0; 
    int height = 0;
    TextureRedrawerListener listener = null;
	
	public TextureRedrawer(GameContext context, 
			               String textureName,
			               String objectName,
			               int resourceId, 
			               int width, 
			               int height,
			               Paint paint,
			               TextureRedrawerListener listener) {
		
		this.context = context;
		this.textureName = textureName;
		this.objectName = objectName;
		this.resourceId = resourceId;
		this.width = width;
		this.height = height;
		this.paint = paint;
		this.listener = listener;
		
	}
	
	public void draw() {
		preDraw(context);
		listener.drawCallback(canvas, height, width);
		postDraw(context);
	} 
	
	public static Paint getDefaultPaint(Activity activity) {
		Paint paint = new Paint(); 
		paint.setStyle(Paint.Style.FILL); 
		paint.setTextSize(22); 
		paint.setAntiAlias(true); 
		Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/adam.ttf");
		paint.setTypeface(tf);
		
		paint.setColor(Color.WHITE); 
		return paint;
	}

	public static Paint getPersonalScorePaint(Activity activity) {
		Paint paint = new Paint(); 
		paint.setStyle(Paint.Style.FILL); 
		paint.setColor(Color.GREEN); 
		paint.setTextSize(22); 
		paint.setAntiAlias(true); 
		Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/adam.ttf");
		paint.setTypeface(tf);
		
		return paint;
	}

	
	public static Paint getDefaultPaint(Activity activity, String font) {
		Paint paint = new Paint(); 
		paint.setStyle(Paint.Style.FILL); 
		paint.setColor(Color.GREEN); 
		paint.setTextSize(22); 
		paint.setAntiAlias(true); 
		Typeface tf = Typeface.createFromAsset(activity.getAssets(), font);
		//paint.setTypeface(Typeface.MONOSPACE);
		paint.setTypeface(tf);
		
		paint.setColor(Color.WHITE); 
		return paint;
	}

	
	private void preDraw(GameContext context) {
		Texture texture = TextureManager.getInstance().getTexture(textureName);
		System.out.println("context.frameBuffer " + context.getFrameBuffer());
		TextureManager.getInstance().unloadTexture(context.getFrameBuffer(), texture);

		bitmap = BitmapHelper.rescale(BitmapHelper.convert(
				context.getActivity().getResources().getDrawable(resourceId)), width, height);
		canvas = new Canvas(bitmap);
		
	}
	
	private void postDraw(GameContext context) {
		/*
		 * Some quirk, by accessing these methods, some side effect makes it work :/
		 */
		bitmap.getWidth(); 
		bitmap.getHeight(); 

		Texture texture = new Texture(bitmap);
		TextureManager.getInstance().replaceTexture(textureName, texture);
		Object3D object = JPCTUtils.getObjectByName(objectName, context.getWorld());
		object.setTexture(textureName);
		object.build();
		
		/*
		 * Clean up
		 */
		bitmap = null;
		paint = null;
		canvas = null;
	}

}
