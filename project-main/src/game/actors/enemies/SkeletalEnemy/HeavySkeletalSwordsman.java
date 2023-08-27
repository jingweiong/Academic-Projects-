package game.actors.enemies.SkeletalEnemy;

import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import game.items.weapons.Grossmesser;

/**
 * Class for Heavy Skeletal Swordsman enemy.
 * Starts with a Grossmesser.
 *
 * Created by:
 * @author Jun Lim
 */
public class HeavySkeletalSwordsman extends SkeletalEnemy {
    /**
     * Constructor.
     */
    public HeavySkeletalSwordsman() {
        super("Heavy Skeletal Swordsman", 'q', 153, 35, 892);
        this.addWeaponToInventory(new Grossmesser()); // adding starting weapon to inventory
    }

    /**
	 * Creates and returns the Heavy Skeletal Swordsman's intrinsic weapon
     * (intrinsic weapon is not mentioned in spec, creating own)
	 * 
	 * @return a new instance of the IntrinsicWeapon
	 */
    @Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        return new IntrinsicWeapon(15, "shambles at", 100);
    }
}
