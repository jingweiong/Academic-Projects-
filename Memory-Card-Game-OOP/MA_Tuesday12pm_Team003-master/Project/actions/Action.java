package actions;
import game.DragonToken;
import game.VolcanoCard;

public abstract class Action {
    protected DragonToken token;
    protected int steps;
    protected VolcanoCard volcanoCard;


    public Action(DragonToken token, VolcanoCard volcanoCard,int steps ) {
        this.token = token;
        this.steps = steps;
        this.volcanoCard = volcanoCard;
    }

    public abstract void execute();

}
