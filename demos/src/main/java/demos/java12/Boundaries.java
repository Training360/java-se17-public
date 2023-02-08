package demos.java12;

public class Boundaries {

    private Employee min;

    private Employee max;

    public Boundaries(Employee min, Employee max) {
        this.min = min;
        this.max = max;
    }

    public Employee getMin() {
        return min;
    }

    public void setMin(Employee min) {
        this.min = min;
    }

    public Employee getMax() {
        return max;
    }

    public void setMax(Employee max) {
        this.max = max;
    }
}
