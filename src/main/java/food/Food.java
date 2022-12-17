package food;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Food {
    private int foodUnit;
    public String name;
    public final Lock lock = new ReentrantLock();

    public void setName(String name) {
        this.name = name;
    }

    public Food(int foodUnit, String name) {
        this.foodUnit = foodUnit;
        this.name = name;
    }

    public int getFoodUnit() {
        return foodUnit;
    }

    public void decrement() {
        this.foodUnit--;
    }

    public void increment(int units) {
        this.foodUnit += units;
    }

    @Override
    public String toString() {
        return "Food{" +
                "foodUnit=" + foodUnit +
                ", name='" + name + '\'' +
                '}';
    }

}