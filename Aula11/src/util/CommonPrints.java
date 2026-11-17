package util;

import domain.classical.greedy.KnapsackItem;

import java.util.List;

public final class CommonPrints {
    private CommonPrints() {
    }

    public static void printHeadline(String title) {
        System.out.println("\n==========================================================================");
        System.out.println(title);
        System.out.println("==========================================================================");
    }

    public static void printSeparator() {
        System.out.println("---------------------------------------------------------------------------");
    }

    public static void printSelectedItems(List<KnapsackItem> items, int maxItemsToPrint) {
        int itemsToPrint = Math.min(items.size(), maxItemsToPrint);
        for (int index = 0; index < itemsToPrint; index++) {
            KnapsackItem item = items.get(index);
            System.out.printf("  Selecionado: %s (peso=%.2f, valor=%.2f)%n",
                    item.getName(), item.getWeight(), item.getValue());
        }
        if (items.size() > itemsToPrint) {
            System.out.printf("  ... e mais %d itens%n", items.size() - itemsToPrint);
        }
    }
}
