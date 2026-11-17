package runner;

import domain.Knapsack;
import domain.classical.dynamic.ClassicKnapsack;
import domain.classical.greedy.KnapsackItem;
import service.classical.dynamic.KnapsackClassicDynamicProblem;
import service.classical.greedy.KnapsackClassicProblem;
import util.ExecutionMeasurement;
import util.ExecutionTimer;
import util.CommonPrints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ComparisonRunner {
    private static final int MAX_ITEMS_TO_PRINT = 10;

    public static void run() {
        Locale.setDefault(Locale.US);
        runGreedyCounterexamples();
        compareLargeInstanceExecutionTime();
    }

    private static void runGreedyCounterexamples() {
        CommonPrints.printHeadline("Cenários em que a programação dinâmica encontra o ótimo");
        compareScenario("Cenário 1", 4, Arrays.asList(
                new KnapsackItem("A", 3, 5),
                new KnapsackItem("B", 2, 3),
                new KnapsackItem("C", 2, 3)
        ), 6);
        compareScenario("Cenário 2", 50, Arrays.asList(
                new KnapsackItem("A", 10, 60),
                new KnapsackItem("B", 20, 100),
                new KnapsackItem("C", 30, 120)
        ), 220);
        compareScenario("Cenário 3", 10, Arrays.asList(
                new KnapsackItem("A", 6, 30),
                new KnapsackItem("B", 5, 24),
                new KnapsackItem("C", 5, 24)
        ), 48);
    }

    private static void compareScenario(String name, double capacity,
                                        List<KnapsackItem> items, double expectedOptimalValue) {
        Knapsack greedyResult = new KnapsackClassicProblem(capacity).resolve(items);
        Knapsack dynamicResult = new KnapsackClassicDynamicProblem(capacity).resolve(items);
        ClassicKnapsack dynamicKnapsack = (ClassicKnapsack) dynamicResult;

        System.out.printf("\n%s | capacidade: %.2f%n", name, capacity);
        CommonPrints.printSeparator();
        System.out.println("Itens de entrada:");
        for (KnapsackItem item : items) {
            System.out.printf("  %s (peso=%.2f, valor=%.2f, valor/peso=%.2f)%n",
                    item.getName(), item.getWeight(), item.getValue(), item.getValue() / item.getWeight());
        }
        System.out.printf("\nGuloso: valor=%.2f%n", ((domain.classical.greedy.ClassicKnapsack) greedyResult).getTotalValue());
        System.out.println("  Itens escolhidos: " + greedyResult);

        System.out.printf("\nDinâmico: valor=%.2f, peso=%.2f%n",
                dynamicKnapsack.getTotalValue(), dynamicKnapsack.getUsedCapacity());
        CommonPrints.printSelectedItems(dynamicKnapsack.getSelectedItems(), MAX_ITEMS_TO_PRINT);

        System.out.printf("Ótimo esperado: %.2f | Dinâmico correto: %s | Guloso subótimo: %s%n",
                expectedOptimalValue,
                dynamicKnapsack.getTotalValue() == expectedOptimalValue ? "sim" : "não",
                ((domain.classical.greedy.ClassicKnapsack) greedyResult).getTotalValue() < expectedOptimalValue ? "sim" : "não");
    }

    private static void compareLargeInstanceExecutionTime() {
        CommonPrints.printHeadline("Comparação de tempo no lote grande");
        double capacity = 500;
        List<KnapsackItem> items = KnapsackInstances.createLargeInstance();

        ExecutionMeasurement<Knapsack> greedyMeasurement = ExecutionTimer.measure(
                () -> new KnapsackClassicProblem(capacity).resolve(items));
        ExecutionMeasurement<Knapsack> dynamicMeasurement = ExecutionTimer.measure(
                () -> new KnapsackClassicDynamicProblem(capacity).resolve(items));

        domain.classical.greedy.ClassicKnapsack greedyResult =
                (domain.classical.greedy.ClassicKnapsack) greedyMeasurement.getResult();
        ClassicKnapsack dynamicResult = (ClassicKnapsack) dynamicMeasurement.getResult();

        System.out.printf("Instância: %d itens | capacidade: %.2f%n", items.size(), capacity);
        System.out.printf("Guloso: valor=%.2f | tempo=%.3f ms%n",
                greedyResult.getTotalValue(), greedyMeasurement.getElapsedMilliseconds());
        System.out.printf("Dinâmico: valor=%.2f | tempo=%.3f ms%n",
                dynamicResult.getTotalValue(), dynamicMeasurement.getElapsedMilliseconds());
    }

}
