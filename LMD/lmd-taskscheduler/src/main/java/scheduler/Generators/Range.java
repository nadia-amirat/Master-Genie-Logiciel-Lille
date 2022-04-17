package scheduler.Generators;

import java.util.ArrayList;
import java.util.List;

public class Range implements TaskScheduler {
    public int min;
    public int max;
    public int step;

    public List<Integer> values = new ArrayList<>();
    public String value = null;

    public Range(int min, int max, int step) {
        this.max = max;
        this.min = min;
        this.step = step;
        this.generateValues();
        this.generateStringValue();
    }

    @Override
    public String generateStringValue() {
        this.value = String.format("[%s-%s]/%s", min, max, step);
        return this.value;
    }

    @Override
    public List<Integer> generateValues() { //j'ai un doute i peut être supp à max ici
        for (int i = min; i <= max; i += step) {
            this.values.add(i);
        }
        return this.values;
    }

    @Override
    public boolean exist(int val) {
        if (this.values == null || this.values.contains(val))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return this.value;

    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getStep() {
        return step;
    }
}
