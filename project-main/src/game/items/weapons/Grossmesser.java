package game.items.weapons;
/**
 * A curved sword represented by "?", carried around by Heavy Skeletal Swordsman
 * It deals 115 damage with 85% accuracy
 * Created by:
 * @author Jun Lim
 */
public class Grossmesser extends CurvedSword {
    /**
     * Constructor.
     */
    public Grossmesser() {
        super("Grossmesser", '?', 115, "slashes", 85);
    }
}
