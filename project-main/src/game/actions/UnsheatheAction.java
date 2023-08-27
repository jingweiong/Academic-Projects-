package game.actions;

import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.weapons.Weapon;
import game.items.weapons.UnsheatheWeapon;

/**
 * Class representing the special Unsheathe attack action.
 * Created by:
 * @author Toh Xi Heng
 * Modified by:
 * @author Jun Lim
 */
public class UnsheatheAction extends AttackAction{
    /**
     * Constructor.
     * 
     * @param target    the Actor to attack
     * @param direction the direction of the Actor
     * @param weapon    the weapon used for the attack
	 */
    public UnsheatheAction(Actor target, String direction, Weapon weapon) {
        super(target, direction, weapon);
        this.weapon = new UnsheatheWeapon(weapon.toString(), '0', weapon.damage(), "unsheathes at"); // creating temporary weapon with 2x damage and 60% accuracy
    }

    /**
	 * Describes the unsheathe attack
	 *
	 * @param actor the actor performing the unsheathe attack
	 * @return      a description used for the menu UI
	 */
	@Override
	public String menuDescription(Actor actor) {
		return actor + " unsheathes " + (weapon) + " towards " + this.target + " at " + this.direction;
	}
}
