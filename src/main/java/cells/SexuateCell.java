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
                // if there is another sexuate cell
                if(!(currentCell instanceof SexuateCell sexuateCell))
                    continue;
                // and different from itself
                if (!sexuateCell.equals(this)) {
                    // locks the cell chosen to reproduce with
                        boolean lockCell = sexuateCell.lock.tryLock();
                        // locks also the current sexuate cell
                        if (lockCell && this.lock.tryLock()) {
                            try {
                                playgroundObj.getLogger().info(this.cellName + " was locked by " + currentCell.cellName);

                                //reproduce
                                playgroundObj.getLogger().info("Sexual division betweeen " + this.cellName + " and " + currentCell.cellName);
                                Cell c = new SexuateCell(this.timeFull, this.timeStarve, this.cellName + " child");
                                // add the newborn child into the queue of cells
                                playgroundObj.addCell(c);
                                Thread t = new Thread(c);
                                t.start();

                                //make the parents hungry
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