package game;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.actors.DroppedRunes;
import game.actors.enemies.Enemy;

/**
 * Class to represent the rune manager.
 * 
 * Created by:
 * @author jingweiong
 * Modified by:
 * @author Jun Lim
 */
public class RuneManager {
    /**
     * The singular instance of the RuneManager.
     */
    private static RuneManager instance;

    /**
     * The number of runes the player currently has.
     */
    private int runes = 0;

    /**
     * The instance of DroppedRunes the player last dropped on death.
     */
    private DroppedRunes droppedRunes;

    /**
     * The last location of the player before they took their turn, to drop runes at if they die.
     */
    private Location lastPlayerLocation;

    /**
     * Private singleton constructor.
     */
    private RuneManager(){}

    /**
     * Singleton constructor to limit RuneManager to a single instance
     * 
     * @return the existing RuneManager instance, or a new one of it doesn't exist
     */
    public static RuneManager getInstance() {
        if (instance == null) {
            instance = new RuneManager();
        }
        return instance;
    }

    /**
     * Returns the number of runes the player currently has.
     * 
     * @return the number of runes the player currently has
     */
    public int getRunes() {
        return runes;
    }

    /**
     * Sets the number of runes the player currently has.
     * 
     * @param amount the new amount
     */
    public void setRunes(int amount) {
        this.runes = amount;
    }

    /**
     * Changes the number of runes the player currently has.
     * 
     * @param amount the amount to change by
     */
    public void changeRunes(int amount) {
        this.runes += amount;
    }

    /**
     * Gets the player's last Location before their action.
     * 
     * @return their last Location
     */
    public Location getLastPlayerLocation() {
        return lastPlayerLocation;
    }

    /**
     * Sets the player's last Location before their action.
     * 
     * @param lastLocation their last Location
     */
    public void setLastPlayerLocation(Location lastLocation) {
        this.lastPlayerLocation = lastLocation;
    }

    /**
     * Gets the random rune reward from an enemy between the minimum and maximum amount.
     * 
     * @return the random rune reward
     */
    public int getEnemyRuneReward(Enemy enemy) {
        int reward = RandomNumberGenerator.getRandomInt(enemy.getRunesMin(), enemy.getRunesMax());
        return reward;
    }

    /**
     * Returns the instance of DroppedRunes the player last dropped on death.
     * @return the instance of DroppedRunes
     */
    public DroppedRunes getDroppedRunes() {
        return this.droppedRunes;
    }

    /**
     * Sets the instance of DroppedRunes the player last dropped on death.
     * @param droppedRunes the instance of DroppedRunes
     */
    public void setDroppedRunes(DroppedRunes droppedRunes) {
        this.droppedRunes = droppedRunes;
    }

    /**
     * Clears any dropped runes on the map, and clears the tracked instance variable.
     * 
     * @param map the map the dropped runes are in
     */
    public void clearDroppedRunes(GameMap map) {
        if (getDroppedRunes() != null) {                // if there are dropped runes
            map.removeActor(this.getDroppedRunes());    // remove them from the map
            this.setDroppedRunes(null);    // update the instance
        }
    }

    /**
     * Drops the player's current amount of runes, at their last location before their death.
     * Removes any previous instances of dropped runes, if any.
     * 
     * @param map the map the player is in
     */
    public void dropRunes(GameMap map) {
        // System.out.println(map.getActorAt(getLastPlayerLocation()));
        // System.out.println(this.getLastPlayerLocation().x() + " " + this.getLastPlayerLocation().y() + "xxx");
        this.clearDroppedRunes(map);                                   // remove any previous dropped runes
        DroppedRunes droppedRunes = new DroppedRunes(this.getRunes()); // create the new instance of dropped runes
        map.addActor(droppedRunes, this.getLastPlayerLocation());      // drop runes at the player's previous location
        this.setDroppedRunes(droppedRunes);                            // update the last dropped runes to the newly dropped runes
        this.setRunes(0);                                       // reset the rune count
    }

    /**
     * Recovers the player's dropped runes.
     * 
     * @param map the map the player is in
     */
    public void recoverRunes(GameMap map) {
        this.changeRunes(this.getDroppedRunes().getAmount()); // update the rune count
        this.clearDroppedRunes(map);                          // remove the dropped runes from ther map
    }
}
