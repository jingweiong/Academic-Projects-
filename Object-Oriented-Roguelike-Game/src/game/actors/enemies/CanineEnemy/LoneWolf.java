package game.actors.enemies.CanineEnemy;

import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;

/**
 * Class for Lone Wolf enemy
 *
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 * @author Jun Lim
 */
public class LoneWolf extends CanineEnemy {
    /** 
     * Constructor.
    */
    public LoneWolf() {
        super("Lone Wolf", 'h', 102, 55, 1470);
    }

    /**
	 * Creates and returns the Lone Wolf's intrinsic weapon
	 * 
	 * @return a new instance of the IntrinsicWeapon
	 */
    @Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        return new IntrinsicWeapon(97, "bites", 95);
    }
}
