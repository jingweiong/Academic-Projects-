package game;

import java.util.ArrayList;
import java.util.List;

import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;

/**
 * A reset manager class that manages a list of resettables.
 * Created by:
 * @author Adrian Kristanto
 * Modified by: Jun Lim
 */
public class ResetManager {
    /**
     * List of resettable objects.
     */
    private List<Resettable> resettables;

    /**
     * Singular of instance of ResetManager.
     */
    private static ResetManager instance;

    /**
     * Location of the last Site of Lost Grace the player rested at
     */
    private Location lastGraceLocation = null;

    /**
     * Private constructor.
     */
    private ResetManager() {
        this.resettables = new ArrayList<>();
    }

    /**
     * Singleton constructor to limit ResetManager to a single instance
     * 
     * @return the existing ResetManager instance, or a new one of it doesn't exist
     */
    public static ResetManager getInstance() {
        if (instance == null) {
            instance = new ResetManager();
        }
        return instance;
    }

    /**
     * Resets all resettable objects.
     * Also removes them from the list of resettable objects, if they are not persistent.
     * 
     * @param map the map the resettable objects are in
     */
    public void run(GameMap map) {
        if (getLastGraceLocation() == null) {              // if the player has never rested at a site of lost grace
            this.setLastGraceLocation(map.at(38, 11)); // set the default grace location to "The First Step"
        }
        ArrayList<Resettable> removeList = new ArrayList<>(); 
        for (Resettable resettable : this.resettables) { // for each resettable object
            // System.out.println(resettable + " is being reset");
            resettable.reset(map);                       // execute the reset function
            if (resettable.isPersistent() == false) {    // if the resettable object does not remain after being reset
                removeList.add(resettable);
            }
        }
        for (Resettable resettable : removeList) { // for each resettable object which no longer exists
            // System.out.println("removing resettable " + resettable);
            this.removeResettable(resettable);     // remove it from the list of resettable objects
        }

    }

    /**
     * Adds a resettable to the list of objects to be reset.
     * 
     * @param resettable the resettable being added
     */
    public void registerResettable(Resettable resettable) {
        this.resettables.add(resettable);
    }

    /**
     * Removes a resettable to the list of objects to be reset.
     * 
     * @param resettable the resettable being removed
     */
    public void removeResettable(Resettable resettable) {
        this.resettables.remove(resettable);
    }

    /**
     * Returns the Location of the last Site of Lost Grace the player rested at.
     * @return the Location
     */
    public Location getLastGraceLocation() {
        return lastGraceLocation;
    }

    /**
     * Sets the Location of the last Site of Lost Grace the player rested at.
     * @return the Location
     */
    public void setLastGraceLocation(Location lastGraceLocation) {
        this.lastGraceLocation = lastGraceLocation;
    }

}
