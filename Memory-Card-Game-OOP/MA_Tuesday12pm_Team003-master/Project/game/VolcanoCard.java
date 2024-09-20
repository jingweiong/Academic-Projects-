package game;

import Animal.Animal;

import java.awt.*;

/**
 * Volcano card class
 * @author jingweiong
 */
public class VolcanoCard {
    private final Animal animal;
    private Cave cave; // cave attach to this volcano card
    private boolean otherToken;
    private final int index;
    private final int startX;
    private final int startY;
    private final int endX;
    private final int endY;
    private final int centerX;
    private final int centerY;

    /**
     * Constructor
     * @param index index
     * @param animal animal
     * @param numberOfVolcanoCards total number of volcano cards
     * @param OUTER_RADIUS outer radius of the board
     * @param INNER_RADIUS inner radius of the board
     * @param WIDTH width of the board
     * @param HEIGHT height of the board
     */
    public VolcanoCard(int index, Animal animal, int numberOfVolcanoCards, int OUTER_RADIUS, int INNER_RADIUS, int WIDTH, int HEIGHT){
        this.index = index;
        this.animal = animal;
        double angle = Math.toRadians(360.0 / numberOfVolcanoCards * index);
        startX = (int) (Math.cos(angle) * 150) + 300;
        startY = (int) (Math.sin(angle) * 150) + 300;
        endX = (int) (Math.cos(angle) * 250) + 300;
        endY = (int) (Math.sin(angle) * 250) + 300;
        double midAngle = Math.toRadians(360.0 / 24 * (index + 0.5)); // Angle for the midpoint
        centerX = (int) (Math.cos(midAngle) * (OUTER_RADIUS + INNER_RADIUS) / 2) + WIDTH / 2;
        centerY = (int) (Math.sin(midAngle) * (OUTER_RADIUS + INNER_RADIUS) / 2) + HEIGHT / 2;
        otherToken = false;

        if (index == 0 || index == 7 || index == 13 || index == 18) {
            // Specify the position of the cave relative to the volcano card
            Point cavePosition = calculateCavePosition(index, OUTER_RADIUS, WIDTH, HEIGHT);
            this.cave = new Cave(animal, cavePosition);
            cave.setVolcanoCard(this);
        }
    }

    /**
     * Method to calculate the position of the cave attached to the volcano card
     * @param index index
     * @param OUTER_RADIUS outer radius of the board
     * @param WIDTH width of the board
     * @param HEIGHT height of the board
     * @return position of the cave
     */
    private Point calculateCavePosition(int index, int OUTER_RADIUS, int WIDTH, int HEIGHT) {
        double angle = Math.toRadians(360.0 / 24 * index); // Angle for the volcano card

        // Calculate the coordinates of the cave based on the angle and the outer radius
        int x = (int) (Math.cos(angle) * OUTER_RADIUS) + WIDTH / 2;
        int y = (int) (Math.sin(angle) * OUTER_RADIUS) + HEIGHT / 2;

        return new Point(x, y);
    }

    public void setToken() {
        otherToken = !otherToken;
    }

    public boolean otherToken() {
        return otherToken;
    }

    /**
     * Getter for volcano card index
     * @return integer index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Getter for attached cave
     * @return cave
     */
    public Cave getCave() {
        return cave;
    }

    /**
     * Getter for animal
     * @return animal
     */
    public Animal getAnimal() {
        return animal;
    }

    /**
     * Getter for Start of x coordinate
     * @return start of x coordinate
     */
    public int getStartX() {
        return startX;
    }

    /**
     * Getter for Start of y coordinate
     * @return start of y coordinate
     */
    public int getStartY() {
        return startY;
    }

    /**
     * Getter for end of x coordinate
     * @return end of x coordinate
     */
    public int getEndX() {
        return endX;
    }

    /**
     * Getter for end of y coordinate
     * @return end of y coordinate
     */
    public int getEndY() {
        return endY;
    }

    /**
     * Getter for center of x coordinate
     * @return center of x coordinate
     */
    public int getCenterX() {
        return centerX;
    }

    /**
     * Getter for center of y coordinate
     * @return center of y coordinate
     */
    public int getCenterY() {
        return centerY;
    }

}
