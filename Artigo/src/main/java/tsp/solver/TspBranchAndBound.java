package tsp.solver;

import metrics.SearchMetrics;
import tsp.domain.TspInstance;
import tsp.domain.TspResult;

/** Minimiza o custo da rota usando um limite inferior baseado em MST. */
public final class TspBranchAndBound implements TspSolver {
    public static final String ALGORITHM = "branch_and_bound";
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
        search(instance, 1, 0, route, visited, metrics);
        metrics.finish(start);
        return new TspResult(bestCost, bestRoute, metrics, ALGORITHM);
    }

    private void search(TspInstance instance, int depth, long partialCost, int[] route, boolean[] visited,
                        SearchMetrics metrics) {
        metrics.addNode();
        int cityCount = instance.cityCount();
        if (depth == cityCount) {
            if (TspSupport.isCanonicalOrientation(route, cityCount)) {
                metrics.evaluateCompleteSolution();
                long totalCost = partialCost + instance.distanceBetween(route[depth - 1], route[0]);
                if (totalCost < bestCost) {
                    bestCost = totalCost;
                    bestRoute = TspSupport.completedRoute(route, cityCount);
                }
            }
            return;
        }

        metrics.evaluateBound();
        long lowerBound = partialCost + lowerBound(instance, route[depth - 1], visited);
        if (lowerBound >= bestCost) {
            metrics.pruneBound();
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
            long nextCost = partialCost + instance.distanceBetween(route[depth - 1], city);
            search(instance, depth + 1, nextCost, route, visited, metrics);
            visited[city] = false;
        }
    }

    private long lowerBound(TspInstance instance, int currentCity, boolean[] visited) {
        int cityCount = instance.cityCount();
        boolean[] inTree = new boolean[cityCount];
        long[] bestEdge = new long[cityCount];
        long minimumFromCurrent = Long.MAX_VALUE;
        long minimumToOrigin = Long.MAX_VALUE;
        int remainingCount = 0;

        for (int city = 1; city < cityCount; city++) {
            if (!visited[city]) {
                remainingCount++;
                bestEdge[city] = Long.MAX_VALUE;
                long fromCurrent = instance.distanceBetween(currentCity, city);
                long toOrigin = instance.distanceBetween(city, 0);
                if (fromCurrent < minimumFromCurrent) {
                    minimumFromCurrent = fromCurrent;
                }
                if (toOrigin < minimumToOrigin) {
                    minimumToOrigin = toOrigin;
                }
            }
        }

        if (remainingCount == 0) {
            return instance.distanceBetween(currentCity, 0);
        }

        int firstRemaining = firstUnvisited(visited);
        inTree[firstRemaining] = true;
        long mstCost = 0;
        for (int city = 1; city < cityCount; city++) {
            if (!visited[city]) {
                bestEdge[city] = instance.distanceBetween(firstRemaining, city);
            }
        }

        for (int added = 1; added < remainingCount; added++) {
            int nextCity = -1;
            long smallestEdge = Long.MAX_VALUE;
            for (int city = 1; city < cityCount; city++) {
                boolean cityIsUnvisited = !visited[city];
                boolean cityIsOutsideTree = !inTree[city];
                boolean edgeIsSmallest = bestEdge[city] < smallestEdge;
                boolean candidateIsBetter = cityIsUnvisited && cityIsOutsideTree && edgeIsSmallest;
                if (candidateIsBetter) {
                    nextCity = city;
                    smallestEdge = bestEdge[city];
                }
            }
            inTree[nextCity] = true;
            mstCost += smallestEdge;
            for (int city = 1; city < cityCount; city++) {
                boolean cityIsUnvisited = !visited[city];
                boolean cityIsOutsideTree = !inTree[city];
                boolean cityNeedsEdgeUpdate = cityIsUnvisited && cityIsOutsideTree;
                if (cityNeedsEdgeUpdate) {
                    long edge = instance.distanceBetween(nextCity, city);
                    if (edge < bestEdge[city]) {
                        bestEdge[city] = edge;
                    }
                }
            }
        }
        return minimumFromCurrent + mstCost + minimumToOrigin;
    }

    private int firstUnvisited(boolean[] visited) {
        for (int city = 1; city < visited.length; city++) {
            if (!visited[city]) {
                return city;
            }
        }
        throw new IllegalStateException("Nenhuma cidade não visitada disponível.");
    }
}
