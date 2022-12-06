package food;

public class FoodResources {
    private int food_units = 10 ;

    public boolean hasUnits(){
        return food_units > 0;
    }

    public void decrementUnits(){
        food_units--;
    }
    public void increaseUnits(){
        food_units++;
    }
}
