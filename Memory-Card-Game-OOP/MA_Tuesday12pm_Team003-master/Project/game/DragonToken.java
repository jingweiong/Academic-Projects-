package game;

import Animal.Animal;

import java.awt.*;

/**
 * Dragon token class which represent token object
 * @author jingweiong
 */
public class DragonToken {
    private final Animal animal;
    private int x;
    private int y;
    private VolcanoCard currentVolcanoCard;
    private Cave cave;
    private int counter;

    /**
     * Constructor
     * @param animal animal
     */
    public DragonToken(Animal animal){
        this.animal = animal;
    }

    /**
     * Move the token to new volcano card
     * @param volcanoCard volcano card
     */
    public void move(VolcanoCard volcanoCard, int moveCount){

        if(this.currentVolcanoCard != null){
            this.currentVolcanoCard.setToken();
        }

        this.currentVolcanoCard = volcanoCard;
        x = volcanoCard.getCenterX();
        y = volcanoCard.getCenterY();

        // set the volcano as true
        // indicating that volcano card is occupied
        this.currentVolcanoCard.setToken();
        counter = counter - moveCount;
    }

    /**
     * Method to reset the dragon token to its cave
     */
    public void returnCave() {
        x = cave.getPosition().x;
        y = cave.getPosition().y;
    }

    /**
     * Method to get the exact moves for the token to its cave
     * @return exact moves to cave
     */
    public int getExactMovesToCave() {
        //get current volcano card index and the volcano card which attach to my cave
        if (currentVolcanoCard == null) {
            counter = 26;
        }
        return counter;
    }

    /**
     * Drawing token on the board
     * @param g
     */
    public void draw(Graphics g) {
        // Determine the color of the token based on the animal
        Color color;
        switch (cave.getAnimal().getName()) {
            case "Bat": // Bat
                color = Color.LIGHT_GRAY;
                break;
            case "Spi": // Spider
                color = Color.RED;
                break;
            case "Sal": // Salamander
                color = Color.GREEN;
                break;
            case "D": // Baby Dragon
                color = Color.BLUE;
                break;
            default:
                color = Color.BLACK; // Default color
        }
        g.setColor(color);
        int RADIUS = 12;
        g.fillOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2); // Draw the token as a circle


        // Draw the animal inside the token
        g.setColor(Color.WHITE);
        g.drawString(animal.getName(), x - 10, y);
    }

    /**
     * Getter for animal
     * @return animal
     */
    public Animal getAnimal() {
        return animal;
    }

    /**
     * Getter for current volcano card the token located at
     * @return volcano card
     */
    public VolcanoCard getCurrentVolcanoCard() {
        return currentVolcanoCard;
    }

    public void setCurrentVolcanoCard(VolcanoCard volcanoCard) {
        this.currentVolcanoCard = volcanoCard;
    }

    /**
     * Setter for x coordinate
     * @param x x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Setter for y coordinate
     * @param y y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Getter for token's cave
     * @return cave
     */
    public Cave getCave() {
        return cave;
    }

    /**
     * Setter for token's cave
     * @param cave cave
     */
    public void setCave(Cave cave) {
        this.cave = cave;
    }
}
