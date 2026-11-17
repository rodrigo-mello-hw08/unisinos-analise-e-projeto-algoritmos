package service.classical.dynamic;

import domain.Knapsack;
import domain.classical.dynamic.ClassicKnapsack;
import domain.classical.greedy.KnapsackItem;
import service.KnapsackProblem;

import java.util.ArrayList;
import java.util.List;

public class KnapsackClassicDynamicProblem implements KnapsackProblem {
    private final int maxCapacity;

    public KnapsackClassicDynamicProblem(double maxCapacity) {
        this.maxCapacity = (int) maxCapacity;
    }

    @Override
    public Knapsack resolve(List<KnapsackItem> items) {
        double[][] bestValueByItemAndCapacity = createBestValueTable(items.size());
        fillBestValueTable(items, bestValueByItemAndCapacity);

        List<KnapsackItem> selectedItems = findSelectedItems(items, bestValueByItemAndCapacity);
        return new ClassicKnapsack(maxCapacity, selectedItems);
    }

    private double[][] createBestValueTable(int itemCount) {
        // A linha zero representa nenhum item; a coluna zero representa capacidade zero.
        int rowCount = itemCount + 1;
        int capacityCount = maxCapacity + 1;
        return new double[rowCount][capacityCount];
    }

    private void fillBestValueTable(List<KnapsackItem> items, double[][] bestValueByItemAndCapacity) {
        // Cada linha considera os itens até itemNumber e consulta apenas a linha anterior.
        for (int itemNumber = 1; itemNumber <= items.size(); itemNumber++) {

            KnapsackItem currentItem = items.get(itemNumber - 1);
            int currentItemWeight = (int) currentItem.getWeight();
            double currentItemValue = currentItem.getValue();

            for (int currentCapacity = 0; currentCapacity <= maxCapacity; currentCapacity++) {
                double bestValueWithoutCurrentItem = bestValueByItemAndCapacity[itemNumber - 1][currentCapacity];
                double bestValueWithCurrentItem = bestValueWithoutCurrentItem;

                boolean currentItemFits = currentItemWeight <= currentCapacity;
                if (currentItemFits) {
                    // Ao incluir o item, somamos seu valor à solução ótima para a capacidade restante.
                    int capacityRemainingAfterTakingItem = currentCapacity - currentItemWeight;
                    double bestValueForRemainingCapacity =
                            bestValueByItemAndCapacity[itemNumber - 1][capacityRemainingAfterTakingItem];
                    bestValueWithCurrentItem = bestValueForRemainingCapacity + currentItemValue;
                }

                boolean takingItemGivesHigherValue = bestValueWithCurrentItem > bestValueWithoutCurrentItem;
                // A célula guarda o melhor valor entre incluir o item ou deixá-lo de fora.
                if (currentItemFits && takingItemGivesHigherValue) {
                    bestValueByItemAndCapacity[itemNumber][currentCapacity] = bestValueWithCurrentItem;
                } else {
                    bestValueByItemAndCapacity[itemNumber][currentCapacity] = bestValueWithoutCurrentItem;
                }
            }
        }
    }

    private List<KnapsackItem> findSelectedItems(List<KnapsackItem> items, double[][] bestValueByItemAndCapacity) {
        List<KnapsackItem> selectedItems = new ArrayList<>();
        int remainingCapacity = maxCapacity;

        // Percorre a tabela de trás para frente para reconstruir os itens da solução ótima.
        for (int itemNumber = items.size(); itemNumber > 0; itemNumber--) {
            KnapsackItem currentItem = items.get(itemNumber - 1);
            int currentItemWeight = (int) currentItem.getWeight();

            double bestValueIncludingCurrentItem = bestValueByItemAndCapacity[itemNumber][remainingCapacity];
            double bestValueWithoutCurrentItem = bestValueByItemAndCapacity[itemNumber - 1][remainingCapacity];

            boolean currentItemFits = currentItemWeight <= remainingCapacity;
            boolean currentItemWasChosen = bestValueIncludingCurrentItem > bestValueWithoutCurrentItem;

            if (currentItemFits && currentItemWasChosen) {
                // Ao selecionar o item, reduz a capacidade antes de analisar os itens anteriores.
                selectedItems.add(currentItem);
                remainingCapacity -= currentItemWeight;
            }
        }
        return selectedItems;
    }
}
