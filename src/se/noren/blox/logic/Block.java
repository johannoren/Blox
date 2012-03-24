package se.noren.blox.logic;

import com.threed.jpct.Object3D;

public class Block {

	public Object3D object = null;
	public Blocks   type   = Blocks.EMPTY;
	public int      x;
	public int      y;

	public boolean  ongoingAnimation = false;
	public int      animationFromX;
	public int      animationFromY;
	public int      animationToX;
	public int      animationToY;

}
