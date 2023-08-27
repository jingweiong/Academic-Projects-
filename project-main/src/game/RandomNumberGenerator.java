package game;

import java.util.Random;

/**
 * A random number generator
 * Created by:
 * @author Adrian Kristanto
 * Modified by:
 * @author Jun Lim
 */
public class RandomNumberGenerator {
    public static int getRandomInt(int bound) {
        return bound > 0 ? new Random().nextInt(bound) : 0;
    }

    public static int getRandomInt(int lowerBound, int upperBound) {
        int range = upperBound - lowerBound + 1;
        return new Random().nextInt(range) + lowerBound;
    }

    /**
     * Runs a random check with a given chance of success
     * 
     * @param chance percentage success chance
     * @return true if the check succeeded, false otherwise
     */
    public static boolean percentChance(int chance) {
        int rand = new Random().nextInt(100) + 1; // Random.nextInt returns a value between 0 and the bound exclusive, adding 1 to change range to 1~100
        boolean bool;
        if (chance >= rand) {
            // System.out.println(rand + " " + chance + " success");
            bool = true;
        } else {
            // System.out.println(rand + " " + chance + " false");
            bool = false;
        }
        return bool;
    }
}