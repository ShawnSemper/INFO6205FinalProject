import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class MyPanel extends JPanel implements Runnable {



    public MyPanel() {
        super();
        this.setBackground(new Color(0x444444));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //绘制代表人类的圆点
        java.util.List<Person> people = PersonPool.getInstance().getPersonList();
        if (people == null) {
            return;
        }
        for (Person person : people) {
            switch (person.getState()) {
                case Person.State.SUSCEPTIBLE: {
                    //健康人
                    g.setColor(new Color(0xdddddd));
                    break;
                }
                case Person.State.EXPOSED: {
                    //潜伏期感染者
                    g.setColor(new Color(0xffee00));
                    break;
                }

                case Person.State.INFECTIOUS: {
                    //确诊患者
                    g.setColor(new Color(0xff0000));
                    break;
                }
            }
            g.fillOval((int) person.getRx(), (int)person.getRy(), 3, 3);

        }

        int captionStartOffsetX = 700 + 40;
        int captionStartOffsetY = 40;
        int captionSize = 24;

        //显示数据信息
        g.setColor(Color.WHITE);
        g.drawString("城市总人数：" + Variables.TOTAL_POPULATION, captionStartOffsetX, captionStartOffsetY);
        g.setColor(new Color(0xdddddd));
        g.drawString("健康者人数：" + PersonPool.getInstance().getPeopleSize(Person.State.SUSCEPTIBLE), captionStartOffsetX, captionStartOffsetY + captionSize);
        g.setColor(new Color(0xffee00));
        g.drawString("潜伏期人数：" + PersonPool.getInstance().getPeopleSize(Person.State.EXPOSED), captionStartOffsetX, captionStartOffsetY + 2 * captionSize);
        g.setColor(new Color(0xff0000));
        g.drawString("发病者人数：" + PersonPool.getInstance().getPeopleSize(Person.State.INFECTIOUS), captionStartOffsetX, captionStartOffsetY + 3 * captionSize);
        g.setColor(new Color(0x48FFFC));

//        g.drawString("世界时间（天）：" + (int) (worldTime / 10.0), captionStartOffsetX, captionStartOffsetY + 8 * captionSize);

    }


//    public static int worldTime = 0;//世界时间
//
//    public java.util.Timer timer = new Timer();
//
//    class MyTimerTask extends TimerTask {
//        @Override
//        public void run() {
//            MyPanel.this.repaint();
//            worldTime++;
//        }
//    }
//
    @Override
    public void run() {
//        timer.schedule(new MyTimerTask(), 0, 100);//启动世界计时器，时间开始流动（突然脑补DIO台词：時は停た）

    }


}