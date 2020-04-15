import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
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
        g.drawString("Populationï¼š" + Variables.TOTAL_POPULATION, captionStartOffsetX, captionStartOffsetY);
        g.drawString("World Timeï¼ˆDayï¼‰ï¼š" + (int) (worldTime / 10.0), captionStartOffsetX, captionStartOffsetY + captionSize);

        int susNum = Citizens.getInstance().getPeopleSize(Person.State.SUSCEPTIBLE);
        int exposedNum = Citizens.getInstance().getPeopleSize(Person.State.EXPOSED);
        int infectNum = Citizens.getInstance().getPeopleSize(Person.State.INFECTIOUS);
        int recoverNum = Citizens.getInstance().getPeopleSize(Person.State.RECOVERED);

        g.setColor(new Color(0x000000));
        g.drawString("Susceptible numberï¼š" + susNum, captionStartOffsetX, captionStartOffsetY + 2 * captionSize);
        g.setColor(new Color(0xffee00));
        g.drawString("Exposed numberï¼š" + exposedNum, captionStartOffsetX, captionStartOffsetY + 3 * captionSize);
        g.setColor(new Color(0xff0000));
        g.drawString("Infectious numberï¼š" + infectNum, captionStartOffsetX, captionStartOffsetY + 4 * captionSize);
        g.setColor(new Color(0x00ff00));
        g.drawString("Recovered numberï¼š" + recoverNum, captionStartOffsetX, captionStartOffsetY + 5 * captionSize);



        // TODO : export csv file in order to do analysis
        if((worldTime / 10.0) % 1 == 0 ){
            System.out.println("======= DAY " + (int) worldTime / 10.0 + " ========");
            System.out.println("Susceptible numberï¼š" + susNum);
            System.out.println("Exposed numberï¼š" + exposedNum);
            System.out.println("Infectious numberï¼š" + infectNum);
            System.out.println("Recovered numberï¼š" + recoverNum);
            WriteStuCSV();
        }

    }

	public static File WriteStuCSV() {
		File csvFile = new File("Data.csv");
		FileOutputStream fos = null;
        int susNum = Citizens.getInstance().getPeopleSize(Person.State.SUSCEPTIBLE);
        int exposedNum = Citizens.getInstance().getPeopleSize(Person.State.EXPOSED);
        int infectNum = Citizens.getInstance().getPeopleSize(Person.State.INFECTIOUS);
        int recoverNum = Citizens.getInstance().getPeopleSize(Person.State.RECOVERED);
		// try with resources: all resources in () are closed at conclusion of try clause
		try {	// open output stream to output file for writing purpose.
	            if(!csvFile.exists()){
	                csvFile.createNewFile();
	                fos = new FileOutputStream(csvFile);
	            }else{

	                fos = new FileOutputStream(csvFile,true);
	            }		
	            OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");
				out.write("\r\n");			 
				out.write("======= DAY " + (int) worldTime / 10.0 + " ========");
				out.write("\r\n");
				out.write("Susceptible numberï¼š" + susNum);
				out.write("\r\n");
				out.write("Exposed numberï¼š" + exposedNum);
				out.write("\r\n");
				out.write("Infectious numberï¼š" + infectNum);
				out.write("\r\n");			
				out.write("Recovered numberï¼š" + recoverNum);
			out.close();
			return csvFile;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

    public static int worldTime = 0;//ä¸–ç•Œæ—¶é—´

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
        timer.schedule(new MyTimerTask(), 0, 100);//å�¯åŠ¨ä¸–ç•Œè®¡æ—¶å™¨ï¼Œæ—¶é—´å¼€å§‹æµ�åŠ¨ï¼ˆçª�ç„¶è„‘è¡¥DIOå�°è¯�ï¼šæ™‚ã�¯å�œã�Ÿï¼‰

    }


}

