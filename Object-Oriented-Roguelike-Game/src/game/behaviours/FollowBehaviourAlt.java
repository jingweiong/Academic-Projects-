package game.behaviours;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.positions.Exit;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

import edu.monash.fit2099.engine.actions.MoveActorAction;

/**
 * Modification of FollowBehaviour class, to better follow the target
 *
 * Created by:
 * @author Jun Lim
 */
public class FollowBehaviourAlt implements Behaviour {

	/**
	 * The actor being followed.
	 */
	private final Actor target;

	/**
	 * Constructor.
	 * 
	 * @param subject the Actor to follow
	 */
	public FollowBehaviourAlt(Actor subject) {
		this.target = subject;
	}

	@Override
	public Action getAction(Actor actor, GameMap map) {
		if(!map.contains(target) || !map.contains(actor))
			return null;
		
		Location here = map.locationOf(actor);
		Location there = map.locationOf(target);

        int currentDistance = distance(here, there);
        if (currentDistance == 1) { // if already at minimum distance to target
            // System.out.println("already next to target!");
            return new DoNothingAction(); // do nothing
        }

		Exit bestexit = null;
        Location bestdestination;
        int bestdistance = currentDistance;

		for (Exit exit : here.getExits()) {
			Location destination = exit.getDestination();
			if (destination.canActorEnter(actor)) {
				int newDistance = distance(destination, there);
				// System.out.println("new proprosed distance: " + newDistance + " vs: " + currentDistance);
				if (newDistance < bestdistance) { // if the distance is better
					// System.out.println(exit.getName() + " " + newDistance  + " vs " + bestdistance);
					bestdistance = newDistance;
					bestexit = exit;
					// return new MoveActorAction(destination, exit.getName());
				}
			}
		}
        
		if (bestexit != null) { // if a move was found
            bestdestination = bestexit.getDestination(); // get the destination
			return new MoveActorAction(bestdestination, bestexit.getName()); // return the move
		}
		// System.out.println("follow move not found");
		return new DoNothingAction(); // do nothing
	}

	/**
	 * Compute the distance between two locations assuming actor can move in all eight directions.
     * @param a the first location
	 * @param b the first location
	 * @return the number of steps between a and b if you can move in all eight cardinal and intercadinal directions.
	 */
	private int distance(Location a, Location b) {
		double delta_x = a.x() - b.x();
		double delta_y = a.y() - b.y();
		double res = Math.ceil(Math.sqrt((delta_x * delta_x) + (delta_y * delta_y)));
		return (int) res;
	}
}