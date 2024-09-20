package game;

import Animal.Animal;
import game.VolcanoCard;

import java.awt.*;

/**
 * Cave class
 * @author jingweiong
 */
public class Cave {
    private final Animal animal;
    private final Point position;
    private VolcanoCard volcanoCard;

    /**
     * Constructor
     * @param animal animal
     * @param position cave's position
     */
    public Cave(Animal animal, Point position){
        this.animal = animal;
        this.position = position;
    }

    /**
     * Drawing cave on the board
     * @param g
     */
    public void draw(Graphics g) {
        // Draw the cave as a circle
        int radius = 20;
        int x = position.x - radius;
        int y = position.y - radius;
        g.setColor(Color.GRAY);
        g.fillOval(x, y, radius * 2, radius * 2);

        // Draw the animal inside the cave
        g.setColor(Color.WHITE);
        g.drawString(animal.getName(), x + 5, y + radius);
    }

    /**
     * Getter for animal
     * @return animal
     */
    public Animal getAnimal() {
        return animal;
    }

    /**
     * Getter for cave's position
     * @return cave's position
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Setter for attached volcano card
     * @param volcanoCard volcano card
     */
    public void setVolcanoCard(VolcanoCard volcanoCard) {
        this.volcanoCard = volcanoCard;
    }

    /**
     * Getter for attached volcano card
     * @return volcano card
     */
    public VolcanoCard getVolcanoCard() {
        return volcanoCard;
    }
}
