package scheduler.Generators;


import java.util.List;

public interface TaskScheduler {
    public String generateStringValue();

    public List<Integer> generateValues();

    public boolean exist(int val);
}
