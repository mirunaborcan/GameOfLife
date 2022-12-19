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

    private LinkedBlockingQueue<Cell> cellsList = new LinkedBlockingQueue<>();
    private LinkedBlockingQueue<Food> food = new LinkedBlockingQueue<>();
    private static final Logger log = LoggerFactory.getLogger(Playground.class);

    public Logger getLogger(){
        return log;
    }
    public boolean canEat(String threadInfo) {
        try {
            boolean lockFood;
            for (final Food resource : food) {
                //find a resource and lock it
                lockFood = resource.lock.tryLock();
                if (lockFood) {
                    try {
                        //log.info(threadInfo + " demand lock for " + resource.name);
                        if (resource.getFoodUnit() > 0) {
                            resource.decrement();
                            log.info("Food " + resource.name + " decremented by " + threadInfo);
                            return true;
                        }
                    } finally {
                     // unlock the resource after a cell ate 1 food unit from it
                        resource.lock.unlock();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addCell(Cell c) {
        this.cellsList.add(c);
    }


    public void addFood(int foodUnits, String threadInfo) {

        boolean lockFood;
        for (final Food resource : food) {
            lockFood = resource.lock.tryLock();
            if (lockFood) {
                try {
                    resource.increment(foodUnits);
                    log.info("Food " + resource.name + " has been incremented after cell" + threadInfo + "died");
                    break;
                } finally {
                    resource.lock.unlock();
                }
            }

        }

   }

    public LinkedBlockingQueue<Cell> getCellsList() {
        return this.cellsList;
    }

    public void startInitialThreads() {
        for(Cell cell: cellsList) {
            Thread t = new Thread(cell);
            cell.thread = t;
            t.start();
        }
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
        gameOfLife.addInitialFood(1);

        for(int i=0; i<4; i++)
        {
            gameOfLife.addCell(new AsexuateCell(1, 1, "ACell" + i) );
            gameOfLife.addCell(new SexuateCell(1, 1,"SCell" + i) );

        }

        Cell.playgroundObj = gameOfLife;
        gameOfLife.startInitialThreads();


    }

}