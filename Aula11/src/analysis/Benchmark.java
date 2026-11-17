package analysis;

import domain.Knapsack;
import domain.classical.dynamic.ClassicKnapsack;
import domain.classical.greedy.KnapsackItem;
import service.classical.dynamic.KnapsackClassicDynamicProblem;
import service.classical.greedy.KnapsackClassicProblem;
import service.classical.greedy.KnapsackOptimalSolver;
import service.fractional.KnapsackFractionalProblem;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Benchmark de acerto do guloso (0/1) usando a PD do projeto como oraculo.
 * Usa as classes REAIS da Aula10 (greedy, dynamic, fractional, optimal solver).
 * Semente fixa => reproduzivel. Uso: java -cp out analysis.Benchmark 2000 resultados/acerto-guloso.csv
 */
public class Benchmark {
    static final long SEED = 42L;

    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.US);
        int trials = Integer.parseInt(args[0]);
        String out = args[1];
        try (PrintWriter w = new PrintWriter(out)) {
            w.println("series,n,ratio,trials,hits,hit_rate_pct,mean_gap_pct,max_gap_pct,"
                    + "dp_eq_bruteforce_pct");
            // Serie A: n varia, W = 50% do peso total
            for (int n : new int[]{5, 10, 15, 20, 30, 50, 100, 200}) {
                run(w, "A_vary_n", n, 0.5, trials);
            }
            // Serie B: razao W/soma(pesos) varia, n=20 e n=50
            for (int n : new int[]{20, 50}) {
                for (double r : new double[]{0.1, 0.25, 0.5, 0.75, 0.9}) {
                    run(w, "B_vary_ratio_n" + n, n, r, trials);
                }
            }
        }
        System.out.println("ok -> " + out);
    }

    static void run(PrintWriter w, String series, int n, double ratio, int trials) throws Exception {
        Random rnd = new Random(SEED + n * 1000L + (long) (ratio * 100));
        int hits = 0, bfOk = 0, bfTotal = 0;
        double gapSum = 0, gapMax = 0;
        for (int t = 0; t < trials; t++) {
            List<KnapsackItem> items = new ArrayList<>();
            int totalWeight = 0;
            for (int i = 0; i < n; i++) {
                int wt = 1 + rnd.nextInt(20);
                int val = 1 + rnd.nextInt(100);
                totalWeight += wt;
                items.add(new KnapsackItem("I" + i, wt, val));
            }
            int cap = Math.max(1, (int) Math.round(totalWeight * ratio));

            double greedy = ((domain.classical.greedy.ClassicKnapsack)
                    new KnapsackClassicProblem(cap).resolve(items)).getTotalValue();
            double dp = ((ClassicKnapsack) new KnapsackClassicDynamicProblem(cap).resolve(items)).getTotalValue();
            double fracGreedy = totalValue(new KnapsackFractionalProblem(cap).resolve(items));

            if (Math.abs(greedy - dp) < 1e-9) hits++;
            double gap = (dp - greedy) / dp * 100.0;
            gapSum += gap;
            gapMax = Math.max(gapMax, gap);
            if (n <= 15) {
                bfTotal++;
                double bf = 0;
                for (KnapsackItem it : new KnapsackOptimalSolver().solve(cap, items)) bf += it.getValue();
                if (Math.abs(bf - dp) < 1e-9) bfOk++;
            }
            if (greedy > dp + 1e-9) throw new IllegalStateException("guloso > otimo?!");
            if (dp > fracGreedy + 1e-9) throw new IllegalStateException("PD 0/1 > fracionaria?!");
        }
        w.printf("%s,%d,%.2f,%d,%d,%.2f,%.4f,%.4f,%s%n", series, n, ratio, trials, hits,
                100.0 * hits / trials, gapSum / trials, gapMax,
                bfTotal == 0 ? "NA" : String.format("%.2f", 100.0 * bfOk / bfTotal));
    }

    static double totalValue(Knapsack k) throws Exception {
        Field f = Knapsack.class.getDeclaredField("totalValue");
        f.setAccessible(true);
        return f.getDouble(k);
    }
}
