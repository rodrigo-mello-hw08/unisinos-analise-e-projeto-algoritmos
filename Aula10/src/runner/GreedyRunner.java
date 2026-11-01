package runner;

import domain.classical.greedy.ClassicKnapsack;
import domain.classical.greedy.KnapsackItem;
import service.classical.greedy.KnapsackClassicProblem;
import service.classical.greedy.KnapsackOptimalSolver;
import service.fractional.KnapsackFractionalProblem;
import util.CommonPrints;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GreedyRunner {

    private static final int MAX_INPUT_ITEMS_TO_PRINT = 5;

    public static void run(){
        Locale.setDefault(Locale.US);

        runGreedyVersusOptimalDemonstration();
        runTest("Teste 1 — Instância pequena", 10, KnapsackInstances.createSmallInstance());
        runTest("Teste 2 — Instância média", 100, KnapsackInstances.createMediumInstance());
        runTest("Teste 3 — Instância grande", 500, KnapsackInstances.createLargeInstance());
    }

    private static void runGreedyVersusOptimalDemonstration() {
        double capacity = 4;
        List<KnapsackItem> items = new ArrayList<>();
        items.add(new KnapsackItem("A", 3, 5));
        items.add(new KnapsackItem("B", 2, 3));
        items.add(new KnapsackItem("C", 2, 3));

        CommonPrints.printHeadline("Demonstração: solução gulosa e solução ótima (mochila clássica 0/1)");
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
        CommonPrints.printHeadline(testName);
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


}
