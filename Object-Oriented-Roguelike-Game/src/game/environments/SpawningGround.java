package game.environments;

import edu.monash.fit2099.engine.positions.Ground;
import edu.monash.fit2099.engine.positions.Location;
import game.RandomNumberGenerator;
/**
 * Abstract class extended by all environments which spawn enemies.
 * Spawns a different enemy based on which half of the map it is in.
 * 
 * @author Jun Lim
 * 
 */
public abstract class SpawningGround extends Ground{
    /**
     * Percentage chance of enemy being spawned each turn if in the west half of the map
     */
    private int chance_west;

    /**
     * Percentage chance of enemy being spawned each turn if in the east half of the map
     */
    private int chance_east;

    /**
     * Constructor.
     * 
     * @param displaychar character to display for this type of terrain
     * @param _chance_west percentage chance of enemy being spawned each turn/tick if in the west half of the map
     * @param _chance_east percentage chance of enemy being spawned each turn/tick if in the east half of the map
     * 
     */
    public SpawningGround(char displaychar, int _chance_west, int _chance_east) {
        super(displaychar);
        this.chance_west = _chance_west;
        this.chance_east = _chance_east;
    }

    /**
     * Randomly spawns enemies each turn depending on which half of the map the environment is in
     * 
     * @param location the location of the environment
     */
    @Override
    public void tick(Location location) {
        if (location.containsAnActor() == false) { // if the location doesn't already have an actor
        // provided map is not even width (75) so splitting to 38 and 37 spaces for west and east respectively.
            if (location.x() <= 38) {// if in the west half of the map
                if (RandomNumberGenerator.percentChance(chance_west)) { // with the chance of chance_west
                    spawn_west(location); // spawn an enemy
                }
            } else {
                if (RandomNumberGenerator.percentChance(chance_east)) { // with the chance of chance_east
                    spawn_east(location);
                }
            }
        }
    }
    /**
     * Abstract method to create and spawn an enemy at a location if the environment is in the west side of the map.
     * 
     * @param location location to spawn the enemy at
     */
    public abstract void spawn_west(Location location);

    /**
     * Abstract method to create and spawn an enemy at a location if the environment is in the east side of the map.
     * 
     * @param location location to spawn the enemy at
     */
    public abstract void spawn_east(Location location);
}
