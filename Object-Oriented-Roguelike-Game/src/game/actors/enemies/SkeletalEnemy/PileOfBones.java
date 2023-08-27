package game.actors.enemies.SkeletalEnemy;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import game.Status;
import game.actions.PileOfBonesReviveAction;
import game.actors.enemies.Enemy;

/**
 * Pile of Bones actor for Pile of Bones special skill.
 * 
 * Created by:
 * @author Jun Lim
 */
public class PileOfBones extends SkeletalEnemy {
    /**
     * The enemy instance the Pile of Bones was crated from
     */
    private Enemy target;

    /**
     * Number of turns until the Pile of Bones revives the enemy.
     */
    private int turnsLeft;

    /**
     * Constuctor.
     * 
     * @param target the user actor of the Pile of Bones action
     * @param turnsLeft the number of turns until the actor revives
     */
    public PileOfBones(Enemy target, int turnsLeft) {
        super("Pile of Bones", 'X', 1, 0, 0 );
        this.addCapability(Status.ENEMY_REVIVER); // preventing the pile of bones from turning into another pile of bones on death
        this.target = target;
        this.turnsLeft = turnsLeft;
        this.setRunesMin(this.target.getRunesMin()); 
        this.setRunesMax(this.target.getRunesMax());
    }
    
    /** 
     * If the Pile Of Bones survives for the specified number of turns, revives the enemy.
     * Otherwise decrements the number of turns left and does nothing.
     * 
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return           the valid action that can be performed in that iteration or null if no valid action is found
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        if (this.turnsLeft == 0) {                           // if the counter has reached 0
            return new PileOfBonesReviveAction(this.target); // revive the enemy
        } else {
            this.turnsLeft -= 1;          // decrement counter by 1
            return new DoNothingAction(); // do nothing for the turn
        }
    }
}
