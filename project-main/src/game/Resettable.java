package game;

import edu.monash.fit2099.engine.positions.GameMap;

/**
 * A resettable interface
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 * Jun Lim
 */
public interface Resettable {
    /**
     * Resets the resettable.
     *
     * @param map the map the resettable is in
     */
    void reset(GameMap map);
    
    /**
     * Whether or not the reettable should persist and remain in the list of resettable objects after being reset.
     * 
     * @return whether or not the reettable should persist
     */
    boolean isPersistent();
}
