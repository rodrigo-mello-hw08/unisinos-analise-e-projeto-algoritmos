package analysis;

import domain.classical.greedy.KnapsackItem;
import runner.KnapsackInstances;
import service.classical.divideandconquer.KnapsackClassicDivideAndConquerProblem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Réplica instrumentada de KnapsackClassicDivideAndConquerProblem (mesma lógica, mesma ordem de
 * ramos e mesma poda) que conta chamadas recursivas, divisões (nós que geram filhos), podas,
 * e profundidade máxima, e conta as chamadas da versão pura (sem poda) nas instâncias pequenas.
 * Confere o valor com a classe original e interrompe com erro se divergir.
 */
public final class DcStats {
    private long calls, divisions, prunes, leaves, includeBranches;
    private int maxDepth;
    private double bestValue;

    /** Acima deste número de itens a versão pura (sem poda) não é executada: até 2^n chamadas. */
    private static final int MAX_ITEMS_PURE = 25;

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        System.out.println("# Divisão e conquista (mochila 0/1): contadores da recursão");
        System.out.println();
        System.out.println("Versão do projeto (KnapsackClassicDivideAndConquerProblem, com poda por limite superior).");
        System.out.println("Divisão = chamada que gera subproblemas; poda = ramo descartado pelo limite superior;");
        System.out.println("profundidade = maior número de chamadas empilhadas abaixo da raiz.");
        System.out.println();
        System.out.println("| instância | n | W | valor | valor da classe original | chamadas | divisões | ramos \"levar\" | podas | profundidade máx. | chamadas da versão pura (sem poda) |");
        System.out.println("|---|---|---|---|---|---|---|---|---|---|---|");
        run("pequena", 10, KnapsackInstances.createSmallInstance());
        run("média", 100, KnapsackInstances.createMediumInstance());
        run("grande", 500, KnapsackInstances.createLargeInstance());
        System.out.println();
        System.out.println("Versão pura = recursão levar/não levar sem poda e sem ordenação (primeira versão do código),");
        System.out.println("executada só até " + MAX_ITEMS_PURE + " itens; acima disso faria até 2^(n+1) - 1 chamadas.");
    }

    private static void run(String name, double capacity, List<KnapsackItem> items) {
        DcStats s = new DcStats();
        List<KnapsackItem> ordered = new ArrayList<>(items);
        ordered.sort(Comparator.comparingDouble((KnapsackItem i) -> i.getValue() / i.getWeight()).reversed());
        s.search(ordered, 0, capacity, 0, 0);
        double original = ((domain.classical.divideandconquer.ClassicKnapsack)
                new KnapsackClassicDivideAndConquerProblem(capacity).resolve(items)).getTotalValue();
        if (Math.abs(original - s.bestValue) > 1e-9) {
            throw new IllegalStateException("Valor diferente da classe original em " + name);
        }
        String pure;
        if (items.size() <= MAX_ITEMS_PURE) {
            long[] pureCalls = {0};
            double pureValue = pureSolve(items, 0, capacity, pureCalls);
            if (Math.abs(pureValue - original) > 1e-9) {
                throw new IllegalStateException("Versão pura com valor diferente em " + name);
            }
            pure = String.valueOf(pureCalls[0]);
        } else {
            pure = "inviável (até 2^" + (items.size() + 1) + " - 1)";
        }
        System.out.printf("| %s | %d | %.0f | %.0f | %.0f | %d | %d | %d | %d | %d | %s |%n",
                name, items.size(), capacity, s.bestValue, original, s.calls, s.divisions,
                s.includeBranches, s.prunes, s.maxDepth, pure);
    }

    /** Recursão pura: resolve os dois ramos e combina pelo maior valor (sem poda, sem ordenação). */
    private static double pureSolve(List<KnapsackItem> items, int idx, double rem, long[] calls) {
        calls[0]++;
        if (idx >= items.size()) return 0;
        double without = pureSolve(items, idx + 1, rem, calls);
        KnapsackItem it = items.get(idx);
        double with = it.getWeight() <= rem ? it.getValue() + pureSolve(items, idx + 1, rem - it.getWeight(), calls) : 0;
        return Math.max(with, without);
    }

    private void search(List<KnapsackItem> items, int idx, double rem, double cur, int depth) {
        calls++;
        maxDepth = Math.max(maxDepth, depth);
        if (cur > bestValue) bestValue = cur;
        if (idx >= items.size()) { leaves++; return; }
        if (bound(items, idx, rem, cur) <= bestValue) { prunes++; return; }
        divisions++;
        KnapsackItem it = items.get(idx);
        if (it.getWeight() <= rem) {
            includeBranches++;
            search(items, idx + 1, rem - it.getWeight(), cur + it.getValue(), depth + 1);
        }
        search(items, idx + 1, rem, cur, depth + 1);
    }

    private double bound(List<KnapsackItem> items, int idx, double rem, double cur) {
        double ub = cur, cap = rem;
        for (int i = idx; i < items.size() && cap > 0; i++) {
            KnapsackItem it = items.get(i);
            if (it.getValue() <= 0) continue;
            if (it.getWeight() <= cap) { ub += it.getValue(); cap -= it.getWeight(); }
            else { ub += it.getValue() * (cap / it.getWeight()); break; }
        }
        return ub;
    }
}
