package tsp.solver;

import metrics.SearchMetrics;
import tsp.domain.TspInstance;
import tsp.domain.TspResult;

/** Avalia todas as permutações de cidades, sem poda antecipada. */
public final class TspBruteForce implements TspSolver {
    public static final String ALGORITHM = "brute_force";
    private long bestCost;
    private int[] bestRoute;

    @Override
    public TspResult solve(TspInstance instance) {
        int cityCount = instance.cityCount();
        SearchMetrics metrics = new SearchMetrics();
        long start = System.nanoTime();
        int[] route = new int[cityCount];
        boolean[] used = new boolean[cityCount];
        route[0] = 0;
        used[0] = true;
        bestCost = Long.MAX_VALUE;
        bestRoute = new int[]{0, 0};
        enumerate(instance, 1, route, used, metrics);
        metrics.finish(start);
        return new TspResult(bestCost, bestRoute, metrics, ALGORITHM);
    }

    private void enumerate(TspInstance instance, int depth, int[] route, boolean[] used, SearchMetrics metrics) {
        metrics.addNode();
        int cityCount = instance.cityCount();
        if (depth == cityCount) {
            if (TspSupport.isCanonicalOrientation(route, cityCount)) {
                metrics.evaluateCompleteSolution();
                long cost = TspSupport.routeCost(instance, route, cityCount);
                if (cost < bestCost) {
                    bestCost = cost;
                    bestRoute = TspSupport.completedRoute(route, cityCount);
                }
            }
            return;
        }

        for (int city = 1; city < cityCount; city++) {
            metrics.addAttempt();
            if (used[city]) {
                continue;
            }
            route[depth] = city;
            used[city] = true;
            enumerate(instance, depth + 1, route, used, metrics);
            used[city] = false;
        }
    }
}
