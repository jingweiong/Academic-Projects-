package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import game.ResetManager;

/**
 * Class to represent the resting at a Site of Lost Grace action.
 * 
 * Created by:
 * @author Jun Lim
 */
public class GraceRestAction extends Action {
    /**
     * The Location of the Site of Lost Grace being rested at.
     */
    private Location graceLocation;

    /**
     * The name of the Site of Lost Grace being rested at.
     */
    private String graceName;

    /**
     * Constructor.
     * 
     * @param graceLocation the Location of the grace the player is resting at
     * @param graceName     the name of the grace the player is resting at
     */
    public GraceRestAction(Location graceLocation, String graceName) {
        this.graceLocation = graceLocation;
        this.graceName = graceName;
    }
    
    /**
     * Stores the location of the Site of Lost Grace, and resets the game.
     * 
     * @param actor the Actor resting at the Site of Lost Grace
     * @param map   the map the Actor is on
     * @return      the result of the action to be displayed on the UI
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        ResetManager resetManager =  ResetManager.getInstance();
        resetManager.setLastGraceLocation(graceLocation);// update the player's last visited site of lost grace
        resetManager.run(map); // reset the game
        return menuDescription(actor);
    }

    /**
	 * Describes the resting action.
	 *
	 * @param actor the actor resting
	 * @return      a description used for the menu UI
	 */
    @Override
    public String menuDescription(Actor actor) {
        return (actor + " rests at " + this.getGraceName());
    }
    
    /**
     * Returns the name of the Site of Lost Grace being rested at.
     * 
     * @return the name of the Site of Lost Grace being rested at
     */
    public String getGraceName() {
        return graceName;
    }
}
