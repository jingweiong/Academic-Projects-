package game.actors.enemies.SkeletalEnemy;

import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.items.weapons.Scimitar;
/**
 * Class for Skeletal Bandit enemy.
 * Starts with a Scimitar.
 *
 * Created by:
 * @author Jun Lim
 */
public class SkeletalBandit extends SkeletalEnemy {
    /**
     * Constructor.
     */
    public SkeletalBandit() {
        super("Skeletal Bandit", 'b', 184, 35, 892);
        this.addWeaponToInventory(new Scimitar()); // adding starting weapon to inventory
    }

    /**
	 * Creates and returns the Skeletal Bandit's intrinsic weapon
     * (intrinsic weapon is not mentioned in spec, creating own)
	 * 
	 * @return a new instance of the IntrinsicWeapon
	 */
    @Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        return new IntrinsicWeapon(15, "shambles at", 100);
    }
}
