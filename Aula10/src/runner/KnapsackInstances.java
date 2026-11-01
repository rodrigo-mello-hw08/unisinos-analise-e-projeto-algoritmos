package runner;

import domain.classical.greedy.KnapsackItem;

import java.util.ArrayList;
import java.util.List;

public final class KnapsackInstances {
    private KnapsackInstances() {
    }

    public static List<KnapsackItem> createSmallInstance() {
        List<KnapsackItem> items = new ArrayList<>();
        items.add(new KnapsackItem("A", 2, 10));
        items.add(new KnapsackItem("B", 3, 14));
        items.add(new KnapsackItem("C", 5, 25));
        items.add(new KnapsackItem("D", 4, 16));
        items.add(new KnapsackItem("E", 1, 3));
        return items;
    }

    public static List<KnapsackItem> createMediumInstance() {
        return createGeneratedInstance(100, 20, 5, 17, 100);
    }

    public static List<KnapsackItem> createLargeInstance() {
        return createGeneratedInstance(2_000, 50, 10, 37, 500);
    }

    private static List<KnapsackItem> createGeneratedInstance(int itemCount, int weightModulo,
                                                               int valueOffset, int valueMultiplier,
                                                               int valueModulo) {
        List<KnapsackItem> items = new ArrayList<>();
        for (int itemNumber = 1; itemNumber <= itemCount; itemNumber++) {
            double weight = 1 + (itemNumber % weightModulo);
            double value = valueOffset + ((itemNumber * valueMultiplier) % valueModulo);
            items.add(new KnapsackItem("Item-" + itemNumber, weight, value));
        }
        return items;
    }
}
