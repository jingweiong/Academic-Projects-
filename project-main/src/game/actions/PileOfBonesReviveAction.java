package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/**
 * An action executed by actors to revive the original target.
 * @see PileofBonesAction
 * Created by:
 * @author Jun Lim
 */
public class PileOfBonesReviveAction extends Action{
    /**
     * Target actor to be revived.
     */
    private Actor target;
    
    /**
     * Constructor.
     * 
     * @param target the target to be revived
     */
    public PileOfBonesReviveAction(Actor target) {
        this.target = target;
    }

    /**
     * Removes the Pile of Bones from the map, restoring the newly revived enemy at the same location with full health.
     * 
     * @param actor the Pile of Bones executing the action
     * @param map   the map the Pile of Bones is in
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        Location here = map.locationOf(actor);
        map.removeActor(actor);               // remove the Pile of Bones
        map.addActor(this.target, here);      // add the target back to the map with the same state as before
        this.target.increaseMaxHp(0);; // reset the target's health
        return (menuDescription(actor));
    }

    /**
	 * Describes the revival action.
	 *
	 * @param actor the actor being revived
	 * @return      a description used for the menu UI
	 */
    @Override
    public String menuDescription(Actor actor) {
        return (actor + " is revived!");
    }
    
}
