package se.noren.blox.gameengine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.threed.jpct.Light;
import com.threed.jpct.World;

public class GameEngine implements OnTouchListener, GLSurfaceView.Renderer, GameEngineInterface {

	GameContext  context      = null;
	GameState    currentState = null;
	GameRenderer renderer     = null;
	long         lastTime     = 0;
	
	public GameEngine(Activity activity) {
		context = new GameContext();
		context.activity = activity;
		context.world = new World();
		context.sun = new Light(context.world);
		context.engine = this;
		renderer = new GameRenderer();
	}
	
	
	
	/*
	 * (non-Javadoc)
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	public boolean onTouch(View v, MotionEvent event) {
		System.out.println("Event = " + event.getAction());
		if (event.getAction() == MotionEvent.ACTION_UP) {
			currentState.handleTouchEvent(context, event.getX(), event.getY());
			return true;
		} 
		
		return true;
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see se.noren.android.gameengine.GameEngineInterface#changeGameState(se.noren.android.gameengine.GameState)
	 */
	public void changeGameState(GameState newState) {
		System.out.println("Changing game state to " + newState.getClass().getName());
		currentState = newState;
		context.frameBuffer = renderer.getFrameBuffer();
		newState.initializeGameState(context);
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.khronos.opengles.GL10)
	 */
	public void onDrawFrame(GL10 gl) {
		/*
		 * Update loop
		 */
		long time = System.currentTimeMillis();
		long dt   = time - lastTime;
		lastTime  = time;
		currentState.update(context, dt);
		renderer.drawFrame(context, currentState);
		context.frameBuffer = renderer.getFrameBuffer();
	}

	/*
	 * (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition.khronos.opengles.GL10, int, int)
	 */
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		renderer.surfaceChanged(gl, width, height);
	}

	/*
	 * (non-Javadoc)
	 * @see android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition.khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
	 */
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		renderer.surfaceCreated(gl, config);
	}

}
