package se.noren.blox.gamestates;

import se.noren.blox.gameengine.GameContext;
import se.noren.blox.gameengine.GameState;
import se.noren.blox.logic.Block;
import se.noren.blox.logic.BloxConstants;

import com.threed.jpct.Object3D;

public class AnimateBlocksGameState extends GameState {

	public final static long ANIM_LENGTH = 400;
	
	PlayGameState gameState = null;
	long animStart = 0;
	long currentAnimTime = 0;
	private Block[][] matrix = null;
	
	public AnimateBlocksGameState(GameContext context, PlayGameState gameState, Block[][] matrix) {
		this.gameState = gameState;
		this.matrix = matrix;
	}
	
	@Override
	public void initializeGameState(GameContext context) {
		animStart = System.currentTimeMillis();
		currentAnimTime = animStart;
	}
	
	@Override
	public void update(GameContext context, long dt) {
		currentAnimTime += dt;
		float percentage = (currentAnimTime - animStart) / (float) ANIM_LENGTH;
		
		System.out.println("animation step: " + percentage);

		if (currentAnimTime > animStart + ANIM_LENGTH) {
			/*
			 * Stop animation
			 */
			context.engine.changeGameState(gameState);
			percentage = 1.0f;
		}
		
		System.out.println("animation update, currentAnimTime " + currentAnimTime);
		for (int i = 0; i < BloxConstants.DIM_X; i++)
			for (int j = 0; j < BloxConstants.DIM_Y; j++) {
				Block b = matrix[i][j];
				if (b != null && b.ongoingAnimation) {
					positionBlock(b, context, percentage);
				}
			}
	}

	private void positionBlock(Block b, GameContext context, float percentageDone) {
		Object3D object = b.object;
		float x = b.animationFromX * (1.0f - percentageDone) + b.animationToX * percentageDone; 
		float y = b.animationFromY * (1.0f - percentageDone) + b.animationToY * percentageDone; 

		object.clearTranslation();
		object.translate(x * PlayGameState.SPACING, -y * PlayGameState.SPACING, 0);					
	}	
}
