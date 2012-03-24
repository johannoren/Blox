package se.noren.blox.jcpt;

import java.util.Enumeration;

import com.threed.jpct.Object3D;
import com.threed.jpct.PolygonManager;
import com.threed.jpct.World;

public class JPCTUtils {

	
	public static Object3D getObjectByName(String prfix, World w) {
		Enumeration<Object3D> objects = w.getObjects();
		while (objects.hasMoreElements()) {
			Object3D o = objects.nextElement();
	//		System.out.println("Searching " + prfix + " in " + o.getName());
			if (o.getName().startsWith(prfix)) {
	//			System.out.println("FOUND IT!");
				return o;
			}
		}
		return null;
	}
	
	public static void printUVCoords(Object3D o) {
		PolygonManager pm = o.getPolygonManager();
		for (int i = 0; i < pm.getMaxPolygonID(); i++) {
		   for (int p = 0; p < 3; p++) {
		      System.out.println("UV: " + i + "/" + p + ": " + pm.getTextureUV(i, p));
		   }
		}

	}
	
	
	public static void setupForPicking(String name, World world) {
		Object3D obj = JPCTUtils.getObjectByName(name, world);
		obj.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
		obj.strip();
		obj.build();
	}
	
	public static void removePicking(String name, World world) {
		Object3D obj = JPCTUtils.getObjectByName(name, world);
		obj.setCollisionMode(Object3D.COLLISION_CHECK_NONE);
		obj.strip();
		obj.build();
	}

}
