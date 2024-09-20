package actions;

import game.DragonToken;
import game.GameManager;

/**
 * Do nothing action class which extends Action abstract class
 * @author mei hui
 */
public class DoNothingAction extends Action{
    public DoNothingAction() {
        super(null, null,0);
    }

    /**
     * Method to execute the move action
     */
    @Override
    public void execute() {
        GameManager.getInstance().switchTurns();
    }
}


