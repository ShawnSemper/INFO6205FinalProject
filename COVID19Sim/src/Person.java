import java.util.List;
import java.util.Random;

public class Person {
    private int rx, ry; // position
    private int expose_time = 0;
    private int infect_time = 0;
    private City city;
    private MoveTarget moveTarget;

    /**
     * Use normal distribution N(mu, sigma)to generate people's moving target
     */

    double targetXU;// average value of x direction
    double targetYU;// average value of y direction
    double targetSig = 50;

    public int getRx() {
        return rx;
    }

    public void setRx(int rx) {
        this.rx = rx;
    }

    public int getRy() {
        return ry;
    }

    public void setRy(int ry) {
        this.ry = ry;
    }

    /**
     * coefficient of people's willing to move：sigma
     */
    int sig = 1;

    // different number stands for different status, coming for SEIR model
    public interface State {
        int SUSCEPTIBLE = 1;
        int EXPOSED = 2;
        int INFECTIOUS = 3;
        int RECOVERED = 0;
    }

    // initialize position and move target
    public Person(City city, int rx, int ry) {
        this.rx = rx;
        this.ry = ry;
        this.city = city;

        targetXU = MathUtility.stdGaussian(100, rx);
        targetYU = MathUtility.stdGaussian(100, ry);

    }

    /**
     * Use Normal Distribution to decide people's willing to move
     *
     * StdX = (X-u)/sigma
     * X = sigma * StdX + u
     *
     *
     * @return
     */
    public boolean wantMove() {
        return MathUtility.stdGaussian(sig, Variables.WILLING_TO_MOVE) > 0;
    }

    /**
     * Init state and set states
     */
    private int state = State.SUSCEPTIBLE;

    public int getState() {
        return state;
    }

    public boolean isExposed() {
        return state == State.EXPOSED;
    }

    public void beExposed(int time) {
        state = State.EXPOSED;
        expose_time = time;
    }

    public boolean isInfected() {
        return state >= State.INFECTIOUS;
    }

    public void beInfected(int time) {
        state = State.INFECTIOUS;
        infect_time = time;
    }

    /**
     * Calculate the distance to another point
     *
     * @param thatX, thatY
     * @return
     */
    public double distance(int thatX, int thatY) {
        return Math.sqrt(Math.pow(rx - thatX, 2) + Math.pow(ry - thatY, 2));
    }


    /**
     * the move function of a person
     */
    private void move() {

        if (!wantMove()) {
            return;
        }
        /*
         * if willing to move, use normal distribution to generate move target
         */
        if (moveTarget == null || moveTarget.isArrived()) {

            double targetX = MathUtility.stdGaussian(targetSig, targetXU);
            double targetY = MathUtility.stdGaussian(targetSig, targetYU);
            moveTarget = new MoveTarget((int) targetX, (int) targetY);

        }

        // calculate the distance to move target
        double dr = distance(moveTarget.getX(), moveTarget.getY());

        // if distance is less than 1, set moveTarget to be arrived
        if (dr < 1) {
            moveTarget.setArrived();
            return;
        }

        int dX = moveTarget.getX() - rx;
        int udX = (int) (dX / dr);// udx is the velocity of x direction
        if (udX == 0 && dX != 0) {
            if (dX > 0) {
                udX = 1;
            } else {
                udX = -1;
            }
        }

        int dY = moveTarget.getY() - ry;
        int udY = (int) (dY / dr);// udy is the velocity of y direction
        if (udY == 0 && dY != 0) {
            if (dY > 0) {
                udY = 1;
            } else {
                udY = -1;
            }
        }

        // if this person if out of city, flip its velocity
        if (rx > Variables.CITY_WIDTH || rx < 0) {
            moveTarget = null;
            if (udX > 0) {
                udX = -udX;
            }
        }
        if (ry > Variables.CITY_HEIGHT || ry < 0) {
            moveTarget = null;
            if (udY > 0) {
                udY = -udY;
            }
        }

        // update this person's position
        rx += udX;
        ry += udY;

    }

    // safe distance, used to evaluate how many a person contact per day
    private float SAFE_DIST = 2f;

    /*
     * per SEIR model, susceptible people will have INFECT_RATE possibility to become exposed
     * exposed people will have INCUBATION_INFECT_RATE possibility to become infected
     * those who are infected will have RECOVER_RATE possibility to recover
     */
    public void updateState() {

        // if the person is already recovered, no need to process
        if (state == State.RECOVERED) {

        }

        /*
         * if the person is susceptible, check all the people with in its safe distance,
         * each people who is exposed or infect will have some possibility to make the person exposed
         */
        if (state == State.SUSCEPTIBLE) {
            for (Person person : Citizens.getInstance().getPersonList()) {
                if(distance(person.getRx(), person.getRy()) > SAFE_DIST || person.getState() <= 1){
                    continue;
                }
                float possibility = 0;
                if(person.getState() == 2) possibility = Variables.INCUBATION_INFECT_RATE;
                if(person.getState() == 3) possibility = Variables.INFECT_RATE;

                // if the random number is less than the exposed possibility, make this person to be exposed
                float random = new Random().nextFloat();
                if (random < possibility) {
                    this.beExposed(MyPanel.worldTime);
                    break;
                }
            }
        }

        /*
         * if the person is already exposed, use normal distribution to randomize the time he becomes infected
         */
        if(state == State.EXPOSED){
            double randomInfectedTime = MathUtility.stdGaussian(25, Variables.INCUBATION_PERIOD / 2);
            if (MyPanel.worldTime - expose_time > randomInfectedTime && state == State.EXPOSED) {
                state = State.INFECTIOUS;// update state to be infectious
                infect_time = MyPanel.worldTime;// update time
            }
        }

        /*
         * if the person is infectious, each day he will have RECOVER_RATE possibility to recover
         */
        if(state == State.INFECTIOUS){
            float random = new Random().nextFloat();
            if(random < Variables.RECOVER_RATE){
                this.state = State.RECOVERED;
            }
        }

        // the person moves
        move();
    }



}
