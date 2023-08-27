package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.items.FlaskOfCrimsonTears;

/**
 * Class to represent using the Flask of Crimson Tears.
 * 
 * Created by:
 * @author Jun Lim
 */
public class DrinkFlaskAction extends Action {
    /**
     * The flask used by the actor
     */
    private FlaskOfCrimsonTears flask;

    /**
     * Constructor.
     * 
     * @param flask the flask used by the actor
     */
    public DrinkFlaskAction(FlaskOfCrimsonTears flask) {
        this.flask = flask;
    }
    
    /**
     * Lets the actor attempt to drink the flask.
     * If the flask still has uses left, heals the actor and removes a use.
     * Otherwise, does nothing.
     * 
     * @param actor the actor drinking the flask
     * @param map   the map the actor is on
     * @return      the result of the action to be displayed on the UI
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        if (this.flask.getUses() >= 1) { // if the flask has uses left
            actor.heal(this.flask.getHealAmount()); // heal the actor
            this.flask.changeUses(-1);
            return (menuDescription(actor));
        } else {
            return (actor + " tries to drink from the Flask of Crimson Tears, but it is empty.");
        }
    }

    /**
	 * Describes the drinking action.
	 *
	 * @param actor the actor drinking the flask
	 * @return      a description used for the menu UI
	 */
    @Override
    public String menuDescription(Actor actor) {
        return (actor + " drinks from the Flask of Crimson Tears. (" + this.flask.getUses() + "/" + this.flask.getMaxUses() + ")");
    }
    
}
