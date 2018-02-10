import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Simulation {
    private ArrayList<Item> items;
    private boolean hasLanded = true;
    private File phase1 = new File("phase-1.txt");
    private File phase2 = new File("phase-2.txt");

    public Simulation() {

    }

//    Simulation class is responsible for reading item data and filling up the rockets.


//  loadItems method loads all items from a text file and returns an ArrayList of Items:
//  Each line in the text file consists of the item name followed by = then its weigh in kg. For example:
//  loadItems should read the text file line by line and create an Item object for each
//  and then add it to an ArrayList of Items. The method should then return that ArrayList.

    public ArrayList<Item> loadItems(int phase) throws FileNotFoundException {

        if (phase == 1) {
            System.out.println("Loading phase 1");
            loadPerPhaseItems(phase1);
            System.out.println("Done loading phase 1");
        } else {
            System.out.println("Loading phase 2");
            loadPerPhaseItems(phase2);
            System.out.println("Done loading phase 2\n");
        }

        return items;
    }

    public void loadPerPhaseItems(File file) throws FileNotFoundException {
        items = new ArrayList<>();
        Scanner fileScanner = new Scanner(file);

        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] list = line.split("=");

            Item item = new Item(list[0], Integer.parseInt(list[1]));
            items.add(item);
        }
    }

//    this method takes the ArrayList of Items returned from loadItems and starts creating U1 rockets.
//    It first tries to fill up 1 rocket with as many items as possible before creating a new rocket object
//    and filling that one until all items are loaded. The method then returns the ArrayList of those U1 rockets
//    that are fully loaded.

    public ArrayList<Rocket> loadU1(ArrayList<Item> list) {
        System.out.println("loading U1...");
        ArrayList<Rocket> rocketU1 = new ArrayList<>();
        Rocket rocket = new U1();
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Item item = (Item) iterator.next();
            if (rocket.canCarry(item)) {
                rocket.carry(item);
            } else {
                rocketU1.add(rocket);
                rocket = new U1();
                System.out.println("New U1 rocket created");
                rocket.carry(item);
            }
            if (!iterator.hasNext()) {
                rocketU1.add(rocket);
            }
        }

        return rocketU1;
    }

//    this method also takes the ArrayList of Items and starts creating U2 rockets and filling them with those items
//    the same way as with U1 until all items are loaded.
//    The method then returns the ArrayList of those U2 rockets that are fully loaded.

    public ArrayList loadU2(ArrayList<Item> list) {
        System.out.println("loading U2");
        ArrayList<Rocket> rocketU2 = new ArrayList<>();
        Rocket rocket = new U2();
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            Item item = (Item) iterator.next();
            if (rocket.canCarry(item)) {
                rocket.carry(item);
            } else {
                rocketU2.add(rocket);
                rocket = new U2();
                System.out.println("New U2 rocket created");
                rocket.carry(item);
            }
            if (!iterator.hasNext()) {
                rocketU2.add(rocket);
            }
        }

        return rocketU2;
    }

//    this method takes an ArrayList of Rockets and calls launch and land methods for each of the rockets in the ArrayList.
//    Every time a rocket explodes or crashes (i.e if launch or land return false) it will have to send that rocket again.
//    All while keeping track of the total budget required to send each rocket safely to Mars.
//    runSimulation then returns the total budget required to send all rockets (including the crashed ones).

    public void runSimulation(ArrayList<Rocket> list, int i) {
        for (Rocket rocket : list) {

            while (!rocket.launch()) {
                launchSimulation(i);
            }

            while (!rocket.land()) {
                while (!rocket.launch()) {
                    launchSimulation(i);
                }
                landSimulation(i);
            }

        }
    }

    public void launchSimulation(int i) {

        if (i == 1) {
            int counter1 = U1.getRocketU1Counter();
            counter1++;
            U1.setRocketU1Counter(counter1);

        } else {
            int counter1 = U2.getRocketU2Counter();
            counter1++;
            U2.setRocketU2Counter(counter1);

        }
    }

    public void landSimulation(int i) {
        if (i == 1) {
            int counter = U1.getRocketU1Counter();
            counter++;
            U1.setRocketU1Counter(counter);

        } else {
            int counter = U2.getRocketU2Counter();
            counter++;
            U2.setRocketU2Counter(counter);
        }
        hasLanded = false;
    }

}
