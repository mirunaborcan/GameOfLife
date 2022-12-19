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
    private LinkedBlockingQueue<Cell> cellsList = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Food> food = new LinkedBlockingQueue<>();
    private static final Logger log = LoggerFactory.getLogger(Playground.class);

    public Logger getLogger(){
        return log;
    }
    public boolean canEat(String threadInfo) {
        try {
            boolean lockFood;
            Iterator<Food> foodIt = food.iterator();
            while(foodIt.hasNext()) {
                Food resource = foodIt.next();
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
        this.cellsList.add(c);
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

    public LinkedBlockingQueue<Cell> getCellsList() {
        return this.cellsList;
    }

    public void removeCell(Cell c){
        this.cellsList.remove(c);
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
                this.food.put(new Food(100,"resource"+i));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void main(String[] args) {


        Playground gameOfLife = new Playground();
        gameOfLife.addInitialFood(2);

        //gameOfLife.addCell(new AsexuateCell(5, 5, "ACell1") );
        for(int i=0; i<4; i++)
        {
            //gameOfLife.addCell(new AsexuateCell(1, 8, "ACell" + i) );
           gameOfLife.addCell(new SexuateCell(1, 10,"SCell" + i) );

        }


        Cell.playgroundObj = gameOfLife;
        gameOfLife.startInitialThreads();


    }

}