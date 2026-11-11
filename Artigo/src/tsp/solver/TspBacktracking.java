package tsp.solver;

import metrics.SearchMetrics;
import tsp.domain.TspInstance;
import tsp.domain.TspResult;

/** Constrói rotas incrementais, evitando cidades já visitadas. */
public final class TspBacktracking implements TspSolver {
    public static final String ALGORITHM = "backtracking";
    private long bestCost;
    private int[] bestRoute;

    @Override
    public TspResult solve(TspInstance instance) {
        int cityCount = instance.cityCount();
        SearchMetrics metrics = new SearchMetrics();
        long start = System.nanoTime();
        int[] route = new int[cityCount];
        boolean[] visited = new boolean[cityCount];
        route[0] = 0;
        visited[0] = true;
        bestCost = Long.MAX_VALUE;
        bestRoute = new int[]{0, 0};
        search(instance, 1, route, visited, metrics);
        metrics.finish(start);
        return new TspResult(bestCost, bestRoute, metrics, ALGORITHM);
    }

    private void search(TspInstance instance, int depth, int[] route, boolean[] visited, SearchMetrics metrics) {
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
            if (visited[city]) {
                metrics.pruneVisitedCity();
                continue;
            }
            route[depth] = city;
            visited[city] = true;
            search(instance, depth + 1, route, visited, metrics);
            visited[city] = false;
        }
    }
}
