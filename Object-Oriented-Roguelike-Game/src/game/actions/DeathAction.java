package game.actions;

import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.WeaponItem;
import game.FancyMessage;
import game.ResetManager;
import game.RuneManager;
import game.Status;
import game.actors.enemies.Enemy;

/**
 * An action executed if an actor is killed.
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 * @author Jun Lim
 * @author jingweiong
 */
public class DeathAction extends Action {
    /**
     * The actor attacking and killing the target.
     */
    private Actor attacker;

    /** 
     * Constructor.
     * 
     * @param actor the actor attacking and killing the target
     */
    public DeathAction(Actor actor) {
        this.attacker = actor;
    }

    /**
     * When the target is killed, the items & weapons carried by target
     * will be dropped to the location in the game map where the target was
     * 
     * If the actor has special on-death actions, execute those instead
     * If the actor was an enemy killed by a player, award them the runes
     *
     * @param target The actor performing the action.
     * @param map The map the actor is on.
     * @return result of the action to be displayed on the UI
     */
    @Override
    public String execute(Actor target, GameMap map) {
        String result = "";
        RuneManager runeManager = RuneManager.getInstance();
        ResetManager resetManager = ResetManager.getInstance();

        if (target.hasCapability(Status.CAN_DROP_RUNES)) { // if the actor who died drops runes on death (only the player currently)
            resetManager.run(map);                    // reset the game
            result += (System.lineSeparator() + target + " died and lost " +  runeManager.getRunes() + " runes." + "\n" + FancyMessage.YOU_DIED); // show death message with rune count
            runeManager.dropRunes(map);               // drop the runes
            return result;
        }

        if ((target.hasCapability(Status.ENEMY_SKELETAL)) && (target.hasCapability(Status.ENEMY_REVIVER) == false)) { // if the actor is a skeletal enemy and not already a pile of bones
            PileOfBonesAction pileOfBones = new PileOfBonesAction(); // create a new PileOfBones action
            pileOfBones.execute(target, map);                        // execute it
            result += System.lineSeparator() + pileOfBones.menuDescription(target); // return the appropriate message
            return result;
        }

        ActionList dropActions = new ActionList();
        // drop all items
        for (Item item : target.getItemInventory())
            dropActions.add(item.getDropAction(target));
        for (WeaponItem weapon : target.getWeaponInventory())
            dropActions.add(weapon.getDropAction(target));
        for (Action drop : dropActions)
            drop.execute(target, map);
        
        if ((target.hasCapability(Status.HOSTILE_TO_PLAYER)) && (this.attacker.hasCapability(Status.CAN_GAIN_RUNES))) { // if the actor is an enemy and it was killed by an actor which can gain runes(only the player currently)
            Enemy enemy = (Enemy) target;                       // downcasting, not ideal
            int reward = runeManager.getEnemyRuneReward(enemy); // get the amount of runes rewarded
            runeManager.changeRunes(reward);                    // reward the player
            result = (System.lineSeparator() + menuDescription(target) + System.lineSeparator() + (target + " awards the player " + reward + " runes."));
        } else { // if the target does not runes and/or was not killed by the player
            result = System.lineSeparator() + menuDescription(target);
        }

        map.removeActor(target); // remove the actor from the map
        return result;
    }

    @Override
    public String menuDescription(Actor actor) {
        return actor + " is killed.";
    }
}
