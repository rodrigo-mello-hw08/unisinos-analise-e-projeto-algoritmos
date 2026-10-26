package domain.classical;

public class KnapsackItem {
    private final String name;
    private final double weight;
    private final double value;

    public KnapsackItem(String name, double weight, double value) {
        this.name = name;
        this.weight = weight;
        this.value = value;
    }

    public double getWeight() {
        return weight;
    }

    public double getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "(name=" + name
                + ", weight=" + weight
                + ", value=" + value
                +")";
    }
}
