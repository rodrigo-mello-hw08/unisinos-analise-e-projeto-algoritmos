package service.classical.divideandconquer;

import domain.Knapsack;
import domain.classical.divideandconquer.ClassicKnapsack;
import domain.classical.greedy.KnapsackItem;
import service.KnapsackProblem;

import java.util.ArrayList;
import java.util.List;

public class KnapsackClassicDivideAndConquerProblem implements KnapsackProblem {
    private final double maxCapacity;

    public KnapsackClassicDivideAndConquerProblem(double maxCapacity) {
        if (maxCapacity < 0) {
            throw new IllegalArgumentException("A capacidade não pode ser negativa.");
        }
        this.maxCapacity = maxCapacity;
    }

    @Override
    public Knapsack resolve(List<KnapsackItem> items) {
        if (items == null) {
            throw new IllegalArgumentException("A lista de itens não pode ser nula.");
        }
        List<KnapsackItem> selectedItems = solve(items, 0, maxCapacity).selectedItems;
        return new ClassicKnapsack(maxCapacity, selectedItems);
    }

    private Solution solve(List<KnapsackItem> items, int itemIndex, double remainingCapacity) {
        if (itemIndex >= items.size()) {
            return Solution.empty();
        }
        KnapsackItem currentItem = items.get(itemIndex);
        Solution bestSolWithoutCurrentItem = solveWithoutCurrentItem(items, itemIndex, remainingCapacity);
        Solution bestSolWithCurrentItem = solveWithCurrentItem(items, itemIndex, remainingCapacity, currentItem);

        return bestSolWithCurrentItem.totalValue > bestSolWithoutCurrentItem.totalValue
                ? bestSolWithCurrentItem
                : bestSolWithoutCurrentItem;
    }

    private Solution solveWithoutCurrentItem(List<KnapsackItem> items, int itemIndex, double remainingCapacity) {
        return solve(items, itemIndex + 1, remainingCapacity);
    }

    private Solution solveWithCurrentItem(List<KnapsackItem> items, int itemIndex,
                                          double remainingCapacity, KnapsackItem currentItem) {
        if (currentItem.getWeight() > remainingCapacity) {
            return Solution.empty();
        }

        double remainingCapacityAfterTakingCurrentItem = remainingCapacity - currentItem.getWeight();
        Solution bestSolutionForRemainingItems = solve(
                items, itemIndex + 1, remainingCapacityAfterTakingCurrentItem);

        List<KnapsackItem> selectedItemsWithCurrentItem = new ArrayList<>();
        selectedItemsWithCurrentItem.add(currentItem);
        selectedItemsWithCurrentItem.addAll(bestSolutionForRemainingItems.selectedItems);

        return new Solution(
                currentItem.getValue() + bestSolutionForRemainingItems.totalValue,
                selectedItemsWithCurrentItem);
    }

    private static final class Solution {
        private final double totalValue;
        private final List<KnapsackItem> selectedItems;

        private Solution(double totalValue, List<KnapsackItem> selectedItems) {
            this.totalValue = totalValue;
            this.selectedItems = selectedItems;
        }

        private static Solution empty() {
            return new Solution(0, new ArrayList<>());
        }
    }
}
