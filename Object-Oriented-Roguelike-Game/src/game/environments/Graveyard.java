package game.environments;

import edu.monash.fit2099.engine.positions.Location;
import game.actors.enemies.SkeletalEnemy.HeavySkeletalSwordsman;
import game.actors.enemies.SkeletalEnemy.SkeletalBandit;
/**
 * Class representing Gust of Graveyard environment
 * 
 * @author Jun Lim
 * 
 */
public class Graveyard extends SpawningGround{
    /**
     * Character used to display the environment
     */
    private static final char displayChar = 'n';

    /**
     * Constructor.
     * 
	 * @param displayChar character to display for this type of terrain
     */
    public Graveyard() {
        super(displayChar, 27, 27);
    }

    /**
     * Spawns a Heavy Skeletal Swordsman, if the Graveyard is in the west half of the map
     * 
     * @param location the location to spawn the enemy at
     */
    public void spawn_west(Location location) {
        HeavySkeletalSwordsman hss = new HeavySkeletalSwordsman();
        location.addActor(hss);
    }

    /**
     * Spawns a Skeletal Bandit, if the Graveyard is in the east half of the map
     * 
     * @param location the location to spawn the enemy at
     */
    public void spawn_east(Location location) {
        SkeletalBandit skb = new SkeletalBandit();
        location.addActor(skb);
    }
}