package debugger;
public class ToDebug {
    int cpt; //test method 10 receveir-variables
    String name;//test method 10 receveir-variables
    public static void main(String[] args) {
        ToDebug o = new ToDebug();
        int y = 0;
        for (int i = 0; i < 10; i++) {
            y = o.sum(y,i); //tester sender
        }
    }
    public  int sum(int x, int y){
        return x+y;
    }

    }
