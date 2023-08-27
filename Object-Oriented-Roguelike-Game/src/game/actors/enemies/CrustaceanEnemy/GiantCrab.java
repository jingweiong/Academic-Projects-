package game.actors.enemies.CrustaceanEnemy;

import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.Status;

/**
 * Class for Giant Crab enemy
 *
 * Created by:
 * @author Jun Lim
 */
public class GiantCrab extends CrustaceanEnemy {
    /**
     * Constructor.
     */
    public GiantCrab() {
        super("Giant Crab", 'C', 407, 318, 4961);
        this.addCapability(Status.ENEMY_GIANT); // add ability to use slam attack
    }

    /**
	 * Creates and returns the Giant Crab's intrinsic weapon.
	 * 
	 * @return a new instance of the IntrinsicWeapon
	 */
    @Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        return new IntrinsicWeapon(208, "pinches", 90);
    }
}
