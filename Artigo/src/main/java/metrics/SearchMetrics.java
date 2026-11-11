package metrics;

/** Contadores comuns das execuções de busca. */
public final class SearchMetrics {
    private long elapsedNanos;
    private long nodesExplored;
    private long placementAttempts;
    private long prunedByColumn;
    private long prunedByDiagonal;
    private long prunedByBound;
    private long invalidCompleteConfigurations;
    private long boundEvaluations;
    private long prunedByVisitedCity;
    private long completeSolutionsEvaluated;

    public long getElapsedNanos() {
        return elapsedNanos;
    }
    public long getNodesExplored() {
        return nodesExplored;
    }
    public long getPlacementAttempts() {
        return placementAttempts;
    }
    public long getPrunedByColumn() {
        return prunedByColumn;
    }
    public long getPrunedByDiagonal() {
        return prunedByDiagonal;
    }
    public long getPrunedByBound() {
        return prunedByBound;
    }
    public long getInvalidCompleteConfigurations() {
        return invalidCompleteConfigurations;
    }
    public long getBoundEvaluations() {
        return boundEvaluations;
    }
    public long getPrunedByVisitedCity() {
        return prunedByVisitedCity;
    }
    public long getCompleteSolutionsEvaluated() {
        return completeSolutionsEvaluated;
    }
    public void addNode() {
        nodesExplored++;
    }
    public void addAttempt() {
        placementAttempts++;
    }
    public void pruneColumn() {
        prunedByColumn++;
    }
    public void pruneDiagonal() {
        prunedByDiagonal++;
    }
    public void pruneBound() {
        prunedByBound++;
    }
    public void invalidComplete() {
        invalidCompleteConfigurations++;
    }
    public void evaluateBound() {
        boundEvaluations++;
    }
    public void pruneVisitedCity() {
        prunedByVisitedCity++;
    }
    public void evaluateCompleteSolution() {
        completeSolutionsEvaluated++;
    }
    public void finish(long startNanos) {
        elapsedNanos = System.nanoTime() - startNanos;
    }
}
