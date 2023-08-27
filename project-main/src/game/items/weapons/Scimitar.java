package game.items.weapons;
/**
 * A curved sword represented by "s", carried around by Skeletal Bandit
 * It deals 118 damage with 88% accuracy
 * Created by:
 * @author Jun Lim
 */
public class Scimitar extends CurvedSword {
    /**
     * Constructor.
     */
    public Scimitar() {
        super("Scimitar", 's', 118, "slices", 88);
    }
}
