package service.classical.divideandconquer;

import domain.Knapsack;
import domain.classical.divideandconquer.ClassicKnapsack;
import domain.classical.greedy.KnapsackItem;
import service.KnapsackProblem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KnapsackClassicDivideAndConquerProblem implements KnapsackProblem {
    private final double maxCapacity;

    public KnapsackClassicDivideAndConquerProblem(double maxCapacity) {
        if (maxCapacity < 0 || !Double.isFinite(maxCapacity)) {
            throw new IllegalArgumentException("A capacidade deve ser finita e não negativa.");
        }
        this.maxCapacity = maxCapacity;
    }

    @Override
    public Knapsack resolve(List<KnapsackItem> items) {
        if (items == null) {
            throw new IllegalArgumentException("A lista de itens não pode ser nula.");
        }

        List<KnapsackItem> orderedItems = new ArrayList<>(items);
        orderedItems.sort(Comparator.comparingDouble(
                (KnapsackItem item) -> item.getValue() / item.getWeight()).reversed());

        SearchState searchState = new SearchState();
        search(orderedItems, 0, maxCapacity, 0, new ArrayList<>(), searchState);
        return new ClassicKnapsack(maxCapacity, searchState.bestSelectedItems);
    }

    private void search(List<KnapsackItem> items, int itemIndex,
                        double remainingCapacity, double currentValue,
                        List<KnapsackItem> currentSelectedItems,
                        SearchState searchState) {

        if (currentValue > searchState.bestValue) {
            searchState.bestValue = currentValue;
            searchState.bestSelectedItems = new ArrayList<>(currentSelectedItems);
        }

        boolean allItemsHaveBeenConsidered = itemIndex >= items.size();
        double maximumReachableValue = calculateUpperBound(items, itemIndex, remainingCapacity, currentValue);
        boolean upperBoundCannotImproveBestSolution = maximumReachableValue <= searchState.bestValue;
        if (allItemsHaveBeenConsidered || upperBoundCannotImproveBestSolution) {
            return;
        }

        exploreBranches(items, itemIndex, remainingCapacity,
                currentValue, currentSelectedItems, searchState);
    }

    private void exploreBranches(List<KnapsackItem> items, int itemIndex,
                                 double remainingCapacity, double currentValue,
                                 List<KnapsackItem> currentSelectedItems, SearchState searchState) {

        KnapsackItem currentItem = items.get(itemIndex);
        boolean currentItemFits = currentItem.getWeight() <= remainingCapacity;
        if (currentItemFits) {
            currentSelectedItems.add(currentItem);
            search(items,
                    itemIndex + 1,
                    remainingCapacity - currentItem.getWeight(),
                    currentValue + currentItem.getValue(),
                    currentSelectedItems,
                    searchState);
            currentSelectedItems.removeLast();
        }

        search(items, itemIndex + 1, remainingCapacity, currentValue, currentSelectedItems, searchState);
    }

    private double calculateUpperBound(List<KnapsackItem> items, int itemIndex,
                                       double remainingCapacity, double currentValue) {

        double upperBound = currentValue;
        double capacityAvailable = remainingCapacity;

        for (int i = itemIndex; i < items.size() && capacityAvailable > 0; i++) {
            KnapsackItem item = items.get(i);
            if (item.getValue() <= 0) {
                continue;
            }

            if (item.getWeight() <= capacityAvailable) {
                upperBound += item.getValue();
                capacityAvailable -= item.getWeight();
            } else {
                upperBound += item.getValue() * (capacityAvailable / item.getWeight());
                break;
            }
        }
        return upperBound;
    }

    private static final class SearchState {
        private double bestValue;
        private List<KnapsackItem> bestSelectedItems = new ArrayList<>();
    }
}
