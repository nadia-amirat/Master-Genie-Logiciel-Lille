package scheduler.Generators;

import java.util.List;

public class Sequence implements TaskScheduler {

    public String value;
    public List<Integer> values;


    public Sequence(List<Integer> values) {
        this.values = values;
        generateStringValue();

    }

    @Override
    public String generateStringValue() {
        if (this.values == null) {
            this.value = "*";
            return this.value;
        }

        String res = "";
        int i;
        for (i = 0; i < this.values.size() - 1; i++) {
            res += this.values.get(i) + ",";
        }
        res += this.values.get(i);
        this.value = res;
        return res;
    }

    @Override
    public List<Integer> generateValues() {
        return this.values;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public boolean exist(int val) {

        if (this.values == null || this.values.contains(val))
            return true;
        return false;
    }
}
