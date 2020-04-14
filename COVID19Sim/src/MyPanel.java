import com.sun.xml.internal.bind.v2.TODO;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class MyPanel extends JPanel implements Runnable {



    public MyPanel() {
        super();
        this.setBackground(new Color(0xaaaaaa));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        java.util.List<Person> people = Citizens.getInstance().getPersonList();
        if (people == null) {
            return;
        }
        for (Person person : people) {
            switch (person.getState()) {
                case Person.State.SUSCEPTIBLE: {
                    g.setColor(new Color(0x444444));
                    break;
                }
                case Person.State.EXPOSED: {
                    g.setColor(new Color(0xffee00));
                    break;
                }

                case Person.State.INFECTIOUS: {
                    g.setColor(new Color(0xff0000));
                    break;
                }

                case Person.State.RECOVERED: {
                    g.setColor(new Color(0x00ff00));
                    break;
                }
            }
            person.updateState();
            g.fillOval((int) person.getRx(), (int)person.getRy(), 3, 3);
        }

        int captionStartOffsetX = 700 + 40;
        int captionStartOffsetY = 40;
        int captionSize = 24;

        // display datas
        g.setColor(Color.WHITE);
        g.drawString("Population：" + Variables.TOTAL_POPULATION, captionStartOffsetX, captionStartOffsetY);
        g.drawString("World Time（Day）：" + (int) (worldTime / 10.0), captionStartOffsetX, captionStartOffsetY + captionSize);

        int susNum = Citizens.getInstance().getPeopleSize(Person.State.SUSCEPTIBLE);
        int exposedNum = Citizens.getInstance().getPeopleSize(Person.State.EXPOSED);
        int infectNum = Citizens.getInstance().getPeopleSize(Person.State.INFECTIOUS);
        int recoverNum = Citizens.getInstance().getPeopleSize(Person.State.RECOVERED);

        g.setColor(new Color(0x000000));
        g.drawString("Susceptible number：" + susNum, captionStartOffsetX, captionStartOffsetY + 2 * captionSize);
        g.setColor(new Color(0xffee00));
        g.drawString("Exposed number：" + exposedNum, captionStartOffsetX, captionStartOffsetY + 3 * captionSize);
        g.setColor(new Color(0xff0000));
        g.drawString("Infectious number：" + infectNum, captionStartOffsetX, captionStartOffsetY + 4 * captionSize);
        g.setColor(new Color(0x00ff00));
        g.drawString("Recovered number：" + recoverNum, captionStartOffsetX, captionStartOffsetY + 5 * captionSize);


        // TODO : export csv file in order to do analysis
        if((worldTime / 10.0) % 1 == 0 ){
            System.out.println("======= DAY " + (int) worldTime / 10.0 + " ========");
            System.out.println("Susceptible number：" + susNum);
            System.out.println("Exposed number：" + exposedNum);
            System.out.println("Infectious number：" + infectNum);
            System.out.println("Recovered number：" + recoverNum);
        }

    }


    public static int worldTime = 0;//世界时间

    public java.util.Timer timer = new Timer();

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            MyPanel.this.repaint();
            worldTime++;
        }
    }

    @Override
    public void run() {
        timer.schedule(new MyTimerTask(), 0, 100);//启动世界计时器，时间开始流动（突然脑补DIO台词：時は停た）

    }


}