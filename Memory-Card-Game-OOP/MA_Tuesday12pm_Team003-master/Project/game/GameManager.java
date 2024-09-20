package game;

import Animal.Animal;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import actions.*;
import actions.Action;

/**
 * GameManager singleton class to manage the game flow
 * @author jingweiong
 */
public class GameManager implements CardClickListener {
    private static GameManager instance;
    private final List<Player> players;
    private VolcanoCard[] volcanoCards;
    private int currentPlayerIndex;
    private GameBoard gameBoard;
    private List<DragonCard> flippedCards;
    private static final DoNothingAction doNothingAction = new DoNothingAction();


    /**
     * Private constructor to prevent instantiation from outside
     */
    private GameManager() {
        players = new ArrayList<>();
        volcanoCards = new VolcanoCard[24];
        flippedCards = new ArrayList<>();
    }

    /**
     * Method to get the singleton instance
     * @return game manager instance
     */
    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     * Method to add a player to the game
     * @param player player
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    public void setVolcanoCards(VolcanoCard[] volcanoCards) {
        this.volcanoCards = volcanoCards;
    }

    /**
     * Method to handle start game logic
     * @param gameBoard main game board
     */
    public void startGame(GameBoard gameBoard) {
//        gameOver = false;
        this.gameBoard = gameBoard;
        updateTurnLabel();
    }

    /**
     * Method to handle Player's turn
     * @param flippedCardAnimal the animal of the flipped dragon card
     * @param animalCount the animal count of the flipped dragon card
     */
    private void handlePlayerTurn(Animal flippedCardAnimal, int animalCount) {
        Player currentPlayer = players.get(this.currentPlayerIndex);
        DragonToken token = currentPlayer.getToken();
        Action action;

        int exactMovesToCave = token.getExactMovesToCave();
        VolcanoCard currentVolcanoCard = token.getCurrentVolcanoCard();
        Animal animalOfCurrentPosition;
        int nextVolcanoCardIndex;

        if (currentVolcanoCard == null) {
            int caveAttachedVolcanoCardIndex = token.getCave().getVolcanoCard().getIndex();
            nextVolcanoCardIndex = (caveAttachedVolcanoCardIndex + animalCount - 1) % volcanoCards.length;
            animalOfCurrentPosition = token.getAnimal();
        } else {
            nextVolcanoCardIndex = (currentVolcanoCard.getIndex() + animalCount) % volcanoCards.length;
            animalOfCurrentPosition = currentVolcanoCard.getAnimal();
        }

        if (animalCount > exactMovesToCave) {
            action = doNothingAction;
        } else if (animalCount < exactMovesToCave) {
            if (flippedCardAnimal.getName().equals(animalOfCurrentPosition.getName())) {
                //got token
                currentVolcanoCard = volcanoCards[nextVolcanoCardIndex];
                if(!currentVolcanoCard.otherToken()){
                   action = new MoveForwardAction(token, currentVolcanoCard, animalCount);
                }else{
                    action = doNothingAction;
                }

            } else if (flippedCardAnimal.getName().equals("P")) {
                // x in cave
                if(currentVolcanoCard != null){
                    currentVolcanoCard = volcanoCards[nextVolcanoCardIndex];

                    if(!currentVolcanoCard.otherToken()){
                        action = new MoveBackwardAction(token, currentVolcanoCard,-animalCount);
                    }
                    else{
                        action = doNothingAction;
                    }
                }else{
                    action = doNothingAction;
                }
            } else {
                action =doNothingAction;
            }
        } else { // animalCount == exactMovesToCave
            if (flippedCardAnimal.getName().equals(animalOfCurrentPosition.getName())) {
                endGame();
                return; // End the turn immediately if the game is over
            } else {
                action =doNothingAction;
            }
        }

        action.execute();
        updateTurnLabel();
        gameBoard.repaint();
        System.out.println("animal count: " + animalCount);
        System.out.println("exact moves to cave: " + token.getExactMovesToCave());
    }

    /**
     * Method to switch to next player
     */
    public void switchTurns() {
        this.currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        Player currentPlayer = players.get(this.currentPlayerIndex);
        JOptionPane.showMessageDialog(null,currentPlayer.getToken().getAnimal().getName() + "'s turn");
        updateTurnLabel();
        flippedBack();
    }

    /**
     * Method to show the Player's turn and current location of the token
     */
    public void updateTurnLabel() {
        Player currentPlayer = players.get(currentPlayerIndex);
        String turnMessage = currentPlayer.getToken().getAnimal().getName() + "'s turn";

        VolcanoCard currentVolcanoCard = currentPlayer.getToken().getCurrentVolcanoCard();
        System.out.println(currentVolcanoCard);

        if (currentVolcanoCard != null) {
            turnMessage += "          Current location: " + currentVolcanoCard.getAnimal().getName();
        } else {
            turnMessage += "          Current location: " + currentPlayer.getToken().getAnimal().getName() + "'s Cave";
        }

        gameBoard.updateTurnLabel(turnMessage);
    }

    /**
     * Method triggered when the dragon card is flipped
     * @param flippedCardAnimal the animal of the flipped dragon card
     * @param animalCount the animal count of the flipped dragon card
     * @param dragonCard the flipped dragon card
     */
    @Override
    public void onCardClicked(Animal flippedCardAnimal, int animalCount, DragonCard dragonCard) {
        // insert the flipped dragon card to a list
        flippedCards.add(dragonCard);

        handlePlayerTurn(flippedCardAnimal, animalCount);


    }

    /**
     * Method to flip back all the dragon card
     */
    public void flippedBack(){
        for(DragonCard dragonCard: flippedCards){
            dragonCard.faceDown();
        }

        flippedCards.clear();
    }

    /**
     * Method to handle end game logic
     */
    private void endGame() {
        Player currentPlayer = players.get(this.currentPlayerIndex);
        JOptionPane.showMessageDialog(null,currentPlayer.getToken().getAnimal().getName() + " wins");
        resetGame();
    }

    /**
     * Method to reset all the game components
     */
    private void resetGame() {
        for (Player player : players) {
            //return token back to cave
            player.getToken().returnCave();
            player.getToken().setCurrentVolcanoCard(null);

        }
        // reset volcano card othertoken as false
        for(VolcanoCard volcanoCard: volcanoCards){
            // if volcano card othertoken is true
            if (volcanoCard.otherToken()){
                volcanoCard.setToken();
            }
        }
        flippedBack();
        gameBoard.repaint();

    }

    /**
     * Getter for list of players
     * @return player list
     */
    public List<Player> getPlayers() {
        return players;
    }
}
