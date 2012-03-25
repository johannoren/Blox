package se.noren.blox.jcpt;

import java.io.InputStream;

import android.app.Activity;

import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

/**
 * Load Blender objects.
 * 
 * @author Johan Norén - 25 mar 2012
 */
public class ModelLoader {

	/**
	 * Load a 3DS object.
	 * @param activity Android activity
	 * @param resId Resource id
	 * @param scale Rescale object?
	 * @return Object found in resource bundle.
	 */
	public static Object3D load3DSModel(Activity activity, int resId,
			float scale) {
		InputStream object = activity.getResources().openRawResource(resId);

		Object3D[] model = Loader.load3DS(object, scale);
		Object3D o3d = new Object3D(0);
		Object3D temp = null;
		for (int i = 0; i < model.length; i++) {
			System.out.println("model name: " + model[i].getName());
			temp = model[i];
			temp.setCenter(SimpleVector.ORIGIN);
			temp.rotateX((float) (-.5 * Math.PI));
			temp.rotateMesh();
			temp.setRotationMatrix(new Matrix());
			o3d = Object3D.mergeObjects(o3d, temp);
			o3d.build();
		}
		return o3d;
	}
	
	public static Object3D loadObjModel(Activity activity, int resId, int mtlId,
			float scale) {
		InputStream object = activity.getResources().openRawResource(resId);
		InputStream material = activity.getResources().openRawResource(mtlId);

		Object3D[] model = Loader.loadOBJ(object, material, scale);
		Object3D o3d = new Object3D(0);
		Object3D temp = null;
		for (int i = 0; i < model.length; i++) {
			System.out.println("model name: " + model[i].getName());
			temp = model[i];
			temp.setCenter(SimpleVector.ORIGIN);
			temp.rotateX((float) (-.5 * Math.PI));
			temp.rotateMesh();
			temp.setRotationMatrix(new Matrix());
			o3d = Object3D.mergeObjects(o3d, temp);
			o3d.build();
		}
		return o3d;
	}
	
	public static void load3DSModelsAsSeparateWorldObjects(Activity activity, World world, int resId,
			float scale, String[] exceptions) {
		InputStream object = activity.getResources().openRawResource(resId);

		Object3D[] model = Loader.load3DS(object, scale);
		Object3D o3d = new Object3D(0);
		Object3D newObj = null;
		for (int i = 0; i < model.length; i++) {
			String name = model[i].getName();
			System.out.println("model name: " + name);
			boolean exc = false;
			for (int j = 0; j < exceptions.length && !exc; j++) {
				if (name.indexOf(exceptions[j]) != -1) {
					/*
					 * Found an object that should be treated separately
					 */
					exc = true;
				}
			}
			newObj = model[i];
			newObj.setCenter(SimpleVector.ORIGIN);
			newObj.rotateX((float) (-.5 * Math.PI));
			newObj.rotateMesh();
			newObj.setRotationMatrix(new Matrix());
			
			if (exc) {
				System.out.println(name + " added separately");
				newObj.build();
				world.addObject(newObj);
			} else {
				o3d = Object3D.mergeObjects(o3d, newObj);
				o3d.build();				
			}
		}
		
		o3d.strip();
		
		world.addObject(o3d);
	}

}
