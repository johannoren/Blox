package se.noren.blox.gameengine;

import android.app.Activity;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.World;

public class GameContext {

	public World               world       = null;
	public Light               sun         = null;
	public Activity            activity    = null;
	public FrameBuffer         frameBuffer = null;
	public GameEngineInterface engine      = null;
	
	
}
