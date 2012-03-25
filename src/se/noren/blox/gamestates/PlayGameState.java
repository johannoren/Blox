package se.noren.blox.gamestates;

import java.util.LinkedList;
import java.util.List;

import se.noren.blox.gameengine.GameContext;
import se.noren.blox.gameengine.GameState;
import se.noren.blox.glfont.GLFont;
import se.noren.blox.logic.Block;
import se.noren.blox.logic.Blocks;
import se.noren.blox.logic.BloxConstants;

import android.graphics.Paint;
import android.graphics.Typeface;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;

/**
 * Main state during playing the game.
 * 
 * @author Johan Norén - 25 mar 2012
 */
public class PlayGameState extends GameState {

	public static final float  SPACING = 2.2f;
	
	private List<Block>    blocks;
	private Block[][]      matrix = new Block[BloxConstants.DIM_X][BloxConstants.DIM_Y];
	private List<Object3D> hammers;
	private int            lifes = BloxConstants.LIFES;
	private GLFont         glFont;
	private int            score;
	
	public PlayGameState(GameContext context, int level, List<Block> blocks, List<Object3D> hammers, int score) {
		this.blocks  = blocks;
		this.hammers = hammers;
		this.score   = score;
		updateSledgeHammers();
		randomizeMatrix();
		
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTypeface(Typeface.create((String)null, Typeface.BOLD));
		
		paint.setTextSize(16);
		glFont = new GLFont(paint);
	}
	
	@Override
	public void initializeGameState(GameContext context) {
		// ...
	}

	private void updateSledgeHammers() {
		for (int i = 0; i < BloxConstants.LIFES; i++) {
			if (i < lifes) {
				hammers.get(BloxConstants.LIFES - i - 1).setVisibility(true);
			} else {
				hammers.get(BloxConstants.LIFES - i - 1).setVisibility(false);				
			}
		}
	}
	
	private void randomizeMatrix() {
		List<Block> blocksCopy = new LinkedList<Block>();
		for (int i = 0; i < blocks.size(); i++) {
			blocksCopy.add(blocks.get(i));
		}
		
		for (int i = 0; i < BloxConstants.DIM_X; i++) {
			for (int j = 0; j < BloxConstants.DIM_Y; j++) {
				int index = (int) (Math.random() * blocksCopy.size());
				matrix[i][j] = blocksCopy.get(index);
				blocksCopy.remove(index);
				matrix[i][j].object.setVisibility(true);
				matrix[i][j].x = i;
				matrix[i][j].y = j;
			}
		}
	}
	
	private void drawBlocks() {
		for (int i = 0; i < BloxConstants.DIM_X; i++) {
			for (int j = 0; j < BloxConstants.DIM_Y; j++) {
				if (matrix[i][j] != null) {
					Object3D object = matrix[i][j].object;
					object.clearTranslation();
					object.translate(i * SPACING, -j * SPACING, 0);
				}
			}
		}
	}
	
	private int removeAdjacentBlocks(int x, int y, Blocks color) {
		System.out.println("check remove " + x + " " + y);
		if (x < 0 || x >= BloxConstants.DIM_X || y < 0 || y >= BloxConstants.DIM_Y)
			return 0;
		if (matrix[x][y] == null)
			return 0;
		if (matrix[x][y].type != color)
			return 0;
		System.out.println("remove " + x + " " + y);
		matrix[x][y].object.setVisibility(false);
		matrix[x][y] = null;
		
		return 1 + removeAdjacentBlocks(x + 1, y, color) +
				   removeAdjacentBlocks(x - 1, y, color) +  
				   removeAdjacentBlocks(x, y + 1, color) +  
				   removeAdjacentBlocks(x, y - 1, color);  
	}
	
	private boolean nonEmptyAbove(int x, int y) {
		for (int yy = y + 1; yy < BloxConstants.DIM_Y; yy++) {
			if (matrix[x][yy] != null)
				return true;
		}
		return false;
	}

