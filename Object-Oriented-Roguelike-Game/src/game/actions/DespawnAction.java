package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;

/**
 * An Action to despawn an actor, removing it from the game.
 * Created by:
 * @author Jun Lim
 */
public class DespawnAction extends Action {
	/**
	 * Constructor.
	 */
	public DespawnAction() {}

	/**
	 * When executed, despawns the actor.
	 *
	 * @param actor the actor being despawned
	 * @param map the map the actor is on
	 * @return the despawn message
	 */
	@Override
	public String execute(Actor actor, GameMap map) {
		map.removeActor(actor); // remove the actor from the map
		return (actor + " despawned");
	}

	/**
	 * Describes the actor is being despawned.
	 *
	 * @param actor the actor performing the action
	 * @return 	    a description used for the menu UI
	 */
	@Override
	public String menuDescription(Actor actor) {
		return (actor + "despawns");
	}
}
