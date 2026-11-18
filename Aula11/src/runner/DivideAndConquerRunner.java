package runner;

import domain.classical.divideandconquer.ClassicKnapsack;
import domain.classical.greedy.KnapsackItem;
import service.classical.divideandconquer.KnapsackClassicDivideAndConquerProblem;
import util.CommonPrints;
import util.ExecutionMeasurement;
import util.ExecutionTimer;

import java.util.List;
import java.util.Locale;

public final class DivideAndConquerRunner {
    private static final int MAX_ITEMS_TO_PRINT = 10;
    static final int MAX_ITEMS_TO_EXECUTE = 2000;

    private DivideAndConquerRunner() {}

    public static void run() {
        Locale.setDefault(Locale.US);
        runBatch("Lote pequeno", 10, KnapsackInstances.createSmallInstance());
        runBatch("Lote médio", 100, KnapsackInstances.createMediumInstance());
        runBatch("Lote grande", 500, KnapsackInstances.createLargeInstance());
    }

    private static void runBatch(String batchName, double capacity, List<KnapsackItem> items) {
        CommonPrints.printHeadline(batchName + " - mochila clássica por divisão e conquista (0/1)");
        System.out.printf("Itens: %d | Capacidade: %.2f%n", items.size(), capacity);

        if (items.size() > MAX_ITEMS_TO_EXECUTE) {
            System.out.printf("Execução omitida: %d itens excedem o limite configurado de %d. " +
                            "A poda reduz a busca em muitos casos, mas o pior caso continua exponencial.%n",
                    items.size(), MAX_ITEMS_TO_EXECUTE);
            return;
        }

        KnapsackClassicDivideAndConquerProblem problem =
                new KnapsackClassicDivideAndConquerProblem(capacity);
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
