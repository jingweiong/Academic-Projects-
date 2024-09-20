package Animal;

/**
 * Spider class which implements animal interface class
 * @author jingweiong
 */
public class Spider implements Animal{

    private String name;

    /**
     * Constructor
     */
    public Spider(){
        this.name = "Spi";
    }

    /**
     * Getter for animal's name
     * @return name
     */
    @Override
    public String getName() {
        return name;
    }
}
