package game.items.weapons;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.actions.UnsheatheAction;

/**
 * A katana type weapon. Deals 115 damage with 80% accuracy.
 * Created by:
 * @author Toh Xi Heng
 * Modified by:
 * @author Jun Lim
 */
public class Uchigatana extends WeaponItem {
    /**
     * Constructor
     */
    public Uchigatana() {
        super("Uchigatana", ')', 115, "slices", 80);
    }

    /**
     * Returns the special unsheathe attack action.
     * 
     * @param target    target actor
     * @param direction direction of target actor
     * @return          an unsheathe attack Action
     */
    @Override
    public Action getSkill(Actor target, String direction) {
        return new UnsheatheAction(target, direction, this);
    }
}
