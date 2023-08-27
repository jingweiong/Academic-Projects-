package game.actors.enemies.CrustaceanEnemy;

import game.Status;
import game.actors.enemies.Enemy;

/**
 * Abstract class which all crustacean enemy types extend.
 * 
 *@author Jun Lim
 */
public abstract class CrustaceanEnemy extends Enemy {
    /**
     * Constructor.
     * @param name         name of the enemy
     * @param displaychar character used to display enemy in the terminal
     * @param hitPoints   starting hit points of the enemy
     * @param runes_min   the minimum number of runes the enemy drops when killed by a player
     * @param runes_max   the maximum number of runes the enemy drops when killed by a player
     */
    public CrustaceanEnemy(String name, char displayChar, int hitPoints, int runes_min, int runes_max) {
        super(name, displayChar, hitPoints, Status.ENEMY_CRUSTACEAN, runes_min, runes_max);
    }
}
