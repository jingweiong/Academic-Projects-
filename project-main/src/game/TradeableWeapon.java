package game;

import edu.monash.fit2099.engine.weapons.WeaponItem;

/**
 * Class to represent tradeable weapons.
 * 
 * Created by:
 * @author jingweiong
 * Modified by:
 * @author Jun Lim
 */
public class TradeableWeapon {
    /**
     * The WeaponItem instance which can be traded.
     */
    private WeaponItem weapon;

    /**
     * The price the item can be bought for form traders.
     */
    private final int purchasePrice;

    /**
     * The price the item can be sold for to traders.
     */
    private final int sellingPrice;
    
    /**
     * Constructor.
     * 
     * @param weapon        the WeaponItem instance which can be traded
     * @param purchasePrice the price the item can be bought for form traders
     * @param sellingPrice  the price the item can be sold for to traders.
     */
    public TradeableWeapon(WeaponItem weapon, int purchasePrice, int sellingPrice){
        this.weapon = weapon;
        this.purchasePrice = purchasePrice;
        this.sellingPrice = sellingPrice;
    }

    /**
     * Returns the instance of the weapon which can be traded.
     * 
     * @return the weapon
     */
    public WeaponItem getWeapon() {
        return weapon;
    }

    /**
     * Sets the instance of the weapon which can be traded.
     * 
     * @param weapon the weapon
     */
    public void setWeapon(WeaponItem weapon) {
        this.weapon = weapon;
    }

    /**
     * Returns the price the item can be bought for form traders.
     * 
     * @return the price the item can be bought for form traders
     */
    public int getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * Returns the price the price the item can be sold for to traders.
     * 
     * @return the price the item can be sold for to traders
     */
    public int getSellingPrice() {
        return sellingPrice;
    }
}
