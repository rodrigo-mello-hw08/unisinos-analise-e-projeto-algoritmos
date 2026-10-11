import domain.classical.KnapsackItem;
import service.classical.KnapsackClassicProblem;
import service.fractional.KnapsackFractionalProblem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {

    private static final int MAX_INPUT_ITEMS_TO_PRINT = 10;

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        runTest("Teste 1 - Pequena instancia", 10, createSmallInstance());
        runTest("Teste 2 - Media instancia", 100, createMediumInstance());
        runTest("Teste 3 - Grande instancia", 500, createLargeInstance(2_000));
    }

    private static void runTest(String testName, double capacity, List<KnapsackItem> items) {
        System.out.println("\n========================================");
        System.out.println(testName);
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
            System.out.printf(
                    "  %s: peso=%.2f, valor=%.2f%n",
                    item.getName(), item.getWeight(), item.getValue()
            );
        }

        if (countToPrint < items.size()) {
            System.out.printf("  ... mais %d itens%n", items.size() - countToPrint);
        }
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

    private static List<KnapsackItem> createMediumInstance() {
        List<KnapsackItem> items = new ArrayList<>();

        for (int i = 1; i <= 100; i++) {
            double weight = 1 + (i % 20);
            double value = 5 + ((i * 17) % 100);
            items.add(new KnapsackItem("Item-" + i, weight, value));
        }

        return items;
    }

    private static List<KnapsackItem> createLargeInstance(int itemCount) {
        List<KnapsackItem> items = new ArrayList<>();

        for (int i = 1; i <= itemCount; i++) {
            double weight = 1 + (i % 50);
            double value = 10 + ((i * 37) % 500);
            items.add(new KnapsackItem("Item-" + i, weight, value));
        }

        return items;
    }
}
