package se.noren.blox.gameengine;

import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Interact2D;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

/**
 * A game state is a modularized part of a game responsible
 * for rendering its graphics and handling callback events 
 * occuring during its execution.
 * 
 * Via the game engine interface the game state may change 
 * execution to another game state. 
 * 
 * A game state may, depending on its purpose, override 
 * appropriate methods in this base class.
 * 
 * @author Johan Norén - 25 mar 2012
 */
public abstract class GameState {
	
	/**
	 * Do proper setup of this game state before it starts
	 * executing. 
	 */
	public void initializeGameState(GameContext context) {
		
	}

	/**
	 * Time step system
	 * @param dt
	 */
	public void update(GameContext context, long dt) {
		// ..
	}

	public void draw(FrameBuffer fb) {
		// ..
	}

	
	/**
	 * Handle input
	 * @param x
	 * @param y
	 */
    public void handleTouchEvent(GameContext context, float x, float y) {
    	// ..
    }
    
    
    
    protected Object3D touchedObject(GameContext context, float x, float y) {
    	Camera cam = context.getWorld().getCamera();
    	SimpleVector dir = Interact2D.reproject2D3DWS(cam, context.getFrameBuffer(), (int) x, (int) y).normalize();
    	Object[] res = context.getWorld().calcMinDistanceAndObject3D(cam.getPosition(), dir, 10000 /*or whatever*/);
   		if (res != null && res[1] != null) {
   	    	System.out.println("Picked: " + ((Object3D)res[1]).getName());
   			return (Object3D) res[1];
   		}
   		
   		return null;
    } 

}
