package metrics;

/** Contadores comuns das execuções de busca. */
public final class SearchMetrics {
    private long elapsedNanos, nodesExplored, placementAttempts, prunedByColumn,
            prunedByDiagonal, prunedByBound, invalidCompleteConfigurations, boundEvaluations;

    public long getElapsedNanos() { return elapsedNanos; }
    public long getNodesExplored() { return nodesExplored; }
    public long getPlacementAttempts() { return placementAttempts; }
    public long getPrunedByColumn() { return prunedByColumn; }
    public long getPrunedByDiagonal() { return prunedByDiagonal; }
    public long getPrunedByBound() { return prunedByBound; }
    public long getInvalidCompleteConfigurations() { return invalidCompleteConfigurations; }
    public long getBoundEvaluations() { return boundEvaluations; }
    public void addNode() { nodesExplored++; }
    public void addAttempt() { placementAttempts++; }
    public void pruneColumn() { prunedByColumn++; }
    public void pruneDiagonal() { prunedByDiagonal++; }
    public void pruneBound() { prunedByBound++; }
    public void invalidComplete() { invalidCompleteConfigurations++; }
    public void evaluateBound() { boundEvaluations++; }
    public void finish(long startNanos) { elapsedNanos = System.nanoTime() - startNanos; }
}
