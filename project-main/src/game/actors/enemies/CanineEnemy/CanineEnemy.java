package game.actors.enemies.CanineEnemy;

import game.Status;
import game.actors.enemies.Enemy;

/**
 * Abstract class which all canine enemy types extend.
 * 
 *@author Jun Lim
 */
public abstract class CanineEnemy extends Enemy {
    /**
     * Constructor.
     * @param name        name of the enemy
     * @param displaychar character used to display enemy in the terminal
     * @param hitPoints   starting hit points of the enemy
     * @param runes_min   the minimum number of runes the enemy drops when killed by a player
     * @param runes_max   the maximum number of runes the enemy drops when killed by a player
     */
    public CanineEnemy(String name, char displayChar, int hitPoints, int runes_min, int runes_max) {
        super(name, displayChar, hitPoints, Status.ENEMY_CANINE, runes_min, runes_max);
    }
}
