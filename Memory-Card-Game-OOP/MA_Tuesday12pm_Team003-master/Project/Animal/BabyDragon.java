package Animal;

/**
 * BabyDragon class which implements animal interface class
 * @author jingweiong
 */
public class BabyDragon implements Animal{

    private String name;

    /**
     * Constructor
     */
    public BabyDragon(){
        this.name = "D";
    }

    /**
     * Getter for name
     * @return name
     */
    @Override
    public String getName() {
        return name;
    }
}
