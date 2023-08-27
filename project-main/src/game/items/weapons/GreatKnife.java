package game.items.weapons;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.actions.QuickstepAction;

/**
 * A dagger type weapon. Deals 75 damage with 70% accuracy.
 * Created by:
 * @author Toh Xi Heng
 * Modified by:
 * @author Jun Lim
 */
public class GreatKnife extends WeaponItem {
    /**
     * Constructor
     */
    public GreatKnife() {
        super("Great Knife", '/', 75, "stabs", 70);
    }

    /**
     * Returns the special quickstep attack action.
     * 
     * @param target    target actor
     * @param direction direction of target actor
     * @return          an quickstepthe attack Action
     */
    @Override
    public Action getSkill(Actor target, String direction) {
        return new QuickstepAction(target, direction, this);
    }
}
