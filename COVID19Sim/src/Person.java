import java.util.Random;

public class Person {
    private double rx, ry; // position
    private double vx, vy; // velocity
    private int expose_time;
    private int infect_time;
    private City city;

    public double getRx() {
        return rx;
    }

    public void setRx(double rx) {
        this.rx = rx;
    }

    public double getRy() {
        return ry;
    }

    public void setRy(double ry) {
        this.ry = ry;
    }

    /**
     * coefficient of people's willing to move：sigma
     */
    int sig = 1;

    // different number stands for different status
    public interface State {
        int SUSCEPTIBLE = 1;
        int EXPOSED = 2;
        int INFECTIOUS = 3;
        int RECOVERED = 0;
    }

    // initialize position and velocity
    public Person(City city, int rx, int ry) {
        this.rx = rx;
        this.ry = ry;
        this.city = city;

        initVelocity();

    }

    // initialize velocity
    public void initVelocity() {
        // if this person doesn't want to move, initialize the velocity to be 0
        if(!wantMove()){
            this.vx = 0;
            this.vy = 0;
        }else {
            Random random = new Random();
            this.vx = 10 * random.nextInt();
            this.vy = 10 * random.nextInt();
        }
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
        return state == State.INFECTIOUS;
    }

    public void beInfected(int time) {
        state = State.INFECTIOUS;
        infect_time = time;
    }


    /**
     * Calculate the distance between two people
     *
     * @param person
     * @return
     */
    public double distance(Person person) {
        return Math.sqrt(Math.pow(rx - person.getRx(), 2) + Math.pow(ry - person.getRy(), 2));
    }

    public void move(int dt) {
        if(rx > Variables.CITY_WIDTH || rx < 0){
            vx = -vx;
        }
        if(ry > Variables.CITY_HEIGHT || ry < 0){
            vy = -vy;
        }
        rx += vx * dt;
        ry += vy * dt;
    }

    public void draw() {

    }

    public double timeToContact(Person that) {
        if(this == that) return Double.POSITIVE_INFINITY;

        double dx  = that.rx - this.rx;
        double dy  = that.ry - this.ry;
        double dvx = that.vx - this.vx;
        double dvy = that.vy - this.vy;
        double dvdr = dx*dvx + dy*dvy;
        if (dvdr > 0) return Double.POSITIVE_INFINITY;
        double dvdv = dvx*dvx + dvy*dvy;
        if (dvdv == 0) return Double.POSITIVE_INFINITY;
        double drdr = dx*dx + dy*dy;
        double d = dvdr * dvdr - dvdv * drdr;
        if (d < 0) return Double.POSITIVE_INFINITY;

        return -(dvdr + Math.sqrt(d)) / dvdv;
    }

    // when two people contact, update their status
    public void updateState(Person that, int time) {
        if(this.getState() == 1 && that.getState() > 1){
            float random = new Random().nextFloat();
            if(random < Variables.INFECT_RATE) this.beInfected(time);
        }
        if(that.getState() == 1 && this.getState() > 1){
            float random = new Random().nextFloat();
            if(random < Variables.INFECT_RATE) that.beInfected(time);
        }
    }


}
