package se.noren.blox.gameengine;

import android.app.Activity;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.World;

/**
 * Context that holds objects which might be needed 
 * around the game.
 * 
 * @author Johan Norén - 25 mar 2012
 */
public class GameContext {

	private World               world       = null;
	private Light               sun         = null;
	private Activity            activity    = null;
	private FrameBuffer         frameBuffer = null;
	private GameEngineInterface engine      = null;
	
	public World getWorld() {
		return world;
	}
	public void setWorld(World world) {
		this.world = world;
	}
	public Light getSun() {
		return sun;
	}
	public void setSun(Light sun) {
		this.sun = sun;
	}
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	public FrameBuffer getFrameBuffer() {
		return frameBuffer;
	}
	public void setFrameBuffer(FrameBuffer frameBuffer) {
		this.frameBuffer = frameBuffer;
	}
	public GameEngineInterface getEngine() {
		return engine;
	}
	public void setEngine(GameEngineInterface engine) {
		this.engine = engine;
	}
	
	
}
