package playground;

import cells.AsexuateCell;
import cells.Cell;
import cells.SexuateCell;
import food.Food;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Playground {

    //private ExecutorService executor = Executors.newFixedThreadPool(100);
    private ArrayList<Cell> cellsList = new ArrayList<>();
    private LinkedBlockingQueue<Food> food = new LinkedBlockingQueue<>();
    private static final Logger log = LoggerFactory.getLogger(Playground.class);

    public Logger getLogger(){
        return log;
    }
    public boolean canEat(String threadInfo) {
        try {
            boolean lockFood;
            for (final Food resource : food) {
                lockFood = resource.lock.tryLock();
                if (lockFood) {
                    try {
                        //log.info(threadInfo + " acquired lock for " + resource.name);
                        if (resource.getFoodUnit() > 0) {
                            resource.decrement();
                            log.info("Food "+resource.name+" decremented by " + threadInfo);
                            return true;
                        }
                    } finally {
                        // Make sure to unlock so that we don't cause a deadlock
                        //log.info(threadInfo + " unlocked " + resource.name);
                        resource.lock.unlock();
                    }
                }
//                else
//                    log.info(threadInfo + " tried to lock food resource " + resource.name + " but it was locked");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addCell(Cell c) {
        cellsList.add(c);
       // Thread t = new Thread(c);
        //c.thread = t;
       // executor.execute(t);
    }


    public void addFood(int resources, String threadInfo) {

        boolean lockFood;
        for (final Food resource : food) {
            lockFood = resource.lock.tryLock();
            if (lockFood) {
                try {
                    resource.increment(resources);
                    log.info("Food " + resource.name + " has been incremented");
                    break;
                } finally {
                    // Make sure to unlock so that we don't cause a deadlock
                    // log.info(threadInfo + " unlocked " + resource.name);
                    resource.lock.unlock();
                }
            } //else
            // log.info(threadInfo + " tried to lock food " + resource.name);


        }

   }

    public ArrayList<Cell> getCellsList() {
        return cellsList;
    }

    public void removeCell(Cell c){
        cellsList.remove(c);
    }

    public void startInitialThreads() {
        for(Cell cell: cellsList) {
            Thread t = new Thread(cell);
            cell.thread = t;
            t.start();
            //executor.execute(t);//calling execute method of ExecutorService
        }
        //executor.shutdown();
        //while (!executor.isTerminated()) {   }
    }


    public void addInitialFood(int recourceNr){

       for(int i=1; i<=recourceNr; i++){
            try {
                this.food.put(new Food(50,"resource"+i));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void main(String[] args) {


        Playground gameOfLife = new Playground();
        gameOfLife.addInitialFood(5);

        //gameOfLife.addCell(new AsexuateCell(5, 5, "ACell1") );
        for(int i=0; i<5; i++)
        {
            //gameOfLife.addCell(new AsexuateCell(1, 8, "ACell" + i) );
           gameOfLife.addCell(new SexuateCell(1, 5, "SCell" + i) );

        }


        Cell.playgroundObj = gameOfLife;
        gameOfLife.startInitialThreads();


    }

}