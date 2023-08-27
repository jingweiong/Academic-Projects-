package game.actors.enemies;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.RandomNumberGenerator;
import game.ResetManager;
import game.Resettable;
import game.Status;
import game.actions.AreaAttackAction;
import game.actions.AttackAction;
import game.actions.DespawnAction;
import game.behaviours.Behaviour;
// import game.behaviours.FollowBehaviour;
import game.behaviours.FollowBehaviourAlt;
import game.behaviours.WanderBehaviour;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class which all enemy classes extend.
 * 
 * Created by:
 * @author Jun Lim
 * Modified by:
 * @author jingweiong
 */
public abstract class Enemy extends Actor implements Resettable{
    /**
     * Map of enemy behaviours
     */
    protected Map<Integer, Behaviour> behaviours = new HashMap<>();
    /**
     * Enemy type enum, to prevent attacking others of the same enemy type
     */
    protected Status type;
    /**
     * the minimum number of runes the enemy drops when killed by a player
     */
    protected int runesMin;
    /**
     * the maximum number of runes the enemy drops when killed by a player
     */
    protected int runesMax;

    /**
     * Constructor.
     * 
     * @param name        name of the enemy
     * @param displayChar character used to display enemy in the terminal
     * @param hitPoints   starting hit points of the enemy
     * @param type        the enemy type (canine, skeletal, etc.)
     * @param runesMin    the minimum number of runes the enemy drops when killed by a player
     * @param runesMax    the maximum number of runes the enemy drops when killed by a player
     */
    public Enemy(String name, char displayChar, int hitPoints, Status type, int runesMin, int runesMax) {
        super(name, displayChar, hitPoints);
        this.type = type;           // setting enemy type to prevent attacking others of the same type
        this.runesMin = runesMin;   // setting value of runes rewarded when killed by a player
        this.runesMax = runesMax;
        this.addCapability(this.type);
        this.addCapability(Status.HOSTILE_TO_PLAYER); // allowing players to attack enemies
        this.addCapability(Status.HOSTILE_TO_ENEMY); // alowing enemies to attack each other
        this.behaviours.put(999, new WanderBehaviour()); // default wandering behaviour
        ResetManager.getInstance().registerResettable(this); // allowing all enemies to be reset
    }
    
    /**
     * Allows the enemy to follow and attack players, and attack other actors.
     * NOTE: players move first
     * 1. if a player is adjacent after moving, attack them
     * 2. if the player is not adjacent after moving, and the enemy was following them, follow them
     * 3. if a player is not adjacent, and the enemy was not following any, 10% chance to despawn
     *
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return           the valid action that can be performed in that iteration or null if no valid action is found
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        for (Action action : actions) { // for each action
            if (action.getClass() == AttackAction.class || action.getClass().getSuperclass() == AttackAction.class) { // if the action is an attackaction or extends it
                // System.out.println("attack action found!!");
                return action;
            }
        }
        if (this.behaviours.containsKey(2) == false) { // if the enemy isn't following the player
            if (RandomNumberGenerator.percentChance(10)) { // with a 10% chance
                ResetManager.getInstance().removeResettable(this); // remove the enemy from the list of resettables
                return new DespawnAction(); // despawn the enemy
            }
        }
        for (Behaviour behaviour : behaviours.values()) { // for each available behaviour in ascending key order
            // System.out.println("trying: " + behaviour);
            Action action = behaviour.getAction(this, map);
            if(action != null)
                // System.out.println("performing: " + action.getClass().getSimpleName());
                return action;
            }
        return new DoNothingAction(); // do nothing
    }

    /**
     * Lets the enemy follow nearby players, and other actors to attack the enemy.
     *
     * @param otherActor the Actor that is nearby
     * @param direction  String representing the direction of the other Actor
     * @param map        current GameMap
     * @return           the list of actions to be added to the other actor's ActionList
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actions = new ActionList();
        if (otherActor.hasCapability(Status.HOSTILE_TO_ENEMY) && !otherActor.hasCapability(Status.HOSTILE_TO_PLAYER)) {                   // if the actor is a player
            this.behaviours.put(2, new FollowBehaviourAlt(otherActor)); // start following them
        }
        if((otherActor.hasCapability(Status.HOSTILE_TO_ENEMY)) && (otherActor.hasCapability(type) == false)){ // if the actor is hostile to all enemies, or hostile to this enemy type
            if (otherActor.hasCapability(Status.ENEMY_GIANT)) {       // if the actor can use the slam attack
                if (RandomNumberGenerator.percentChance(50)) { // with a 50% chance
                    IntrinsicWeapon intrinsicWeapon = otherActor.getIntrinsicWeapon(); // create a copy of the intrinsic weapon but with "slam" for the verb
					IntrinsicWeapon newWeapon = new IntrinsicWeapon(intrinsicWeapon.damage(), "slams", intrinsicWeapon.chanceToHit());
                	actions.add(new AreaAttackAction(this, newWeapon)); // add the slam attack
                }
            }
            if (otherActor.getWeaponInventory().isEmpty() == false) {       // if the other actor has weapons
                if (otherActor.hasCapability(Status.HOSTILE_TO_ENEMY) && !otherActor.hasCapability(Status.HOSTILE_TO_PLAYER)) { // if the other actor is a player
                    for (WeaponItem weapon : otherActor.getWeaponInventory()) { // for each weapon
                        actions.add(weapon.getSkill(this, direction));    // add the active skill action
                        actions.add(new AttackAction(this, direction, weapon)); // add an attack with the weapon
                    }
                } else if (otherActor.hasCapability(Status.HOSTILE_TO_PLAYER)) {      // if the other actor is an enemy
                    WeaponItem weapon = otherActor.getWeaponInventory().get(0); // get the first weapon
                    if (RandomNumberGenerator.percentChance(50)) {             // with a 50% chance
                        // System.out.println("enemy weapon special skill!");
                        actions.add(weapon.getSkill(this, direction));          // add the active skill action
                    }
                }
            }
            actions.add(new AttackAction(this, direction)); // add an attack with the intrinsic weapon
        }
        return actions;
    }
    
    
    /** 
     * Returns the minimum rune reward when the enemy is killed by the player.
     * 
     * @return the minimum rune reward
     */
    public int getRunesMin() {
        return runesMin;
    }

    /** 
     * Returns the maximum rune reward when the enemy is killed by the player.
     * 
     * @return the maximum rune reward
     */
    public int getRunesMax() {
        return runesMax;
    }

    /** 
     * Sets the minimum rune reward when the enemy is killed by the player.
     * 
     * @return the new minimum rune reward
     */
    public void setRunesMin(int runesNin) {
        this.runesMin = runesNin;
    }

    /** 
     * Sets the maximum rune reward when the enemy is killed by the player.
     * 
     * @return the new maximum rune reward
     */  
    public void setRunesMax(int runesMax) {
        this.runesMax = runesMax;
    }

    /**
     * Despawns the enemy when the game is reset.
     * 
     * @param map the map to despawn the enemy from.
     */
    @Override
    public void reset(GameMap map) {
        map.removeActor(this);
    }

    /** 
     * Returns whether or not the enemy should remain in the list of resettable objects after being reset.
     * Always returns false; enemies are despawned after being reset in current implementation.
     * 
     * @return false
     */
    @Override
    public boolean isPersistent() {return false;}
}
