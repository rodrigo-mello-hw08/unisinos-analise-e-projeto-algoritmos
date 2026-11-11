package tsp.domain;

import metrics.SearchMetrics;

public record TspResult(long optimalCost, int[] route, SearchMetrics metrics, String algorithm) {
    public TspResult {
        route = route.clone();
    }

    @Override
    public int[] route() {
        return route.clone();
    }
}
