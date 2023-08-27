package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.TradeableWeapon;
import game.RuneManager;

/**
 * Class to represent selling weapons from a Trader action.
 * Created by:
 * @author jingweiong
 * Modified by:
 * @author Jun Lim
 */
public class SellWeaponAction extends Action {
    /**
     * The tradeable weapon being sold.
     */
    private TradeableWeapon weapon;
    
    /**
     * Constructor.
     * 
     * @param weapon the tradeable weapon being sold
     */
    public SellWeaponAction(TradeableWeapon weapon){
        this.weapon = weapon;
    }

    /**
     * Lets the player sell the weapon.
     * 
     * @param actor the player pselling the weapon
     * @param map   the map the actor is in
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        RuneManager runeManager = RuneManager.getInstance(); // getting the RuneManager instance       
        WeaponItem weaponItem = actor.getWeaponInventory().get(actor.getWeaponInventory().indexOf(weapon.getWeapon())); // getting the player weapon
        actor.removeWeaponFromInventory(weaponItem);       // removing the weapon from the player's inventory
        runeManager.changeRunes(weapon.getSellingPrice()); // updating the player's rune count
        return menuDescription(actor);
    }

    /**
	 * Describes the successful purchase action.
	 *
	 * @param actor the actor purchasing
	 * @return      a description used for the menu UI
	 */
    @Override
    public String menuDescription(Actor actor) {
        return actor + " sells the " + weapon.getWeapon().toString() + " for " + weapon.getSellingPrice() + " runes";
    }
}
