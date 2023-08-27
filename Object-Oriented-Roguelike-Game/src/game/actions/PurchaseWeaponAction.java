package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.TradeableWeapon;
import game.RuneManager;

/**
 * Class to represent purchasing weapons from a Trader action.
 * Created by:
 * @author jingweiong
 * Modified by:
 * @author Jun Lim
 */
public class PurchaseWeaponAction extends Action {
    /**
     * The tradeable weapon being purchased.
     */
    private TradeableWeapon weapon;

    /**
     * Constructor.
     * 
     * @param weapon the tradeable weapon being purchased
     */
    public PurchaseWeaponAction(TradeableWeapon weapon){
        this.weapon = weapon;
    }

    /**
     * Lets the player attempt to purchase the weapon, succeeding if they can afford it.
     * 
     * @param actor the player purchasing the weapon
     * @param map   the map the actor is in
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        RuneManager runeManager = RuneManager.getInstance();         // getting the RuneManager instance
        if (runeManager.getRunes() < weapon.getPurchasePrice()) {    // if the player has less runes than what the weapon costs
            return (actor + " cannot afford the " + weapon.getWeapon() + ".");
        } else {
            runeManager.changeRunes(-1 * weapon.getPurchasePrice()); // deduct the player's rune count by the price
            actor.addWeaponToInventory(weapon.getWeapon());          // add the weapon to the player's inventory
            return menuDescription(actor);
        } 
    }

    /**
	 * Describes the successful purchase action.
	 *
	 * @param actor the actor purchasing
	 * @return      a description used for the menu UI
	 */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " purchases the " + weapon.getWeapon().toString() + " for " + weapon.getPurchasePrice() + " runes";
    }
}
