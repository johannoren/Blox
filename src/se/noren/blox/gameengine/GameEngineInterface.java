
package se.noren.blox.gameengine;

/**
 * Definition of a game engine which game states can act against.
 * 
 * @author Johan Norén - 25 mar 2012
 */
public interface GameEngineInterface {

	public void changeGameState(GameState newState);

}
