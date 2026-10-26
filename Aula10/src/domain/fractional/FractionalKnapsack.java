package domain.fractional;

import domain.Knapsack;
import domain.classical.KnapsackItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FractionalKnapsack extends Knapsack {

    private final List<FractionalKnapsackItem> selectedItems = new ArrayList<>();

    public FractionalKnapsack(double maxCapacity) {
        super(maxCapacity);
    }

    public void fill(List<KnapsackItem> items) {
        usedCapacity = 0;
        totalValue = 0;
        selectedItems.clear();

        List<KnapsackItem> orderedItems = new ArrayList<>(items);

        orderedItems.sort(Comparator.<KnapsackItem>comparingDouble(item -> item.getValue() / item.getWeight()).reversed());

        for (KnapsackItem item : orderedItems) {

            if (usedCapacity >= maxCapacity) {
                break;
            }

            double remainingCapacity =
                    maxCapacity - usedCapacity;

            double fraction;

            if (item.getWeight() <= remainingCapacity) {
                fraction = 1.0;
            } else {
                fraction =
                        remainingCapacity / item.getWeight();
            }

            selectedItems.add(new FractionalKnapsackItem(item, fraction));

            usedCapacity += item.getWeight() * fraction;
            totalValue += item.getValue() * fraction;
        }
    }

    @Override
    public String toString() {
        String items = selectedItems.stream()
                .map(FractionalKnapsackItem::toString)
                .collect(Collectors.joining(", \n"));

        return "Fractional Knapsack:"
                + "\n> maxCapacity=" + maxCapacity
                + ", \n> usedCapacity=" + usedCapacity
                + ", \n> totalValue=" + totalValue
                + ", \n> selectedItems[\n" + items
                + "\n]";
    }
}
