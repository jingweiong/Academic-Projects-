package game.environments;

import edu.monash.fit2099.engine.positions.Location;
import game.actors.enemies.CanineEnemy.GiantDog;
import game.actors.enemies.CanineEnemy.LoneWolf;

/**
 * Class representing Gust of Wind environment
 * 
 * @author Jun Lim
 * 
 */
public class Wind extends SpawningGround{
    /**
     * Character used to display the environment
     */
    private static final char displayChar = '&';

    /**
     * Constructor.
     * 
	 * @param displayChar character to display for this type of terrain
     */
    public Wind() {
        super(displayChar, 33, 4);
    }

    /**
     * Spawns a Lone Wolf, if the Gust of Wind is in the west half of the map
     * 
     * @param location the location to spawn the enemy at
     */
    public void spawn_west(Location location) {
        LoneWolf wolf = new LoneWolf();
        location.addActor(wolf);
    }

    /**
     * Spawns a Giant Dog, if the Gust of Wind is in the west half of the map
     * 
     * @param location the location to spawn the enemy at
     */
    @Override
    public void spawn_east(Location location) {
        GiantDog giantDog = new GiantDog();
        location.addActor(giantDog);
    }
}
