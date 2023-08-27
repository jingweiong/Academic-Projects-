package game.items.weapons;

import edu.monash.fit2099.engine.weapons.WeaponItem;

/**
 * A hammer type weapon. Deals 103 damage with 80% accuracy.
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 * @author ongjingwei
 */
public class Club extends WeaponItem {
    /**
     * Constructor
     */
    public Club() {
        super("Club", '!', 103, "bonks", 80);
    }
}
