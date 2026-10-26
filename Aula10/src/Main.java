import domain.classical.ClassicKnapsack;
import domain.classical.KnapsackItem;
import service.classical.KnapsackClassicProblem;
import service.classical.KnapsackOptimalSolver;
import service.fractional.KnapsackFractionalProblem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {

    private static final int MAX_INPUT_ITEMS_TO_PRINT = 5;

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        runGreedyVersusOptimalDemonstration();
        runTest("Teste 1 — Instância pequena", 10, createSmallInstance());
        runTest("Teste 2 — Instância média", 100, createMediumInstance());
        runTest("Teste 3 — Instância grande", 500, createLargeInstance(2_000));
    }

    private static void runGreedyVersusOptimalDemonstration() {
        double capacity = 4;
        List<KnapsackItem> items = new ArrayList<>();
        items.add(new KnapsackItem("A", 3, 5));
        items.add(new KnapsackItem("B", 2, 3));
        items.add(new KnapsackItem("C", 2, 3));

        printHeadlineMessage("Demonstração: solução gulosa e solução ótima (mochila clássica 0/1)");
        System.out.printf("Capacidade da mochila: %.2f%n", capacity);
        System.out.println("Dados de entrada:");
        for (KnapsackItem item : items) {
            System.out.printf("  %s: peso=%.2f, valor=%.2f, valor/peso=%.2f%n",
                    item.getName(), item.getWeight(), item.getValue(),
                    item.getValue() / item.getWeight());
        }

        KnapsackClassicProblem greedyProblem = new KnapsackClassicProblem(capacity);
        ClassicKnapsack greedyResult = (ClassicKnapsack) greedyProblem.evaluateAndPrint(
                "Comparação entre a solução gulosa e a ótima", items);

        List<KnapsackItem> optimalItems = new KnapsackOptimalSolver().solve(capacity, items);
        double optimalWeight = 0;
        double optimalValue = 0;
        System.out.println("Solução ótima (enumeração de subconjuntos):");
        for (KnapsackItem item : optimalItems) {
            System.out.printf("  Selecionado: %s (peso=%.2f, valor=%.2f)%n",
                    item.getName(), item.getWeight(), item.getValue());
            optimalWeight += item.getWeight();
            optimalValue += item.getValue();
        }
        System.out.printf("Peso utilizado: %.2f / %.2f%n", optimalWeight, capacity);
        System.out.printf("Valor total ótimo: %.2f%n", optimalValue);
        System.out.printf("Diferença em relação à solução gulosa: %.2f%n",
                optimalValue - greedyResult.getTotalValue());
    }

    private static void runTest(String testName, double capacity, List<KnapsackItem> items) {
        printHeadlineMessage(testName);
        System.out.printf("Capacidade da mochila: %.2f%n", capacity);
        System.out.println("Quantidade de itens: " + items.size());
        printInput(items);

        new KnapsackClassicProblem(capacity).evaluateAndPrint(testName, items);
        new KnapsackFractionalProblem(capacity).evaluateAndPrint(testName, items);
    }

    private static void printInput(List<KnapsackItem> items) {
        System.out.println("Dados de entrada:");
        int countToPrint = Math.min(items.size(), MAX_INPUT_ITEMS_TO_PRINT);

        for (int i = 0; i < countToPrint; i++) {
            KnapsackItem item = items.get(i);
            System.out.printf("  %s: peso=%.2f, valor=%.2f%n", item.getName(), item.getWeight(), item.getValue());
        }

        if (countToPrint < items.size()) {
            System.out.printf("  ... e mais %d itens%n", items.size() - countToPrint);
        }
    }

    private static List<KnapsackItem> createMediumInstance() {
        return createGeneratedInstance(100, 20, 5, 17, 100);
    }

    private static List<KnapsackItem> createLargeInstance(int itemCount) {
        return createGeneratedInstance(itemCount, 50, 10, 37, 500);
    }

    private static List<KnapsackItem> createGeneratedInstance(int itemCount, int weightModulo,
                                                              int valueOffset, int valueMultiplier, int valueModulo) {
        List<KnapsackItem> items = new ArrayList<>();

        for (int i = 1; i <= itemCount; i++) {
            double weight = 1 + (i % weightModulo);
            double value = valueOffset + ((i * valueMultiplier) % valueModulo);
            items.add(new KnapsackItem("Item-" + i, weight, value));
        }

        return items;
    }

    private static List<KnapsackItem> createSmallInstance() {
        List<KnapsackItem> items = new ArrayList<>();
        items.add(new KnapsackItem("A", 2, 10));
        items.add(new KnapsackItem("B", 3, 14));
        items.add(new KnapsackItem("C", 5, 25));
        items.add(new KnapsackItem("D", 4, 16));
        items.add(new KnapsackItem("E", 1, 3));
        return items;
    }

    private static void printHeadlineMessage(String headline) {
        System.out.println("\n==========================================================================");
        System.out.println(headline);
        System.out.println("==========================================================================");
    }
}
