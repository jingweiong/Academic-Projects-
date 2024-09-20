/**import static org.junit.Assert.*;

        import Animal.Bat;
import org.junit.Test;

/**
 * Test class
 * @author jingweiong

public class FieryDragonGameTest {

//    @Test
//    public void testDragonCardFlip() {
//        DragonCard card = new DragonCard(new Bat(), 1, 100, 100, 20);
//        assertFalse(card.is_flipped());
//        card.flip();
//        assertTrue(card.is_flipped());
//    }

    @Test
    public void testVolcanoCardAnimal() {
        VolcanoCard card = new VolcanoCard(1, new Bat(), 24, 250, 150, 600, 600);
        assertEquals("Bat", card.getAnimal().getName());
    }

    @Test
    public void testGameManagerInitialization() {
        GameManager manager = GameManager.getInstance();
        assertNotNull(manager);
    }

    @Test
    public void testPlayerInitialization() {
        // Test initialization of Player
        Player player = new Player("Player test", new Bat());
        assertNotNull(player); // assert that Player instance is not null
    }

    @Test
    public void testTokenInitialization() {
        // Test initialization of DragonToken
        DragonToken token = new DragonToken(new Bat());
        assertNotNull(token); // Assert that DragonToken instance is not null
    }

    @Test
    public void testCaveCreation() {
        // Test creation of Cave attached to VolcanoCard
        VolcanoCard card = new VolcanoCard(0, new Bat(), 24, 250, 150, 600, 600);
        Cave cave = card.getCave(); // Get the attached cave
        assertNotNull(cave); // Assert that a cave is created and not null
    }

    @Test
    public void testTokenMoveForward() {
        VolcanoCard[] volcanoCards = new VolcanoCard[24];
        for (int i=0; i<24; i++) {
            volcanoCards[i] = new VolcanoCard(i, new Bat(), 24, 250, 150, 600, 600);
        }
        DragonToken token = new DragonToken(new Bat());
        VolcanoCard currentCard = volcanoCards[0];
        token.move(currentCard);
        assertEquals(volcanoCards[0], token.getCurrentVolcanoCard());
    }

}
 **/