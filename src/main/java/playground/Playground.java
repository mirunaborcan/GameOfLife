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

    private ArrayList<Cell> cellsList = new ArrayList<Cell>();
    private LinkedBlockingQueue<Food> food = new LinkedBlockingQueue<>();
    private static final Logger log = LoggerFactory.getLogger(Playground.class);

    public Logger getLogger(){
        return log;
    }
    public boolean canEat(String threadInfo) throws InterruptedException {
        try {
            Boolean lockFood;
            Iterator<Food> foodIt = food.iterator();
            while(foodIt.hasNext()) {
                Food resource = foodIt.next();
                lockFood = resource.lock.tryLock();
                if (lockFood){
                    try{
                        log.info(threadInfo + " locked the food resource " + resource.name);
                        if(resource.getFoodUnit() > 0 ) {
                            resource.decrement();
                            log.info("Food " + resource.name + "has been eaten.");
                            return true;
                        }
                    }
                    finally
                    {
                        // Make sure to unlock so that we don't cause a deadlock
                        log.info(threadInfo + " unlocked the food resource " + resource.name);
                        resource.lock.unlock();
                    }
                }else
                    log.info(threadInfo + " tried to lock food resource " + resource.name);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public void addCell(Cell c) {
        cellsList.add(c);
    }


    public void addFood(int resources, String threadInfo) {



        for (int i = 0; i < resources; i++) {

            try {
                this.food.put(new Food(1, "resource" + i + threadInfo));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
//        int aux= resources;
//        Boolean lockFood;
//        Iterator<Food> foodIt = food.iterator();
//        while (foodIt.hasNext()) {
//            Food resource = foodIt.next();
//            lockFood = resource.lock.tryLock();
//            if (lockFood) {
//                try {
//                    log.info(threadInfo + " acquired lock for " + resource.name);
//                    if (resource.getResourceUnits() == 0 && resources >0) {
//                        resource.incrementResourceUnits(1);
//                        resources--;
//                        resource.setName( resource.name + "/"+ resources);
//                        log.info("Food " + resource.name + "has been eaten.");
//
//                    }
//                } finally {
//                    // Make sure to unlock so that we don't cause a deadlock
//                    log.info(threadInfo + " unlocked " + resource.name);
//                    resource.lock.unlock();
//                }
//            } else
//                log.info(threadInfo + " tried to lock food " + resource.name);
//
//
//        }
//
//               if (resources > 0) {
//
//                for (int i = resources; i < aux; i++) {
//
//                    this.food.add(new Food(1, "resource" + i + threadInfo));
//                }
//            }
    }

    public ArrayList<Cell> getCellsList() {
        return cellsList;
    }

    public void removeCell(Cell c){
        cellsList.remove(c);
    }

    public void startInitialThreads() {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for(Cell cell: cellsList) {
            Thread t = new Thread(cell);
            cell.thread = t;
            executor.execute(t);//calling execute method of ExecutorService
        }
        executor.shutdown();
        while (!executor.isTerminated()) {   }
    }

    public void addInitialFood(){

        for(int i=0; i<20; i++){
            try {
                this.food.put(new Food(1,"resource" + i));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void main(String[] args) {


        Playground gameOfLife = new Playground();
        gameOfLife.addInitialFood();

        for(int i=0; i<2; i++)
        {
            gameOfLife.addCell(new AsexuateCell(5, 5, "ACell" + i) );
            gameOfLife.addCell(new SexuateCell(3, 4, "SCell" + i) );

        }


        Cell.playgroundObj = gameOfLife;
        gameOfLife.startInitialThreads();


    }

}