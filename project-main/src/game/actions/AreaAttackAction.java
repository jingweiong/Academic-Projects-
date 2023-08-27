package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.Weapon;

/**
 * Class representing an Action to attack all targets around an Actor, using AttackAction.
 * Created by:
 * @author Jun Lim
 */
public class AreaAttackAction extends AttackAction{
    /**
     * Constructor.
     * 
     * @param target the Actor to attack
     * @param weapon the weapon used for the attack
	 */
    public AreaAttackAction(Actor target, Weapon weapon) {
        super(target, null, weapon);
    }

    /**
	 * For each adjacent target, the chance to hit of the weapon that the Actor used is computed to determine whether
	 * the actor will hit the target. If so, deal damage to the target and determine whether the target is killed.
	 *
	 * @param actor the actor performing the attack action
	 * @param map the map the actor is on
	 * @return the result of the attack, e.g. whether the target is killed, etc
	 * @see DeathAction
	 */
    @Override
    public String execute(Actor actor, GameMap map) {
        Location here = map.locationOf(actor);  // getting the location of the actor
        String result = "";                     // final string to be returned
        for (Exit exit : here.getExits()) {                 // for each exit in the location
			Location destination = exit.getDestination();   // getting the exit destination
            if (destination.containsAnActor()) {            // if there is an actor
                Actor target = destination.getActor();      // get the actor
                // System.out.println("targetting: " + target + " " + destination + " " + actor);
                Action attackaction = new AttackAction(target, null, weapon); // create a new attack action for the target
                result += attackaction.execute(actor, map);                             // execute the attack
                result += "\n";                                                         // add a newline to the result
                }
            }
            result = result.substring(0,result.length() - 1); // remove the last excess newline character
        return result;
    }

    /**
	 * Describes the area attack
	 *
	 * @param actor the actor performing the action
	 * @return      a description used for the menu UI
	 */
	@Override
	public String menuDescription(Actor actor) {
		return actor + " attacks in an area with " + (weapon);
	}
}
