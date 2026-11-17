package domain.classical.dynamic;

import domain.Knapsack;
import domain.classical.greedy.KnapsackItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClassicKnapsack extends Knapsack {
    private final List<KnapsackItem> selectedItems;

    public ClassicKnapsack(double maxCapacity, List<KnapsackItem> selectedItems) {
        super(maxCapacity);
        this.selectedItems = new ArrayList<>(selectedItems);
        for (KnapsackItem item : selectedItems) {
            usedCapacity += item.getWeight();
            totalValue += item.getValue();
        }
    }

    public List<KnapsackItem> getSelectedItems() {
        return Collections.unmodifiableList(selectedItems);
    }

    public double getUsedCapacity() {
        return usedCapacity;
    }

    public double getTotalValue() {
        return totalValue;
    }
}
