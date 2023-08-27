package game.items.weapons;

import edu.monash.fit2099.engine.weapons.WeaponItem;

/**
 * Temporary weapon used by special Unsheathe attack action.
 * Created by:
 * @author Jun Lim
 */
public class UnsheatheWeapon extends WeaponItem {
    /**
     * Constructor
     * 
     * @param name name of the item
	 * @param displayChar character to use for display when item is on the ground
	 * @param damage amount of damage this weapon does
	 * @param verb verb to use for this weapon, e.g. "hits", "zaps"
	 * @param hitRate the probability/chance to hit the target.
     */
    public UnsheatheWeapon(String name, char displayChar, int damage, String verb) {
        super(name, displayChar, damage * 2, verb, 60);
    }
}
