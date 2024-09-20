package game;

import Animal.Animal;

/**
 * Card click listener interface class
 * @author mei hui
 */
public interface CardClickListener {

    void onCardClicked(Animal animal, int count, DragonCard dragonCard);
}
