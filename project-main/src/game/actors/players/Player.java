package game.actors.players;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.IntrinsicWeapon;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.RandomNumberGenerator;
import game.ResetManager;
import game.Resettable;
import game.RuneManager;
import game.Status;
import game.actions.AreaAttackAction;
import game.actions.AttackAction;
import game.items.FlaskOfCrimsonTears;
import edu.monash.fit2099.engine.displays.Menu;

/**
 * Class representing the Player. It implements the Resettable interface.
 * It carries around a club to attack a hostile creature in the Lands Between.
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 * @author Jun Lim
 * @author jingweiong
 */
public abstract class Player extends Actor implements Resettable {

	private final Menu menu = new Menu();

	/**
	 * Constructor.
	 *
	 * @param name        Name to call the player in the UI
	 * @param displayChar Character to represent the player in the UI
	 * @param hitPoints   Player's starting number of hitpoints
	 */
	public Player(String name, char displayChar, int hitPoints) {
		super(name, displayChar, hitPoints);
		this.addCapability(Status.HOSTILE_TO_ENEMY);		 // allowing player to be attacked by enemies
		this.addCapability(Status.FRIENDLY);
		this.addCapability(Status.CAN_DROP_RUNES);
		this.addCapability(Status.CAN_GAIN_RUNES);
		this.addCapability(Status.CAN_TRADE);
		ResetManager.getInstance().registerResettable(this); // allowing the player to be reset
		FlaskOfCrimsonTears newflask = new FlaskOfCrimsonTears(); // creating the starting flask
		this.addItemToInventory(newflask);						  // add the flask to th
	}

	@Override
	public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
		// Handle multi-turn Actions
		if (lastAction.getNextAction() != null)
			return lastAction.getNextAction();
		// Storing player "previous" player position to drop runes at on death
		Location lastLocation = map.locationOf(this);
		RuneManager.getInstance().setLastPlayerLocation(lastLocation);
		// Print player health and rune count
		display.println("Health: " + this.printHp() + " Runes: " + RuneManager.getInstance().getRunes());
		// return/print the console menu
		return menu.showMenu(this, actions, display);
	
	}

	/**
     * Lets the player be attacked by enemies.
     *
     * @param otherActor the Actor that might be performing attack
     * @param direction  String representing the direction of the other Actor
     * @param map        current GameMap
     * @return			 the list of actions to be added to the other actor's ActionList
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actions = new ActionList();
        if (otherActor.hasCapability(Status.HOSTILE_TO_PLAYER)) { 	   // if an enemy is nearby
			if (otherActor.hasCapability(Status.ENEMY_GIANT)) { 	   // if the enemy can use the slam attack
				if (RandomNumberGenerator.percentChance(50)) { // with a 50% chance
					// System.out.println("enemy giant slam skill!");
					IntrinsicWeapon intrinsicWeapon = otherActor.getIntrinsicWeapon(); // create a copy of the intrinsic weapon but with "slam" for the verb
					IntrinsicWeapon newWeapon = new IntrinsicWeapon(intrinsicWeapon.damage(), "slams", intrinsicWeapon.chanceToHit());
                	actions.add(new AreaAttackAction(this, newWeapon)); // add the slam attack
				}
			}
			if (otherActor.getWeaponInventory().isEmpty() == false) { // if the enemy has weapons
				WeaponItem weapon = otherActor.getWeaponInventory().get(0); // get the first weapon
				// System.out.println(weapon);
				if (RandomNumberGenerator.percentChance(50)) { // with a 50% chance
					// System.out.println("enemy weapon special skill!");
					actions.add(weapon.getSkill(this, direction)); // add the active skill action
				}
				actions.add(new AttackAction(this, direction, weapon)); // add an attack with the weapon
			}
			actions.add(new AttackAction(this, direction)); // add an attack with the intrinsic weapon
        }
        return actions;
    }

	/**
	 * Creates and returns the player's intrinsic weapon, fists
	 * (hit rate is not mentioned in spec, defaulting to 100)
	 * 
	 * @return a new instance of the IntrinsicWeapon
	 */
	@Override
    public IntrinsicWeapon getIntrinsicWeapon() {
        return new IntrinsicWeapon(11, "punches", 100);
    }

	/**
     * Resets the player's hp to maximum.
	 * Moves the player to the last visited Site of Lost Grace unless they are already there.
	 * 
	 * @param map the map the player is in
     */
	@Override
	public void reset(GameMap map) {
		this.resetMaxHp(this.getMaxHp()); // reset the player's hp
		Location lastGraceLocation = ResetManager.getInstance().getLastGraceLocation();
		if (lastGraceLocation.containsAnActor() == false) { // if the player is not already at the site of lost grace
			map.moveActor(this, lastGraceLocation); // move them there
		}
	}

	/**
     * Causes the player to remain resettable after being reset.
     */
	@Override
    public boolean isPersistent() {return true;}
}
