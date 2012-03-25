package se.noren.blox;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import se.noren.blox.gameengine.GameEngine;
import se.noren.blox.gamestates.LoadGameState;
import se.noren.blox.remotetooling.CustomExceptionHandler;
import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

/**
 * <p>Activity that flips rendering to GL mode.
 * From now on the game engine takes over keeping 
 * view state. All views are rendered on a GLSurfaceView
 * held by this activity.</p> 
 * 
 * @author Johan Norén - 25 mar 2012
 */
public class MainBloxActivity extends Activity {
	private GLSurfaceView mGLSurfaceView;
	private GameEngine gameEngine;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * If an exception occurs, log it on SD card so we can follow up later.
		 */
		Thread.setDefaultUncaughtExceptionHandler(new CustomExceptionHandler(
		        "/sdcard/bloxcrashlogs", null));
		
		mGLSurfaceView = new GLSurfaceView(this);
		mGLSurfaceView.setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() {
			public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
				// Ensure that we get a 16bit framebuffer. Otherwise, we'll fall
				// back to Pixelflinger on some device (read: Samsung I7500)
				int[] attributes = new int[] { EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE };
				EGLConfig[] configs = new EGLConfig[1];
				int[] result = new int[1];
				egl.eglChooseConfig(display, attributes, configs, 1, result);
				return configs[0];
			}
		});

		gameEngine = new GameEngine(this);
		gameEngine.changeGameState(new LoadGameState());
		
		mGLSurfaceView.setRenderer(gameEngine);
		mGLSurfaceView.setOnTouchListener(gameEngine);
		setContentView(mGLSurfaceView);
	}

	@Override
	protected void onResume() {
		// Ideally a game should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onResume();
		mGLSurfaceView.onResume();
	}

	@Override
	protected void onPause() {
		// Ideally a game should implement onResume() and onPause()
		// to take appropriate action when the activity looses focus
		super.onPause();
		mGLSurfaceView.onPause();
		
		System.exit(0);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mGLSurfaceView.onPause();
		System.exit(0);
	}
}