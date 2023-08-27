package game.items;

import edu.monash.fit2099.engine.items.Item;
import edu.monash.fit2099.engine.positions.GameMap;
import game.ResetManager;
import game.Resettable;
import game.actions.DrinkFlaskAction;

/**
 * Class to represent Flask of Crimson Tears item.
 * 
 * Created by:
 * @author Jun Lim
 */
public class FlaskOfCrimsonTears extends Item implements Resettable{
    /**
     * The amount of hit points recovered when the flask is used
     */
    private int healAmount;

    /**
     * Maximum and starting number of uses for the flask
     */
    private int maxUses;

    /**
     * Current number of uses left for the flask
     */
    private int uses;

    /**
     * Constructor.
     */
    public FlaskOfCrimsonTears() {
        super("Flask of Crimson Tears", '\0', false);
        this.healAmount = 250;
        this.maxUses = 2;
        this.uses = this.maxUses;
        addAction(new DrinkFlaskAction(this));
        ResetManager.getInstance().registerResettable(this); // allowing the flask to be reset
    }

    /**
     * Returns the amount of hit points healed by the flask.
     * 
     * @return the amount of hit points healed by the flask
     */
    public int getHealAmount() {
        return healAmount;
    }

    /**
     * Sets the amount of hit points healed by the flask.
     * 
     * @param healAmount the new amount of hit points healed by the flask
     */
    public void setHealAmount(int healAmount) {
        this.healAmount = healAmount;
    }

    /**
     * Returns the maximum number of uses of the flask.
     * 
     * @return the maximum number of uses of the flask
     */
    public int getMaxUses() {
        return maxUses;
    }

    /**
     * Sets the maximum number of uses of the flask.
     * 
     * @param usesMax the new maximum number of uses of the flask
     */
    public void setMaxUses(int usesMax) {
        this.maxUses = usesMax;
    }

    /**
     * Returns the remaining number of uses of the flask.
     * 
     * @return the remaining number of uses of the flask
     */
    public int getUses() {
        return uses;
    }

    /**
     * Sets the remaining number of uses of the flask.
     * 
     * @param uses the new remaining number of uses of the flask
     */
    public void setUses(int uses) {
        this.uses = uses;
    }

    /**
     * Changes the number of uses of the flask by an amount.
     * 
     * @param amount the amount to change the number of uses by
     */
    public void changeUses(int amount) {
        this.setUses(this.getUses() + amount);
    }

    /**
     * Resets the remaining number of uses of the flask.
     */
    public void resetUses() {
        this.setUses(this.getMaxUses());
    }

    /**
     * Resets the number of remaining uses of the flask when reset.
     * 
     * @param map the map the flask is in
     */
    @Override
    public void reset(GameMap map) {
        this.resetUses();
    }

    /**
     * Causes the flask to remain resettable after being reset.
     */
    @Override
    public boolean isPersistent() {return true;}
}
