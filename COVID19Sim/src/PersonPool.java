import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PersonPool {
    private static PersonPool personPool = new PersonPool();

    public static PersonPool getInstance() {
        return personPool;
    }

    List<Person> personList = new ArrayList<Person>();

    public List<Person> getPersonList() {
        return personList;
    }


    /**
     * @param state  Person.State the value of state，if it is -1, return the total number
     * @return 获取指定人群数量
     */
    public int getPeopleSize(int state) {
        if (state == -1) {
            return personList.size();
        }
        int i = 0;
        for (Person person : personList) {
            if (person.getState() == state) {
                i++;
            }
        }
        return i;
    }


    private PersonPool() {
        City city = new City(400, 400);//设置城市中心为坐标(400,400)
        //添加城市居民
        for (int i = 0; i < Variables.TOTAL_POPULATION; i++) {
            Random random = new Random();
            //产生N(a,b)的数：Math.sqrt(b)*random.nextGaussian()+a
            int x = (int) (100 * random.nextGaussian() + city.getCenterX());
            int y = (int) (100 * random.nextGaussian() + city.getCenterY());
            if (x > 700) {
                x = 700;
            }
            personList.add(new Person(city, x, y));
        }
    }
}
