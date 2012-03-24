package se.noren.blox.gamestates;


import java.util.ArrayList;
import java.util.List;

import se.noren.blox.R;
import se.noren.blox.gameengine.GameContext;
import se.noren.blox.gameengine.GameState;
import se.noren.blox.jcpt.JPCTUtils;
import se.noren.blox.jcpt.ModelLoader;
import se.noren.blox.logic.Block;
import se.noren.blox.logic.Blocks;
import se.noren.blox.logic.BloxConstants;

import com.threed.jpct.Camera;
import com.threed.jpct.Light;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.MemoryHelper;

public class LoadGameState extends GameState {

	private static boolean isSetup = false;
	private List<Block> blocks;
	private List<Object3D> hammers;
	
	/*
	 * (non-Javadoc)
	 * @see se.noren.android.gameengine.GameState#initializeGameState(se.noren.android.gameengine.GameContext)
	 */
	
	@Override
	public void initializeGameState(GameContext context) {
		if (!isSetup) {
			isSetup = true;
			setup(context);
		}
	}

	private void loadTexture(int resource, int dimension, String texName, GameContext context) {
		Texture texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(
		          context.activity.getResources().getDrawable(resource)), dimension, dimension));
		TextureManager.getInstance().addTexture(texName, texture);
	}
	
	public void setup(GameContext context) {
		World world = context.world;
		Light sun   = context.sun;

		world.setAmbientLight(100, 100, 100);
		sun.setIntensity(255, 255, 255);
		
		String[] separateLoadObjects = {"bluecube", "greencube", "redcube", "hammer"};
		ModelLoader.load3DSModelsAsSeparateWorldObjects(context.activity, world, R.raw.bluecube, 1.0f, separateLoadObjects);
		ModelLoader.load3DSModelsAsSeparateWorldObjects(context.activity, world, R.raw.redcube, 1.0f, separateLoadObjects);
		ModelLoader.load3DSModelsAsSeparateWorldObjects(context.activity, world, R.raw.greencube, 1.0f, separateLoadObjects);
		ModelLoader.load3DSModelsAsSeparateWorldObjects(context.activity, world, R.raw.hammer, 1.0f, separateLoadObjects);
		
		Camera cam = world.getCamera();
		cam.setPosition(PlayGameState.SPACING * BloxConstants.DIM_X / 2.0f - 1, -PlayGameState.SPACING * BloxConstants.DIM_Y / 2.0f + 2, -27);
		cam.lookAt(new SimpleVector(PlayGameState.SPACING * BloxConstants.DIM_X / 2.0f - 1, -PlayGameState.SPACING * BloxConstants.DIM_Y / 2.0f + 2, 0));

		SimpleVector sv = new SimpleVector(new SimpleVector(SimpleVector.ORIGIN));
		sv.z -= 100;
		sv.y -= 40;
		sun.setPosition(sv);
		
		blocks = createBlocks(world);	
		hammers = createHammers(world);
			
		MemoryHelper.compact();
	}
	



	private List<Block> createBlocks(World world) {
		
		ArrayList<Block> list = new ArrayList<Block>();
		
		Object3D redcube   = JPCTUtils.getObjectByName("redcube", world);
		Object3D greencube = JPCTUtils.getObjectByName("greencube", world);
		Object3D bluecube  = JPCTUtils.getObjectByName("bluecube", world);
		int c = 0;
		for (int i = 0; i < BloxConstants.DIM_X; i++) {
			for (int j = 0; j < BloxConstants.DIM_Y; j++) {
				Object3D newObject = null;
				Block block = new Block();
				if (i % 3 == 0) {
					newObject = redcube.cloneObject();
					newObject.setName("red" + c++);
					block.type = Blocks.RED;
				} else if (i % 3 == 1) {
					newObject = greencube.cloneObject();
					newObject.setName("green" + c++);					
					block.type = Blocks.GREEN;
				} else {
					newObject = bluecube.cloneObject();
					newObject.setName("blue" + c++);
					block.type = Blocks.BLUE;					
				}
				
				newObject.setCollisionMode(Object3D.COLLISION_CHECK_OTHERS);
				world.addObject(newObject);
				
				block.object = newObject;
				newObject.setUserObject(block);
				
				list.add(block);		
			}
		}
		redcube.setVisibility(false);
		greencube.setVisibility(false);
		bluecube.setVisibility(false);
		return list;
	}
	
	private List<Object3D> createHammers(World world) {
		
		ArrayList<Object3D> list = new ArrayList<Object3D>();
		Object3D hammer = JPCTUtils.getObjectByName("hammer", world);
		for (int i = 0; i < BloxConstants.LIFES; i++) {
			Object3D newHammer = null;
			newHammer = hammer.cloneObject();
			newHammer.setName("sledgehammer" + i);
			
			newHammer.clearRotation();
			newHammer.rotateX((float) Math.PI);
			newHammer.scale(2.0f);
			
			newHammer.clearTranslation();
			newHammer.translate(i * PlayGameState.SPACING * 2 + 15, PlayGameState.SPACING * 2, 0);
			
			world.addObject(newHammer);
			list.add(newHammer);		
		}
		return list;
	}
	
	/*
	 * (non-Javadoc)
	 * @see se.noren.android.gameengine.GameState#update(se.noren.android.gameengine.GameContext, se.noren.android.gameengine.GameEngineInterface, long)
	 */
	@Override
	public void update(GameContext context, long dt) {
		context.engine.changeGameState(new PlayGameState(context, 0, blocks, hammers, 0));
	}

}
