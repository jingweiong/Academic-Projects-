package game.actors.enemies.SkeletalEnemy;

import game.Status;
import game.actors.enemies.Enemy;

/**
 * Abstract class which all skeletal enemy types extend.
 * 
 *@author Jun Lim
 */
public abstract class SkeletalEnemy extends Enemy {
    /**
     * Constructor.
     * @param name        name of the enemy
     * @param displaychar character used to display enemy in the terminal
     * @param hitPoints   starting hit points of the enemy
     * @param runes_min   the minimum number of runes the enemy drops when killed by a player
     * @param runes_max   the maximum number of runes the enemy drops when killed by a player
     */
    public SkeletalEnemy(String name, char displayChar, int hitPoints, int runes_min, int runes_max) {
        super(name, displayChar, hitPoints, Status.ENEMY_SKELETAL, runes_min, runes_max);
    }
}
