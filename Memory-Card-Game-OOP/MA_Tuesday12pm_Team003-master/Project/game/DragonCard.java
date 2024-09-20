package game;

import Animal.Animal;

import java.awt.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class representing the Dragon Card.
 * @author mei hui
 */

public class DragonCard extends JButton implements ActionListener {
    /**
     * The animal on the dragon card
     */
    private final Animal animal;

    /**
     * The number of animals on the dragon card
     */
    private final int numberOfAnimals;

    /**
     * The flag indicating whether the card has been clicked
     */
    boolean clicked;

    private final CardClickListener listener;

    /**
     * Constructor for the Player.
     *
     * @param animal Animal that is on the dragon card in the UI
     * @param numberOfAnimals   Number of animals on the dragon card
     */
    public DragonCard(Animal animal, int numberOfAnimals, CardClickListener listener) {
        this.animal = animal;
        this.numberOfAnimals = numberOfAnimals;
        this.clicked = false;
        this.listener = listener;               // Set the listener
        this.addActionListener(this);
        setText("");
    }

    /**
     * A method that performs an action when the card is clicked.
     *
     * @param e Action event representing the button click
     */
    public void actionPerformed(ActionEvent e) {
        if (!clicked) {
            revealAnimal();
            // this print statement is to see what type of animal is revealed
            System.out.println("Animal reveal: " +animal.getName() +" (" + numberOfAnimals + ")");
//            listener.onCardClicked(this.animal, this.numberOfAnimals); // notify board that a card has been clicked
            listener.onCardClicked(this.animal, this.numberOfAnimals, this);

        }
    }

    /**
     * Method to reveal the flipped dragon card
     */
    public void revealAnimal() {
        setText(animal.getName() + " (" + numberOfAnimals + ")");
        clicked = true;

    }

    /**
     * Method to reset the dragon card
     */
    public void faceDown(){
        clicked = false;
        setText("");
    }
}
