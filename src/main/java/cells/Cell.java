package cells;


import com.sun.tools.javac.Main;
import playground.Playground;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class Cell implements Runnable {
    public static Playground playgroundObj;
    protected int numberOfMeals;
    private long timeSinceLastEaten;
    protected final int timeFull;
    protected final int timeStarve;
    public AtomicBoolean cellAlive = new AtomicBoolean(true);
    public Thread thread;
    public String cellName;
    public Cell(int timeFullInitial, int timeStarveInitial, String name) {
        this.numberOfMeals = 0;
        this.timeFull = timeFullInitial;
        this.timeStarve= timeStarveInitial;
        this.cellName = name;
    }

    public void resetTimers(){
        this.timeSinceLastEaten = System.currentTimeMillis();
    }

    public void addCellToArray(Cell c){
        playgroundObj.addCell(c);
    }

    public void die() {  cellAlive.set(false);}

    public boolean canReproduce() { return this.numberOfMeals >= 10; }
    public abstract void reproduce();

    public void eat(Playground playground) throws InterruptedException {
        if (playground.canEat(cellName)) {
            numberOfMeals++;
            resetTimers(); //time for hungry and starve are reset

        } else { // if the cell hasn't found available food resources
            if(System.currentTimeMillis() - this.timeSinceLastEaten > timeFull* 1000L) {
                    if (System.currentTimeMillis() - this.timeSinceLastEaten > (timeFull* 1000L + timeStarve* 1000L)) {
                        playgroundObj.getLogger().info(" !!!!!! CELL " + this.cellName + " DIED !!!!!!!");
                        die();
                        //generate new food resources
                        int randRes = ThreadLocalRandom.current().nextInt(1, 5);
                        playgroundObj.getLogger().info(" Cell  " + this.cellName + " generates " + randRes + " food units after dying");
                        playgroundObj.addFood(randRes, cellName);
                        Thread.currentThread().interrupt();
                }
            }
        }
    }


    @Override
    public void run()  {
        this.timeSinceLastEaten = System.currentTimeMillis();
        // if a cell is alive, it can reproduce and eat
        while (cellAlive.get()) {
            try {
                if(cellAlive.get() && canReproduce())
                    reproduce();
                eat(playgroundObj);
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