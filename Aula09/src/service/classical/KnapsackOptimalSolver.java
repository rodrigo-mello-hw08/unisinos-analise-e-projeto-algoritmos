package service.classical;

import domain.classical.KnapsackItem;

import java.util.ArrayList;
import java.util.List;

public class KnapsackOptimalSolver {

    public List<KnapsackItem> solve(double capacity, List<KnapsackItem> items) {
        List<KnapsackItem> bestItems = new ArrayList<>();
        List<KnapsackItem> currentItems = new ArrayList<>();
        double[] bestValue = {0};

        search(items, 0, capacity, 0, 0, currentItems, bestItems, bestValue);
        return bestItems;
    }

    private void search(
            List<KnapsackItem> items,
            int itemIndex,
            double capacity,
            double currentWeight,
            double currentValue,
            List<KnapsackItem> currentItems,
            List<KnapsackItem> bestItems,
            double[] bestValue
    ) {
        if (itemIndex == items.size()) {
            if (currentValue > bestValue[0]) {
                bestValue[0] = currentValue;
                bestItems.clear();
                bestItems.addAll(currentItems);
            }
            return;
        }

        search(items, itemIndex + 1, capacity, currentWeight, currentValue,
                currentItems, bestItems, bestValue);

        KnapsackItem item = items.get(itemIndex);
        double newWeight = currentWeight + item.getWeight();
        if (newWeight <= capacity) {
            currentItems.add(item);
            search(items, itemIndex + 1, capacity, newWeight,
                    currentValue + item.getValue(), currentItems, bestItems, bestValue);
            currentItems.remove(currentItems.size() - 1);
        }
    }
}
