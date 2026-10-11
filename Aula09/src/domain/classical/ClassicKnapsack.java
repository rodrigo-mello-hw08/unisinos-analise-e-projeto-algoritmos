package domain.classical;

import domain.Knapsack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ClassicKnapsack extends Knapsack {
    private final List<KnapsackItem> selectedItems = new ArrayList<>();

    public ClassicKnapsack(double maxCapacity) {
        super(maxCapacity);
    }

    public void fill(List<KnapsackItem> items) {
        usedCapacity = 0;
        items.sort(Comparator.comparing(KnapsackItem::getValue).reversed());
        while (usedCapacity <= maxCapacity) {
            double remainingCapacity =  maxCapacity - usedCapacity;
            KnapsackItem nextItem = findNextItem(items, remainingCapacity);
            if (nextItem == null) {
                break;
            }

            selectedItems.add(nextItem);
            totalValue += nextItem.getValue();
            usedCapacity += nextItem.getWeight();
            items.remove(nextItem);
        }

    }

    private KnapsackItem findNextItem(List<KnapsackItem> items, double remainingCapacity) {
        for (KnapsackItem currentItem : items) {
            if (currentItem.getWeight() <= remainingCapacity) {
                return currentItem;
            }
        }
        return null;
    }

    public double getTotalValue() {
        return totalValue;
    }

    @Override
    public String toString() {
        String items = selectedItems.stream().map(KnapsackItem::toString).collect(Collectors.joining(", \n"));
        return "Classic Knapsack: "
                + "\n> maxCapacity="+ maxCapacity
                + ", \n> capacityUsed=" + usedCapacity
                + ", \n> totalValue=" + totalValue
                + ", \n> selectedItems[\n" + items
                +"\n]";
    }
}
