import java.util.Random;

public class MathUtility {
    /**
     * Random number generator
     */
    private static final Random randomGen = new Random();

    /**
     * Use Normal Distribution to decide people's willing to move
     *
     * StdX = (X-u)/sigma
     * X = sigma * StdX + u
     *
     * @param sigma
     * @param u  aka WILLING_TO_MOVE variable
     * @return
     */
    public static double stdGaussian(double sigma, double u) {
        double X = randomGen.nextGaussian();
        return sigma * X + u;
    }

}
