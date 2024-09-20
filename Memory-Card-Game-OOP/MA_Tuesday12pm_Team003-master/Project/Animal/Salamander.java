package Animal;

/**
 * Salamander class which implements animal interface class
 * @author jingweiong
 */
public class Salamander implements Animal{
    private String name;

    /**
     * Constructor
     */
    public Salamander(){
        this.name = "Sal";
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
