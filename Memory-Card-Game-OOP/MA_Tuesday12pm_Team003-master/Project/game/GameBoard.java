package game;

import Animal.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The main class to initialize all game components
 */
public class GameBoard extends JPanel{

    private final int WIDTH = 600; // Adjust according to your preference
    private final int HEIGHT = 600; // Adjust according to your preference
    private final int OUTER_RADIUS = 250; // Radius of the outer circle
    private final int INNER_RADIUS = 150; // Radius of the inner circle
    private final int NUM_VOLCANO_CARDS = 24; // Number of volcano cards
    private final VolcanoCard[] volcanoCards = new VolcanoCard[NUM_VOLCANO_CARDS];
    private List<DragonCard> dragonCards = new ArrayList<>();
    private final List<Animal> animals = new ArrayList<>();
    private final GameManager gameManager; // Leave this for next sprint
    private JPanel cardPanel;
    private JLabel turnLabel;


    /**
     * GameBoard constructor
     */
    public GameBoard() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        gameManager = GameManager.getInstance();
        initializeAnimals();
        getPlayers();
        initializeVolcanoCards();
        initializeDragonCards();
        initializeTurnLabel();
        gameManager.startGame(this);
        setLayout(null); // Use absolute positioning
    }
    // Initialize the JLabel for displaying the current turn
    private void initializeTurnLabel() {
        turnLabel = new JLabel("", SwingConstants.LEFT);
        turnLabel.setBounds(10, 0, 580, 30); // Position and size of the label
        add(turnLabel);
    }

    /**
     * Initialize all the animal types
     */
    private void initializeAnimals() {
        // Initialize the list of animals (Bat, Spider, Salamander, BabyDragon)
        animals.add(new Bat());
        animals.add(new Spider());
        animals.add(new Salamander());
        animals.add(new BabyDragon());
    }

    /**
     * Initialize Volcano cards which allows token to move on
     */
    private void initializeVolcanoCards() {
        for (int i = 0; i < NUM_VOLCANO_CARDS; i++) {
            Animal animal = animals.get(i % animals.size());
            volcanoCards[i] = new VolcanoCard(i, animal, NUM_VOLCANO_CARDS, OUTER_RADIUS, INNER_RADIUS, WIDTH, HEIGHT);
            Cave cave = volcanoCards[i].getCave();
            if (cave != null) {
                for (Player player : gameManager.getPlayers()) {
                    DragonToken token = player.getToken();
                    if (cave.getAnimal() == token.getAnimal()) {
                        token.setX(cave.getPosition().x);
                        token.setY(cave.getPosition().y);
                        token.setCave(cave);
                    }
                }
            }
        }
        gameManager.setVolcanoCards(volcanoCards);
    }

    /**
     * Initialize Dragon cards in the middle of the board which allows player to flip
     */
    private void initializeDragonCards() {
        cardPanel = new JPanel();
        dragonCards = new ArrayList<>();
        animals.add(new PirateDragon());

        cardPanel.setLayout(new GridLayout(4, 4)); // Grid layout for cards

        int[] quantities = {3, 3, 3, 3, 4};

        for (int i = 0; i < animals.size(); i++) {
            Animal animal = animals.get(i);
            int quantity = quantities[i];

            for (int j = 0; j < quantity; j++) {
                int numberOfAnimals;

                // Choose the number of animals
                switch (j) {
                    case 0:
                        numberOfAnimals = 1;
                        break;
                    case 1:
                        numberOfAnimals = 2;
                        break;
                    default:
                        numberOfAnimals = 3;
                        break;
                }

                // For PirateDragon
                if (animal instanceof PirateDragon) {
                    // For the first two PirateDragon cards, set the number of animals differently
                    if (j < 2) {
                        numberOfAnimals = -1;
                    } else {
                        numberOfAnimals = -2;
                    }
                }

                DragonCard card = new DragonCard(animal, numberOfAnimals, gameManager);
                dragonCards.add(card);
            }
        }
        // Shuffle the cards
        Collections.shuffle(dragonCards);

        // Add the shuffled cards to the cardPanel
        for (DragonCard card : dragonCards) {
            cardPanel.add(card);
        }

        // Calculate the position of the cardPanel in the center of the circle
        int cardPanelWidth = cardPanel.getPreferredSize().width - 30;
        int cardPanelHeight = cardPanel.getPreferredSize().height - 30;
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int circleRadius = 300; // Assuming the radius of the circle
        int cardPanelX = centerX - cardPanelWidth / 2;
        int cardPanelY = centerY - cardPanelHeight / 2;

        // Calculate position relative to the center of the circle
        int relativeX = (int) (circleRadius * Math.cos(Math.toRadians(10)));
        int relativeY = (int) (circleRadius * Math.sin(Math.toRadians(90))); // Angle 45°

        // To move more to the right, increase the relativeX value
        int offsetX = 5; // Adjust this value according to your preference
        relativeX += offsetX;

        // To move the panel downwards, increase the relativeY value
        int offsetY = 20; // Adjust this value according to your preference
        relativeY += offsetY;

        // Set the final position of the cardPanel
        cardPanel.setBounds(cardPanelX + relativeX, cardPanelY + relativeY, cardPanelWidth, cardPanelHeight);
        add(cardPanel);
    }

    /**
     * Get the players count
     */
    public void getPlayers() {
        for (int i = 0; i < animals.size(); i++) {
            Player player = new Player("Player " + i, animals.get(i));
            gameManager.addPlayer(player);
        }
    }

    /**
     * Paint all the game components
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw outer circle
        int outerCircleX = (WIDTH - OUTER_RADIUS * 2) / 2;
        int outerCircleY = (HEIGHT - OUTER_RADIUS * 2) / 2;
        g2d.drawOval(outerCircleX, outerCircleY, OUTER_RADIUS * 2, OUTER_RADIUS * 2);

        // Draw inner circle
        int innerCircleX = (WIDTH - INNER_RADIUS * 2) / 2;
        int innerCircleY = (HEIGHT - INNER_RADIUS * 2) / 2;
        g2d.drawOval(innerCircleX, innerCircleY, INNER_RADIUS * 2, INNER_RADIUS * 2);

        // Draw volcano cards
        for (VolcanoCard card : volcanoCards) {
            g2d.drawLine(card.getStartX(), card.getStartY(), card.getEndX(), card.getEndY());
            g2d.drawString(card.getAnimal().getName(), card.getCenterX(), card.getCenterY());
        }

        for (VolcanoCard card : volcanoCards) {
            Cave cave = card.getCave();
            if (cave != null) {
                cave.draw(g2d);
            }
        }

        for (Player player : gameManager.getPlayers()) {
            player.getToken().draw(g2d);
        }

    }

    /**
     * Method to update the turn label which show on the game interface
     * @param text the token's current location and indicating which player's turn
     */
    public void updateTurnLabel(String text) {
        turnLabel.setText(text);
    }

    /**
     * Application Driver
     * @param args
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("Fiery Dragon Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GameBoard gameBoard = new GameBoard();
        frame.add(gameBoard);
        frame.pack();
        frame.setVisible(true);
    }
}