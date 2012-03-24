/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.noren.blox.gameengine;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.RGBColor;
import com.threed.jpct.Texture;

/**
 * Render a pair of tumbling cubes.
 */

public class GameRenderer {

	private FrameBuffer fb = null;
	private RGBColor back = new RGBColor(0, 0, 0);

	public GameRenderer() {
		System.out.println("In intro renderer");
        Texture.defaultToMipmapping(true);
	}
	
	public void drawFrame(GameContext ctx, GameState state) {
		fb.clear(back);
		ctx.world.renderScene(fb);
		ctx.world.draw(fb);
		state.draw(fb);
		fb.display();
	}

	public void surfaceChanged(GL10 gl, int w, int h) {
		if (fb != null) {
			fb.dispose();
		}
		fb = new FrameBuffer(gl, w, h);
	}

    public void surfaceCreated(GL10 gl, EGLConfig config) {
    	System.out.println("onSurfaceCreated");
        /*
         * By default, OpenGL enables features that improve quality
         * but reduce performance. One might want to tweak that
         * especially on software renderer.
         */
        gl.glDisable(GL10.GL_DITHER);

        /*
         * Some one-time OpenGL initialization can be made here
         * probably based on features of this particular context
         */
         gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                 GL10.GL_FASTEST);

         gl.glClearColor(1,1,1,1);
        // gl.glDisable(GL10.GL_CULL_FACE);
         gl.glEnable(GL10.GL_CULL_FACE);
         gl.glShadeModel(GL10.GL_SMOOTH);
         gl.glEnable(GL10.GL_DEPTH_TEST);
    }
    
    public FrameBuffer getFrameBuffer() {
    	return fb;
    }
}
