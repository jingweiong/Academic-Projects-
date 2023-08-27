package game.environments;

import edu.monash.fit2099.engine.positions.Location;
import game.actors.enemies.CrustaceanEnemy.GiantCrab;
import game.actors.enemies.CrustaceanEnemy.GiantCrayfish;

/**
 * Class representing Puddle of Water environment
 * 
 * @author Jun Lim
 * 
 */
public class Water extends SpawningGround{
    /**
     * Character used to display the environment
     */
    private static final char displayChar = '~';

    /**
     * Constructor.
     * 
	 * @param displayChar character to display for this type of terrain
     */
    public Water() {
        super(displayChar, 2, 1);
    }

    /**
     * Spawns a Giant Crab, if the Puddle of Water is in the west half of the map
     * 
     * @param location the location to spawn the enemy at
     */
    public void spawn_west(Location location) {
        GiantCrab crab = new GiantCrab();
        location.addActor(crab);
    }

    /**
     * Spawns a Giant Crayfish, if the Puddle of Water is in the east half of the map
     * 
     * @param location the location to spawn the enemy at
     */
    @Override
    public void spawn_east(Location location) {
        GiantCrayfish crayfish = new GiantCrayfish();
        location.addActor(crayfish);
    }
}
