package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.positions.GameMap;
import game.RuneManager;
/**
 * An action executed by the player to recover their runes dropped on death.
 * 
 * Created by:
 * @author Jun Lim
 */
public class RecoverRunesAction extends Action{
    /**
     * Constructor.
     *
     * @param droppedAmount the amount of dropped runes being recovered
     */
    public RecoverRunesAction() {}

    /**
     * Recovers the player's dropped runes
     * 
     * @param actor the actor recovering the runes
     * @param map   the map the actor is in
     */
    @Override
    public String execute(Actor actor, GameMap map) {
        String result = menuDescription(actor);
        RuneManager runeManager = RuneManager.getInstance();
        runeManager.recoverRunes(map);  // recover the runes
        return (result);
    }

    /**
	 * Describes the rune recovery action.
	 *
	 * @param actor the actor recovering the runes
	 * @return      a description used for the menu UI
	 */
    @Override
    public String menuDescription(Actor actor) {
        return (actor + " recovers " + RuneManager.getInstance().getDroppedRunes().getAmount() + " runes.");
    }
}
