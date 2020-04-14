import javax.swing.*;
import java.util.List;
import java.util.Random;

public class COVIDSim {
    private MinPQ<Event> pq;          // the priority queue
    private double t  = 0;          // simulation clock time
    private List<Person> people;
    private MyPanel p;

    public COVIDSim(List<Person> people){
        this.people = people;
        p = new MyPanel();
    }

    private void predict(Person a) {
        if(a == null) return;

        for(Person person : people){
            int dt = (int) a.timeToContact(person);
            pq.insert(new Event(t + dt, a, person));
        }
    }

    public void simulate() {
        // initialize PQ with contact events
        pq = new MinPQ<Event>();
        for (int i = 0; i < people.size(); i++) {
            predict(people.get(i));
        }

        // the main event-driven simulation loop
        while (!pq.isEmpty()) {

            // get impending event, discard if invalidated
            Event e = pq.delMin();

            Person a = e.a;
            Person b = e.b;

            // physical collision, so update positions, and then simulation clock
            for (int i = 0; i < people.size(); i++)
                people.get(i).move((int)(e.time - t));

            t = e.time;

            // process event
            a.updateState(b, (int)t);

            // update the priority queue with new collisions involving a or b
            predict(a);
            predict(b);

            // redraw
            p.repaint();
        }
    }

    private static class Event implements Comparable<Event> {
        private final double time;         // time that event is scheduled to occur
        private final Person a, b;       // particles involved in event, possibly null

        // create a new event to occur at time t involving a and b
        public Event(double t, Person a, Person b) {
            this.time = t;
            this.a    = a;
            this.b    = b;
        }

        // compare times when two events will occur
        public int compareTo(Event that) {
            return Double.compare(this.time, that.time);
        }

    }

    private void initInfected() {
        for (int i = 0; i < Variables.ORIGIN_INFECTED_COUNT; i++) {
            Person person;
            do {
                person = people.get(new Random().nextInt(people.size() - 1));//随机挑选一个市民
            } while (person.isInfected());//如果该市民已经被感染，重新挑选
            person.beInfected(0);//让这个幸运的市民成为感染者
        }
    }

    private void initPanel() {
        Thread panelThread = new Thread(p);
        JFrame frame = new JFrame();
        frame.add(p);
        frame.setSize(Variables.CITY_WIDTH + 300, Variables.CITY_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setTitle("瘟疫传播模拟");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panelThread.start();//开启画布线程，即世界线程，接着看代码的下一站可以转MyPanel.java
    }

    public static void main(String[] args) {
        COVIDSim sim = new COVIDSim(PersonPool.getInstance().getPersonList());
        sim.initInfected();
        sim.initPanel();
        sim.simulate();
    }

}
