package service.classical;

import domain.Knapsack;
import domain.classical.ClassicKnapsack;
import domain.classical.KnapsackItem;
import service.KnapsackProblem;
import util.ExecutionMeasurement;
import util.ExecutionTimer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class KnapsackClassicProblem implements KnapsackProblem {

    private static final int MAX_DECISIONS_TO_PRINT = 10;

    private final double maxCapacity;

    public KnapsackClassicProblem(double maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @Override
    public Knapsack resolve(List<KnapsackItem> items) {
        ClassicKnapsack knapsack = new ClassicKnapsack(maxCapacity);
        knapsack.fill(new ArrayList<>(items));
        return knapsack;
    }

    public Knapsack evaluateAndPrint(String testName, List<KnapsackItem> items) {
        System.out.println("\n--- " + testName + ": mochila clássica (0/1) ---");

        ExecutionMeasurement<Knapsack> measurement = ExecutionTimer.measure(() -> resolve(items));
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
            boolean selected = usedWeight + item.getWeight() <= maxCapacity;

            if (selected) {
                usedWeight += item.getWeight();
                totalValue += item.getValue();
                selectedCount++;
            }

            decisionsPrinted = printPrecision(items, item, decisionsPrinted, selected, usedWeight);
        }

        printOmittedDecisionCount(items.size(), decisionsPrinted);
        printSummary(result, "Itens selecionados", selectedCount, usedWeight,
                maxCapacity, totalValue, measurement.getElapsedMilliseconds());
        return result;
    }

    private int printPrecision(List<KnapsackItem> items, KnapsackItem item, int decisionsPrinted, boolean selected, double usedWeight) {
        if (shouldPrintDecision(items.size(), decisionsPrinted)) {
            if (selected) {
                System.out.printf(
                        "Selecionado: %s (peso=%.2f, valor=%.2f)%n",
                        item.getName(), item.getWeight(), item.getValue()
                );
            } else {
                System.out.printf(
                        "Ignorado: %s (não cabe; peso=%.2f, capacidade restante=%.2f)%n",
                        item.getName(), item.getWeight(), maxCapacity - usedWeight
                );
            }
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
