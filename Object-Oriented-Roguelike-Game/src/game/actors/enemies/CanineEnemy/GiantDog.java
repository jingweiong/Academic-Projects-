package game.actors.enemies.CanineEnemy;

import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.Status;

/**
 * Class for Giant Dog enemy
 *
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 * @author Jun Lim
 */
public class GiantDog extends CanineEnemy {
    /** 
     * Constructor.
    */
    public GiantDog() {
        super("Giant Dog", 'G', 693, 313, 1808);
        this.addCapability(Status.ENEMY_GIANT); // allowing Giant Dog to use the slam attack
    }

    /**
	 * Creates and returns the Giant Dog's intrinsic weapon
	 * 
	 * @return a new instance of the IntrinsicWeapon
	 */
    @Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        return new IntrinsicWeapon(314, "swings their head at", 90);
    }
}
