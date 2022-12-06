package cells;

public class SexuateCell extends Cell{

    public SexuateCell (int t_full, int t_starve, String name){
        super(t_full,t_starve,name);
    }

    public void run(){}
    public void setState(){}
    public void getState(){}
    public boolean readyToMultiply(){
        return true;
    }
}
