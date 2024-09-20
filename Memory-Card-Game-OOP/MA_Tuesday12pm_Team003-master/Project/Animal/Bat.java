package Animal;

/**
 * Bat class which implements animal interface class
 * @author jingweiong
 */
public class Bat implements Animal{
    private String name;

    /**
     * Constructor
     */
    public Bat(){
        this.name = "Bat";
    }

    /**
     * Getter for animal's name
     * @return
     */
    public String getName() {
        return name;
    }
}
