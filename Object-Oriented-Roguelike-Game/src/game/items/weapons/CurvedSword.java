package game.items.weapons;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.actions.AreaAttackAction;

/**
 * Base class for curved swords, which have the area attack special attack
 * Created by:
 * @author Jun Lim
 */
public abstract class CurvedSword extends WeaponItem {
    /**
     * Constructor.
     * 
     * @param name        name of the item
     * @param displayChar character to use for display when item is on the ground
	 * @param damage      amount of damage this weapon does
	 * @param verb        verb to use for this weapon, e.g. "hits", "zaps"
	 * @param hitRate     the probability/chance to hit the target.
     */
    public CurvedSword(String name, char displayChar, int damage, String verb, int hitRate) {
        super(name, displayChar, damage, verb, hitRate);
    }

    /**
     * Returns the special area attack action.
     * 
     * @param target    target actor
     * @param direction direction of target actor
     * @return          an area attack Action
     */
    @Override
    public Action getSkill(Actor target, String direction) {
        return new AreaAttackAction(target, this);
    }

}
