package game;

import Animal.Animal;
import game.DragonToken;

/**
 * Player class to handle player's behaviour and methods
 * @author jingweiong
 */
public class Player {
    private final String name;
    private final DragonToken token;
    private final boolean win;

    /**
     * Constructor
     * @param name Player's name
     * @param animal animal
     */
    public Player(String name, Animal animal){
        this.name = name;
        this.win = false;
        this.token = new DragonToken(animal);
    }

    /**
     * Getter for player's token
     * @return token
     */
    public DragonToken getToken() {
        return token;
    }

    /**
     * Getter for player's name
     * @return name
     */
    public String getName() {
        return name;
    }

    public String getAnimal(){
        return token.getAnimal().getName();
    }
}
