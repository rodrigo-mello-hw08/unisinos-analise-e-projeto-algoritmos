package service;

import domain.Knapsack;
import domain.classical.KnapsackItem;

import java.util.List;

public interface KnapsackProblem {
    Knapsack resolve(List<KnapsackItem> items);

    default void printSummary(
            Knapsack result,
            String itemCountLabel,
            int itemCount,
            double usedCapacity,
            double maxCapacity,
            double totalValue,
            double elapsedMilliseconds
    ) {
        System.out.println("\n> Tipo de resultado retornado: " + result.getClass().getSimpleName());
        System.out.printf("> %s: %d%n", itemCountLabel, itemCount);
        System.out.printf("> Peso utilizado: %.2f / %.2f%n", usedCapacity, maxCapacity);
        System.out.printf("> Valor total: %.2f%n", totalValue);
        System.out.printf("> Tempo de execução: %.3f ms%n", elapsedMilliseconds);
    }
}
