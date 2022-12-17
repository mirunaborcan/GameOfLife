package cells;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SexuateCell extends Cell {
    public final Lock lock = new ReentrantLock();
    private boolean divisible = false;
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
        playgroundObj.getLogger().info(this.cellName + " wants to divide!");
        //search for cells that want to divide too
        ArrayList<Cell> cellsQ = playgroundObj.getCellsList();
        Iterator<Cell> it = cellsQ.iterator();
        try {
            while (it.hasNext()) {
                Cell currentCell = it.next();
                if(!(currentCell instanceof SexuateCell))
                    continue;
                SexuateCell sexuateCell = (SexuateCell)currentCell;
                if (!sexuateCell.equals(this)) {
                    if (sexuateCell.getDivisibleStatus() && sexuateCell.hasDivided==false) {
                        boolean lockCell = sexuateCell.lock.tryLock(); // blocks the cell with which our cell tries to divide with
                        if (lockCell && sexuateCell.lockCell(this)) {
                            try {
                                playgroundObj.getLogger().info(this.cellName + " was locked by " + currentCell.cellName);
                                //make baby
                                this.divisible = false;
                                this.hasDivided = true;
                                sexuateCell.setDivisibleStatus(false);
                                sexuateCell.hasDivided = true;
                                playgroundObj.getLogger().info("Sexual division betweeen " + this.cellName + " and " + currentCell.cellName);
                                Cell c = new SexuateCell(this.timeFullInitial, this.timeStarveInitial, this.cellName + " child");
                                playgroundObj.addCell(c);
                                Thread t = new Thread(c);
                                t.start();
                            } finally {
                                this.die();
                                currentCell.die();
                                playgroundObj.removeCell(sexuateCell);
                                playgroundObj.getLogger().info("Cell died after reproduction " + this.cellName);
                                playgroundObj.removeCell(this);
                                playgroundObj.getLogger().info("Cell died after reproduction " + sexuateCell.cellName);
                                sexuateCell.unlockCell(this);
                                sexuateCell.lock.unlock();
                                currentCell.thread.interrupt();
                                this.thread.interrupt();
                            }
                        }
                    }
                }
               if(this.hasDivided == true)
                    break;
                it.next();
            }

        } catch (Exception e) {
            System.out.println("No cells left for reproducing!");
        }
    }

    public boolean lockCell(SexuateCell c) {
        if (c.lock.tryLock()) {
            return true;
        } else {
            return false;
        }
    }

    public void unlockCell(SexuateCell c) {
        c.lock.unlock();
    }

    public boolean canReproduce() {
        if ((this.numberOfMeals >= 10) && hasDivided==false ) {
            divisible = true;
            return true;
        }
        return false;
    }
}