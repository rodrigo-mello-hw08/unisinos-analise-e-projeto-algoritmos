package runner;

import domain.Knapsack;
import domain.classical.divideandconquer.ClassicKnapsack;
import domain.classical.greedy.KnapsackItem;
import service.classical.divideandconquer.KnapsackClassicDivideAndConquerProblem;
import service.classical.dynamic.KnapsackClassicDynamicProblem;
import service.classical.greedy.KnapsackClassicProblem;
import util.CommonPrints;
import util.ExecutionMeasurement;
import util.ExecutionTimer;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class ComparisonRunner {
    private static final int MAX_ITEMS_TO_PRINT = 6;

    private ComparisonRunner() {}

    public static void run() {
        Locale.setDefault(Locale.US);
        compareGreedyCounterexamples();
        compareBatch("Lote pequeno", 10, KnapsackInstances.createSmallInstance());
        compareBatch("Lote médio", 100, KnapsackInstances.createMediumInstance());
        compareBatch("Lote grande", 500, KnapsackInstances.createLargeInstance());
    }

    private static void compareGreedyCounterexamples() {
        CommonPrints.printHeadline("Cenários em que o guloso pode não encontrar o ótimo");
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

    private static void compareScenario(
            String name, double capacity, List<KnapsackItem> items, double expectedOptimalValue) {
        ExecutionMeasurement<Knapsack> greedyMeasurement = ExecutionTimer.measure(
                () -> new KnapsackClassicProblem(capacity).resolve(items));
        ExecutionMeasurement<Knapsack> dynamicMeasurement = ExecutionTimer.measure(
                () -> new KnapsackClassicDynamicProblem(capacity).resolve(items));
        ExecutionMeasurement<Knapsack> divideAndConquerMeasurement = ExecutionTimer.measure(
                () -> new KnapsackClassicDivideAndConquerProblem(capacity).resolve(items));

        domain.classical.greedy.ClassicKnapsack greedyResult =
                (domain.classical.greedy.ClassicKnapsack) greedyMeasurement.getResult();
        domain.classical.dynamic.ClassicKnapsack dynamicResult =
                (domain.classical.dynamic.ClassicKnapsack) dynamicMeasurement.getResult();
        ClassicKnapsack divideAndConquerResult =
                (ClassicKnapsack) divideAndConquerMeasurement.getResult();

        System.out.printf("\n%s | capacidade: %.2f | itens: %d%n", name, capacity, items.size());
        CommonPrints.printSeparator();
        printAlgorithmResult("Guloso", greedyResult.getTotalValue(),
                greedyResult.getUsedCapacity(), greedyMeasurement.getElapsedMilliseconds());
        printAlgorithmResult("Dinâmico", dynamicResult.getTotalValue(),
                dynamicResult.getUsedCapacity(), dynamicMeasurement.getElapsedMilliseconds());
        printAlgorithmResult("Divisão e conquista", divideAndConquerResult.getTotalValue(),
                divideAndConquerResult.getUsedCapacity(), divideAndConquerMeasurement.getElapsedMilliseconds());
        System.out.printf("Ótimo esperado: %.2f | Guloso atingiu o ótimo: %s | " +
                        "Dinâmico atingiu o ótimo: %s | Divisão e conquista atingiu o ótimo: %s%n",
                expectedOptimalValue,
                isExpectedValue(greedyResult.getTotalValue(), expectedOptimalValue),
                isExpectedValue(dynamicResult.getTotalValue(), expectedOptimalValue),
                isExpectedValue(divideAndConquerResult.getTotalValue(), expectedOptimalValue));
    }

    private static void compareBatch(String batchName, double capacity, List<KnapsackItem> items) {
        CommonPrints.printHeadline("Comparação de algoritmos — " + batchName);
        System.out.printf("Instância: %d itens | capacidade: %.2f%n", items.size(), capacity);

        ExecutionMeasurement<Knapsack> greedyMeasurement = ExecutionTimer.measure(
                () -> new KnapsackClassicProblem(capacity).resolve(items));
        var greedyResult = (domain.classical.greedy.ClassicKnapsack) greedyMeasurement.getResult();
        printAlgorithmResult("Guloso", greedyResult.getTotalValue(),
                greedyResult.getUsedCapacity(), greedyMeasurement.getElapsedMilliseconds());

        ExecutionMeasurement<Knapsack> dynamicMeasurement = ExecutionTimer.measure(
                () -> new KnapsackClassicDynamicProblem(capacity).resolve(items));
        var dynamicResult = (domain.classical.dynamic.ClassicKnapsack) dynamicMeasurement.getResult();
        printAlgorithmResult("Dinâmico", dynamicResult.getTotalValue(),
                dynamicResult.getUsedCapacity(), dynamicMeasurement.getElapsedMilliseconds());

        if (items.size() > DivideAndConquerRunner.MAX_ITEMS_TO_EXECUTE) {
            System.out.printf("Divisão e conquista: medição omitida (%d itens; limite configurado: %d). " +
                            "A poda não elimina o pior caso exponencial.%n",
                    items.size(), DivideAndConquerRunner.MAX_ITEMS_TO_EXECUTE);
            return;
        }

        getDivideAndConquerMeasurements(capacity, items, dynamicResult);
    }

    private static void getDivideAndConquerMeasurements(double capacity, List<KnapsackItem> items, domain.classical.dynamic.ClassicKnapsack dynamicResult) {
        ExecutionMeasurement<Knapsack> divideAndConquerMeasurement = ExecutionTimer.measure(
                () -> new KnapsackClassicDivideAndConquerProblem(capacity).resolve(items));
        var divideAndConquerResult =
                (ClassicKnapsack) divideAndConquerMeasurement.getResult();
        printAlgorithmResult("Divisão e conquista", divideAndConquerResult.getTotalValue(),
                divideAndConquerResult.getUsedCapacity(), divideAndConquerMeasurement.getElapsedMilliseconds());
        System.out.printf("Valores iguais entre dinâmico e divisão e conquista: %s%n",
                isExpectedValue(divideAndConquerResult.getTotalValue(), dynamicResult.getTotalValue()));

        CommonPrints.printSelectedItems(divideAndConquerResult.getSelectedItems(), MAX_ITEMS_TO_PRINT);
    }

    private static void printAlgorithmResult(String algorithmName, double totalValue,
                                             double usedCapacity, double elapsedMilliseconds) {
        System.out.printf("%s: valor=%.2f | peso=%.2f | tempo=%.3f ms%n",
                algorithmName, totalValue, usedCapacity, elapsedMilliseconds);
    }

    private static String isExpectedValue(double actualValue, double expectedValue) {
        return Math.abs(actualValue - expectedValue) < 0.000001 ? "sim" : "não";
    }
}
