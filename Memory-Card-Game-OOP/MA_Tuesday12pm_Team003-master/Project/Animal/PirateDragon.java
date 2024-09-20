package Animal;

/**
 * PirateDragon class which implements animal interface class
 * @author jingweiong
 */
public class PirateDragon implements Animal{

    private String name;

    /**
     * Constructor
     */
    public PirateDragon(){
        this.name = "P";
    }

    /**
     * Getter for animal's name
     * @return
     */
    @Override
    public String getName() {
        return name;
    }
}
