import javax.swing.*;
import java.util.List;
import java.util.Random;

public class COVIDSim {
    private double t  = 0;          // simulation clock time
    private List<Person> people;
    private MyPanel p;

    public COVIDSim(List<Person> people){
        this.people = people;
        p = new MyPanel();
    }


    private void initInfected() {
        for (int i = 0; i < Variables.ORIGIN_INFECTED_COUNT; i++) {
            Person person;
            do {
                person = people.get(new Random().nextInt(people.size() - 1));// randomly pick a citizen
            } while (person.isInfected());// if the citizen is already infected, repick
            person.beInfected(0);// make this citizen to be infected
        }
    }

    private void initPanel() {
        Thread panelThread = new Thread(p);
        JFrame frame = new JFrame();
        frame.add(p);
        frame.setSize(Variables.CITY_WIDTH + 300, Variables.CITY_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setTitle("COVID-19 Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panelThread.start();
    }

    public static void main(String[] args) {
        COVIDSim sim = new COVIDSim(Citizens.getInstance().getPersonList());
        sim.initInfected();
        sim.initPanel();
    }

}
