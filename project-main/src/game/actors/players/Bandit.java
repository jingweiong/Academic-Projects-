package game.actors.players;

import game.items.weapons.GreatKnife;

/**
 * Class to represent the Bandit player class.
 * Starts with the Great Knife weapon.
 * Created by:
 * @author Jun Lim
 */
public class Bandit extends Player{
	/**
	 * Constructor.
	 */
	public Bandit() {
		super("Bandit", '@', 414);
		this.addWeaponToInventory(new GreatKnife()); // adding starting weapon
	}
}