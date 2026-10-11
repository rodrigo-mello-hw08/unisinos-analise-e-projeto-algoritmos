package domain.fractional;

import domain.classical.KnapsackItem;

public class FractionalKnapsackItem extends KnapsackItem {

    private final double fraction;

    public FractionalKnapsackItem(KnapsackItem item, double fraction) {
        super(item.getName(), item.getWeight(), item.getValue());
        this.fraction = fraction;
    }

    public double getFraction() {
        return fraction;
    }

    public double getUsedWeight() {
        return getWeight() * fraction;
    }

    public double getUsedValue() {
        return getValue() * fraction;
    }

    @Override
    public String toString() {
        return "(name=" + getName()
                + ", fraction=" + fraction
                + ", weight=" + getUsedWeight()
                + ", value=" + getUsedValue()
                + ")";
    }

}
