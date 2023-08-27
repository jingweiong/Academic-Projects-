package game.actors.players;

import game.items.weapons.Uchigatana;

/**
 * Class to represent the Samurai player class.
 * Starts with the Uchigatana weapon.
 * Created by:
 * @author Jun Lim
 */
public class Samurai extends Player{
	/**
	 * Constructor.
	 */
	public Samurai() {
		super("Samurai", '@', 455);
		this.addWeaponToInventory(new Uchigatana()); // adding starting weapon
	}
}