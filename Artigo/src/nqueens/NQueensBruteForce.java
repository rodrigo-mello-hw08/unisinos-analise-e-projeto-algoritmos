package nqueens;

import metrics.SearchMetrics;

/** Enumera N^N tabuleiros e valida apenas as configurações completas. */
public final class NQueensBruteForce implements NQueensSolver {
    @Override public NQueensResult solve(int n) {
        NQueensSupport.validateSize(n);
        SearchMetrics metrics = new SearchMetrics(); long start = System.nanoTime();
        long[] count = {0}; int[] queens = new int[n];
        enumerate(0, n, queens, count, metrics);
        metrics.finish(start);
        return new NQueensResult(n, count[0], metrics, "brute_force");
    }
    private void enumerate(int row, int n, int[] queens, long[] count, SearchMetrics m) {
        m.addNode();
        if (row == n) {
            if (NQueensSupport.isValid(queens)) count[0]++; else m.invalidComplete();
            return;
        }
        for (int col = 0; col < n; col++) {
            m.addAttempt(); queens[row] = col;
            enumerate(row + 1, n, queens, count, m);
        }
    }
}
