package service.fractional;

import domain.Knapsack;
import domain.classical.KnapsackItem;
import domain.fractional.FractionalKnapsack;
import service.KnapsackProblem;
import util.ExecutionMeasurement;
import util.ExecutionTimer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KnapsackFractionalProblem implements KnapsackProblem {

    private static final int MAX_DECISIONS_TO_PRINT = 10;

    private final double maxCapacity;

    public KnapsackFractionalProblem(double maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Override
    public Knapsack resolve(List<KnapsackItem> items) {
        FractionalKnapsack knapsack = new FractionalKnapsack(maxCapacity);
        knapsack.fill(items);
        return knapsack;
    }

    public Knapsack evaluateAndPrint(String testName, List<KnapsackItem> items) {
        System.out.println("\n--- " + testName + ": mochila fracionária ---");

        ExecutionMeasurement<Knapsack> measurement = ExecutionTimer.measure(() -> resolve(new ArrayList<>(items)));
        Knapsack result = measurement.getResult();

        List<KnapsackItem> orderedItems = new ArrayList<>(items);
        orderedItems.sort(
                Comparator.<KnapsackItem>comparingDouble(
                        item -> item.getValue() / item.getWeight()).reversed()
        );

        double usedWeight = 0;
        double totalValue = 0;
        int decisionsPrinted = 0;
        int selectedCount = 0;

        for (KnapsackItem item : orderedItems) {
            double remainingCapacity = maxCapacity - usedWeight;
            if (remainingCapacity <= 0) {
                break;
            }

            double fraction = Math.min(1.0, remainingCapacity / item.getWeight());
            if (fraction <= 0) {
                continue;
            }

            usedWeight += item.getWeight() * fraction;
            totalValue += item.getValue() * fraction;
            selectedCount++;

            decisionsPrinted = printPrecision(items, item, decisionsPrinted, fraction);
        }

        printOmittedDecisionCount(items.size(), decisionsPrinted);
        printSummary(result, "Itens aproveitados integral ou parcialmente", selectedCount,
                usedWeight, maxCapacity, totalValue, measurement.getElapsedMilliseconds());
        return result;
    }

    private int printPrecision(List<KnapsackItem> items, KnapsackItem item, int decisionsPrinted, double fraction) {
        if (shouldPrintDecision(items.size(), decisionsPrinted)) {
            System.out.printf(
                    "Selecionado: %s (fração=%.4f, peso usado=%.2f, valor obtido=%.2f)%n",
                    item.getName(), fraction,
                    item.getWeight() * fraction,
                    item.getValue() * fraction
            );
            decisionsPrinted++;
        }
        return decisionsPrinted;
    }

    private boolean shouldPrintDecision(int itemCount, int decisionsPrinted) {
        return itemCount <= MAX_DECISIONS_TO_PRINT || decisionsPrinted < MAX_DECISIONS_TO_PRINT;
    }

    private void printOmittedDecisionCount(int itemCount, int decisionsPrinted) {
        if (itemCount > MAX_DECISIONS_TO_PRINT
                && decisionsPrinted >= MAX_DECISIONS_TO_PRINT) {
            System.out.println("As demais decisões foram omitidas para manter a saída legível.");
        }
    }
}
