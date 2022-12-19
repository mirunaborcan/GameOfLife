package cells;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SexuateCell extends Cell {

    public final Lock lock = new ReentrantLock();

    public SexuateCell(int timeUntilHungry, int timeUntilStarve, String name) {
        super(timeUntilHungry, timeUntilStarve, name);
    }


    @Override
    public void reproduce() {
        //search for cells that can reproduce too
        LinkedBlockingQueue<Cell> cellsQ = playgroundObj.getCellsList();
        Iterator<Cell> it = cellsQ.iterator();
        try {
            while (it.hasNext()) {
                Cell currentCell = it.next();
                if(!(currentCell instanceof SexuateCell sexuateCell))
                    continue;
                if (!sexuateCell.equals(this)) {
                        boolean lockCell = sexuateCell.lock.tryLock(); // blocks the cell with which our cell tries to divide with
                        if (lockCell && this.lock.tryLock()) {
                            try {
                                playgroundObj.getLogger().info(this.cellName + " was locked by " + currentCell.cellName);

                                //reproduce
                                playgroundObj.getLogger().info("Sexual division betweeen " + this.cellName + " and " + currentCell.cellName);
                                Cell c = new SexuateCell(this.timeFull, this.timeStarve, this.cellName + " child");
                                playgroundObj.addCell(c);
                                Thread t = new Thread(c);
                                t.start();

                                //make them hungry
                                sexuateCell.numberOfMeals = 0;
                                this.numberOfMeals = 0;
                            } finally {
                                this.lock.unlock();
                                sexuateCell.lock.unlock();
                            }
                            return;
                        }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean canReproduce() {
        return this.numberOfMeals >= 10;
    }
}