package nqueens.domain;

import metrics.SearchMetrics;

public record NQueensResult(
        int n,
        long solutionCount,
        SearchMetrics metrics,
        String algorithm
) { }
