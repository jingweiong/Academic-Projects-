package game.actors;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.Status;
import game.actions.RecoverRunesAction;

/**
 * Dropped runes actor for runes dropped by player on death.
 * 
 * Created by:
 * @author Jun Lim
 */
public class DroppedRunes extends Actor{
    /**
     * Amount of runes dropped.
     */
    private int amount;

    /**
     * Constuctor.
     */
    public DroppedRunes(int amount) {
        super("Dropped Runes", '$', 1);
        this.amount = amount;
    }
    
    /**
     * Always does nothing.
     * 
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return           the valid action that can be performed in that iteration or null if no valid action is found
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        return new DoNothingAction(); // do nothing for the turn
    }

    /**
     * Allows the player to recover their runes.
     * 
     * @param otherActor the player recovering their runes
     * @param direction  String representing the direction of the player
     * @param map        current GameMap
     * @return			 the list of actions to be added to the other actor's ActionList
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actions = new ActionList();
        if (otherActor.hasCapability(Status.CAN_DROP_RUNES)) {          // if other actor can recover runes
            actions.add(new RecoverRunesAction()); // recover their runes
        }
        return actions;
    }

    /**
     * Returns the amount of runes dropped.
     * 
     * @return the amount of runes dropped
     */
    public int getAmount() {
        return amount;
    }
}
