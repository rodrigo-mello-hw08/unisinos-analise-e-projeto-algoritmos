package runner;

import domain.classical.dynamic.ClassicKnapsack;
import domain.classical.greedy.KnapsackItem;
import service.classical.dynamic.KnapsackClassicDynamicProblem;
import util.ExecutionMeasurement;
import util.ExecutionTimer;
import util.CommonPrints;

import java.util.List;
import java.util.Locale;

public class DynamicRunner {
    private static final int MAX_ITEMS_TO_PRINT = 10;

    public static void run() {
        Locale.setDefault(Locale.US);
        runBatch("Lote pequeno", 10, KnapsackInstances.createSmallInstance());
        runBatch("Lote médio", 100, KnapsackInstances.createMediumInstance());
        runBatch("Lote grande", 500, KnapsackInstances.createLargeInstance());
    }

    private static void runBatch(String batchName, double capacity, List<KnapsackItem> items) {
        CommonPrints.printHeadline(batchName + " - mochila clássica dinâmica (0/1)");
        System.out.printf("Itens: %d | Capacidade: %.2f%n", items.size(), capacity);

        KnapsackClassicDynamicProblem problem = new KnapsackClassicDynamicProblem(capacity);
        ExecutionMeasurement<domain.Knapsack> measurement =
                ExecutionTimer.measure(() -> problem.resolve(items));
        ClassicKnapsack result = (ClassicKnapsack) measurement.getResult();

        CommonPrints.printSelectedItems(result.getSelectedItems(), MAX_ITEMS_TO_PRINT);
        System.out.printf("Itens selecionados: %d%n", result.getSelectedItems().size());
        System.out.printf("Peso utilizado: %.2f / %.2f%n", result.getUsedCapacity(), capacity);
        System.out.printf("Valor total: %.2f%n", result.getTotalValue());
        System.out.printf("Tempo de execução: %.3f ms%n", measurement.getElapsedMilliseconds());
    }


}
