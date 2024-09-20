package actions;

import game.DragonToken;
import game.GameManager;
import game.VolcanoCard;

/**
 * Token's move backward action class which extends the Action abstract class
 * @author mei hui
 */
public class MoveBackwardAction extends Action {
    public MoveBackwardAction(DragonToken token, VolcanoCard volcanoCard, int steps) {
        super(token, volcanoCard,steps);
    }

    @Override
    public void execute() {
        token.move(volcanoCard, -steps);
        GameManager.getInstance().switchTurns();
    }
}