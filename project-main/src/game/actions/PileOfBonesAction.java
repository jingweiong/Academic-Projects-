package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.positions.Location;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.actors.enemies.Enemy;
import game.actors.enemies.SkeletalEnemy.PileOfBones;

/** 
 * Pile of Bones special Action used by skeletal enemies.
 * Created by:
 * @author Jun Lim
*/
public class PileOfBonesAction extends Action{
    /**
     * Constructor.
     */
    public PileOfBonesAction() {}

    /**
     * Turns an enemy into a Pile of Bones and which revives it with full health in 3 turns if it is not hit.
     * 
     * @param target the enemy becoming a Pile of Bones
     * @param map    the map the enemy is in
     * @return       the result of the action to be displayed in the console
     */
    @Override
    public String execute(Actor target, GameMap map) {
        Location here = map.locationOf(target);
        map.removeActor(target);                                                // remove the enemy from the map
        PileOfBones pileOfBones = new PileOfBones((Enemy) target, 2); // create the pile of bones
        // add all the enemy's items to the pile of bones, to be dropped on death
        for (Item item : target.getItemInventory()) {
            pileOfBones.addItemToInventory(item);
        }
        for (WeaponItem weapon : target.getWeaponInventory()) {
            pileOfBones.addWeaponToInventory(weapon);
        }
        map.addActor(pileOfBones, here); // add the pile of bones to the map
        return menuDescription(pileOfBones);
    }

    /**
     * Describes the enemy turning into a pile of bones.
     * 
     * @param actor the enemy using the Action
     * @return      a description used for the menu UI
     */
    @Override
    public String menuDescription(Actor actor) {
        return (actor + " turns into a pile of bones.");
    }
}