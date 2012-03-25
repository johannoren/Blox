package se.noren.blox.jcpt;

import java.util.Enumeration;

import com.threed.jpct.Object3D;
import com.threed.jpct.PolygonManager;
import com.threed.jpct.World;

/**
 * Utils for jPCT.
 * 
 * @author Johan Norén - 25 mar 2012
 */
public class JPCTUtils {
	
	/**
	 * @param prfix Prefix of object name in model file.
	 * @param w World to search for objects
	 * @return First found object with given prefix.
	 */
	public static Object3D getObjectByName(String prfix, World w) {
		Enumeration<Object3D> objects = w.getObjects();
		while (objects.hasMoreElements()) {
			Object3D o = objects.nextElement();
			if (o.getName().startsWith(prfix)) {
				return o;
			}
		}
		return null;
	}
	
	/**
	 * Print out all UV coordinates of a given object
	 * @param o Object to inspect
	 */
	public static void printUVCoords(Object3D o) {
		PolygonManager pm = o.getPolygonManager();
		for (int i = 0; i < pm.getMaxPolygonID(); i++) {
		   for (int p = 0; p < 3; p++) {
		      System.out.println("UV: " + i + "/" + p + ": " + pm.getTextureUV(i, p));
		   }
		}

	}
	
	/**
	 * Make a certain object available for touching and selecting via picking
	 * API.
	 * @param name Name of object
	 * @param world World to find object.
	 */
	public static void setupForPicking(String name, World world) {
		Object3D obj = JPCTUtils.getObjectByName(name, world);
		obj.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		obj.strip();
		obj.build();
	}
	
	/**
	 * Make an object unavailable for picking.
	 * @param name Name of object
	 * @param world World to find object
	 */
	public static void removePicking(String name, World world) {
		Object3D obj = JPCTUtils.getObjectByName(name, world);
		obj.setCollisionMode(Object3D.COLLISION_CHECK_NONE);
		obj.strip();
		obj.build();
	}

}