	/**
	 * Move all blox at x y and above one step down.
	 * @param x
	 * @param y
	 */
	private void decreaseColumn(int x, int y) {
		for (; y < BloxConstants.DIM_Y; y++) {
			matrix[x][y - 1] = matrix[x][y];
			matrix[x][y] = null;				
		}
	}
	
	private void checkEmpty(int x, int y) {
		if (matrix[x][y] == null && nonEmptyAbove(x, y)) {
			decreaseColumn(x, y + 1);
			checkEmpty(x, y);
		}
	}
	
	private boolean emptyColumn(int x) {
		if (x < 0 || x >= BloxConstants.DIM_X)
			return true;
		return matrix[x][0] == null;
	}
	
	private void moveColumn(int fromx, int tox) {
		for (int i = 0; i < BloxConstants.DIM_Y; i++) {
			matrix[tox][i] = matrix[fromx][i];
			matrix[fromx][i] = null;
		}
	}
	
	private void compressColumns() {
		boolean found = false;
		for (int i = 0; i < BloxConstants.DIM_X; i++) {
			if (emptyColumn(i)) {
				boolean left  = false;
				boolean right = false;
				
				// Is there a col with blocks to left and right?
				for (int x = i - 1; x >= 0; x--) {
					if (!emptyColumn(x))
						left = true;
				}
				for (int x = i + 1; x < BloxConstants.DIM_X; x++) {
					if (!emptyColumn(x))
						right = true;
				}
				
				if (left && right) {
					moveColumn(i + 1, i);
					found = true;
				}
			}
		}
		
		if (found)
			compressColumns();
	}
	
	private void normalizeMatrix() {
		for (int i = 0; i < BloxConstants.DIM_X; i++) {
			for (int j = 0; j < BloxConstants.DIM_Y; j++) {
				checkEmpty(i, j);
			}
		}
		
		// Move empty columns together
		compressColumns();
		
		updateMatrixCoordinates();
	}
	
	private void updateMatrixCoordinates() {
		for (int i = 0; i < BloxConstants.DIM_X; i++) {
			for (int j = 0; j < BloxConstants.DIM_Y; j++) {
				if (matrix[i][j] != null) {
					matrix[i][j].animationFromX = matrix[i][j].x;
					matrix[i][j].animationFromY = matrix[i][j].y;
					matrix[i][j].animationToX = i;
					matrix[i][j].animationToY = j;
					matrix[i][j].ongoingAnimation = (matrix[i][j].x != i || matrix[i][j].y != j);
					matrix[i][j].x = i;
					matrix[i][j].y = j;
				}
			}
		}
	}

	@Override
	public void update(GameContext context, long dt) {
		drawBlocks();
	}

	@Override
	public void draw(FrameBuffer fb) {
		glFont.blitString(fb, "Score: " + score + " left = " + blockLeft(), 5, fb.getHeight() - 5, 10, RGBColor.WHITE);
	}


	private int blockLeft() {
		int count = 0;
		for (int i = 0; i < BloxConstants.DIM_X; i++) 
			for (int j = 0; j < BloxConstants.DIM_Y; j++) 
				if (matrix[i][j] != null) 
					count++;
		
		return count;
	}
	
	@Override
	public void handleTouchEvent(GameContext context, float x, float y) {
		Object3D touchedObject = touchedObject(context, x, y);
		if (touchedObject != null) {
			Block block = (Block) touchedObject.getUserObject();
			
			int removedBlocks = removeAdjacentBlocks(block.x, block.y, block.type);
			normalizeMatrix();
			
			/*
			 *  Loose a life!
			 */
			if (removedBlocks == 1) {
				lifes--;
				updateSledgeHammers();
			} else {
				score += removedBlocks * 20;
			}
			
			/*
			 * Animate falling blocks, so change game state. 
			 */
			context.getEngine().changeGameState(new AnimateBlocksGameState(context, this, matrix));
			
			if (blockLeft() == 0) {
				randomizeMatrix();
				updateSledgeHammers();
			}
		}
	}
}
