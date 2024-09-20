package actions;

import game.DragonToken;
import game.VolcanoCard;

public class MoveForwardAction extends Action {
    public MoveForwardAction(DragonToken token, VolcanoCard volcanoCard, int steps) {
        super(token, volcanoCard, steps);
    }

    @Override
    public void execute() {
        token.move(volcanoCard, steps);
    }
}
