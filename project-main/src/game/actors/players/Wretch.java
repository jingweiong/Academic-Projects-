package game.actors.players;

import game.items.weapons.Club;

/**
 * Class to represent the Wretch player class.
 * Starts with the Club weapon.
 * Created by:
 * @author Jun Lim
 */
public class Wretch extends Player{
	/**
	 * Constructor.
	 */
	public Wretch() {
		super("Wretch", '@', 414);
		this.addWeaponToInventory(new Club()); // adding starting weapon
	}
}