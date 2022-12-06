package cells;

abstract class Cell {
    private int T_full;
    private int T_starve;
    private int number_of_meals;


    public void eat(){
        number_of_meals++;
    }
    public void die(){

    }
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
    public void reproduce(){
        System.out.println("reproducing");
    }
}
