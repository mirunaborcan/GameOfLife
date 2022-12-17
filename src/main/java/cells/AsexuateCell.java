package cells;

public class AsexuateCell extends Cell {

    public AsexuateCell(int timeFull, int timeStarve, String name) {
        super(timeFull, timeStarve, name);
    }

    @Override
    public void reproduce() {

        Cell cell1 = new AsexuateCell(this.timeFullInitial,this.timeStarveInitial,this.cellName+"child1");
        Cell cell2 = new AsexuateCell(this.timeFullInitial,this.timeStarveInitial,this.cellName+"child2");
        this.die(); // this cell that divided is no longer alive
        playgroundObj.getLogger().info(" CELL " + this.cellName + " HAS REPRODUCED! ");
        Thread t1 = new Thread(cell1);
        Thread t2 = new Thread(cell2);

        addCellToArray(cell1);
        addCellToArray(cell2);

        t1.start();
        t2.start();
    }

}

