package game.actors.traders;
import edu.monash.fit2099.engine.actions.Action;
import edu.monash.fit2099.engine.actions.ActionList;
import edu.monash.fit2099.engine.actions.DoNothingAction;
import edu.monash.fit2099.engine.actors.Actor;
import edu.monash.fit2099.engine.displays.Display;
import edu.monash.fit2099.engine.positions.GameMap;
import edu.monash.fit2099.engine.weapons.WeaponItem;
// import game.TradeableItem;
import game.TradeableWeapon;
import game.Status;
import game.actions.PurchaseWeaponAction;
import game.actions.SellWeaponAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent traders.
 * 
 * Created by:
 * @author jingweiong
 * Modified by:
 * @author Jun Lim
 */
public class Trader extends Actor{
    /**
     * List of weapons to buy and sell.
     */
    protected ArrayList<TradeableWeapon> weaponList;
    // protected ArrayList<TradeableItem> itemList;

    // protected List<Item> playerItemList;
    // protected List<WeaponItem> playerWeaponList;

    /**
     * Constructor.
     * 
     * @param name        name of the trader
     * @param displayChar character used to display trader in the terminal
     */
    public Trader(String name, char displayChar){
        super(name, displayChar, 1);
        this.addCapability(Status.FRIENDLY);
        weaponList = new ArrayList<>();
    }

     /**
     * Always does nothing.
     * 
     * @param actions    collection of possible Actions for this Actor
     * @param lastAction The Action this Actor took last turn. Can do interesting things in conjunction with Action.getNextAction()
     * @param map        the map containing the Actor
     * @param display    the I/O object to which messages may be written
     * @return           the valid action that can be performed in that iteration or null if no valid action is found
     */
    @Override
    public Action playTurn(ActionList actions, Action lastAction, GameMap map, Display display) {
        return new DoNothingAction();
    }

    /**
     * Allows the player to trade with the trader.
     * 
     * @param otherActor the Actor trading
     * @param direction  String representing the direction of the other Actor
     * @param map        current GameMap
     * @return			 the list of actions to be added to the other actor's ActionList
     */
    @Override
    public ActionList allowableActions(Actor otherActor, String direction, GameMap map) {
        ActionList actions = new ActionList();
        if(otherActor.hasCapability(Status.CAN_TRADE)){ // if the actor can be traded with
            for (TradeableWeapon weapon: getSellableWeaponList(otherActor)){ // for each weapon the trader is willing to buy
                actions.add(new SellWeaponAction(weapon));                   // create and add an action to sell it
            }
            for (TradeableWeapon weapon : getPurchasableWeaponList()){ // for each weapon the trader is willing to sell
                actions.add(new PurchaseWeaponAction(weapon));         // create and add an action to buy it
            }
        }
        return actions;
    }

    /**
     * Returns a list of weapons the trader is willing to sell to the player.
     * 
     * @return the list of weapons
     */
    public ArrayList<TradeableWeapon> getPurchasableWeaponList() {
        ArrayList<TradeableWeapon> purchasableWeaponList = new ArrayList<>();
        for (TradeableWeapon weapon : weaponList){                                          // for each weapon the trader can buy or sell
            if (weapon.getPurchasePrice() != 0 && !purchasableWeaponList.contains(weapon)){ // if the trader is willing to sell it, and if is not already added
                purchasableWeaponList.add(weapon);
            }
        }
        return purchasableWeaponList;
    }

    /**
     * Returns a list of weapons the trader is willing to buy from the player.
     * 
     * @return the list of weapons
     */
    public ArrayList<TradeableWeapon> getSellableWeaponList(Actor otherActor) {
        ArrayList<TradeableWeapon> sellableWeaponList = new ArrayList<>();
        List<WeaponItem> playerWeaponList = otherActor.getWeaponInventory();
        for (TradeableWeapon weapon : weaponList) {            // for each weapon the trader can buy or sell
            for (WeaponItem playerWeapon : playerWeaponList) { // for each weapon the player has
                if (weapon.getSellingPrice() != 0 && (weapon.getWeapon().toString().equals(playerWeapon.toString()))) { // if the trader is willing to buy it, and if is not already added
                    sellableWeaponList.add(new TradeableWeapon(playerWeapon, weapon.getPurchasePrice(), weapon.getSellingPrice())); // create and add a TradeableWeapon object using the player's weapon
                }
            }
        }
        return sellableWeaponList;
    }

    /**
     * Add a weapon to buy and/or sell to the trader.
     * 
     * @param tradeableWeapon the weapon to buy and/or sell
     */
    public void addWeapon(TradeableWeapon tradeableWeapon) {
        this.weaponList.add(tradeableWeapon);
    }
    
    // public ArrayList<TradeableItem> getPurchasableItemList() {
    //     purchasableItemList = new ArrayList<>();
    //     for (TradeableItem item : itemList){
    //         if (item.getPurchasePrice() != 0 && !purchasableItemList.contains(item)){
    //             purchasableItemList.add(item);
    //         }
    //     }
    //     return purchasableItemList;
    // }

    // public ArrayList<TradeableItem> getSellableItemList(Actor otherActor) {
    //     sellableItemList = new ArrayList<>();
    //     playerItemList = otherActor.getItemInventory();
    //     for (TradeableItem item : itemList) {
    //         for (Item playerItem : playerItemList) {
    //             if (item.getSellingPrice() != 0 && (item.getItem().toString().equals(playerItem.toString()))) {
    //                 sellableItemList.add(new TradeableItem(playerItem, item.getPurchasePrice(), item.getSellingPrice()));
    //             }
    //         }
    //     }
    //     return sellableItemList;
    // }
    // 
    // public void addItem(TradeableItem tradeableItem) {
    //     this.itemList.add(tradeableItem);
    // }
}
