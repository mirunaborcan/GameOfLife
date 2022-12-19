package cells;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SexuateCell extends Cell {
    public final Lock lock = new ReentrantLock();
    private boolean divisible = true;
    public boolean hasDivided = false;

    public boolean getDivisibleStatus() {

        return divisible;
    }

    public void setDivisibleStatus(boolean status) {
        this.divisible = status;
    }

    public SexuateCell(int timeUntilHungry, int timeUntilStarve, String name) {
        super(timeUntilHungry, timeUntilStarve, name);
    }



    @Override
    public void reproduce() {
        //playgroundObj.getLogger().info(this.cellName + " wants to divide!");
        //search for cells that want to divide too
        LinkedBlockingQueue<Cell> cellsQ = playgroundObj.getCellsList();
        Iterator<Cell> it = cellsQ.iterator();
        try {
            while (it.hasNext()) {
                Cell currentCell = it.next();
                if(!(currentCell instanceof SexuateCell))
                    continue;
                SexuateCell sexuateCell = (SexuateCell)currentCell;
                if (!sexuateCell.equals(this)) {
                    if (true) {
                        boolean lockCell = sexuateCell.lock.tryLock(); // blocks the cell with which our cell tries to divide with
                        if (lockCell && this.lock.tryLock()) {
                            sexuateCell.setDivisibleStatus(false);
                            try {
                                playgroundObj.getLogger().info(this.cellName + " was locked by " + currentCell.cellName);
                                //make baby
                                //this.divisible = false;
                                //this.hasDivided = true;

                               // sexuateCell.hasDivided = true;
                                playgroundObj.getLogger().info("Sexual division betweeen " + this.cellName + " and " + currentCell.cellName);
                                Cell c = new SexuateCell(this.timeFullInitial, this.timeStarveInitial, this.cellName + " child");
                                playgroundObj.addCell(c);
                                Thread t = new Thread(c);
                                t.start();
                                sexuateCell.numberOfMeals = 0;
                                this.numberOfMeals = 0;
                            } finally {
                                //this.die();
                                //currentCell.die();
                               // playgroundObj.removeCell(sexuateCell);
                                //playgroundObj.removeCell(this);
                               // sexuateCell.unlockCell(this);
                                this.lock.unlock();
                                sexuateCell.lock.unlock();
                                //currentCell.thread.interrupt();
                                //this.thread.interrupt();
                                //playgroundObj.getLogger().info("Cell died after reproduction " + this.cellName);
                               // playgroundObj.getLogger().info("Cell died after reproduction " + sexuateCell.cellName);
                            }
                            return;
                        }
                    }
                }

                //it.hasNext();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean lockCell(SexuateCell c) {
        return c.lock.tryLock();
    }

    public void unlockCell(SexuateCell c) {
        c.lock.unlock();
    }

    public boolean canReproduce() {
        if ((this.numberOfMeals >= 10)) {
            divisible = true;
            return true;
        }
        return false;
    }
}