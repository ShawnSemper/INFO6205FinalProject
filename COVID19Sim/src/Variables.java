
// The variables of this simulation
public class Variables {
    // Changeable variables

    public static int ORIGIN_INFECTED_COUNT = 0;
    public static int TOTAL_POPULATION = 100;
    /* [-0.99, 0.99]
     * 0.99 is uncontrolled situation, the virus will transmit fast.
     * -0.99 is that no body is moving.
     */
    public static float WILLING_TO_MOVE = 0.99f;

    // Constants
    public static float INCUBATION_INFECT_RATE = 0.5f;
    public static float INFECT_RATE = 0.8f;
    public static float INCUBATION_PERIOD = 140;// 140 = 14 days

    /*
     * The size of the city, nobody is moving out of the city
     */
    public static final int CITY_WIDTH = 700;
    public static final int CITY_HEIGHT = 800;
}
