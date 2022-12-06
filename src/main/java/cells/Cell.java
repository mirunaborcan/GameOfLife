package cells;

abstract class Cell implements Runnable{

    protected int T_full;
    protected int T_starve;
    protected int number_of_meals;
    protected String cell_name;

    private int T_full_curr;
    private int T_starve_curr;

    public Cell (int t_full, int t_starve, String name) {

        this.T_full = t_full;
        this.T_starve = t_starve;
        this.cell_name = name;
        setTime();
    }

    public void setTime(){
        this.T_starve_curr = T_starve;
        this.T_full_curr = T_full;
    }
    public void eat(){
        number_of_meals++;
    }
    public void die(){}
    public int getFullTime(){
        return T_full;
    }
    public int getStarvationTime(){
        return T_starve;
    }
    public int getNumberOfMeals(){
        return number_of_meals;
    }
    public void incrementMeals(){
        number_of_meals++;
    }
    public void setNumberOfMeals(int nr){
        number_of_meals=nr;
    }
    public boolean reproduce(){

        if(this.number_of_meals >= 10){
            return true;
        }
        return false;
    }
    @Override public void run()
    {

    }
}
