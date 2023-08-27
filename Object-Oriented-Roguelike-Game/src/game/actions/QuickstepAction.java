package game.actions;

import java.util.List;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.Weapon;

/**
 * Class representing the special Quickstep attack action.
 * Created by:
 * @author Toh Xi Heng
 * Modified by:
 * @author Jun Lim
 */
public class QuickstepAction extends AttackAction{
    /**
     * Constructor.
     * 
     * @param target    the Actor to attack
     * @param direction the direction of the Actor
     * @param weapon    the weapon used for the attack
	 */
    public QuickstepAction(Actor target, String direction, Weapon weapon) {
        super(target, direction, weapon);
    }

    /**
	 * Performs the default attack action, then checks the actor's surrounding exits to see if there is a valid destination to move to, and moves into it if it exists.
	 *
	 * @param actor the actor performing the attack action
	 * @param map   the map the actor is on
	 * @return      the result of the attack
	 * @see AttackAction
	 */
    @Override
    public String execute(Actor actor, GameMap map) {
        String result = "";
        Location newDestination = null;                      // the destination to the actor move to
        List<Exit> exits = map.locationOf(actor).getExits(); // get each exit around the actor

        for (Exit exit : exits) {                            // for each exit
            Location destination = exit.getDestination();    // get the destination of that exit
            if ((destination.containsAnActor() == false) && (destination.canActorEnter(actor) == true)) { // if the destination does not already contain an actor and the actor can move into it
                newDestination = destination; // update the destination to move to
            }
        }
        result += super.execute(actor, map); // performing the action
        if (newDestination != null) {           // if a valid destination was found
            map.moveActor(actor, newDestination);// move the actor there
            result += ("\n" + actor + " moved."); 
        }
        return result;
    }

    /**
	 * Describes the quickstep attack
	 *
	 * @param actor the actor performing the quickstep attack
	 * @return      a description used for the menu UI
	 */
	@Override
	public String menuDescription(Actor actor) {
		return actor + " retreats with " + (weapon) + " while attacking " + this.target + " at " + this.direction;
	}
}
