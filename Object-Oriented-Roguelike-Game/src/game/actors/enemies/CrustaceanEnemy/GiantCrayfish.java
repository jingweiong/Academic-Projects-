package game.actors.enemies.CrustaceanEnemy;

import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.Status;

/**
 * Class for Giant Crayfish enemy
 *
 * Created by:
 * @author Jun Lim
 */
public class GiantCrayfish extends CrustaceanEnemy {
    /**
     * Constructor.
     */
    public GiantCrayfish() {
        super("Giant Crayfish", 'R', 4803, 500, 2374);
        this.addCapability(Status.ENEMY_GIANT); // add ability to use slam attack
    }

    /**
	 * Creates and returns the Giant Crayfish's intrinsic weapon
	 * 
	 * @return a new instance of the IntrinsicWeapon
	 */
    @Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        return new IntrinsicWeapon(527, "pinches", 100);
    }
}
