package cells;

import playground.Playground;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Cell implements Runnable {
    public static Playground playgroundObj; //the actual game



    protected int numberOfMeals;

    protected int timeFullInitial;
    protected int timeStarveInitial;

    private int timeFull;
    private int timeStarve;

    public AtomicBoolean cellAlive = new AtomicBoolean(true);

    public Thread thread;

    public String cellName;


    public Cell(int timeFullInitial, int timeStarveInitial, String name) {
        this.numberOfMeals = 0;
        this.timeFullInitial = timeFullInitial;
        this.timeStarveInitial = timeStarveInitial;
        this.cellName = name;
        resetTimers();
    }

    public void resetTimers(){
        this.timeFull = this.timeFullInitial;
        this.timeStarve = this.timeStarveInitial;
    }

    public void addCellToArray(Cell c){
        playgroundObj.addCell(c);
    }

    public void die() {
        cellAlive.set(false);
    }

    public boolean canReproduce() {

        return this.numberOfMeals >= 10 ? true : false;

    }
    public abstract void reproduce();

    public void eat(Playground playground) throws InterruptedException {
        if (playground.canEat(cellName)) {
            numberOfMeals++;
            playgroundObj.getLogger().info(" - Cell: " + this.cellName + " has eaten. ");
            resetTimers(); //time for hungry&starve are reset

        } else { // if the cell hasn't found available food resources
            timeFull--;
            if (timeFull < 0) {
                timeStarve--;
                if (timeStarve == 0) {
                    playgroundObj.getLogger().info(" CELL " + this.cellName + " DIED!!!!!!!");
                    die();
                    generateNewResources();
                }
            }
        }
    }

    private void generateNewResources() {
        int randRes = ThreadLocalRandom.current().nextInt(1, 5);
        playgroundObj.getLogger().info(" Cell  " + this.cellName + " generates " + randRes + " resources! ");
        playgroundObj.addFood(randRes, cellName);
        Thread.currentThread().interrupt();
    }

    @Override
    public void run()  {

            while (cellAlive.get()) {
                try {
                    eat(playgroundObj);
                    if(cellAlive.get() && canReproduce()) reproduce();
                } catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                    playgroundObj.getLogger().info("Thread interrupted");
                }
            }

    }
    public String toString() {
        return this.cellName;
    }
}